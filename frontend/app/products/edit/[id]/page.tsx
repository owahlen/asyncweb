'use client';

import { use, useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';
import { Button, Container, TextInput } from '@mantine/core';
import apiClient from '../../../../services/apiClient';

export default function EditProductPage({ params }: { params: Promise<{ id: number }> }) {
  const { id } = use(params);
  const [product, setProduct] = useState<any | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const router = useRouter();

  useEffect(() => {
    const fetchProduct = async () => {
      try {
        setIsLoading(true);
        const response = await apiClient.get(`/product/${id}`);
        setProduct(response.data);
      } catch (error) {
        console.error('Error fetching product:', error);
      } finally {
        setIsLoading(false);
      }
    };
    fetchProduct();
  }, [id]);

  const handleSave = async () => {
    try {
      await apiClient.put(`/product/${id}`, product);
      router.push('/products');
    } catch (error) {
      console.error('Error updating product:', error);
    }
  };

  if (isLoading) return <div>Loading...</div>;

  return (
    <Container>
      <h1>Edit Product</h1>
      <TextInput
        label="Product Name"
        value={product.name}
        onChange={(e) =>
          setProduct({
            ...product,
            name: e.currentTarget.value,
          })
        }
      />
      <Button onClick={handleSave}>Save Changes</Button>
    </Container>
  );
}
