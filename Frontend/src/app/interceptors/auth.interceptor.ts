import { HttpInterceptorFn } from '@angular/common/http';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const isPublicRequest = req.url.includes('/public');

  if (!isPublicRequest) {
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

  return next(req);
};