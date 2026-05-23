import { Component, inject, signal, ElementRef, HostListener } from '@angular/core';
import { SellerService } from '../../services/seller.service';

@Component({
  selector: 'app-profile-switcher',
  imports: [],
  templateUrl: './profile-switcher.html',
  styleUrl: './profile-switcher.css',
})
export class ProfileSwitcher {
  protected readonly sellerService = inject(SellerService);
  protected readonly isOpen = signal(false);
  private elementRef = inject(ElementRef);

  toggleDropdown(): void {
    this.isOpen.update((v) => !v);
  }

  selectSeller(id: number | null): void {
    this.sellerService.selectSeller(id);
    this.isOpen.set(false);
  }

  @HostListener('document:click', ['$event'])
  onDocumentClick(event: MouseEvent): void {
    if (!this.elementRef.nativeElement.contains(event.target)) {
      this.isOpen.set(false);
    }
  }
}
