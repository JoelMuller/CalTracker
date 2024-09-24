import { Nutriments } from "./nutriments.model";

export class FoodProduct{
    id?: number;
    productName: string;
    categories?: string;
    servingSize: string;
    nutriments: Nutriments;

    constructor(id: number, productName: string, categories: string, servingSize: string, nutriments: Nutriments){
        this.id = id;
        this.productName = productName;
        this.categories = categories;
        this.servingSize = servingSize;
        this.nutriments = nutriments;
    }
}