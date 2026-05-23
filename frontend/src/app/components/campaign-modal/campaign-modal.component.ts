import {
  Component,
  inject,
  input,
  output,
  signal,
  OnInit,
  OnDestroy,
  HostListener,
  ElementRef,
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Product } from '../../models/product.model';
import { Campaign } from '../../models/campaign.model';
import { CampaignService } from '../../services/campaign.service';
import { SellerService } from '../../services/seller.service';
import { MetadataService } from '../../services/metadata.service';
import { AdPreviewComponent } from '../ad-preview/ad-preview.component';

@Component({
  selector: 'app-campaign-modal',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, AdPreviewComponent],
  templateUrl: './campaign-modal.component.html',
  styleUrl: './campaign-modal.component.css',
})
export class CampaignModalComponent implements OnInit, OnDestroy {
  private fb = inject(FormBuilder);
  private elementRef = inject(ElementRef);
  protected readonly campaignService = inject(CampaignService);
  protected readonly sellerService = inject(SellerService);
  protected readonly metadataService = inject(MetadataService);

  product = input.required<Product>();
  campaign = input<Campaign | undefined>(undefined);
  modalClose = output<void>();

  private isBackdropMouseDown = false;

  protected onBackdropMouseDown(event: MouseEvent): void {
    this.isBackdropMouseDown = event.target === event.currentTarget;
  }

  protected onBackdropClick(event: MouseEvent): void {
    if (this.isBackdropMouseDown && event.target === event.currentTarget) {
      this.modalClose.emit();
    }
    this.isBackdropMouseDown = false;
  }

  protected readonly isEditor = signal(false);
  protected readonly showDeleteConfirm = signal(false);
  protected readonly activeMobileTab = signal<'form' | 'preview'>('form');

  campaignForm!: FormGroup;

  protected readonly selectedKeywords = signal<string[]>([]);
  protected readonly keywordSearchQuery = signal('');
  protected readonly showKeywordsDropdown = signal(false);

  @HostListener('document:click', ['$event'])
  onDocumentClick(event: MouseEvent): void {
    const clickedInside = this.elementRef.nativeElement
      .querySelector('.keywords-form-group')
      ?.contains(event.target);
    if (!clickedInside) {
      this.showKeywordsDropdown.set(false);
    }
  }

  ngOnInit(): void {
    document.body.style.overflow = 'hidden';

    const existingCampaign = this.campaign();
    this.selectedKeywords.set(existingCampaign ? [...existingCampaign.keywords] : []);

    this.initForm(existingCampaign);

    if (!existingCampaign) {
      this.isEditor.set(true);
    }
  }

  ngOnDestroy(): void {
    document.body.style.overflow = '';
  }

  private initForm(c: Campaign | undefined): void {
    const activeWallets = this.sellerService.accounts();
    const defaultAccountId =
      c?.emeraldAccountId ?? (activeWallets.length > 0 ? activeWallets[0].id : '');
    const activeThemes = this.metadataService.themes();
    const defaultTheme = c?.adTheme ?? (activeThemes.length > 0 ? activeThemes[0] : 'PASTEL_MINT');

    this.campaignForm = this.fb.group({
      name: [c?.name ?? '', [Validators.required]],
      emeraldAccountId: [{ value: defaultAccountId, disabled: !!c }, [Validators.required]],
      bidAmount: [c?.bidAmount ?? 1.0, [Validators.required, Validators.min(1.0)]],
      campaignFund: [c?.campaignFund ?? 100.0, [Validators.required, Validators.min(0.01)]],
      status: [c?.status ?? true, [Validators.required]],
      town: [c?.town ?? '', [Validators.required]],
      radiusKm: [c?.radiusKm ?? 10, [Validators.required, Validators.min(0)]],
      adTheme: [defaultTheme, [Validators.required]],
    });
  }

  getFilteredKeywords(): string[] {
    const query = this.keywordSearchQuery().toLowerCase().trim();
    const selected = this.selectedKeywords();
    const all = this.metadataService.keywords();
    return all.filter((k) => k.toLowerCase().includes(query) && !selected.includes(k));
  }

  addKeyword(keyword: string): void {
    if (keyword && !this.selectedKeywords().includes(keyword)) {
      this.selectedKeywords.update((kws) => [...kws, keyword]);
    }
    this.keywordSearchQuery.set('');
    this.showKeywordsDropdown.set(false);
  }

  removeKeyword(keyword: string): void {
    this.selectedKeywords.update((kws) => kws.filter((k) => k !== keyword));
  }

  toggleEditMode(): void {
    this.isEditor.update((v) => !v);
    if (!this.isEditor()) {
      this.initForm(this.campaign());
      this.selectedKeywords.set(this.campaign() ? [...this.campaign()!.keywords] : []);
    }
  }

  onSave(): void {
    if (this.campaignForm.invalid || this.selectedKeywords().length === 0) {
      this.campaignForm.markAllAsTouched();
      return;
    }

    const formVal = this.campaignForm.value;
    const sellerId = this.sellerService.selectedSellerId();
    const prodId = this.product().id;

    if (sellerId === null) return;

    const payload = {
      name: formVal.name,
      keywords: this.selectedKeywords(),
      bidAmount: formVal.bidAmount,
      campaignFund: formVal.campaignFund,
      status: formVal.status,
      town: formVal.town,
      radiusKm: formVal.radiusKm,
      adTheme: formVal.adTheme,
    };

    const existingCampaign = this.campaign();
    if (existingCampaign) {
      this.campaignService.updateCampaign(existingCampaign.id, payload);
    } else {
      this.campaignService.createCampaign({
        ...payload,
        sellerId,
        productId: prodId,
        emeraldAccountId: Number(formVal.emeraldAccountId),
      });
    }

    this.modalClose.emit();
  }

  onDelete(): void {
    const existingCampaign = this.campaign();
    if (existingCampaign) {
      this.campaignService.deleteCampaign(existingCampaign.id);
    }
    this.modalClose.emit();
  }
}
