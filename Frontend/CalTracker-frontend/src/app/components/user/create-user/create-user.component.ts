import { Component } from '@angular/core';
import { User } from '../../../models/user.model';
import { FormsModule } from '@angular/forms';
import { UserService } from '../../../services/user.service';
import { NgIf } from '@angular/common';
import { CalculationsService } from '../../../services/calculations.service';

@Component({
  selector: 'app-create-user',
  standalone: true,
  imports: [FormsModule, NgIf],
  templateUrl: './create-user.component.html',
  styleUrl: './create-user.component.scss'
})

export class CreateUserComponent {
  newUser = new User('', '', '', 0, 0.5)
  emailInUse: boolean = false;
  bmrInfo: any = {
    weight: null,
    height: null,
    age: null,
    gender: null,
    activity: null
  };

  constructor(private userService: UserService, private calculationsService: CalculationsService) { }

  onSubmit() {
    if (!this.emailInUse) {
      let {weight, height, age, gender, activity} = this.bmrInfo;
      if(weight && height && age && gender && activity !== null){
        this.calculationsService.basalMetabolicRate(weight, height, age, gender, activity)
        .subscribe({
          next: (result) => {
            this.newUser.basalMetabolicRate = result;
            this.userService.createUser(this.newUser, this.bmrInfo).subscribe({
              next: (response) =>
                console.log("user created"),
              error: (e) =>
                console.log("error:", e)
            });
          },
          error: (e) =>
            console.log("Error calculating BMR:", e)
        })
      }else{
        alert("Please fill in all fields.")
      }
    } else {
      alert("Email adres is already in use.")
    }
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
