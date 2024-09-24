import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment.development';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CalculationsService {
  private readonly apiRoute = environment.apiUrl + "/calculate";

  constructor(private http: HttpClient) { }

  caloriesPerDay(weight: number, height: number, age: number, male: boolean, activity: number): Observable<number>{
    return this.http.post<any>(`${this.apiRoute}/calories-per-day`, {
      "weight": weight,
      "height": height,
      "age": age,
      "male": male,
      "activity": activity
    })
  }
  
  basalMetabolicRate(weight: number, height: number, age: number, male: boolean, activity: number): Observable<number>{
    return this.http.post<any>(`${this.apiRoute}/bmr`, {
      "weight": weight,
      "height": height,
      "age": age,
      "male": male,
      "activity": activity
    })
  }
}
