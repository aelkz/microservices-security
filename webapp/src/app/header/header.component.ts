import { Component, OnInit, Renderer2, HostListener } from '@angular/core';
import { MessageItem } from '../message/message-item';
import { IconDefinition, faLock, faShieldAlt } from '@fortawesome/free-solid-svg-icons';
import { MessageHistoryService } from '../message/message-history.service';
import { KeycloakService } from 'keycloak-angular';
import { SidebarService } from '../sidebar/sidebar.service';

@Component({
  styleUrls: ['./header.component.css'],
  selector: 'app-header',
  templateUrl: './header.component.html'
})
export class HeaderComponent implements OnInit {
  accountUrl: string;
  isLoggedIn: boolean = false;
  logoutIcon: IconDefinition = faLock;
  accountIcon: IconDefinition = faShieldAlt;
  sidebarVisible: boolean = true;
  username: string = 'Guest';
  messages: MessageItem[];

  constructor(private messageHistoryService: MessageHistoryService, public sidebarService: SidebarService, private keycloak: KeycloakService) {
    // hide sidebar by default on mobile
    this.checkForMobile();
  }

  @HostListener('window:resize', ['$event'])
  checkForMobile() {
    if (window.innerWidth < 640) {
      this.sidebarService.toggleSidebar();
    }
  }

  doLogout(): void {
    if (this.isLoggedIn) {
      this.keycloak.logout();
    }
  }

  doAccount(): void {
    if (this.isLoggedIn) {
      window.open(this.accountUrl, '_blank');
    }
  }

  clear() {
    this.messageHistoryService.clear();
  }

  ngOnInit(): void {
    this.messages = this.messageHistoryService.getHistory();

    this.keycloak.isLoggedIn().then(isLoggedIn => {
      if (isLoggedIn) {
        this.isLoggedIn = true;
        this.username = this.keycloak.getUsername();

        const instance = this.keycloak.getKeycloakInstance();
        this.accountUrl = `${instance.authServerUrl}/realms/${instance.realm}/account`;
      }
    });
  }
}
