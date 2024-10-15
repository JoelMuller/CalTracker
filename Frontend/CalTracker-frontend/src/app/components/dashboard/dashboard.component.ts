import { Component, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ChartComponent, ApexAxisChartSeries, ApexChart, ApexXAxis, ApexTitleSubtitle, ApexStroke, ApexGrid, NgApexchartsModule, ApexAnnotations, ApexYAxis, ApexResponsive } from 'ng-apexcharts';
import { FoodProductService } from '../../services/food-product.service';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { animate } from '@angular/animations';
import { User } from '../../models/user.model';
import { CalculationsService } from '../../services/calculations.service';
import { UserService } from '../../services/user.service';
import { Router, RouterLink, RouterOutlet } from '@angular/router';
import { FoodProduct } from '../../models/food-product.model';
import { NgFor, NgIf } from '@angular/common';
import { LoggedFoodProduct } from '../../models/logged-food-product.model';

export type ChartOptions = {
  series: ApexAxisChartSeries;
  chart: ApexChart;
  xaxis: ApexXAxis;
  yaxis: ApexYAxis;
  title: ApexTitleSubtitle;
  grid: ApexGrid;
  annotations: ApexAnnotations;
}

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [NgApexchartsModule, ReactiveFormsModule, FormsModule, RouterLink, RouterOutlet, CommonModule],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent {
  @ViewChild("chart") chart: ChartComponent | undefined;
  public calChart!: ChartOptions;
  public protChart!: ChartOptions;

  showCalChart: boolean = true;
  showProtChart: boolean = false;

  weekdays: string[] = ["Maandag", "Dinsdag", "Woensdag", "Donderdag", "Vrijdag", "Zaterdag", "Zondag"];
  today = new Date();
  weekStart = this.getMonday(new Date(this.today.getFullYear(), this.today.getMonth(), this.today.getDate())); //variable here in case user can change week to show in graph
  calData: number[] = [0];
  protData: number[] = [0];
  consumedFoodItems: [number, LoggedFoodProduct[]][] = [];
  bmr: number = 0;
  proteinGoal: number = 0;

  constructor(private foodProductService: FoodProductService, private userService: UserService, private router: Router) {
    this.getCaloriesConsumedByWeek(this.userService.getUserId(), this.weekStart);
    this.getProteinConsumedByWeek(this.userService.getUserId(), this.weekStart)
    this.getFoodItemsConsumedByWeek(this.userService.getUserId(), this.weekStart);
    this.updateChartOptions()
  }

  ngAfterViewInit() {
    this.userService.getUser(this.userService.getUserId()).subscribe({
      next: (response) => {
        this.bmr = response.basalMetabolicRate! - (response.weightLossPerWeek! * 1000);
        this.proteinGoal = response.weight! * 1.8; //1.6 - 2.4g per kg body weight is enough to maintain muscle
        this.updateChartOptions();
      },
      error: (e) => console.log("error getting user", e)
    });
  }

  updateChartOptions() {
    this.calChart = {
      series: [
        {
          name: "calsEatenPerDay",
          data: this.calData
        }
      ],
      chart: {
        type: "line",
        height: 400,
        zoom: {
          enabled: false
        }
      },
      title: {
        text: "Calorieën geconsumeerd per dag",
        align: "left",
        margin: 0,
        offsetY: 30,
        style:{
          fontWeight: 800,
          fontSize: '1.25rem'
        }
      },
      grid: {
        row: {
          colors: ["#f3f3f3"],
          opacity: 0.5
        }
      },
      xaxis: {
        type: 'category',
        max: 7, //to show all categories
        categories: ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"],
        labels: {
          style:{
            fontSize: '1rem',
            fontWeight: 500
          }
        }
      },
      yaxis: {
        min: 0,
        max: 4000,
        decimalsInFloat: 0,
        labels: {
          style:{
            fontSize: '0.75rem',
            fontWeight: 500
          }
        }
      },
      annotations: {
        yaxis: [{
          y: this.bmr,
          borderColor: '#80AF81',
          fillColor: '#80AF81',
          opacity: 1,
          strokeDashArray: 8,
          offsetY: 0,
          label: {
            borderColor: '#739072',
            style: {
              fontSize: '15px',
              color: '#fff',
              background: '#739072',
              padding:{
                top: 5,
                left: 5
              }
            },
            text: 'Max calorieën per dag: ' + this.bmr
          }
        }]
      }
    };

    this.protChart = {
      series: [
        {
          name: "protsEatenPerDay",
          data: this.protData
        }
      ],
      chart: {
        type: "line",
        height: 400,
        zoom: {
          enabled: false
        }
      },
      title: {
        text: "Proteïne geconsumeerd per dag",
        align: "left",
        margin: 0,
        offsetY: 30,
        style:{
          fontWeight: 800,
          fontSize: '1.25rem'
        }
      },
      grid: {
        row: {
          colors: ["#f3f3f3"],
          opacity: 0.5
        }
      },
      xaxis: {
        type: 'category',
        max: 7, //to show all categories
        categories: ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"],
        labels: {
          style:{
            fontSize: '1rem',
            fontWeight: 500
          }
        }
      },
      yaxis: {
        min: 0,
        max: 200,
        decimalsInFloat: 0,
        labels: {
          style:{
            fontSize: '0.75rem',
            fontWeight: 500
          }
        }
      },
      annotations: {
        yaxis: [{
          y: this.proteinGoal,
          borderColor: '#80AF81',
          fillColor: '#80AF81',
          opacity: 1,
          strokeDashArray: 8,
          offsetY: 0,
          label: {
            borderColor: '#739072',
            style: {
              fontSize: '15px',
              color: '#fff',
              background: '#739072',
              padding:{
                top: 5,
                left: 5
              }
            },
            text: 'Proteïne doel: ' + this.proteinGoal
          }
        }]
      }
    };
  }

  getMonday(currentDate: Date) {
    let date = new Date(currentDate);
    let day = date.getDay();
    let diff = day === 0 ? -6 : 1 - day;
    date.setDate(date.getDate() + diff);
    return date;
  }

  getCaloriesConsumedByWeek(userId: number, week: Date) {
    for (let i = 0; i < 7; i++) {
      let day = new Date(week); //adds the number of days in the loop to the right day to get the calories
      day.setDate(day.getDate() + i);
      this.foodProductService.getCaloriesConsumedByDay(userId, day).subscribe({
        next: (data) => {
          this.calData[i] = data;
        },
        error: (e) =>
          console.log("error getting calories consumed by day ", e),
        complete: () =>
          this.updateChartOptions()
      })
    }
  }

  getProteinConsumedByWeek(userId: number, week: Date){
    for (let i = 0; i < 7; i++) {
      let day = new Date(week); //adds the number of days in the loop to the right day to get the calories
      day.setDate(day.getDate() + i);
      this.foodProductService.getProteinConsumedByDay(userId, day).subscribe({
        next: (data) => {
          this.protData[i] = data;
        },
        error: (e) =>
          console.log("error getting calories consumed by day ", e),
        complete: () =>
          this.updateChartOptions()
      })
    }
  }

  getFoodItemsConsumedByWeek(userId: number, week: Date){
    for (let i = 0; i < 7; i++) {
      let day = new Date(week); //adds the number of days in the loop to the right day to get the calories
      day.setDate(day.getDate() + i);
      this.foodProductService.getLoggedItemsByDay(userId, day).subscribe({
        next: (foodProducts) => {
          this.consumedFoodItems[i] = [i, foodProducts];
        },
        error: (e) =>
          console.log("error getting food items consumed by day ", e)
      })
    }
  }

  deleteLoggedFoodProduct(logId: number){
    this.foodProductService.deleteLoggedFoodProduct(logId).subscribe({
      next: (response) => 
        this.getFoodItemsConsumedByWeek(this.userService.getUserId(), this.weekStart),
      error: (e) => 
        console.log("error deleting log", e),
      complete: () => {
        this.updateCalChartData(this.userService.getUserId(), this.weekStart)
      }
    })
  }

  updateCalChartData(userId: number, week: Date) {
    this.getCaloriesConsumedByWeek(userId, week);
    this.chart?.updateSeries([{
      data: this.calData
    }])
    this.chart?.updateOptions({
      animate: true
    })
  }

  updateProtChartData(userId: number, week: Date) {
    this.getProteinConsumedByWeek(userId, week);
    this.chart?.updateSeries([{
      data: this.protData
    }])
    this.chart?.updateOptions({
      animate: true
    })
  }

  updateWeek(){
    this.getCaloriesConsumedByWeek(this.userService.getUserId(), this.weekStart);
    this.getFoodItemsConsumedByWeek(this.userService.getUserId(), this.weekStart);
    this.updateChartOptions();
  }

  switchDiv(div: number){
    if(div == 1){
      this.showCalChart = true;
      this.showProtChart = false;
    }else{
      this.showCalChart = false;
      this.showProtChart = true;
    }
  }
}
