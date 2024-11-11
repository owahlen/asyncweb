import {Container, Text, Title} from '@mantine/core';

export default function HomePage() {
    return (
        <Container>
            <Title>Welcome to the Product Management App</Title>
            <Text mt="md">
                Manage your products efficiently with features to view, edit, and delete products.
            </Text>
        </Container>

    );
}
