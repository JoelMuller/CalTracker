import { Component } from '@angular/core';
import { UserService } from '../../../services/user.service';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { tap } from 'rxjs';
import { NgIf } from '@angular/common';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, NgIf],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {
  email: string = '';
  password: string = '';
  invalidCredentials: boolean = false;

  constructor(private userService: UserService, private router: Router) { }

  onSubmit() {
    this.userService.login(this.email, this.password).pipe(
      tap(response => {
        if (response.status === 200) {
          localStorage.setItem('token', response.body.token);
          localStorage.setItem('userId', response.body.userId);
        }
      })
    ).subscribe({
      next: (response) => {
        console.log('Status code:', response.status);
        this.router.navigate(['/dashboard']);
      },
      error: (e) => {
        console.log("Login unsuccessful", e);

        if (e.status === 401) {
          console.log("Error: Unauthorized (401) - Invalid credentials");
          this.invalidCredentials = true;
        } else {
          console.log("Other error status code:", e.status);
        }
      }
    });
  }

}
