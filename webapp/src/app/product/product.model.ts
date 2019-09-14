import {Deserializable} from "./deserializable.model";

export class Product implements Deserializable {
  id: number;
  name: string;
  description: string;
  code: string;
  price: number;
  active: boolean;
  created: number[];

  deserialize(input: any): this {
    Object.assign(this, input);
    return this;
  }
}
