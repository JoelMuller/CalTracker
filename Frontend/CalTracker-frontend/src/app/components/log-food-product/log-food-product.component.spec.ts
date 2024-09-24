import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LogFoodProductComponent } from './log-food-product.component';

describe('LogFoodProductComponent', () => {
  let component: LogFoodProductComponent;
  let fixture: ComponentFixture<LogFoodProductComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LogFoodProductComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(LogFoodProductComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
