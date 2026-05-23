import { Injectable, inject, signal, computed, effect } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { SellerApi } from '../api/seller.api';
import { Seller } from '../models/seller.model';
import { EmeraldAccount } from '../models/emerald-account.model';
import { Product } from '../models/product.model';

@Injectable({
  providedIn: 'root',
})
export class SellerService {
  private sellerApi = inject(SellerApi);

  sellers = toSignal(this.sellerApi.getSellers(), { initialValue: [] as Seller[] });

  selectedSellerId = signal<number | null>(null);

  selectedSeller = computed(() => {
    const id = this.selectedSellerId();
    return this.sellers().find((s) => s.id === id) ?? null;
  });

  accounts = signal<EmeraldAccount[]>([]);
  products = signal<Product[]>([]);

  constructor() {
    effect(
      () => {
        const id = this.selectedSellerId();
        if (id !== null) {
          this.sellerApi.getSellerAccounts(id).subscribe((accs) => this.accounts.set(accs));
          this.sellerApi.getSellerProducts(id).subscribe((prods) => this.products.set(prods));
        } else {
          this.accounts.set([]);
          this.products.set([]);
        }
      },
      { allowSignalWrites: true },
    );
  }

  selectSeller(id: number | null): void {
    this.selectedSellerId.set(id);
  }

  refreshAccounts(): void {
    const id = this.selectedSellerId();
    if (id !== null) {
      this.sellerApi.getSellerAccounts(id).subscribe((accs) => this.accounts.set(accs));
    }
  }
}
