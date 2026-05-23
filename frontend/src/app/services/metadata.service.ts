import { Injectable, inject } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { MetadataApi } from '../api/metadata.api';
import { AdTheme } from '../models/campaign.model';

@Injectable({
  providedIn: 'root',
})
export class MetadataService {
  private metadataApi = inject(MetadataApi);

  themes = toSignal(this.metadataApi.getThemes(), { initialValue: [] as AdTheme[] });
  keywords = toSignal(this.metadataApi.getKeywords(), { initialValue: [] as string[] });
  towns = toSignal(this.metadataApi.getTowns(), { initialValue: [] as string[] });
}
