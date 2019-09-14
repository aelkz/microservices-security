import { Injectable } from '@angular/core';
import { MessageItem } from './message-item';
import { ToastrService } from 'ngx-toastr';
import {
  faCheckCircle,
  IconDefinition
} from '@fortawesome/free-solid-svg-icons';
import { faTimesCircle } from '@fortawesome/free-solid-svg-icons';
import { faInfoCircle } from '@fortawesome/free-solid-svg-icons';
import { faExclamationCircle } from '@fortawesome/free-solid-svg-icons';
import { Subject } from 'rxjs/internal/Subject';

@Injectable({
  providedIn: 'root'
})
export class MessageService {

  private successIcon: IconDefinition = faCheckCircle;
  private errorIcon: IconDefinition = faTimesCircle;
  private infoIcon: IconDefinition = faInfoCircle;
  private warningIcon: IconDefinition = faExclamationCircle;
  public newMessage$: Subject<MessageItem> = new Subject();

  constructor(private toaster: ToastrService) { }

  success(msg: string): void {
    this.toaster.success(msg);
    this.emitMessage(this.successIcon, msg);
  }

  error(msg: string): void {
    this.toaster.error(msg);
    this.emitMessage(this.errorIcon, msg);
  }

  info(msg: string): void {
    this.toaster.info(msg);
    this.emitMessage(this.infoIcon, msg);
  }

  warning(msg: string): void {
    this.toaster.warning(msg);
    this.emitMessage(this.warningIcon, msg);
  }

  private emitMessage(icon: IconDefinition, text: string): void {
    const message = new MessageItem(icon, text);

    // message history service will pick up the change
    this.newMessage$.next(message);
  }
}
