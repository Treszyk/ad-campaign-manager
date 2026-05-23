import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Component } from '@angular/core';
import { AdPreviewComponent } from './ad-preview.component';

@Component({
  template: `<app-ad-preview
    [productName]="'Test Product'"
    [productDescription]="'A test description'"
    [adTheme]="'PASTEL_MINT'"
  />`,
  imports: [AdPreviewComponent],
})
class TestHostComponent {}

describe('AdPreviewComponent', () => {
  let fixture: ComponentFixture<TestHostComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TestHostComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(TestHostComponent);
    fixture.detectChanges();
  });

  it('should create', () => {
    const adPreview = fixture.nativeElement.querySelector('app-ad-preview');
    expect(adPreview).toBeTruthy();
  });
});
