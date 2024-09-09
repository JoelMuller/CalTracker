import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment.development';
import { HttpClient, HttpParams } from '@angular/common/http';
import { map, Observable } from 'rxjs';
import { FoodProduct } from '../models/food-product.model';
import { SearchResults } from '../models/search-results.model';
import { CustomFoodProduct } from '../models/custom-food-product.model';

@Injectable({
  providedIn: 'root'
})
export class FoodProductService {
  private readonly apiRoute = environment.apiUrl + "/food-item";

  constructor(private http: HttpClient) { }

  logFoodItemBarcode(foodProductBarcode: string, date: Date, userId: number) {
    this.http.post<any>(`${this.apiRoute}/log`, {
      "date": date.toJSON().split('T')[0],
      "userId": userId,
      "foodProductBarcode": foodProductBarcode
    })
  }

  logCustomFoodItem(customFoodProduct: CustomFoodProduct, date: Date, userId: number){
    this.http.post<any>(`${this.apiRoute}/log`, {
      "date": date.toJSON().split('T')[0],
      "userId": userId,
      "customFoodProduct": {
        "id": customFoodProduct.id,
        "product_name": customFoodProduct.productName,
        "serving_size": customFoodProduct.servingSize,
        "nutriments": customFoodProduct.nutriments
      }
    })
  }

  searchFoodItems(searchTerms: string, page: number): Observable<SearchResults> {
    let params = new HttpParams().set('search_terms', searchTerms).set('page', page);
    return this.http.get<any>(`${this.apiRoute}/search`, { params })
      .pipe(
        map((response: any) => ({
          count: response.count,
          page: response.page,
          products: response.products.map((foodProduct: any) => ({
            id: foodProduct._id,
            productName: foodProduct.product_name,
            categories: foodProduct.categories,
            servingSize: foodProduct.serving_size,
            nutriments: foodProduct.nutriments
          }))
        }))
      )
  }

  getFoodItemByBarcode(barcode: string): Observable<FoodProduct> {
    return this.http.get<any>(`${this.apiRoute}/product/${barcode}`)
      .pipe(
        map(response => ({
          id: response._id,
          productName: response.product_name,
          categories: response.categories,
          servingSize: response.serving_size,
          nutriments: response.nutriments
        }))
      )
  }

  getProteinConsumedByDay(userId: number, date: Date): Observable<number> {
    let params = new HttpParams().set('userId', userId).set('date', date.toJSON().split('T')[0]);
    return this.http.get<number>(`${this.apiRoute}/get-protein-consumed-by-day`, { params });
  }

  getFoodItemsConsumedByDay(userId: number, date: Date): Observable<FoodProduct[]> {
    let params = new HttpParams().set('userId', userId).set('date', date.toJSON().split('T')[0]);
    return this.http.get<any>(`${this.apiRoute}/get-items-consumed-by-day`, { params })
      .pipe(
        map((response: FoodProduct[]) =>
          response.map((foodProduct: any) => ({
            id: foodProduct._id,
            productName: foodProduct.product_name,
            categories: foodProduct.categories,
            servingSize: foodProduct.serving_size,
            nutriments: foodProduct.nutriments
          }))
        )
      )
  }
}
