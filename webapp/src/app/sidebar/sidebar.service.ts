import { Injectable, Renderer2, RendererFactory2 } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class SidebarService {
  private renderer: Renderer2;
  sidebarVisible: boolean = true;

  constructor(private rendererFactory: RendererFactory2) {
    this.renderer = rendererFactory.createRenderer(null, null);
  }

  toggleSidebar() {
    this.sidebarVisible = !this.sidebarVisible;

    if (this.sidebarVisible === false) {
      this.renderer.removeClass(document.body, 'sidebar-show');
    } else {
      this.renderer.addClass(document.body, 'sidebar-show');
    }
    // triggering this event so that the mapbox api will auto resize the map
    if (window.innerWidth > 640) {
      setTimeout(() => {
        window.dispatchEvent(new Event('resize'));
      }, 500);
    }
  }
}
