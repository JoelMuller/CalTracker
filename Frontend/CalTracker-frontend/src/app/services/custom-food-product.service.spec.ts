import { TestBed } from '@angular/core/testing';

import { CustomFoodProductService } from './custom-food-product.service';

describe('CustomFoodProductService', () => {
  let service: CustomFoodProductService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CustomFoodProductService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
