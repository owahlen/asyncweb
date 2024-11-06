'use client';

import {ProductUpdateDTO} from '@/dtos/product';
import {Category} from "@/dtos/category";
import {use, useEffect, useState} from 'react';
import {useRouter} from 'next/navigation';
import {Button, Container, NumberInput, Select, TextInput} from '@mantine/core';
import apiClient from '../../../../services/apiClient';

export default function EditProductPage({params}: { params: Promise<{ id: number }> }) {
    const {id} = use(params);
    const [product, setProduct] = useState<ProductUpdateDTO | null>(null);
    const [categories, setCategories] = useState<Category[]>([]);
    const router = useRouter();

    useEffect(() => {
        const fetchProduct = async () => {
            try {
                const response = await apiClient.get(`/product/${id}`);
                setProduct(response.data);
            } catch (error) {
                console.error('Error fetching product:', error);
            }
        };

        const fetchCategories = async () => {
            try {
                const response = await apiClient.get('/category');
                setCategories(response.data);
            } catch (error) {
                console.error('Error fetching categories:', error);
            }
        };
        fetchProduct();
        fetchCategories();
    }, [id]);

    const handleSave = async () => {
        try {
            await apiClient.put(`/product/${id}`, product);
            router.push('/products');
        } catch (error) {
            console.error('Error updating product:', error);
        }
    };

    const handleCancel = () => {
        router.push('/products');
    };

    if (!product || !categories) return <div>Loading...</div>;

    const isFormValid = product.name && product.price && product.categoryId;

    return (
        <Container>
            <h1>Edit Product</h1>
            <TextInput
                label="Product Name"
                value={product.name}
                onChange={(e) => setProduct({...product, name: e.currentTarget.value})}
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
                onChange={(value) => setProduct({...product, categoryId: value?parseInt(value):null})}
                required
            />
            <Button onClick={handleSave} disabled={!isFormValid}>Save Changes</Button>
            <Button variant="outline" color="gray" onClick={handleCancel} style={{ marginLeft: '10px' }}>
                Cancel
            </Button>
        </Container>
    );
}
