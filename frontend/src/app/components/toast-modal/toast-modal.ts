import { Component, inject } from '@angular/core';
import { ToastService } from '../../services/toast.service';
import { ToastType } from '../../models/toast.model';

@Component({
  selector: 'app-toast-modal',
  imports: [],
  templateUrl: './toast-modal.html',
  styleUrl: './toast-modal.css',
})
export class ToastModal {
  protected readonly toastService = inject(ToastService);
  protected readonly ToastType = ToastType;
}
