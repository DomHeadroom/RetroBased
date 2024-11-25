import { Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { CartComponent } from './cart/cart.component';

/*import { HomeComponent } from './home/home.component';
import { AboutComponent } from './about/about.component';*/

export const routes: Routes = [
    { path: 'home', component: HomeComponent },
    { path: 'bin', component: CartComponent },
    { path: '', redirectTo: 'home', pathMatch: 'full' }
  /*
  { path: 'about', component: AboutComponent },
   */
];
