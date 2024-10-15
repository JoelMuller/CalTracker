import { Component } from '@angular/core';
import { User } from '../../../models/user.model';
import { FormsModule } from '@angular/forms';
import { UserService } from '../../../services/user.service';
import { NgIf } from '@angular/common';
import { CalculationsService } from '../../../services/calculations.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-create-user',
  standalone: true,
  imports: [FormsModule, NgIf],
  templateUrl: './create-user.component.html',
  styleUrl: './create-user.component.scss'
})

export class CreateUserComponent {
  newUser = new User('', '', '');
  emailInUse: boolean = false;
  bmrInfo: any = {
    height: null,
    age: null,
    gender: null,
    activity: null
  };

  constructor(private userService: UserService, private calculationsService: CalculationsService, private router: Router) { }

  onSubmit() {
    if (!this.emailInUse) {
      let { height, age, gender, activity } = this.bmrInfo;
      if (this.newUser.weight && height && age && activity !== null) {
        this.calculationsService.basalMetabolicRate(this.newUser.weight, height, age, gender, activity)
          .subscribe({
            next: (result) => {
              this.newUser.basalMetabolicRate = result;
            },
            error: (e) =>
              console.log("Error calculating BMR:", e),
            complete: () =>
              this.createUser()
          })
      } else {
        alert("Vul alle velden in.")
      }
    } else {
      alert("Email adres is al in gebruik.")
    }
  }

  createUser() {
    this.userService.createUser(this.newUser, this.bmrInfo).subscribe({
      next: (response) =>
        console.log(response),
      error: (e) =>
        console.log("error creating user:", e),
      complete: () => {
        this.router.navigate(['/login'])
      }
    });
  }

  checkEmail() {
    let email = this.newUser.email;

    if (email) {
      this.userService.checkEmailExists(email).subscribe({
        next: (exists) =>
          this.emailInUse = exists,
        error: (e) =>
          console.log("error checking email", e)
      })
    }
  }
}
