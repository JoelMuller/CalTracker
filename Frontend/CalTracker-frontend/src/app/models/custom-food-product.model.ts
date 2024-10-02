import { Nutriments } from "./nutriments.model";

export class CustomFoodProduct{
    id?: number;
    productName: string;
    nutriments: Nutriments;
    servingSize: string;
    gramsConsumed?: number;
    userId: number;
    
    constructor(productName: string, nutriments: Nutriments, servingSize: string, userId: number){
        this.productName = productName;
        this.nutriments = nutriments;
        this.servingSize = servingSize;
        this.userId = userId;
    }
}