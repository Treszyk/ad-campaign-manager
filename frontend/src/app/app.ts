import { Component, inject } from '@angular/core';
import { DecimalPipe } from '@angular/common';
import { SellerService } from './services/seller.service';
import { CampaignService } from './services/campaign.service';
import { MetadataService } from './services/metadata.service';
import { ProfileSwitcher } from './components/profile-switcher/profile-switcher';
import { Dashboard } from './components/dashboard/dashboard';

@Component({
  selector: 'app-root',
  imports: [ProfileSwitcher, Dashboard, DecimalPipe],
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class App {
  protected readonly sellerService = inject(SellerService);
  protected readonly campaignService = inject(CampaignService);
  protected readonly metadataService = inject(MetadataService);
}
