import { Nutriments } from "./nutriments.model";

export class LoggedFoodProduct{
    id?: number;
    productName: string;
    categories?: string;
    servingSize: string;
    gramsConsumed: number;
    nutriments: Nutriments;

    constructor(id: number, productName: string, categories: string, servingSize: string, gramsConsumed: number, nutriments: Nutriments){
        this.id = id;
        this.productName = productName;
        this.categories = categories;
        this.servingSize = servingSize;
        this.gramsConsumed = gramsConsumed;
        this.nutriments = nutriments;
    }
}