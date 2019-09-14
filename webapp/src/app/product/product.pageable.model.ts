import {Deserializable} from "./deserializable.model";
import {Product} from "./product.model";

/*
from springboot apis:
org.springframework.data.domain.Page;
org.springframework.data.domain.Pageable;
*/
export class ProductPageable implements Deserializable {
  content: Product[];
  pageable: {
    sort: {
      sorted: boolean;
      unsorted: boolean;
      empty: boolean;
    };
    offset: number;
    pageSize: number;
    pageNumber: number;
    unpaged: boolean;
    paged: boolean
  };
  totalPages: number;
  last: boolean;
  totalElements: number;
  size: number;
  number: number;
  numberOfElements: number;
  first: boolean;
  sort: {
    sorted: boolean;
    unsorted: boolean;
    empty: boolean
  };
  empty: boolean;

  deserialize(input: any): this {
    // Assign input to our object BEFORE deserialize our recipes to prevent already de-serialized recipes from being overwritten.
    Object.assign(this, input);
    // Iterate over all recipes for our RecipePageable and map them to a proper `Product` model
    this.content = input.content.map(recipe => new Product().deserialize(recipe));
    return this;
  }
}
