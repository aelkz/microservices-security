import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {ActivatedRoute, NavigationEnd, Router} from '@angular/router';
import {BreadCrumb} from './breadcrumb';
import {distinctUntilChanged, filter, map} from 'rxjs/operators';

@Component({
  selector: 'app-breadcrumb',
  templateUrl: './breadcrumb.component.html',
  encapsulation: ViewEncapsulation.None
})
export class BreadcrumbComponent implements OnInit {
  breadcrumbs = this.router.events.pipe(
    filter(event => event instanceof NavigationEnd),
    distinctUntilChanged(),
    map(event => this.buildBreadCrumb(this.activatedRoute.firstChild))
  );
  title = '';

  // Build your breadcrumb starting with the root route of your current activated route
  constructor(private activatedRoute: ActivatedRoute,
              private router: Router) {
  }

  ngOnInit() { }

  buildBreadCrumb(route: ActivatedRoute, url: string = '',
                  breadcrumbs: Array<BreadCrumb> = []): Array<BreadCrumb> {
    // If no routeConfig is available we are on the root path
    const label = route.routeConfig ? route.routeConfig.data['breadcrumb'] : 'Home';
    const path = route.routeConfig ? route.routeConfig.path : '';
    // In the routeConfig the complete path is not available,
    // so we rebuild it each time
    const nextUrl = `${url}${path}/`;
    const breadcrumb = {
      label: label,
      url: nextUrl
    };
    const newBreadcrumbs = [...breadcrumbs, breadcrumb];
    if (route.firstChild) {
      // If we are not on our current path yet,
      // there will be more children to look after, to build our breadcrumb
      return this.buildBreadCrumb(route.firstChild, nextUrl, newBreadcrumbs);
    } else {
      this.title = label;
    }
    return newBreadcrumbs;
  }
}
