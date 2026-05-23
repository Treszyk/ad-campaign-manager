import { TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { SellerService } from './seller.service';
import { SellerApi } from '../api/seller.api';
import { Seller } from '../models/seller.model';
import { EmeraldAccount } from '../models/emerald-account.model';
import { Product } from '../models/product.model';

describe('SellerService', () => {
  let service: SellerService;
  let sellerApiMock: Partial<SellerApi>;

  const mockSellers: Seller[] = [
    { id: 1, firstName: 'Alice', lastName: 'Smith' },
    { id: 2, firstName: 'Bob', lastName: 'Jones' },
  ];

  const mockAccounts: EmeraldAccount[] = [
    { id: 10, accountName: 'Alice Wallet', balance: 5000, sellerId: 1 },
  ];

  const mockProducts: Product[] = [
    { id: 20, name: 'Laptop', description: 'Powerful laptop', sellerId: 1 },
  ];

  beforeEach(() => {
    sellerApiMock = {
      getSellers: () => of(mockSellers),
      getSellerAccounts: () => of(mockAccounts),
      getSellerProducts: () => of(mockProducts),
    };

    TestBed.configureTestingModule({
      providers: [SellerService, { provide: SellerApi, useValue: sellerApiMock as SellerApi }],
    });

    service = TestBed.inject(SellerService);
  });

  it('should be created and load sellers on init', () => {
    expect(service).toBeTruthy();
    expect(service.sellers()).toEqual(mockSellers);
    expect(service.selectedSellerId()).toBeNull();
    expect(service.selectedSeller()).toBeNull();
  });

  it('should select seller and compute selectedSeller details', () => {
    service.selectSeller(1);
    expect(service.selectedSellerId()).toBe(1);
    expect(service.selectedSeller()).toEqual(mockSellers[0]);
  });

  it('should trigger API loads when seller id changes', async () => {
    service.selectSeller(1);

    await TestBed.runInInjectionContext(() => {
      return new Promise<void>((resolve) => {
        setTimeout(() => {
          expect(service.accounts()).toEqual(mockAccounts);
          expect(service.products()).toEqual(mockProducts);
          resolve();
        }, 10);
      });
    });
  });

  it('should clear accounts and products when seller is unselected', () => {
    service.selectSeller(null);
    expect(service.accounts()).toEqual([]);
    expect(service.products()).toEqual([]);
  });
});
