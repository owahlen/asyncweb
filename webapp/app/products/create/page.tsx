'use client';

import {ProductCreateDTO} from '@/dtos/product';
import {Category} from "@/dtos/category";
import {useEffect, useState} from 'react';
import {useRouter} from 'next/navigation';
import {Button, Container, NumberInput, Select, TextInput} from '@mantine/core';
import apiClient from '../../../services/apiClient';

export default function CreateProductPage() {
    const [product, setProduct] = useState<ProductCreateDTO>({categoryId: null, name: '', price: ''});
    const [categories, setCategories] = useState<Category[]>([]); // Assume categories are fetched from the API
    const router = useRouter();

    useEffect(() => {
        const fetchCategories = async () => {
            try {
                const response = await apiClient.get('/category');
                setCategories(response.data);
            } catch (error) {
                console.error('Error fetching categories:', error);
            }
        };
        fetchCategories();
    }, []);

    const handleCreate = async () => {
        try {
            await apiClient.post('/product', product);
            router.push('/products');
        } catch (error) {
            console.error('Error creating product:', error);
        }
    };

    const handleCancel = () => {
        router.push('/products');
    };

    const isFormValid = product.name && product.price && product.categoryId;

    return (
        <Container>
            <h1>Create New Product</h1>
            <TextInput
                label="Product Name"
                value={product.name}
                onChange={(e) => setProduct({...product, name: e.currentTarget.value})}
                required
            />
            <NumberInput
                label="Price"
                decimalScale={2}
                allowNegative={false}
                value={product.price}
                onChange={(value) => setProduct({...product, price: value})}
                required
            />
            <Select
                label="Category"
                data={categories.map((cat) => ({value: String(cat.id), label: cat.name}))}
                value={String(product.categoryId)}
                onChange={(value) => setProduct({...product, categoryId: value ? parseInt(value) : null})}
                required
            />
            <Button onClick={handleCreate} disabled={!isFormValid}>Create Product</Button>
            <Button variant="outline" color="gray" onClick={handleCancel} style={{ marginLeft: '10px' }}>
                Cancel
            </Button>
        </Container>
    );
}
