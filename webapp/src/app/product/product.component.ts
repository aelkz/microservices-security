import { AfterViewInit, Component, OnDestroy, OnInit, ViewChild, TemplateRef } from '@angular/core';
import { MessageService } from '../message/message.service';
import { ProductService } from './product.service';
import { Product } from './product.model';
import { Subject } from 'rxjs';
import { DataTableDirective } from 'angular-datatables';
import { IconDefinition, faSync, faEraser, faPlusCircle, faTrash } from '@fortawesome/free-solid-svg-icons';
import { BsModalService, BsModalRef } from 'ngx-bootstrap';

@Component({
  selector: 'app-product',
  templateUrl: './product.component.html',
  styleUrls: ['./product.component.css']
})
export class ProductComponent implements OnDestroy, OnInit, AfterViewInit {

  @ViewChild(DataTableDirective)
  dtElement: DataTableDirective;

  @ViewChild('editTemplate')
  editTemplate: TemplateRef<any>;

  @ViewChild('deleteTemplate')
  deleteTemplate: TemplateRef<any>;

  dtOptions: any = {};
  products: Product[] = [];
  selectedProduct: Product;
  dtTrigger: Subject<any> = new Subject();
  modalRef: BsModalRef;
  deleteIcon: IconDefinition = faTrash;
  addIcon: IconDefinition;
  years: string[];

  constructor(
      private messageService: MessageService,
      private productService: ProductService,
      private modalService: BsModalService) { }

  load(): void {
    this.productService.getProducts().subscribe(res => {
      this.products = res.content;
      this.rerender();

      if (this.products != null) {
        this.messageService.success(`Successfully loaded ${this.products.length} products from service`);
      }
    });
  }

  rerender(): void {
    if (this.dtElement.dtInstance) {
      this.dtElement.dtInstance.then((dtInstance: DataTables.Api) => {
        // Destroy the table first
        dtInstance.destroy();

        // Call the dtTrigger to rerender again
        this.dtTrigger.next();
      });
    } else {
      this.dtTrigger.next();
    }
  }

  clear(): void {
    this.products = [];
    this.rerender();
    this.messageService.info('Cleared Data');
  }

  add(): void {
    this.selectedProduct = new Product();
    this.modalRef = this.modalService.show(this.editTemplate);
  }

  edit(product: Product): void {
    this.selectedProduct = product;
    this.modalRef = this.modalService.show(this.editTemplate);
  }

  warning(product: Product) {
    this.selectedProduct = product;
    this.modalRef = this.modalService.show(this.deleteTemplate);
  }

  saveOrUpdate(): void {
    if (this.selectedProduct.id != null) {
      this.productService.updateProduct(this.selectedProduct).subscribe(res => {
        if (res != null) {
          this.messageService.success(`Successfully updated Product with ID ${res.id}`);
          this.load();
        }
      });
    } else {
      this.productService.saveProduct(this.selectedProduct).subscribe(res => {
        if (res != null) {
          this.messageService.success(`Successfully added new Product with ID ${res.id}`);
          this.load();
        }
      });
    }
    this.dismiss();
  }

  delete(id: number): void {
    this.productService.deleteProduct(id).subscribe(_ => {
      this.messageService.success(`Successfully deleted product`);
      this.load();
    });
    this.dismiss();
  }

  dismiss(): void {
    this.modalRef.hide();
  }

  ngOnDestroy(): void {
    // Do not forget to unsubscribe the event
    this.dtTrigger.unsubscribe();
  }

  ngOnInit() {
    this.dtOptions = {
      pagingType: 'simple',
      responsive: true,
      pageLength: 10
    };

    const year: number = new Date().getFullYear();
    const range = [];
    range.push(year.toString());

    for (let i = 1; i < 30; i++) {
      range.push((year - i).toString());
    }
    this.years = range;
  }

  ngAfterViewInit() {
    this.rerender();
  }
}
