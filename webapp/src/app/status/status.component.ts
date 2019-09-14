import { Component, OnInit } from '@angular/core';
import { StatusService } from './status.service';
import { MessageService } from '../message/message.service';
import { IconDefinition, faSync } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-status',
  templateUrl: './status.component.html'
})
export class StatusComponent implements OnInit {

  constructor(private messageService: MessageService, private statusService: StatusService) { }

  status: any = {};
  refreshIcon: IconDefinition;

  getAuthIntegration(): void {
    this.statusService.getAuthIntegrationHealth().subscribe(res => {
      this.status = res;

      if (this.status.body != null) {
        this.messageService.success('successfully checked auth-integration-api status');
      }
    });
  }

  getProduct(): void {
    this.statusService.getProductHealth().subscribe(res => {
      this.status = res;

      if (this.status.body != null) {
        this.messageService.success('Successfully checked product-api status');
      }
    });
  }

  getStock(): void {
    this.statusService.getStockHealth().subscribe(res => {
      this.status = res;

      if (this.status.body != null) {
        this.messageService.success('Successfully checked stock-api status');
      }
    });
  }

  getSupplier(): void {
    this.statusService.getSupplierHealth().subscribe(res => {
      this.status = res;

      if (this.status.body != null) {
        this.messageService.success('Successfully checked supplier-api status');
      }
    });
  }

  ngOnInit() {
    this.refreshIcon = faSync;
  }
}
