import { Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { ProductComponent } from './product/product.component';
import { StatusComponent } from './status/status.component';
import {MaintenanceComponent} from './maintenance/maintenance.component';

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
        path: 'maintenance',
        component: MaintenanceComponent,
        data: {
          breadcrumb: 'Maintenance'
        }
      },
      {
        path: 'product',
        component: ProductComponent,
        data: {
          breadcrumb: 'Product'
        }
      }
    ]
  },
  { path: '', redirectTo: 'home', pathMatch: 'full' },
  { path: '**', redirectTo: 'home' }
];
