import { Optional } from "@angular/core";
import { LogFoodProduct } from "./log-food-product.model";

export class User{
    id?: number;
    name: string;
    email: string;
    password: string;
    basalMetabolicRate: number;
    weightLossPerWeek: number;
    loggedFoodProducts?: LogFoodProduct[];
    
    constructor(name: string, email: string, @Optional()password: string, basalMetabolicRate: number, weightLossPerWeek: number){
        this.name = name;
        this.email = email;
        this.password = password;
        this.basalMetabolicRate = basalMetabolicRate;
        this.weightLossPerWeek = weightLossPerWeek;
    }
}