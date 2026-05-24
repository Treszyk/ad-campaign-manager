import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, throwError } from 'rxjs';
import { ToastService } from './toast.service';

export const httpErrorInterceptor: HttpInterceptorFn = (req, next) => {
  const toastService = inject(ToastService);

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      let errorMessage: string;

      if (error.error instanceof ErrorEvent) {
        errorMessage = `Client error: ${error.error.message}`;
      } else if (error.status === 0) {
        errorMessage = 'Network connection failed. Please check if the backend server is running.';
      } else if (error.error && typeof error.error === 'string') {
        errorMessage = error.error;
      } else if (error.error && error.error.message) {
        errorMessage = error.error.message;
      } else {
        errorMessage = error.message || 'An unexpected error occurred.';
      }

      toastService.error(errorMessage);
      return throwError(() => error);
    }),
  );
};
