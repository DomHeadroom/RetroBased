import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, throwError, of } from 'rxjs';
import { catchError, map, switchMap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private keycloakUrl: string =
    'http://localhost:8080/realms/keycloak-psw/protocol/openid-connect/token';
  private clientId: string = 'retrobased-rest-api';
  private isRefreshing: boolean = false;
  public isLogged: boolean = false;

  constructor(private http: HttpClient) {}

  /**
   * Refresh the refresh token.
   */
  private refreshRefreshToken(): Observable<void> {
    const refreshToken = localStorage.getItem('refreshToken');
    if (!refreshToken) {
      console.warn('No refresh token found. User needs to log in again.');
      return throwError(() => new Error('No refresh token available'));
    }

    const body =
      `grant_type=${encodeURIComponent('refresh_token')}` +
      `&client_id=${encodeURIComponent(this.clientId)}` +
      `&refresh_token=${encodeURIComponent(refreshToken)}`;

    const headers = new HttpHeaders({
      'Content-Type': 'application/x-www-form-urlencoded',
    });

    return this.http.post(this.keycloakUrl, body, { headers }).pipe(
      map((response: any) => {
        console.log('Refresh token refreshed successfully', response);
        this.storeTokens(response);
      }),
      catchError((error) => {
        console.error('Refresh token refresh failed', error);
        return throwError(() => error);
      })
    );
  }

  refreshAccessToken(): Observable<void> {
    if (this.isRefreshing) {
      return of(void 0);
    }
    this.isRefreshing = true;

    const refreshToken = localStorage.getItem('refreshToken');
    if (!refreshToken) {
      console.warn('No refresh token found. User needs to log in again.');
      this.isRefreshing = false;
      return throwError(() => new Error('No refresh token available'));
    }

    const refreshTokenExpiry = parseInt(
      localStorage.getItem('refreshTokenExpiry') || '0',
      10
    );

    if (refreshTokenExpiry <= Date.now()) {
      console.error('Refresh token expired. User needs to log in again.');
      this.isRefreshing = false;
      return throwError(() => new Error('Refresh token expired'));
    }

    const body =
      `grant_type=${encodeURIComponent('refresh_token')}` +
      `&client_id=${encodeURIComponent(this.clientId)}` +
      `&refresh_token=${encodeURIComponent(refreshToken)}`;

    const headers = new HttpHeaders({
      'Content-Type': 'application/x-www-form-urlencoded',
    });

    return this.http.post(this.keycloakUrl, body, { headers }).pipe(
      map((response: any) => {
        console.log('Access token refreshed successfully', response);
        this.storeTokens(response);
        this.isRefreshing = false;
      }),
      catchError((error) => {
        console.error('Access token refresh failed', error);
        this.isRefreshing = false;
        return throwError(() => error);
      })
    );
  }

  /**
   * Get a valid access token. If expired, use the refresh token to get a new one.
   */
  getToken(): Observable<string> {
    const tokenExpiry = parseInt(
      localStorage.getItem('tokenExpiry') || '0',
      10
    );

    if (tokenExpiry <= Date.now()) {
      console.log('Access token expired. Refreshing...');
      return this.refreshAccessToken().pipe(
        switchMap(() => {
          return of(localStorage.getItem('accessToken') || '');
        })
      );
    }

    return new Observable<string>((observer) => {
      observer.next(localStorage.getItem('accessToken') || '');
      observer.complete();
    });
  }

  /**
   * Log in with user credentials.
   * @param email User email.
   * @param password User password.
   */
  login(email: string, password: string): Observable<void> {
    const body =
      `grant_type=${encodeURIComponent('password')}` +
      `&client_id=${encodeURIComponent(this.clientId)}` +
      `&username=${encodeURIComponent(email)}` +
      `&password=${encodeURIComponent(password)}`;

    const headers = new HttpHeaders({
      'Content-Type': 'application/x-www-form-urlencoded',
      Accept: 'application/hal+json',
    });

    return this.http.post(this.keycloakUrl, body, { headers }).pipe(
      map((response: any) => {
        console.log('Login successful', response);
        this.storeTokens(response);
      }),
      catchError((error) => {
        console.error('Login failed', error);
        return throwError(() => error);
      })
    );
  }

  /**
   * Clear stored tokens and stop automatic refresh.
   */
  logout() {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
    localStorage.removeItem('tokenExpiry');
    localStorage.removeItem('refreshTokenExpiry');
    console.log('User logged out and tokens cleared.');
  }

  /**
   * Store tokens and their expiration times in localStorage.
   * @param response The Keycloak response containing tokens.
   */
  private storeTokens(response: any) {
    localStorage.setItem('accessToken', response.access_token);
    localStorage.setItem('refreshToken', response.refresh_token);

    const accessTokenExpiryTime = Date.now() + response.expires_in * 1000;
    localStorage.setItem('tokenExpiry', accessTokenExpiryTime.toString());

    const refreshTokenExpiryTime =
      Date.now() + response.refresh_expires_in * 1000;
    localStorage.setItem(
      'refreshTokenExpiry',
      refreshTokenExpiryTime.toString()
    );
  }
}
