import { Component, inject, signal } from '@angular/core';
import { DecimalPipe } from '@angular/common';
import { SellerService } from '../../services/seller.service';
import { CampaignService } from '../../services/campaign.service';
import { Product } from '../../models/product.model';
import { Campaign } from '../../models/campaign.model';
import { CampaignModalComponent } from '../campaign-modal/campaign-modal.component';

@Component({
  selector: 'app-dashboard',
  imports: [DecimalPipe, CampaignModalComponent],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css',
})
export class Dashboard {
  protected readonly sellerService = inject(SellerService);
  protected readonly campaignService = inject(CampaignService);

  activeModalContext = signal<{ product: Product; campaign: Campaign | undefined } | null>(null);

  getProductCampaign(productId: number): Campaign | undefined {
    return this.campaignService.campaigns().find((c) => c.productId === productId);
  }

  getProductName(productId: number): string {
    return (
      this.sellerService.products().find((p) => p.id === productId)?.name ?? `Product #${productId}`
    );
  }

  getAccountName(accountId: number): string {
    return (
      this.sellerService.accounts().find((a) => a.id === accountId)?.accountName ??
      `Account #${accountId}`
    );
  }

  openCampaign(campaign: Campaign): void {
    const product = this.sellerService.products().find((p) => p.id === campaign.productId);
    if (product) {
      this.activeModalContext.set({ product, campaign });
    }
  }
}
