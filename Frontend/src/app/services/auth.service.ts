import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private keycloakUrl = 'http://localhost:8080/realms/keycloak-psw/protocol/openid-connect/token';
  private clientId = 'retrobased-rest-api';
  private refreshTimer: any = null;

  constructor(private http: HttpClient) {}

  /**
   * Log in with user credentials.
   * @param email User email.
   * @param password User password.
   */
  login(email: string, password: string): Observable<void> {
    const body = `grant_type=${encodeURIComponent('password')}` +
                 `&client_id=${encodeURIComponent(this.clientId)}` +
                 `&username=${encodeURIComponent(email)}` +
                 `&password=${encodeURIComponent(password)}`;

    const headers = new HttpHeaders({
      'Content-Type': 'application/x-www-form-urlencoded',
      'Accept': 'application/hal+json',
    });

    return this.http.post(this.keycloakUrl, body, { headers }).pipe(
      map((response: any) => {
        console.log('Login successful', response);
        this.storeTokens(response);
        this.scheduleRefreshToken();
      }),
      catchError((error) => {
        console.error('Login failed', error);
        return throwError(() => error);
      })
    );
  }

  /**
   * Refresh the refresh token.
   */
  private refreshRefreshToken(): Observable<void> {
    const refreshToken = localStorage.getItem('refreshToken');
    if (!refreshToken) {
      console.warn('No refresh token found. User needs to log in again.');
      return throwError(() => new Error('No refresh token available'));
    }

    const body = `grant_type=${encodeURIComponent('refresh_token')}` +
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

  /**
   * Schedule the automatic refresh of the refresh token based on its expiration.
   */
  private scheduleRefreshToken() {
    if (this.refreshTimer) {
      clearTimeout(this.refreshTimer);
      this.refreshTimer = null;
    }

    const refreshTokenExpiry = parseInt(localStorage.getItem('refreshTokenExpiry') || '0', 10);

    if (refreshTokenExpiry <= Date.now()) {
      console.warn('Refresh token expired. User needs to log in again.');
      return;
    }

    const refreshTimeToExpiry = refreshTokenExpiry - Date.now() - 5 * 60 * 1000;
    if (refreshTimeToExpiry > 0) {
      this.refreshTimer = setTimeout(() => {
        this.refreshRefreshToken().subscribe({
          error: () => console.warn('Automatic refresh of refresh token failed.'),
        });
      }, refreshTimeToExpiry);
    }
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

    const refreshTokenExpiryTime = Date.now() + response.refresh_expires_in * 1000;
    localStorage.setItem('refreshTokenExpiry', refreshTokenExpiryTime.toString());
  }

  /**
   * Get a valid access token. If expired, use the refresh token to get a new one.
   */
  getToken(): Observable<string> {
    const tokenExpiry = parseInt(localStorage.getItem('tokenExpiry') || '0', 10);

    if (tokenExpiry <= Date.now()) {
      console.log('Access token expired. Refreshing...');
      const refreshToken = localStorage.getItem('refreshToken');
      if (!refreshToken) {
        console.warn('No refresh token found. User needs to log in again.');
        return throwError(() => new Error('No refresh token available'));
      }

      const body = `grant_type=${encodeURIComponent('refresh_token')}` +
                   `&client_id=${encodeURIComponent(this.clientId)}` +
                   `&refresh_token=${encodeURIComponent(refreshToken)}`;

      const headers = new HttpHeaders({
        'Content-Type': 'application/x-www-form-urlencoded',
      });

      return this.http.post(this.keycloakUrl, body, { headers }).pipe(
        map((response: any) => {
          console.log('Access token refreshed successfully', response);
          this.storeTokens(response);
          return response.access_token;
        }),
        catchError((error) => {
          console.error('Unable to refresh access token', error);
          return throwError(() => error);
        })
      );
    }

    return new Observable<string>((observer) => {
      observer.next(localStorage.getItem('accessToken') || '');
      observer.complete();
    });
  }

  /**
   * Clear stored tokens and stop automatic refresh.
   */
  logout() {
    if (this.refreshTimer) {
      clearTimeout(this.refreshTimer);
    }
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
    localStorage.removeItem('tokenExpiry');
    localStorage.removeItem('refreshTokenExpiry');
    console.log('User logged out and tokens cleared.');
  }
}
