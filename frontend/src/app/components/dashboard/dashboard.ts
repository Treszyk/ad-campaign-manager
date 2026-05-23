import { Component, inject } from '@angular/core';
import { DecimalPipe } from '@angular/common';
import { SellerService } from '../../services/seller.service';

@Component({
  selector: 'app-dashboard',
  imports: [DecimalPipe],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css',
})
export class Dashboard {
  protected readonly sellerService = inject(SellerService);
}
