import { TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { MetadataService } from './metadata.service';
import { MetadataApi } from '../api/metadata.api';
import { AdTheme } from '../models/campaign.model';

describe('MetadataService', () => {
  let service: MetadataService;
  let metadataApiMock: Partial<MetadataApi>;

  const mockThemes: AdTheme[] = [
    'PASTEL_MINT',
    'PASTEL_BLUE',
    'PASTEL_CORAL',
    'PASTEL_LAVENDER',
    'PASTEL_ROSE',
  ];
  const mockKeywords: string[] = ['headphones', 'wireless', 'bluetooth'];
  const mockTowns: string[] = ['Warszawa', 'Kraków', 'Wrocław'];

  beforeEach(() => {
    metadataApiMock = {
      getThemes: () => of(mockThemes),
      getKeywords: () => of(mockKeywords),
      getTowns: () => of(mockTowns),
    };

    TestBed.configureTestingModule({
      providers: [
        MetadataService,
        { provide: MetadataApi, useValue: metadataApiMock as MetadataApi },
      ],
    });

    service = TestBed.inject(MetadataService);
  });

  it('should be created and load metadata on init into signals', () => {
    expect(service).toBeTruthy();
    expect(service.themes()).toEqual(mockThemes);
    expect(service.keywords()).toEqual(mockKeywords);
    expect(service.towns()).toEqual(mockTowns);
  });
});
