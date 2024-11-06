export interface Product {
    id: number;
    categoryId: number;
    categoryName: string;
    name: string;
    price: number;
}

export interface ProductCreateDTO {
    categoryId: number | null;
    name: string;
    price: number | string;
}

export interface ProductUpdateDTO {
    categoryId: number | null;
    name: string;
    price: number | string;
}
