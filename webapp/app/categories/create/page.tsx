'use client';

import { CategoryCreateDTO } from '@/dtos/category';
import { useState } from 'react';
import { useRouter } from 'next/navigation';
import { Button, Container, TextInput } from '@mantine/core';
import apiClient from '../../../services/apiClient';

export default function CreateCategoryPage() {
    const [category, setCategory] = useState<CategoryCreateDTO>({ name: '' });
    const router = useRouter();

    const handleCreate = async () => {
        try {
            await apiClient.post('/category', category);
            router.push('/categories');
        } catch (error) {
            console.error('Error creating category:', error);
        }
    };

    const handleCancel = () => {
        router.push('/categories');
    };

    const isFormValid = category.name;

    return (
        <Container>
            <h1>Create New Category</h1>
            <TextInput
                label="Category Name"
                value={category.name}
                onChange={(e) => setCategory({ ...category, name: e.currentTarget.value })}
                required
            />
            <Button onClick={handleCreate} disabled={!isFormValid}>
                Create Category
            </Button>
            <Button variant="outline" color="gray" onClick={handleCancel} style={{ marginLeft: '10px' }}>
                Cancel
            </Button>
        </Container>
    );
}
