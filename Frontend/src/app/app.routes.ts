import { Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { CartComponent } from './cart/cart.component';
import { LoginComponent } from './login/login.component';

/*import { HomeComponent } from './home/home.component';
import { AboutComponent } from './about/about.component';*/

export const routes: Routes = [
    { path: 'home', component: HomeComponent },
    { path: 'recycle-bin', component: CartComponent },
    { path: 'login', component: LoginComponent},
    { path: '', redirectTo: 'home', pathMatch: 'full' }
  /*
  { path: 'about', component: AboutComponent },
   */
];
