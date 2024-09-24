import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment.development';
import { HttpClient, HttpParams } from '@angular/common/http';
import { map, Observable } from 'rxjs';
import { FoodProduct } from '../models/food-product.model';
import { SearchResults } from '../models/search-results.model';
import { CustomFoodProduct } from '../models/custom-food-product.model';
import { LogFoodProduct } from '../models/log-food-product.model';

@Injectable({
  providedIn: 'root'
})
export class FoodProductService {
  private readonly apiRoute = environment.apiUrl + "/food-item";

  constructor(private http: HttpClient) { }

  logBarcodeFoodItem(foodProductBarcode: number, date: Date, userId: number) {
    let adjustedDate = new Date(date);
    adjustedDate.setHours(adjustedDate.getHours() + 2); //add 2 hours to account for time zone difference

    return this.http.post<any>(`${this.apiRoute}/log`, {
      "date": adjustedDate.toISOString().split('T')[0],
      "userId": userId,
      "foodProductBarcode": foodProductBarcode
    });
  }

  logCustomFoodItem(customFoodProduct: CustomFoodProduct, date: Date, userId: number) {
    let adjustedDate = new Date(date);
    adjustedDate.setHours(adjustedDate.getHours() + 2); //add 2 hours to account for time zone difference

    return this.http.post<any>(`${this.apiRoute}/log`, {
      "date": adjustedDate.toISOString().split('T')[0],
      "userId": userId,
      "customFoodProduct": {
        "id": customFoodProduct.id,
        "product_name": customFoodProduct.productName,
        "serving_size": customFoodProduct.servingSize,
        "nutriments": {
          "energy-kcal_100g": customFoodProduct.nutriments.energyKcal100g,
          "proteins_100g": customFoodProduct.nutriments.proteins100g,
          "carbohydrates_100g": customFoodProduct.nutriments.carbohydrates100g,
          "sugars_100g": customFoodProduct.nutriments.sugars100g,
          "fat_100g": customFoodProduct.nutriments.fat100g,
          "saturated-fat_100g": customFoodProduct.nutriments.saturatedFat100g,
          "fiber_100g": customFoodProduct.nutriments.fiber100g,
          "sodium_100g": customFoodProduct.nutriments.sodium100g
        }
      }
    },{
      headers: {
        'Content-type': 'application/json',
        'Accept': 'application/json'
      }
    });
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
            nutriments: {
              energyKcal100g: foodProduct.nutriments['energy-kcal_100g'],
              proteins100g: foodProduct.nutriments['proteins_100g'],
              carbohydrates100g: foodProduct.nutriments['carbohydrates_100g'] ?? 0,
              sugars100g: foodProduct.nutriments['sugars_100g'],
              fat100g: foodProduct.nutriments['fat_100g'],
              saturatedFat100g: foodProduct.nutriments['saturated-fat_100g'],
              fiber100g: foodProduct.nutriments['fiber_100g'],
              sodium100g: foodProduct.nutriments['sodium_100g']
            }
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
    let adjustedDate = new Date(date);
    adjustedDate.setHours(adjustedDate.getHours() + 2); //add 2 hours to account for time zone difference

    let params = new HttpParams().set('userId', userId).set('date', adjustedDate.toISOString().split('T')[0]);
    return this.http.get<number>(`${this.apiRoute}/get-protein-consumed-by-day`, { params });
  }

  getCaloriesConsumedByDay(userId: number, date: Date): Observable<number> {
    let adjustedDate = new Date(date);
    adjustedDate.setHours(adjustedDate.getHours() + 2); //add 2 hours to account for time zone difference

    let params = new HttpParams().set('userId', userId).set('date', adjustedDate.toISOString().split('T')[0]);
    return this.http.get<number>(`${this.apiRoute}/get-calories-consumed-by-day`, { params });
  }

  getLoggedItemsByDay(userId: number, date: Date): Observable<FoodProduct[]> {
    let adjustedDate = new Date(date);
    adjustedDate.setHours(adjustedDate.getHours() + 2); //add 2 hours to account for time zone difference

    let params = new HttpParams().set('userId', userId).set('date', adjustedDate.toISOString().split('T')[0]);
    return this.http.get<any>(`${this.apiRoute}/get-items-logged-by-day`, { params })
      .pipe(
        map((response: FoodProduct[]) =>
          response.map((foodProduct: any) => ({
            id: foodProduct.id,
            productName: foodProduct.product_name,
            categories: foodProduct.categories,
            servingSize: foodProduct.serving_size,
            nutriments: foodProduct.nutriments
          }))
        )
      )
  }

  deleteLoggedFoodProduct(id: number) {
    return this.http.delete<LogFoodProduct>(`${this.apiRoute}/log/${id}`);
  }
}
