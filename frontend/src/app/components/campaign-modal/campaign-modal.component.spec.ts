import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Component, signal } from '@angular/core';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { CampaignModalComponent } from './campaign-modal.component';
import { SellerService } from '../../services/seller.service';
import { Seller } from '../../models/seller.model';
import { EmeraldAccount } from '../../models/emerald-account.model';
import { Product } from '../../models/product.model';

@Component({
  template: `<app-campaign-modal [product]="product" (modalClose)="closed = true" />`,
  imports: [CampaignModalComponent],
})
class TestHostComponent {
  product: Product = { id: 1, name: 'Test Product', description: 'A test', sellerId: 1 };
  closed = false;
}

describe('CampaignModalComponent', () => {
  let fixture: ComponentFixture<TestHostComponent>;
  let httpTesting: HttpTestingController;

  const sellerServiceMock: Partial<SellerService> = {
    sellers: signal<Seller[]>([{ id: 1, firstName: 'Test', lastName: 'User' }]),
    selectedSellerId: signal<number | null>(1),
    selectedSeller: signal<Seller | null>({ id: 1, firstName: 'Test', lastName: 'User' }),
    accounts: signal<EmeraldAccount[]>([
      { id: 100, accountName: 'Main', balance: 500, sellerId: 1 },
    ]),
    products: signal<Product[]>([
      { id: 1, name: 'Test Product', description: 'A test', sellerId: 1 },
    ]),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TestHostComponent],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        { provide: SellerService, useValue: sellerServiceMock as SellerService },
      ],
    }).compileComponents();

    httpTesting = TestBed.inject(HttpTestingController);

    fixture = TestBed.createComponent(TestHostComponent);
    fixture.detectChanges();

    httpTesting.match('/api/metadata/themes').forEach((req) => req.flush(['PASTEL_MINT']));
    httpTesting.match('/api/metadata/keywords').forEach((req) => req.flush(['headphones']));
    httpTesting.match('/api/metadata/towns').forEach((req) => req.flush(['Warszawa']));
    httpTesting.match((r) => r.url.startsWith('/api/campaigns')).forEach((req) => req.flush([]));

    fixture.detectChanges();
  });

  afterEach(() => {
    httpTesting.verify();
  });

  it('should create', () => {
    const modal = fixture.nativeElement.querySelector('app-campaign-modal');
    expect(modal).toBeTruthy();
  });
});
