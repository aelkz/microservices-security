import { Pipe, PipeTransform } from '@angular/core';
import {DatePipe} from '@angular/common';

// see: https://angular.io/api/common/DatePipe#targetText='shortTime'%20%3A%20equivalent%20to%20',01%20AM%20GMT%2B1%20).

@Pipe({ name: 'jsonDate' })
export class JsonDatePipe extends DatePipe implements PipeTransform {

    //     2019-06-15 10:00:00.0
    //     2019, 6, 15, 10, 0

    transform(arr: Array<number>) {
        if (arr == undefined) {
            return 'invalid date';
        }

        if (arr.length < 5) {
            return 'invalid date';
        }

        // new(year, month, date?, hours?, minutes?, seconds?, ms?): Date;
        return super.transform(new Date(arr[0], arr[1], arr[2], arr[3], arr[4]), `dd/MMM/yyyy HH:mm:ss`);
    }
}
