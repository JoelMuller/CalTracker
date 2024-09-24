import { FoodProduct } from "./food-product.model";

export class SearchResults{
    count: number;
    page: number;
    products: FoodProduct[];

    constructor(count: number, page: number, products: FoodProduct[]){
        this.count = count;
        this.page = page;
        this.products = products;
    }
}