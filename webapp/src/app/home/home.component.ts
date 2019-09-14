import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import { KeycloakService } from 'keycloak-angular';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html'
})
export class HomeComponent implements OnInit {
  fullName: string;

  constructor(public route: ActivatedRoute, private keycloak: KeycloakService) {
    this.fullName = '';
  }

  ngOnInit() {
    this.keycloak.isLoggedIn().then(isLoggedIn => {
      if (isLoggedIn) {
        this.keycloak.loadUserProfile().then(profile => {
          this.fullName = `${profile.firstName} ${profile.lastName}`;
        });
      }
    });
  }

}
