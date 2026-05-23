import { ComponentFixture, TestBed } from '@angular/core/testing';
import { signal } from '@angular/core';
import { ProfileSwitcher } from './profile-switcher';
import { SellerService } from '../../services/seller.service';
import { Seller } from '../../models/seller.model';
import { EmeraldAccount } from '../../models/emerald-account.model';
import { Product } from '../../models/product.model';

describe('ProfileSwitcher', () => {
  let component: ProfileSwitcher;
  let fixture: ComponentFixture<ProfileSwitcher>;
  let sellerServiceMock: Partial<SellerService>;

  const mockSellers: Seller[] = [
    { id: 1, firstName: 'Alice', lastName: 'Smith' },
    { id: 2, firstName: 'Bob', lastName: 'Jones' },
  ];

  beforeEach(async () => {
    sellerServiceMock = {
      sellers: signal<Seller[]>(mockSellers),
      selectedSellerId: signal<number | null>(null),
      selectedSeller: signal<Seller | null>(null),
      accounts: signal<EmeraldAccount[]>([]),
      products: signal<Product[]>([]),
      selectSeller: vi.fn(),
    };

    await TestBed.configureTestingModule({
      imports: [ProfileSwitcher],
      providers: [{ provide: SellerService, useValue: sellerServiceMock as SellerService }],
    }).compileComponents();

    fixture = TestBed.createComponent(ProfileSwitcher);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should render sellers list when trigger is clicked', () => {
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('.dropdown-menu')).toBeNull();

    component.toggleDropdown();
    fixture.detectChanges();

    const menu = compiled.querySelector('.dropdown-menu');
    expect(menu).toBeTruthy();

    const items = compiled.querySelectorAll('.dropdown-item:not(.clear-btn)');
    expect(items.length).toBe(2);
    expect(items[0].textContent).toContain('Alice Smith');
  });

  it('should trigger selectSeller when item clicked', () => {
    component.toggleDropdown();
    fixture.detectChanges();

    const compiled = fixture.nativeElement as HTMLElement;
    const firstItem = compiled.querySelector('.dropdown-item') as HTMLButtonElement;
    firstItem.click();

    expect(sellerServiceMock.selectSeller).toHaveBeenCalledWith(1);
  });
});
