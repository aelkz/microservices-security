import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpResponse} from '@angular/common/http';
import {MessageService} from '../message/message.service';
import {Observable, of} from 'rxjs';
import {catchError} from 'rxjs/operators';
import {Config} from 'codelyzer';

@Injectable({
  providedIn: 'root'
})
export class MaintenanceService {
    private maintenanceAPI = window['_env'].integration_uri + '/api/v1';

    constructor(private messageService: MessageService, private http: HttpClient) { }

    getStockMaintenance(): Observable<HttpResponse<Config>> {
        return this.http.get<Config>(`${this.maintenanceAPI}${window['_env'].stock_path}/maintenance`, {observe: 'response'})
        .pipe(
            catchError(error => {
                this.messageService.error(`getStockMaintenance() ${error.message}`);
                return of(error);
            })
        );
    }

    getSupplierMaintenance(): Observable<HttpResponse<Config>> {
        return this.http.get<Config>(`${this.maintenanceAPI}${window['_env'].supplier_path}/maintenance`, {observe: 'response'})
        .pipe(
            catchError(error => {
                this.messageService.error(`getSupplierMaintenance() ${error.message}`);
                return of(error);
            })
        );
    }

}
