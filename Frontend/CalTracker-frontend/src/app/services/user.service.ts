import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { User } from '../models/user.model';
import { catchError, map, Observable, tap } from 'rxjs';
import { environment } from '../../environments/environment.development';
import { error } from 'console';
import { response } from 'express';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private readonly apiRoute = environment.apiUrl + "/user"

  constructor(private http: HttpClient) { }

  login(email: string, password: string) {
    return this.http.post<any>(`${this.apiRoute}/login`, {
      "email": email,
      "password": password
    },
      {
        headers: {
          'Content-type': 'application/json',
          'Accept': 'application/json'
        }
      })
      .pipe(tap(response => {
        localStorage.setItem('token', response.token);
        localStorage.setItem('userId', response.userId);
      }));
  }

  logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('userId');
    console.log("logged out")
  }

  getUserId(): number {
    return parseInt(localStorage.getItem('userId') || '0');
  }

  isLoggedIn(): boolean {
    return !!localStorage.getItem('token')
  }

  checkEmailExists(email: string): Observable<boolean> {
    return this.http.get<boolean>(`${this.apiRoute}/check-email/${email}`);
  }

  createUser(user: User, bmrInfo: any): Observable<string> {
    return this.http.post<string>(`${this.apiRoute}/register`, {
      "name": user.name,
      "email": user.email,
      "password": user.password,
      "basalMetabolicRate": user.basalMetabolicRate,
      "weightLossPerWeek": user.weightLossPerWeek,
    });
  }

  getUser(id: number): Observable<User> {
    return this.http.get<User>(`${this.apiRoute}/${id}`)
      .pipe(
        map(response => ({
          id: response.id,
          name: response.name,
          email: response.email,
          password: response.password,
          basalMetabolicRate: response.basalMetabolicRate,
          weightLossPerWeek: response.weightLossPerWeek,
          loggedFoodProducts: response.loggedFoodProducts
        }))
      )
  }

  updateUser(user: User): Observable<User> {
    return this.http.put<User>(this.apiRoute, {
      "id": user.id,
      "name": user.name,
      "email": user.email,
      "password": user.password,
      "basalMetabolicRate": user.basalMetabolicRate,
      "weightLossPerWeek": user.weightLossPerWeek
    });
  }

  deleteUser(id: number) {
    this.http.delete<User>(`${this.apiRoute}/${id}`);
  }
}












