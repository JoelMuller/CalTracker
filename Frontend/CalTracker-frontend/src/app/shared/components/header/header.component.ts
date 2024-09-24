import { Component } from '@angular/core';
import { NavigationEnd, Router, RouterLink, RouterOutlet } from '@angular/router';
import { UserService } from '../../../services/user.service';
import { NgIf } from '@angular/common';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [RouterLink, RouterOutlet, NgIf],
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss'
})
export class HeaderComponent {
  loggedIn: boolean = false;
  constructor(private userService: UserService, private router: Router){
    this.loggedIn = this.userService.isLoggedIn();

    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        this.loggedIn = this.userService.isLoggedIn();
      }
    });
  }
  
  ngOnInit(){ 
    this.loggedIn = this.userService.isLoggedIn();
  }

  logout(){
    this.userService.logout();
    this.router.navigate(['/login']);
  }
}
