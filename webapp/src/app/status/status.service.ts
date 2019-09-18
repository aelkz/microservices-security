import {Injectable} from '@angular/core';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {MessageService} from '../message/message.service';
import {Observable, of} from 'rxjs';
import {catchError} from 'rxjs/operators';
import {Config} from 'codelyzer';

@Injectable({
  providedIn: 'root'
})
export class StatusService {
    private integrationAPI = window['_env'].integration_uri + '/api/v1';
    private healthAPI = window['_env'].integration_health_uri;

    jsonType = 'application/json';

  constructor(private messageService: MessageService, private http: HttpClient) { }

  getAuthIntegrationHealth(): Observable<HttpResponse<Config>> {
    return this.http.get<Config>(`${this.healthAPI}/health`, {observe: 'response'})
      .pipe(
        catchError(error => {
          console.log(error);
          this.messageService.error(`getAuthIntegrationHealth() ${error.message}`);
          return of(error);
        })
      );
  }

  getProductHealth(): Observable<HttpResponse<Config>> {
    return this.http.get<Config>(`${this.integrationAPI}${window['_env'].product_path}/status`, {observe: 'response'})
      .pipe(
        catchError(error => {
          this.messageService.error(`getProductHealth() ${error.message}`);
          return of(error);
        })
      );
  }

  getStockHealth(): Observable<HttpResponse<Config>> {
    return this.http.get<Config>(`${this.integrationAPI}${window['_env'].stock_path}/status`, {observe: 'response'})
      .pipe(
        catchError(error => {
          this.messageService.error(`getStockHealth() ${error.message}`);
            return of(error);
        })
      );
  }

  getSupplierHealth(): Observable<HttpResponse<Config>> {
    return this.http.get<Config>(`${this.integrationAPI}${window['_env'].supplier_path}/status`, {observe: 'response'})
      .pipe(
        catchError(error => {
          this.messageService.error(`getSupplierHealth() ${error.message}`);
            return of(error);
          })
      );
  }

}
