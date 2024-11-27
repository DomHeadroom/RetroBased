import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../services/services/auth.service';

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
  loginError: string | null = null;

  constructor(private auth: AuthService) {}

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
      },
      error: (error) => {
        console.error('Login failed:', error);
        this.loginError = 'Invalid credentials. Please try again.';
      },
    });
  }
}
