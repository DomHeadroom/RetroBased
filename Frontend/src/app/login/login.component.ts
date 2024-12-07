import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss',
})
export class LoginComponent implements OnInit, OnDestroy {
  email: string = '';
  password: string = '';
  loginError: string | null = null;

  constructor(private auth: AuthService,
    private router: Router
  ) {}

  ngOnInit() {
    document.documentElement.classList.add('login');
    document.body.classList.add('login-background');
  }

  ngOnDestroy() {
    document.documentElement.classList.remove('login');
    document.body.classList.remove('login-background');
  }

  login() {
    this.auth.login(this.email, this.password).subscribe({
      next: () => {
        console.log('User logged in successfully.');
        this.loginError = null;
        const redirectUrl = localStorage.getItem('redirectUrl') || '/home';
        localStorage.removeItem('redirectUrl');
        this.router.navigateByUrl(redirectUrl);
      },
      error: (error) => {
        console.error('Login failed:', error);
        this.loginError = 'Invalid credentials. Please try again.';
      },
    });
  }

  logout(){
    this.auth.logout();
  }
}
