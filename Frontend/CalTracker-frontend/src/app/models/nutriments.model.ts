export class Nutriments{
    id?: number;
    energyKcal100g: number = 0;
    proteins100g: number = 0;
    carbohydrates100g: number = 0;
    sugars100g: number = 0;
    fat100g: number = 0;
    saturatedFat100g: number = 0;
    fiber100g: number = 0;
    sodium100g: number = 0;

    constructor(id: number, energyKcal100G: number, proteins100g: number, carbohydrates100g: number, sugars100g: number, fat100g: number, saturatedFat100g: number, fiber100g: number, sodium100g: number){
        this.id = id;
        this.energyKcal100g = energyKcal100G;
        this.proteins100g = proteins100g;
        this.carbohydrates100g = carbohydrates100g;
        this.sugars100g = sugars100g;
        this.fat100g = fat100g;
        this.saturatedFat100g = saturatedFat100g;
        this.fiber100g = fiber100g;
        this.sodium100g = sodium100g;
    }
}