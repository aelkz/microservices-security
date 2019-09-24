import { Component, OnInit } from '@angular/core';
import { StatusService } from './status.service';
import { MessageService } from '../message/message.service';
import { IconDefinition, faCog, faCircleNotch } from '@fortawesome/free-solid-svg-icons';
import {appAnimations} from '../app-animations';

@Component({
  selector: 'app-status',
  templateUrl: './status.component.html',
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
export class StatusComponent implements OnInit {

  constructor(private messageService: MessageService, private statusService: StatusService) { }

  response: any = {};
  cogIcon: IconDefinition;
  circleIcon: IconDefinition;

  loadingIntegrationAPI: boolean;
  loadingProductAPI: boolean;
  loadingStockAPI: boolean;
  loadingSupplierAPI: boolean;

  httpSuccess: boolean;
  httpError: boolean;
  httpLoading: boolean;

  setLoading(value: boolean, idx: number, timeout: number) {
    setTimeout(() => {
      switch (idx) {
        case 0:
          this.loadingIntegrationAPI = value;
          break;
        case 1:
          this.loadingProductAPI = value;
          break;
        case 2:
          this.loadingStockAPI = value;
          break;
        case 3:
          this.loadingSupplierAPI = value;
          break;
      }
    }, timeout);
  }

  getAuthIntegration(): void {
    this.loadingIntegrationAPI = true;
    this.httpLoading = true;
    this.httpError = false;
    this.httpSuccess = false;

    this.statusService.getAuthIntegrationHealth().subscribe(res => {
      this.response = res;

      if (this.response.body != null) {
        this.httpSuccess = true;
        if (this.response.status != 200) {
          this.httpLoading = false;
          this.httpError = true;
          this.httpSuccess = false;
        }
        this.messageService.success('successfully checked auth-integration-api status');
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

  getProduct(): void {
    this.loadingProductAPI = true;
    this.httpLoading = true;
    this.httpError = false;
    this.httpSuccess = false;

    this.statusService.getProductHealth().subscribe(res => {
      this.response = res;

      if (this.response.body != null) {
        this.httpSuccess = true;
        if (this.response.status != 200) {
          this.httpLoading = false;
          this.httpError = true;
          this.httpSuccess = false;
        }
        this.messageService.success('Successfully checked product-api status');
      }
    },
    (err) => {
      this.httpError = true;
      this.httpSuccess = false;
      this.httpLoading = false;
      this.setLoading(false, 1, 2000);
      },
    () => this.setLoading(false, 1, 1000)
    );
  }

  getStock(): void {
    this.loadingStockAPI = true;
    this.httpLoading = true;
    this.httpError = false;
    this.httpSuccess = false;

    this.statusService.getStockHealth().subscribe(res => {
      this.response = res;

      if (this.response.body != null) {
        this.httpSuccess = true;
        if (this.response.status != 200) {
          this.httpLoading = false;
          this.httpError = true;
          this.httpSuccess = false;
        }
        this.messageService.success('Successfully checked stock-api status');
      }
    },
      (err) => {
        this.httpError = true;
        this.httpSuccess = false;
        this.httpLoading = false;
        this.setLoading(false, 1, 2000);
      },
    () => this.setLoading(false, 2, 1000)
    );
  }

  getSupplier(): void {
    this.loadingSupplierAPI = true;
    this.httpLoading = true;
    this.httpError = false;
    this.httpSuccess = false;

    this.statusService.getSupplierHealth().subscribe(res => {
      this.response = res;

      if (this.response.body != null) {
        this.httpSuccess = true;
        if (this.response.status != 200) {
          this.httpLoading = false;
          this.httpError = true;
          this.httpSuccess = false;
        }
        this.messageService.success('Successfully checked supplier-api status');
      }
    },
      (err) => {
        this.httpError = true;
        this.httpSuccess = false;
        this.httpLoading = false;
        this.setLoading(false, 1, 2000);
      },
    () => this.setLoading(false, 3, 1000)
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
