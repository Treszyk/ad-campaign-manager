import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Seller } from '../models/seller.model';
import { EmeraldAccount } from '../models/emerald-account.model';
import { Product } from '../models/product.model';

@Injectable({
  providedIn: 'root',
})
export class SellerApi {
  private http = inject(HttpClient);
  private baseUrl = '/api/sellers';

  getSellers(): Observable<Seller[]> {
    return this.http.get<Seller[]>(this.baseUrl);
  }

  getSellerById(id: number): Observable<Seller> {
    return this.http.get<Seller>(`${this.baseUrl}/${id}`);
  }

  getSellerAccounts(sellerId: number): Observable<EmeraldAccount[]> {
    return this.http.get<EmeraldAccount[]>(`${this.baseUrl}/${sellerId}/accounts`);
  }

  getSellerProducts(sellerId: number): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.baseUrl}/${sellerId}/products`);
  }
}
