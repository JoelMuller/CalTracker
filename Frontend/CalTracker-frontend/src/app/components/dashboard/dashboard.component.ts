import { Component, ViewChild } from '@angular/core';
import { ChartComponent, ApexAxisChartSeries, ApexChart, ApexXAxis, ApexTitleSubtitle, ApexStroke, ApexGrid, NgApexchartsModule, ApexAnnotations, ApexYAxis } from 'ng-apexcharts';
import { FoodProductService } from '../../services/food-product.service';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { animate } from '@angular/animations';
import { User } from '../../models/user.model';
import { CalculationsService } from '../../services/calculations.service';
import { UserService } from '../../services/user.service';
import { Router, RouterLink, RouterOutlet } from '@angular/router';
import { FoodProduct } from '../../models/food-product.model';
import { NgFor } from '@angular/common';
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
  imports: [NgApexchartsModule, ReactiveFormsModule, FormsModule, RouterLink, RouterOutlet, NgFor],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent {
  @ViewChild("chart") chart: ChartComponent | undefined;
  public chartOptions!: ChartOptions;

  weekdays: string[] = ["Maandag", "Dinsdag", "Woensdag", "Donderdag", "Vrijdag", "Zaterdag", "Zondag"];
  today = new Date();
  weekStart = this.getMonday(new Date(this.today.getFullYear(), this.today.getMonth(), this.today.getDate())); //variable here in case user can change week to show in graph
  data: number[] = [0];
  consumedFoodItems: [number, LoggedFoodProduct[]][] = [];
  bmr = 10;

  constructor(private foodProductService: FoodProductService, private userService: UserService, private router: Router) {
    this.getCaloriesConsumedByWeek(this.userService.getUserId(), this.weekStart);
    this.getFoodItemsConsumedByWeek(this.userService.getUserId(), this.weekStart);
    this.updateChartOptions()
  }

  ngAfterViewInit() {
    this.userService.getUser(this.userService.getUserId()).subscribe({
      next: (response) => {
        this.bmr = response.basalMetabolicRate - (response.weightLossPerWeek * 1000);
        this.updateChartOptions();
      },
      error: (e) => console.log("error getting user", e)
    });
  }

  updateChartOptions() {
    this.chartOptions = {
      series: [
        {
          name: "calsEatenPerDay",
          data: this.data
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
        next: (cals) => {
          this.data[i] = cals;
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
      complete: () =>
        this.updateChartData(this.userService.getUserId(), this.weekStart)
    })
  }

  updateChartData(userId: number, week: Date) {
    this.getCaloriesConsumedByWeek(userId, week);
    this.chart?.updateSeries([{
      data: this.data
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
}
