import { Component, Input } from '@angular/core';
import { Router, RouterModule } from '@angular/router';

import { ProductControllerService } from '../services/product-controller.service';
import { ProductDto } from '../services/models';
import { ProductDisplay } from '../services/models/product-display';

@Component({
  selector: 'app-taskbar',
  standalone: true,
  imports: [RouterModule],
  templateUrl: './taskbar.component.html',
  styleUrl: './taskbar.component.scss',
})
export class TaskbarComponent {
  currentTime: string = '';
  currentDate: string = '';
  @Input() taskbarIconSrc: string = 'assets/taskbar/icons/buy.ico';

  constructor(
    private router: Router,
    private productService: ProductControllerService,
    private productDisplay: ProductDisplay
  ) {}

  navigateTo(path: string) {
    this.router.navigate([path]);
  }

  ngOnInit(): void {
    this.updateTime();
    setInterval(() => this.updateTime(), 1000);
  }

  updateTime() {
    const now = new Date();
    this.currentTime = now.toLocaleTimeString([], {
      hour: '2-digit',
      minute: '2-digit',
    });
    this.currentDate = now.toLocaleDateString([], {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
    });
  }

  onSearchClick(searchValue: string) {
    this.productService
      .searchProducts({
        k: searchValue,
        page: 0,
      })
      .subscribe((response: any) => {
        const products = response as ProductDto[];
        this.productDisplay.setProducts(products);
      });
  }

  switchToLogin() {
    document.documentElement.classList.add('login');
    document.body.classList.add('login-background');
    const attemptedUrl = this.router.url;
    localStorage.setItem('redirectUrl', attemptedUrl);
    this.navigateTo('login');
  }
}
