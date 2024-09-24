import { Routes } from '@angular/router';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { AuthGuard } from './auth/auth.guard';
import { LoginComponent } from './components/user/login/login.component';
import { CreateUserComponent } from './components/user/create-user/create-user.component';
import { LogFoodProductComponent } from './components/log-food-product/log-food-product.component';

export const routes: Routes = [
    {path: 'dashboard', component: DashboardComponent, canActivate: [AuthGuard]},
    {path: 'login', component: LoginComponent},
    {path: '', component: LoginComponent},
    {path: 'register', component: CreateUserComponent},
    {path: 'log', component: LogFoodProductComponent, canActivate: [AuthGuard]},
];
