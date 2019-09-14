import { Component, OnInit } from '@angular/core';
import { IconDefinition } from '@fortawesome/free-solid-svg-icons';
import { faGithub } from '@fortawesome/free-brands-svg-icons';
import { SidebarService } from './sidebar.service';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html'
})
export class SidebarComponent implements OnInit {
  githubIcon: IconDefinition = faGithub;

  constructor(private sidebarService: SidebarService) { }

  closeOnMobile(): void {
    if (window.innerWidth < 640) {
      this.sidebarService.toggleSidebar();
    }
  }

  ngOnInit() { }
}
