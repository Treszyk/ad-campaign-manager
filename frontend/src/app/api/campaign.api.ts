import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Campaign, CreateCampaignRequest, UpdateCampaignRequest } from '../models/campaign.model';

@Injectable({
  providedIn: 'root',
})
export class CampaignApi {
  private http = inject(HttpClient);
  private baseUrl = '/api/campaigns';

  getCampaigns(): Observable<Campaign[]> {
    return this.http.get<Campaign[]>(this.baseUrl);
  }

  getCampaignById(id: number): Observable<Campaign> {
    return this.http.get<Campaign>(`${this.baseUrl}/${id}`);
  }

  createCampaign(request: CreateCampaignRequest): Observable<Campaign> {
    return this.http.post<Campaign>(this.baseUrl, request);
  }

  updateCampaign(id: number, request: UpdateCampaignRequest): Observable<Campaign> {
    return this.http.put<Campaign>(`${this.baseUrl}/${id}`, request);
  }

  deleteCampaign(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
