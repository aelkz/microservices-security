import { Injectable } from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpHeaders} from '@angular/common/http';
import {catchError, map, retry} from 'rxjs/operators';
import { MessageService } from '../message/message.service';
import { Observable, of } from 'rxjs';
import { Product } from './product.model';
import {ProductPageable} from "./product.pageable.model";

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private productAPI = window['_env'].integration_uri + '/api/v1';
  jsonType = 'application/json';

  constructor(
    private messageService: MessageService,
    private http: HttpClient
  ) { }

  getProducts(): Observable<ProductPageable> {
    return this.http.get<ProductPageable>(`${this.productAPI}/products`, this.headers()).pipe(
      retry(1),
      map(data => new ProductPageable().deserialize(data)),
      catchError(res => {
        return this.handleError('getProducts()', res);
      })
    );
  }

  getProduct(id: number): Observable<Product> {
    return this.http.get<Product>(`${this.productAPI}/product/${id}`, this.headers()).pipe(
        retry(1),
        map(data => new Product().deserialize(data)),
        catchError(res => {
          return this.handleError('getProduct(:id)', res);
        })
    );
  }

  saveProduct(product: Product): Observable<Product> {
    return this.http.post<Product>(`${this.productAPI}/product`, JSON.stringify(product), this.headers()).pipe(
      catchError(res => {
        return this.handleError('saveProduct(:product)', res);
      })
    );
  }

  updateProduct(product: Product, id?: number): Observable<Product> {
    if (id == null) {
      id = product.id;
    }

    return this.http.put<Product>(`${this.productAPI}/product/${id}`, JSON.stringify(product), this.headers()).pipe(
      retry(1),
      catchError(res => {
        return this.handleError('updateProduct(:id, :product)', res);
      })
    );
  }

  deleteProduct(id: number): Observable<any> {
    return this.http.delete<Product>(`${this.productAPI}/${id}`, this.headers()).pipe(
      retry(1),
      catchError(res => {
        return this.handleError('deleteProduct()', res);
      })
    );
  }

  private handleError(method: string, res: HttpErrorResponse): Observable<any> {
    this.messageService.error(`${method} ${res.message}`);
    console.error(res.error);
    return of(null);
  }

  protected headers(): {} {
    const requestOptions = {
      headers: new HttpHeaders(),
      observe: 'body',
      responseType: 'json',
      reportProgress: true
    };

    requestOptions.headers.set('Accept', this.jsonType);
    requestOptions.headers = requestOptions.headers.append('Content-Type', this.jsonType);

    return requestOptions;
  }

}
