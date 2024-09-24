import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { environment } from '../environments/environment.development';
import { UserService } from './services/user.service';
import { User } from './models/user.model';
import { FoodProductService } from './services/food-product.service';
import { CalculationsService } from './services/calculations.service';
import { HeaderComponent } from "./shared/components/header/header.component";
import { FooterComponent } from "./shared/components/footer/footer.component";
import { CreateUserComponent } from "./components/user/create-user/create-user.component";
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgApexchartsModule } from 'ng-apexcharts';
import { DashboardComponent } from "./components/dashboard/dashboard.component";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, HeaderComponent, FooterComponent, CreateUserComponent, FormsModule, NgApexchartsModule, ReactiveFormsModule, DashboardComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  title = 'CalTracker-frontend';

  constructor(){}
}
