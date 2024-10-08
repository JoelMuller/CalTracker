import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment.development';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { map, Observable } from 'rxjs';
import { FoodProduct } from '../models/food-product.model';
import { SearchResults } from '../models/search-results.model';
import { CustomFoodProduct } from '../models/custom-food-product.model';
import { LogFoodProduct } from '../models/log-food-product.model';
import { LoggedFoodProduct } from '../models/logged-food-product.model';

@Injectable({
  providedIn: 'root'
})
export class FoodProductService {
  private readonly apiRoute = environment.apiUrl + "/food-item";

  constructor(private http: HttpClient) { }

  headers() : HttpHeaders{
    return new HttpHeaders({
      'Authorization': `Bearer ${localStorage.getItem('token')}`, // Add the Bearer token here
      'Content-type': 'application/json',
      'Accept': 'application/json'
    });
  }

  logBarcodeFoodItem(foodProductBarcode: number, date: Date, gramsConsumed: number, userId: number) {
    let adjustedDate = new Date(date);
    adjustedDate.setHours(adjustedDate.getHours() + 2); //add 2 hours to account for time zone difference

    let headers = this.headers()

    return this.http.post<any>(`${this.apiRoute}/log`, {
      "date": adjustedDate.toISOString().split('T')[0],
      "userId": userId,
      "gramsConsumed": gramsConsumed,
      "foodProductBarcode": foodProductBarcode
    }, { headers });
  }

  logCustomFoodItem(customFoodProduct: CustomFoodProduct, date: Date, gramsConsumed: number, userId: number) {
    let adjustedDate = new Date(date);
    adjustedDate.setHours(adjustedDate.getHours() + 2); //add 2 hours to account for time zone difference

    let headers = this.headers()

    return this.http.post<any>(`${this.apiRoute}/log`, {
      "date": adjustedDate.toISOString().split('T')[0],
      "userId": userId,
      "gramsConsumed": gramsConsumed,
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
    }, { headers }
    );
  }

  searchFoodItems(searchTerms: string, page: number): Observable<SearchResults> {
    let headers = this.headers()

    let params = new HttpParams().set('search_terms', searchTerms).set('page', page);

    return this.http.get<any>(`${this.apiRoute}/search`, { headers, params })
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
    let headers = this.headers()

    return this.http.get<any>(`${this.apiRoute}/product/${barcode}`, {headers})
      .pipe(
        map(response => ({
          id: response._id,
          productName: response.product_name,
          categories: response.categories,
          servingSize: response.serving_size,
          nutriments: {
            energyKcal100g: response.nutriments['energy-kcal_100g'],
            proteins100g: response.nutriments['proteins_100g'],
            carbohydrates100g: response.nutriments['carbohydrates_100g'] ?? 0,
            sugars100g: response.nutriments['sugars_100g'],
            fat100g: response.nutriments['fat_100g'],
            saturatedFat100g: response.nutriments['saturated-fat_100g'],
            fiber100g: response.nutriments['fiber_100g'],
            sodium100g: response.nutriments['sodium_100g']
          }
        }))
      )
  }

  getProteinConsumedByDay(userId: number, date: Date): Observable<number> {
    let adjustedDate = new Date(date);
    adjustedDate.setHours(adjustedDate.getHours() + 2); //add 2 hours to account for time zone difference

    let headers = this.headers()

    let params = new HttpParams().set('userId', userId).set('date', adjustedDate.toISOString().split('T')[0]);
    
    return this.http.get<number>(`${this.apiRoute}/get-protein-consumed-by-day`, { headers, params });
  }

  getCaloriesConsumedByDay(userId: number, date: Date): Observable<number> {
    let adjustedDate = new Date(date);
    adjustedDate.setHours(adjustedDate.getHours() + 2); //add 2 hours to account for time zone difference

    let headers = this.headers()

    let params = new HttpParams().set('userId', userId).set('date', adjustedDate.toISOString().split('T')[0]);

    return this.http.get<number>(`${this.apiRoute}/get-calories-consumed-by-day`, { headers, params });
  }

  getLoggedItemsByDay(userId: number, date: Date): Observable<LoggedFoodProduct[]> {
    let adjustedDate = new Date(date);
    adjustedDate.setHours(adjustedDate.getHours() + 2); //add 2 hours to account for time zone difference

    let headers = this.headers()

    let params = new HttpParams().set('userId', userId).set('date', adjustedDate.toISOString().split('T')[0]);

    return this.http.get<any>(`${this.apiRoute}/get-items-logged-by-day`, { headers, params })
      .pipe(
        map((response: LoggedFoodProduct[]) =>
          response.map((foodProduct: any) => ({
            id: foodProduct.id,
            productName: foodProduct.product_name,
            categories: foodProduct.categories,
            servingSize: foodProduct.serving_size,
            gramsConsumed: foodProduct.gramsConsumed,
            nutriments: foodProduct.nutriments
          }))
        )
      )
  }

  deleteLoggedFoodProduct(id: number) {
    let headers = this.headers()

    return this.http.delete<LogFoodProduct>(`${this.apiRoute}/log/${id}`, {headers});
  }
}
