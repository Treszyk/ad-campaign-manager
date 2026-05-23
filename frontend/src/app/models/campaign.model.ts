export type AdTheme =
  | 'PASTEL_MINT'
  | 'PASTEL_BLUE'
  | 'PASTEL_CORAL'
  | 'PASTEL_LAVENDER'
  | 'PASTEL_ROSE';

export interface Campaign {
  id: number;
  name: string;
  keywords: string[];
  bidAmount: number;
  campaignFund: number;
  status: boolean;
  town: string;
  radiusKm: number;
  adTheme: AdTheme;
  sellerId: number;
  productId: number;
  emeraldAccountId: number;
}

export interface CreateCampaignRequest {
  name: string;
  keywords: string[];
  bidAmount: number;
  campaignFund: number;
  status: boolean;
  town: string;
  radiusKm: number;
  adTheme: AdTheme;
  sellerId: number;
  productId: number;
  emeraldAccountId: number;
}

export interface UpdateCampaignRequest {
  name: string;
  keywords: string[];
  bidAmount: number;
  campaignFund: number;
  status: boolean;
  town: string;
  radiusKm: number;
  adTheme: AdTheme;
}
