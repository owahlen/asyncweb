'use client';

import { Category } from '@/dtos/category';
import { useEffect, useState } from 'react';
import { Button, Container, Group, Table } from '@mantine/core';
import apiClient from '../../services/apiClient';
import Link from 'next/link';

export default function CategoriesPage() {
    const [categories, setCategories] = useState<Category[]>([]);

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

    const handleDelete = async (id: number) => {
        try {
            await apiClient.delete(`/category/${id}`);
            setCategories(categories.filter((category) => category.id !== id));
        } catch (error) {
            console.error('Error deleting category:', error);
        }
    };

    return (
        <Container>
            <Link href="/">
                <Button mt="lg" variant="outline">
                    Back to Home
                </Button>
            </Link>

            <h1>Categories</h1>
            <Link href="/categories/create">
                <Button mt="lg">Create New Category</Button>
            </Link>
            <Table mt="md">
                <thead>
                <tr>
                    <th style={{ textAlign: 'left' }}>ID</th>
                    <th style={{ textAlign: 'left' }}>Name</th>
                    <th style={{ textAlign: 'left' }}>Actions</th>
                </tr>
                </thead>
                <tbody>
                {categories.map((category) => (
                    <tr key={category.id}>
                        <td>{category.id}</td>
                        <td>{category.name}</td>
                        <td>
                            <Group>
                                <Link href={`/categories/edit/${category.id}`}>
                                    <Button>Edit</Button>
                                </Link>
                                <Button color="red" onClick={() => handleDelete(category.id)}>
                                    Delete
                                </Button>
                            </Group>
                        </td>
                    </tr>
                ))}
                </tbody>
            </Table>
        </Container>
    );
}
