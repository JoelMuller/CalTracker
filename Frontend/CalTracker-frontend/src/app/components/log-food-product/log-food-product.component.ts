import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CustomFoodProductService } from '../../services/custom-food-product.service';
import { CustomFoodProduct } from '../../models/custom-food-product.model';
import { Nutriments } from '../../models/nutriments.model';
import { NgFor, NgIf } from '@angular/common';
import { FoodProductService } from '../../services/food-product.service';
import { response } from 'express';
import { FoodProduct } from '../../models/food-product.model';
import { SearchResults } from '../../models/search-results.model';
import { NgApexchartsModule } from 'ng-apexcharts';
import { UserService } from '../../services/user.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-log-food-product',
  standalone: true,
  imports: [FormsModule, NgFor, NgIf],
  templateUrl: './log-food-product.component.html',
  styleUrl: './log-food-product.component.scss'
})
export class LogFoodProductComponent {
  //custom food product variables
  newNutriments = new Nutriments(0, 0, 0, 0, 0, 0, 0, 0, 0);
  newCustomFoodProduct = new CustomFoodProduct('', this.newNutriments, '', this.userService.getUserId())
  usersCustomFoodProducts?: CustomFoodProduct[];
  logDate: string;
  gramsConsumed!: number;
  gramsConsumedCustomFoodProduct!: number;
  gramsConsumedSearchedFoodProduct!: number;

  //search and pagination variables
  barcode = '';
  barcodeFoodItem?: FoodProduct;
  searchedBarcode = false;
  searchTerms = '';
  searchResults?: SearchResults;
  searched = false;
  totalSearchItems = 0;
  totalPages = 2;
  currentPage = 1;

  //loading circle
  loading = false;

  constructor(private customFoodProductService: CustomFoodProductService,
    private foodProductService: FoodProductService,
    private userService: UserService,
    private router: Router) {
    let today = new Date();
    let day = String(today.getDate()).padStart(2, '0');
    let month = String(today.getMonth() + 1).padStart(2, '0');
    let year = today.getFullYear();

    this.logDate = `${year}-${month}-${day}`;
  }

  ngOnInit() {
    this.getUserCustomFoodProducts(this.userService.getUserId());
  }

  createAndLogNewCustomFoodProduct() {
    let customFoodProduct: CustomFoodProduct;
    this.customFoodProductService.createCustomFoodProduct(this.newCustomFoodProduct).subscribe({
      next: (response) =>
        customFoodProduct = response,
      error: (e) =>
        console.log("error creating custom food product", e),
      complete: () =>
        this.logCustomFoodProduct(customFoodProduct, this.gramsConsumed)
    })
    this.router.navigate(['/dashboard'])
  }

  logFoodProductByBarcode(barcode?: number, gramsConsumed?: number) {
    this.foodProductService.logBarcodeFoodItem(barcode!, new Date(this.logDate), gramsConsumed!, this.userService.getUserId())
      .subscribe({
        next: (response) =>
          console.log("logged food product"),
        error: (e) =>
          console.log("Error logging food product", e),
        complete: () =>
          this.router.navigate(['/dashboard'])
      })
  }

  logCustomFoodProduct(customFoodProduct: CustomFoodProduct, gramsConsumed: number) {
    this.foodProductService.logCustomFoodItem(customFoodProduct, new Date(this.logDate), gramsConsumed, this.userService.getUserId())
      .subscribe({
        next: (response) => {
          console.log("logged custom food product");
        },
        error: (e) =>
          console.log("Error logging custom food product", e)
      })
  }

  logCustomFoodProductById(id?: number) {
    if (id === undefined) {
      console.log("Custom food product has no id")
    } else {
      this.customFoodProductService
        .getCustomFoodItemByUserIdAndCustomFoodProductId(this.userService.getUserId(), id).subscribe({
          next: (response) => {
            const foodProduct = this.usersCustomFoodProducts!.find(product => product.id === id);
            this.logCustomFoodProduct(response, foodProduct?.gramsConsumed!)
          },
          error: (e) =>
            console.log("Error getting custom food product by id", e),
          complete: () =>
            this.router.navigate(['/dashboard'])
        })
    }
  }

  getUserCustomFoodProducts(userId: number) {
    this.customFoodProductService.getCustomFoodItemsByUserId(userId).subscribe({
      next: (response) => {
        this.usersCustomFoodProducts = response;
      },
      error: (e) =>
        console.log("error getting users food products")
    })
  }

  searchFoodProducts(page: number) {
    if (page != 0 && this.totalPages >= page) {
      this.loading = true;
      this.foodProductService.searchFoodItems(this.searchTerms, page).subscribe({
        next: (response) => {
          this.searchResults = response;
          this.totalSearchItems = response.count;
          this.currentPage = response.page;
          this.searchedBarcode = false;
          this.searched = true;
          this.totalPages = Math.ceil(this.totalSearchItems / 10)
        },
        error: (e) =>
          console.log("error getting search results", e),
        complete: () =>
          this.loading = false
      })
    } else {
      console.log(this.totalPages)
      console.log("end of results")
    }
  }

  findFoodProductByBarcode() {
    if (this.barcode !== '' || this.barcode !== undefined) {
      this.loading = true;
      this.foodProductService.getFoodItemByBarcode(this.barcode).subscribe({
        next: (response) => {
          this.barcodeFoodItem = response;
          this.searched = false;
          this.searchedBarcode = true;
          let regex = /(\d+\.?\d*)\s*g/;
          let match = response.servingSize.match(regex);
          if(match){
            this.barcodeFoodItem.gramsConsumed = parseFloat(match[1])
          }
        }, error: (e) =>
          console.log("error getting food product by barcode", e),
        complete: () =>
          this.loading = false
      })
    } else {
      console.log("No barcode inputted")
    }
  }
}
