import { Injectable, signal } from '@angular/core';
import { Toast, ToastType } from '../models/toast.model';

@Injectable({
  providedIn: 'root',
})
export class ToastService {
  private readonly toastList = signal<Toast[]>([]);
  readonly toasts = this.toastList.asReadonly();

  show(message: string, type: ToastType = ToastType.INFO, duration = 4000): void {
    const id = crypto.randomUUID();
    const newToast: Toast = { id, message, type, duration, isLeaving: false };
    this.toastList.update((all) => [...all, newToast]);

    setTimeout(() => {
      this.dismiss(id);
    }, duration);
  }

  success(message: string, duration = 4000): void {
    this.show(message, ToastType.SUCCESS, duration);
  }

  error(message: string, duration = 5000): void {
    this.show(message, ToastType.ERROR, duration);
  }

  info(message: string, duration = 4000): void {
    this.show(message, ToastType.INFO, duration);
  }

  dismiss(id: string): void {
    this.toastList.update((all) => all.map((t) => (t.id === id ? { ...t, isLeaving: true } : t)));
    setTimeout(() => {
      this.remove(id);
    }, 300);
  }

  remove(id: string): void {
    this.toastList.update((all) => all.filter((t) => t.id !== id));
  }
}
