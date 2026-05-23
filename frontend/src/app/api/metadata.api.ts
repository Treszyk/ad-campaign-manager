import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AdTheme } from '../models/campaign.model';

@Injectable({
  providedIn: 'root',
})
export class MetadataApi {
  private http = inject(HttpClient);
  private baseUrl = '/api/metadata';

  getThemes(): Observable<AdTheme[]> {
    return this.http.get<AdTheme[]>(`${this.baseUrl}/themes`);
  }

  getKeywords(): Observable<string[]> {
    return this.http.get<string[]>(`${this.baseUrl}/keywords`);
  }

  getTowns(): Observable<string[]> {
    return this.http.get<string[]>(`${this.baseUrl}/towns`);
  }
}
