import { Component, OnInit } from '@angular/core';
import { MaintenanceService } from './maintenance.service';
import { MessageService } from '../message/message.service';
import { IconDefinition, faCog, faCircleNotch } from '@fortawesome/free-solid-svg-icons';
import {appAnimations} from '../app-animations';

@Component({
  selector: 'app-maintenance',
  templateUrl: './maintenance.component.html',
  animations: appAnimations
})
export class MaintenanceComponent implements OnInit {

  constructor(private messageService: MessageService, private statusService: MaintenanceService) { }

  status: any = {};
  cogIcon: IconDefinition;
  circleIcon: IconDefinition;

  loadingStockMaintenanceAPI: boolean;
  loadingSupplierMaintenanceAPI: boolean;

  setLoading(value: boolean, idx: number, timeout: number) {
    setTimeout(() => {
      switch (idx) {
        case 0:
          this.loadingStockMaintenanceAPI = value;
          break;
        case 1:
          this.loadingSupplierMaintenanceAPI = value;
          break;
      }
    }, timeout);
  }

  getStockMaintenance(): void {
    this.loadingStockMaintenanceAPI = true;
    this.statusService.getStockMaintenance().subscribe(res => {
          this.status = res;

          if (this.status.body != null) {
            this.messageService.success('Successfully called stock-maintenance-api');
          }
        },
        (err) => this.setLoading(false, 0, 2000),
        () => this.setLoading(false, 0, 1000)
    );
  }

  getSupplierMaintenance(): void {
    this.loadingSupplierMaintenanceAPI = true;
    this.statusService.getSupplierMaintenance().subscribe(res => {
          this.status = res;

          if (this.status.body != null) {
            this.messageService.success('Successfully called supplier-maintenance-api');
          }
        },
        (err) => this.setLoading(false, 1, 2000),
        () => this.setLoading(false, 1, 1000)
    );
  }

  ngOnInit() {
    this.cogIcon = faCog;
    this.circleIcon = faCircleNotch;
  }
}
