import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { switchMap, catchError } from 'rxjs/operators';
import { Router } from '@angular/router';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  const isPublicRequest = req.url.includes('/public');

  if (isPublicRequest) {
    return next(req);
  }

  const tokenExpiry = parseInt(localStorage.getItem('tokenExpiry') || '0', 10);
  if (tokenExpiry > Date.now()) {
    const token = localStorage.getItem('accessToken');
    if (token) {
      const clonedRequest = req.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`,
        },
      });
      return next(clonedRequest);
    }
  }
  return authService.refreshAccessToken().pipe(
    switchMap(() => {
      const token = localStorage.getItem('accessToken');
      if (token) {
        const clonedRequest = req.clone({
          setHeaders: {
            Authorization: `Bearer ${token}`,
          },
        });
        return next(clonedRequest);
      } else {
        const attemptedUrl = router.url;
        localStorage.setItem('redirectUrl', attemptedUrl);
        router.navigate(['login']);
      }
      return next(req);
    }),
    catchError((error) => {
      console.error('Token refresh failed, proceeding without token.', error);
      return next(req);
    })
  );
};
