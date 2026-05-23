import { Injectable, inject, signal, computed } from '@angular/core';
import { CampaignApi } from '../api/campaign.api';
import { SellerService } from './seller.service';
import { Campaign, CreateCampaignRequest, UpdateCampaignRequest } from '../models/campaign.model';

@Injectable({
  providedIn: 'root',
})
export class CampaignService {
  private campaignApi = inject(CampaignApi);
  private sellerService = inject(SellerService);

  private campaignsList = signal<Campaign[]>([]);

  campaigns = this.campaignsList.asReadonly();

  filteredCampaigns = computed(() => {
    const sellerId = this.sellerService.selectedSellerId();
    if (sellerId === null) return [];
    return this.campaignsList().filter((c) => c.sellerId === sellerId);
  });

  constructor() {
    this.refreshCampaigns();
  }

  refreshCampaigns(): void {
    this.campaignApi.getCampaigns().subscribe({
      next: (list) => this.campaignsList.set(list),
      error: (err) => console.error('Failed to load campaigns', err),
    });
  }

  createCampaign(request: CreateCampaignRequest): void {
    this.campaignApi.createCampaign(request).subscribe({
      next: () => {
        this.refreshCampaigns();
        this.sellerService.refreshAccounts();
      },
      error: (err) => console.error('Failed to create campaign', err),
    });
  }

  updateCampaign(id: number, request: UpdateCampaignRequest): void {
    this.campaignApi.updateCampaign(id, request).subscribe({
      next: () => {
        this.refreshCampaigns();
        this.sellerService.refreshAccounts();
      },
      error: (err) => console.error('Failed to update campaign', err),
    });
  }

  deleteCampaign(id: number): void {
    this.campaignApi.deleteCampaign(id).subscribe({
      next: () => {
        this.refreshCampaigns();
        this.sellerService.refreshAccounts();
      },
      error: (err) => console.error('Failed to delete campaign', err),
    });
  }
}
