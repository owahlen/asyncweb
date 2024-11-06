'use client';

import { Category } from '@/dtos/category';
import { use, useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';
import { Button, Container, TextInput } from '@mantine/core';
import apiClient from '../../../../services/apiClient';

export default function EditCategoryPage({ params }: { params: Promise<{ id: number }> }) {
    const { id } = use(params);
    const [category, setCategory] = useState<Category | null>(null);
    const router = useRouter();

    useEffect(() => {
        const fetchCategory = async () => {
            try {
                const response = await apiClient.get(`/category/${id}`);
                setCategory(response.data);
            } catch (error) {
                console.error('Error fetching category:', error);
            }
        };
        fetchCategory();
    }, [id]);

    const handleSave = async () => {
        try {
            await apiClient.put(`/category/${id}`, category);
            router.push('/categories');
        } catch (error) {
            console.error('Error updating category:', error);
        }
    };

    const handleCancel = () => {
        router.push('/categories');
    };

    if (!category) return <div>Loading...</div>;

    const isFormValid = category.name;

    return (
        <Container>
            <h1>Edit Category</h1>
            <TextInput
                label="Category Name"
                value={category.name}
                onChange={(e) => setCategory({ ...category, name: e.currentTarget.value })}
                required
            />
            <Button onClick={handleSave} disabled={!isFormValid}>Save Changes</Button>
            <Button variant="outline" color="gray" onClick={handleCancel} style={{ marginLeft: '10px' }}>
                Cancel
            </Button>
        </Container>
    );
}
