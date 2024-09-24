import { CustomFoodProduct } from "./custom-food-product.model";

export class LogFoodProduct{
    id?: number;
    productName: string;
    foodProductBarcode?: string;
    customFoodProduct?: CustomFoodProduct;
    date: Date;
    userId: number;

    constructor(id: number, productName: string, foodProductBarcode: string, customFoodProduct: CustomFoodProduct, date: Date, userId: number){
        this.id = id;
        this.productName = productName
        this.foodProductBarcode = foodProductBarcode;
        this.customFoodProduct = customFoodProduct;
        this.date = date;
        this.userId = userId;
    }
}