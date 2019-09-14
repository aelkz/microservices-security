import { IconDefinition } from '@fortawesome/free-solid-svg-icons';

export class MessageItem {
  className: IconDefinition;
  text: string;

  constructor(className: IconDefinition, text: string) {
    this.className = className;
    this.text = text;
  }
}
