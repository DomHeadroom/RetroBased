import { Component } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss',
})
export class LoginComponent {
  email: string = '';
  password: string = '';

  private keycloakUrl = 'http://localhost:8080/realms/keycloak-psw/protocol/openid-connect/token';
  private clientId = 'retrobased-rest-api';

  constructor(private http: HttpClient) {}

  ngOnInit() {
    document.documentElement.classList.add('login');
    document.body.classList.add('login-background');
    this.scheduleTokenRefresh();
  }

  ngOnDestroy() {
    document.documentElement.classList.remove('login');
    document.body.classList.remove('login-background');
  }

  login() {
    const body = `grant_type=${encodeURIComponent('password')}` +
                 `&client_id=${encodeURIComponent(this.clientId)}` +
                 `&username=${encodeURIComponent(this.email)}` +
                 `&password=${encodeURIComponent(this.password)}`;
  
    const headers = new HttpHeaders({
      'Content-Type': 'application/x-www-form-urlencoded',
      'Accept': 'application/hal+json',
    });
  
    this.http.post(this.keycloakUrl, body, { headers }).subscribe({
      next: (response: any) => {
        console.log('Login successful', response);
        localStorage.setItem('accessToken', response.access_token);
        localStorage.setItem('refreshToken', response.refresh_token);
  
        const expiryTime = Date.now() + response.expires_in * 1000; // Convert seconds to ms
        localStorage.setItem('tokenExpiry', expiryTime.toString());
      },
      error: (error) => {
        console.error('Login failed', error);
      },
    });
  }

  refreshToken() {
    const body = `grant_type=${encodeURIComponent('refresh_token')}` +
                 `&client_id=${encodeURIComponent(this.clientId)}` +
                 `&refresh_token=${encodeURIComponent(localStorage.getItem('refreshToken') || '')}`;
  
    const headers = new HttpHeaders({
      'Content-Type': 'application/x-www-form-urlencoded',
    });
  
    this.http.post(this.keycloakUrl, body, { headers }).subscribe({
      next: (response: any) => {
        console.log('Token refreshed successfully', response);
        localStorage.setItem('accessToken', response.access_token);
        localStorage.setItem('refreshToken', response.refresh_token);
  
        const expiryTime = Date.now() + response.expires_in * 1000; // Convert seconds to ms
        localStorage.setItem('tokenExpiry', expiryTime.toString());
      },
      error: (error) => {
        console.error('Token refresh failed', error);
      },
    });
  }

  scheduleTokenRefresh() {
    const tokenExpiry = parseInt(localStorage.getItem('tokenExpiry') || '0', 10);
    if (tokenExpiry) {
      const now = Date.now();
      const timeToExpiry = tokenExpiry - now - 5 * 60 * 1000;
  
      if (timeToExpiry > 0) {
        setTimeout(() => this.refreshToken(), timeToExpiry);
      } else {
        console.warn('Token already expired. Please log in again.');
      }
    }
  }
}
