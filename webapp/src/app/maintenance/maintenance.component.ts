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

  response: any = {};
  cogIcon: IconDefinition;
  circleIcon: IconDefinition;

  loadingStockMaintenanceAPI: boolean;
  loadingSupplierMaintenanceAPI: boolean;

  httpSuccess: boolean;
  httpError: boolean;
  httpLoading: boolean;

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
    this.httpLoading = true;
    this.httpError = false;
    this.httpSuccess = false;

    this.statusService.getStockMaintenance().subscribe(res => {
          this.response = res;

          if (this.response.body != null) {
            this.httpSuccess = true;
            if (this.response.status != 200) {
              this.httpLoading = false;
              this.httpError = true;
              this.httpSuccess = false;
            }
            this.messageService.success('Successfully called stock-maintenance-api');
          }
        },
        (err) => {
          this.httpError = true;
          this.httpSuccess = false;
          this.httpLoading = false;
          this.setLoading(false, 0, 2000);
        },
        () => this.setLoading(false, 0, 1000)
    );
  }

  getSupplierMaintenance(): void {
    this.loadingSupplierMaintenanceAPI = true;
    this.httpLoading = true;
    this.httpError = false;
    this.httpSuccess = false;

    this.statusService.getSupplierMaintenance().subscribe(res => {
          this.response = res;

          if (this.response.body != null) {
            this.httpSuccess = true;
            if (this.response.status != 200) {
              this.httpLoading = false;
              this.httpError = true;
              this.httpSuccess = false;
            }
            this.messageService.success('Successfully called supplier-maintenance-api');
          }
        },
        (err) => {
          this.httpError = true;
          this.httpSuccess = false;
          this.httpLoading = false;
          this.setLoading(false, 0, 2000);
        },
        () => this.setLoading(false, 1, 1000)
    );
  }

  ngOnInit() {
    this.cogIcon = faCog;
    this.circleIcon = faCircleNotch;
  }

  isSuccess(): boolean {
    return this.httpSuccess;
  }

  isLoading(): boolean {
    return this.httpLoading;
  }

  isError(): boolean {
    return this.httpError;
  }

}
