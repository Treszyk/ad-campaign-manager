import { TestBed } from '@angular/core/testing';
import { signal } from '@angular/core';
import { App } from './app';
import { SellerService } from './services/seller.service';
import { CampaignService } from './services/campaign.service';
import { MetadataService } from './services/metadata.service';
import { Seller } from './models/seller.model';
import { EmeraldAccount } from './models/emerald-account.model';
import { Product } from './models/product.model';

describe('App', () => {
  let sellerServiceMock: Partial<SellerService>;
  let campaignServiceMock: Partial<CampaignService>;
  let metadataServiceMock: Partial<MetadataService>;

  beforeEach(async () => {
    sellerServiceMock = {
      sellers: signal<Seller[]>([]),
      selectedSellerId: signal<number | null>(null),
      selectedSeller: signal<Seller | null>(null),
      accounts: signal<EmeraldAccount[]>([]),
      totalBalance: signal<number>(0),
      products: signal<Product[]>([]),
    };

    campaignServiceMock = {
      campaigns: signal([]),
    };

    metadataServiceMock = {
      themes: signal([]),
      keywords: signal([]),
      towns: signal([]),
    };

    await TestBed.configureTestingModule({
      imports: [App],
      providers: [
        { provide: SellerService, useValue: sellerServiceMock as SellerService },
        { provide: CampaignService, useValue: campaignServiceMock as CampaignService },
        { provide: MetadataService, useValue: metadataServiceMock as MetadataService },
      ],
    }).compileComponents();
  });

  it('should create the app', () => {
    const fixture = TestBed.createComponent(App);
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });

  it('should render welcome typographic screen when no seller is selected', () => {
    const fixture = TestBed.createComponent(App);
    fixture.detectChanges();
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('.welcome-title')?.textContent).toContain(
      'Welcome to Emerald Ad Manager',
    );
  });
});
