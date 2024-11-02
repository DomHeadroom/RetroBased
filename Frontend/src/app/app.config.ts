import { ApplicationConfig } from '@angular/core';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { provideRouter } from '@angular/router';
import { routes } from './app.routes';
import { ApiModule } from './services/api.module';
import { API_BASE_PATH } from './app.tokens';

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    provideHttpClient(withInterceptors([])),
    ApiModule,
    { provide: API_BASE_PATH, useValue: 'http://localhost:8081/api' }
  ]
};
