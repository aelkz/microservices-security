import { Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { ProductComponent } from './product/product.component';
import { StatusComponent } from './status/status.component';

export const AppRoutes: Routes = [
  {
    path: 'home',
    component: HomeComponent,
    data: {
      breadcrumb: 'Home'
    },
    children: [
      {
        path: 'status',
        component: StatusComponent,
        data: {
          breadcrumb: 'Status'
        }
      },
      {
        path: 'table',
        component: ProductComponent,
        data: {
          breadcrumb: 'Table'
        }
      }
    ]
  },
  { path: '', redirectTo: 'home', pathMatch: 'full' },
  { path: '**', redirectTo: 'home' }
];
