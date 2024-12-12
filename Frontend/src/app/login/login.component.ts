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
  protected email: string = '';
  protected password: string = '';
  protected showErrorScreen: boolean = false;
  private errorTimeout: any;

  protected isLogged: boolean = false;

  constructor(private auth: AuthService,
    private router: Router
  ) {}

  ngOnInit() {
    document.documentElement.classList.add('login');
    document.body.classList.add('login-background');
    if(localStorage.getItem('accessToken') || localStorage.getItem('refreshToken')){
      this.isLogged = true;
    }
    else{
      this.isLogged =  false;
    }
  }

  ngOnDestroy() {
    document.documentElement.classList.remove('login');
    document.body.classList.remove('login-background');
    if (this.errorTimeout) {
      clearTimeout(this.errorTimeout);
    }
  }

  login() {
    if(!this.email || !this.password)
      return ;
    this.auth.login(this.email, this.password).subscribe({
      next: () => {
        console.log('User logged in successfully.');
        this.showErrorScreen = false;
      },
      error: (error) => {
        console.error('Login failed:', error);
        this.showErrorScreen = true;

        if (this.errorTimeout) clearTimeout(this.errorTimeout);
        this.errorTimeout = setTimeout(() => {
          this.showErrorScreen = false;
        }, 30000);
      },
    });
  }

  handleErrorOk() {
    if (this.errorTimeout) clearTimeout(this.errorTimeout);
    this.showErrorScreen = false;
  }

  logout(){
    this.auth.logout();
    this.isLogged = false;
  }
}
