import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import { MaintenanceService } from './maintenance.service';
import { MessageService } from '../message/message.service';
import { IconDefinition, faCog, faCircleNotch } from '@fortawesome/free-solid-svg-icons';
import {appAnimations} from '../app-animations';

@Component({
  selector: 'app-maintenance',
  templateUrl: './maintenance.component.html',
  styles: [
    `
      .http_success {
        background-color: #03ed9a;
      }
      .http_error {
        background-color: #d34b5a;
      }
      .http_loading {
        background-color: #c2c2c2;
      }
    `
  ],
  animations: appAnimations
})
export class MaintenanceComponent implements OnInit {

  constructor(
      private messageService: MessageService,
      private statusService: MaintenanceService,
      private cdr: ChangeDetectorRef
  ) { }

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
    this.cdr.detectChanges();
  }

  setCardBackground(isLoading:boolean, isSuccess:boolean, isError:boolean) {
    this.httpLoading = isLoading;
    this.httpSuccess = isSuccess;
    this.httpError = isError;
  }

  getStockMaintenance(): void {
    this.loadingStockMaintenanceAPI = true;
    this.setCardBackground(true, false, false);

    this.statusService.getStockMaintenance().subscribe(res => {
      this.response = res;

      if (this.response.body != null) {
        this.httpSuccess = true;
        this.messageService.success('Successfully called stock-maintenance-api');
        this.setCardBackground(false, true, false);
      }
      if (this.response.status != 200) {
        this.setCardBackground(false, false, true);
      }
    },
    (err) => {
      if (this.response != null && this.response.status != 200) {
        this.setCardBackground(false, false, true);
      }

      this.setLoading(false, 0, 2000);
    },
    () => this.setLoading(false, 0, 1000)
    );
  }

  getSupplierMaintenance(): void {
    this.loadingSupplierMaintenanceAPI = true;
    this.setCardBackground(true, false, false);

    this.statusService.getSupplierMaintenance().subscribe(res => {
      this.response = res;

      if (this.response.body != null) {
        this.httpSuccess = true;
        this.messageService.success('Successfully called supplier-maintenance-api');
        this.setCardBackground(false, true, false);
      }
      if (this.response.status != 200) {
        this.setCardBackground(false, false, true);
      }
    },
    (err) => {
      if (this.response != null && this.response.status != 200) {
        this.setCardBackground(false, false, true);
      }

      this.setLoading(false, 0, 2000);
    },
   () => this.setLoading(false, 1, 1000)
    );
  }

  ngOnInit() {
    this.cogIcon = faCog;
    this.circleIcon = faCircleNotch;
    this.httpLoading = false;
    this.httpSuccess = false;
    this.httpError = false;
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
