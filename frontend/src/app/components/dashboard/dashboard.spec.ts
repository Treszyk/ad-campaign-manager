import { ComponentFixture, TestBed } from '@angular/core/testing';
import { signal } from '@angular/core';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { Dashboard } from './dashboard';
import { SellerService } from '../../services/seller.service';
import { Seller } from '../../models/seller.model';
import { EmeraldAccount } from '../../models/emerald-account.model';
import { Product } from '../../models/product.model';

describe('Dashboard', () => {
  let component: Dashboard;
  let fixture: ComponentFixture<Dashboard>;
  let httpTesting: HttpTestingController;
  let sellerServiceMock: Partial<SellerService>;

  const mockSelectedSeller: Seller = {
    id: 1,
    firstName: 'Alice',
    lastName: 'Smith',
  };

  const mockAccounts: EmeraldAccount[] = [
    { id: 101, accountName: 'Default Wallet', balance: 5000.0, sellerId: 1 },
  ];

  const mockProducts: Product[] = [
    {
      id: 201,
      name: 'Premium Headphones',
      description: 'Wireless premium noise-cancelling headphones',
      sellerId: 1,
    },
  ];

  beforeEach(async () => {
    sellerServiceMock = {
      sellers: signal<Seller[]>([mockSelectedSeller]),
      selectedSellerId: signal<number | null>(1),
      selectedSeller: signal<Seller | null>(mockSelectedSeller),
      accounts: signal<EmeraldAccount[]>(mockAccounts),
      products: signal<Product[]>(mockProducts),
    };

    await TestBed.configureTestingModule({
      imports: [Dashboard],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        { provide: SellerService, useValue: sellerServiceMock as SellerService },
      ],
    }).compileComponents();

    httpTesting = TestBed.inject(HttpTestingController);

    fixture = TestBed.createComponent(Dashboard);
    component = fixture.componentInstance;
    fixture.detectChanges();

    httpTesting.match('/api/metadata/themes').forEach((req) => req.flush([]));
    httpTesting.match('/api/metadata/keywords').forEach((req) => req.flush([]));
    httpTesting.match('/api/metadata/towns').forEach((req) => req.flush([]));
    httpTesting.match('/api/campaigns').forEach((req) => req.flush([]));
  });

  afterEach(() => {
    httpTesting.verify();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should render selected seller greeting', () => {
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('.seller-greeting h2')?.textContent).toContain(
      'Partner Portal: Alice Smith',
    );
  });

  it('should render wallets and products count in badges', () => {
    const compiled = fixture.nativeElement as HTMLElement;
    const badges = compiled.querySelectorAll('.badge');
    expect(badges.length).toBe(2);
    expect(badges[0].textContent).toContain('1 wallets');
    expect(badges[1].textContent).toContain('1 items');
  });

  it('should render wallet list', () => {
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('.wallet-name')?.textContent).toContain('Default Wallet');
    expect(compiled.querySelector('.balance-value')?.textContent).toContain('5,000.00');
  });

  it('should render product catalog list', () => {
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('.product-name')?.textContent).toContain('Premium Headphones');
    expect(compiled.querySelector('.product-desc')?.textContent).toContain(
      'Wireless premium noise-cancelling headphones',
    );
  });
});
