'use client';

import { useEffect, useState } from 'react';
import { Button, Container, Group, Table } from '@mantine/core';
import apiClient from '../../services/apiClient';
import Link from "next/link";

export default function ProductsPage() {
  const [products, setProducts] = useState<any[]>([]);

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

  const handleDelete = async (id: string) => {
    try {
      await apiClient.delete(`/product/${id}`);
      setProducts(products.filter((product) => product.id !== id));
    } catch (error) {
      console.error('Error deleting product:', error);
    }
  };

  return (
    <Container>
      <h1>Products</h1>
      <Table>
        <thead>
          <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {products.map((product) => (
            <tr key={product.id}>
              <td>{product.id}</td>
              <td>{product.name}</td>
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
