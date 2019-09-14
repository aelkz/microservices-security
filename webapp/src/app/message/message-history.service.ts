import { Injectable } from '@angular/core';
import { MessageItem } from './message-item';
import { MessageService } from './message.service';

@Injectable({
  providedIn: 'root'
})
export class MessageHistoryService {
  messageHistory: MessageItem[];

  constructor(private messageService: MessageService) {
    this.messageHistory = [];
    messageService.newMessage$.subscribe(msg => this.messageHistory.push(msg));
  }

  public clear(): void {
    this.messageHistory.length = 0;
  }

  getHistory(): MessageItem[] {
    return this.messageHistory;
  }
}
