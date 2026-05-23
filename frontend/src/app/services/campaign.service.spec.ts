import { TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { signal } from '@angular/core';
import { CampaignService } from './campaign.service';
import { CampaignApi } from '../api/campaign.api';
import { SellerService } from './seller.service';
import { Campaign, CreateCampaignRequest, UpdateCampaignRequest } from '../models/campaign.model';

describe('CampaignService', () => {
  let service: CampaignService;
  let campaignApiMock: Partial<CampaignApi>;
  let sellerServiceMock: Partial<SellerService>;

  const mockCampaigns: Campaign[] = [
    {
      id: 100,
      name: 'Black Friday Campaign',
      keywords: ['headphones', 'wireless'],
      bidAmount: 1.5,
      campaignFund: 200,
      status: true,
      town: 'Kraków',
      radiusKm: 15,
      adTheme: 'PASTEL_MINT',
      sellerId: 1,
      productId: 20,
      emeraldAccountId: 10,
    },
    {
      id: 101,
      name: 'Easter Sale',
      keywords: ['premium', 'music'],
      bidAmount: 1.0,
      campaignFund: 100,
      status: true,
      town: 'Warszawa',
      radiusKm: 10,
      adTheme: 'PASTEL_CORAL',
      sellerId: 2,
      productId: 21,
      emeraldAccountId: 11,
    },
  ];

  beforeEach(() => {
    campaignApiMock = {
      getCampaigns: () => of(mockCampaigns),
      createCampaign: () => of(mockCampaigns[0]),
      updateCampaign: () => of(mockCampaigns[0]),
      deleteCampaign: () => of(void 0),
    };

    sellerServiceMock = {
      selectedSellerId: signal<number | null>(1),
      refreshAccounts: vi.fn(),
    };

    TestBed.configureTestingModule({
      providers: [
        CampaignService,
        { provide: CampaignApi, useValue: campaignApiMock as CampaignApi },
        { provide: SellerService, useValue: sellerServiceMock as SellerService },
      ],
    });

    service = TestBed.inject(CampaignService);
  });

  it('should load all campaigns and expose them as readonly signal', () => {
    expect(service.campaigns()).toEqual(mockCampaigns);
  });

  it('should filter campaigns by active seller ID using computed signal', () => {
    expect(service.filteredCampaigns()).toEqual([mockCampaigns[0]]);
  });

  it('should create campaign, refresh list, and update seller balances', () => {
    const newRequest: CreateCampaignRequest = {
      name: 'Spring Sale',
      keywords: ['wireless', 'sale'],
      bidAmount: 2.0,
      campaignFund: 150,
      status: true,
      town: 'Gdańsk',
      radiusKm: 5,
      adTheme: 'PASTEL_BLUE',
      sellerId: 1,
      productId: 20,
      emeraldAccountId: 10,
    };

    service.createCampaign(newRequest);
    expect(sellerServiceMock.refreshAccounts).toHaveBeenCalled();
  });

  it('should update campaign, refresh list, and update seller balances', () => {
    const updateRequest: UpdateCampaignRequest = {
      name: 'Updated Name',
      keywords: ['wireless', 'sale'],
      bidAmount: 2.0,
      campaignFund: 150,
      status: true,
      town: 'Gdańsk',
      radiusKm: 5,
      adTheme: 'PASTEL_BLUE',
    };

    service.updateCampaign(100, updateRequest);
    expect(sellerServiceMock.refreshAccounts).toHaveBeenCalled();
  });

  it('should delete campaign, refresh list, and update seller balances', () => {
    service.deleteCampaign(100);
    expect(sellerServiceMock.refreshAccounts).toHaveBeenCalled();
  });
});
