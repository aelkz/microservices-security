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

  constructor(private messageService: MessageService, private http: HttpClient) { }

  getAuthIntegration(): Observable<HttpResponse<Config>> {
    return this.http.get<Config>(window['_env'].integration_uri + '/actuator/health', {observe: 'response'})
      .pipe(
        catchError(error => {
          this.messageService.error(`getAuthIntegration() ${error.message}`);
          return of(error);
        })
      );
  }

  getProduct(): Observable<HttpResponse<Config>> {
    return this.http.get<Config>(window['_env'].integration_uri + '/product/health', {observe: 'response'})
      .pipe(
        catchError(error => {
          this.messageService.error(`getProduct() ${error.message}`);
          return of(error);
        })
      );
  }

  getStock(): Observable<HttpResponse<Config>> {
    return this.http.get<Config>(window['_env'].integration_uri + '/stock/health', {observe: 'response'})
      .pipe(
        catchError(error => {
          this.messageService.error(`getStock() ${error.message}`);
            return of(error);
          })
      );
  }

  getSupplier(): Observable<HttpResponse<Config>> {
    return this.http.get<Config>(window['_env'].integration_uri + '/supplier/health', {observe: 'response'})
      .pipe(
        catchError(error => {
          this.messageService.error(`getSupplier() ${error.message}`);
            return of(error);
          })
      );
    }

}
