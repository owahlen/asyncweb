'use client';

import {Product} from '@/dtos/product';
import {useEffect, useState} from 'react';
import {Button, Container, Group, Table} from '@mantine/core';
import apiClient from '../../services/apiClient';
import Link from "next/link";

export default function ProductsPage() {
  const [products, setProducts] = useState<Product[]>([]);

  useEffect(() => {
    const fetchProducts = async () => {
      try {
        const response = await apiClient.get('/product');
        setProducts(response.data);
      } catch (error) {
        console.error('Error fetching products:', error);
      }
    };
    fetchProducts();
  }, []);

  const handleDelete = async (id: number) => {
    try {
      await apiClient.delete(`/product/${id}`);
      setProducts(products.filter((product) => product.id !== id));
    } catch (error) {
      console.error('Error deleting product:', error);
    }
  };

  return (
    <Container>
      <Link href="/">
        <Button mt="lg" variant="outline">
          Back to Home
        </Button>
      </Link>

      <h1>Products</h1>
      <Link href="/products/create">
        <Button mt="lg">Create New Product</Button>
      </Link>
      <Table mt="md">
        <thead>
          <tr>
            <th style={{ textAlign: 'left' }}>ID</th>
            <th style={{ textAlign: 'left' }}>Name</th>
            <th style={{ textAlign: 'left' }}>Price</th>
            <th style={{ textAlign: 'left' }}>Category</th>
            <th style={{ textAlign: 'left' }}>Actions</th>
          </tr>
        </thead>
        <tbody>
          {products.map((product) => (
            <tr key={product.id}>
              <td>{product.id}</td>
              <td>{product.name}</td>
              <td>{product.price}</td>
              <td>{product.categoryName}</td>
              <td>
                <Group>
                  <Link href={`/products/edit/${product.id}`}>
                    <Button>Edit</Button>
                  </Link>
                  <Button color="red" onClick={() => handleDelete(product.id)}>
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
