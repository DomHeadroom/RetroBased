import { Component } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {
  email: string = '';
  password: string = '';

  private keycloakUrl = 'https://localhost:8080/auth/realms/keycloak-psw/protocol/openid-connect/token';
  private clientId = 'retrobased-rest-api';

  constructor(private http: HttpClient) {}

  ngOnInit() {
    document.documentElement.classList.add('login');
    document.body.classList.add('login-background');
  }

  ngOnDestroy() {
    document.documentElement.classList.remove('login');
    document.body.classList.remove('login-background');
  }

  login() {
    const body = new URLSearchParams();
    body.set('client_id', this.clientId);
    body.set('grant_type', 'password');
    body.set('username', this.email);
    body.set('password', this.password);

    const headers = new HttpHeaders({ 'Content-Type': 'application/x-www-form-urlencoded' });

    this.http.post(this.keycloakUrl, body.toString(), { headers }).subscribe({
      next: (response: any) => {
        console.log('Login successful', response);
        localStorage.setItem('accessToken', response.access_token);
        localStorage.setItem('refreshToken', response.refresh_token);
      },
      error: (error) => {
        console.error('Login failed', error);
      }
    });
  }

  refreshToken() {
    const body = new URLSearchParams();
    body.set('client_id', this.clientId);
    body.set('grant_type', 'refresh_token');
    body.set('refresh_token', localStorage.getItem('refreshToken') || '');
  
    const headers = new HttpHeaders({ 'Content-Type': 'application/x-www-form-urlencoded' });
  
    this.http.post(this.keycloakUrl, body.toString(), { headers }).subscribe({
      next: (response: any) => {
        console.log('Token refreshed successfully', response);
        localStorage.setItem('accessToken', response.access_token);
        localStorage.setItem('refreshToken', response.refresh_token);
      },
      error: (error) => {
        console.error('Token refresh failed', error);
      }
    });
  }
}
