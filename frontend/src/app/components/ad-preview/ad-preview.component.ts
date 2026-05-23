import { Component, inject, input, ElementRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AdTheme } from '../../models/campaign.model';
import html2canvas from 'html2canvas';

@Component({
  selector: 'app-ad-preview',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './ad-preview.component.html',
  styleUrl: './ad-preview.component.css',
})
export class AdPreviewComponent {
  private elementRef = inject(ElementRef);

  campaignName = input<string>('');
  productName = input.required<string>();
  productDescription = input.required<string>();
  adTheme = input.required<AdTheme>();
  town = input<string>('');
  radiusKm = input<number>(0);

  downloadPng(): void {
    const cardElement = this.elementRef.nativeElement.querySelector('.preview-card') as HTMLElement;
    if (!cardElement) return;

    cardElement.classList.add('export-forcing');

    html2canvas(cardElement, {
      scale: 2,
      useCORS: true,
      backgroundColor: null,
    })
      .then((canvas) => {
        cardElement.classList.remove('export-forcing');

        const dataUrl = canvas.toDataURL('image/png');
        const link = document.createElement('a');
        link.href = dataUrl;
        link.download = `${this.productName().toLowerCase().replace(/\s+/g, '-')}-ad.png`;
        link.click();
      })
      .catch((err) => {
        cardElement.classList.remove('export-forcing');
        console.error('Failed to export PNG mockup:', err);
      });
  }
}
