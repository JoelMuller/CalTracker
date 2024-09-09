import { TestBed } from '@angular/core/testing';

import { FoodProductService } from './food-product.service';

describe('FoodProductService', () => {
  let service: FoodProductService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FoodProductService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
