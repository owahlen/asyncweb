'use client';

import Link from 'next/link';
import {Button, Container, Stack, Text, Title} from '@mantine/core';

export default function HomePage() {
    return (
        <Container>
            <Title>Welcome to the Product Management App</Title>
            <Text mt="md">
                Manage your products efficiently with features to view, edit, and delete products.
            </Text>
            <Stack>
                <Link href="/products">
                    <Button mt="lg">Go to Products</Button>
                </Link>
                <Link href="/categories">
                    <Button mt="lg">Go to Categories</Button>
                </Link>
            </Stack>
        </Container>
    );
}
