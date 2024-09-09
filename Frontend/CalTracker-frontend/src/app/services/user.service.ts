import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { User } from '../models/user.model';
import { catchError, map, Observable } from 'rxjs';
import { environment } from '../../environments/environment.development';
import { error } from 'console';
import { response } from 'express';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private readonly apiRoute = environment.apiUrl + "/user"

  constructor(private http: HttpClient) { }

  createUser(user: User): Observable<User> {
    return this.http.post<User>(this.apiRoute, {
      "name": user.name,
      "email": user.email,
      "password": user.password,
      "weightLossPerWeek": user.weightLossPerWeek
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
      "weightLossPerWeek": user.weightLossPerWeek
    });
  }

  deleteUser(id: number) {
    this.http.delete<User>(`${this.apiRoute}/${id}`);
  }
}












