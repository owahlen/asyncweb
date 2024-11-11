'use client';

import Link from 'next/link';
import {AppShell, Burger, Button, Container, Group, Stack, Text, Title, Skeleton} from '@mantine/core';
import {useDisclosure} from "@mantine/hooks";

export default function HomePage() {
    const [opened, {toggle}] = useDisclosure();

    return (
        <AppShell
            padding="md"
            header={{height: 60}}
            navbar={{width: 300, breakpoint: 'sm', collapsed: {mobile: !opened}}}
        >
            <AppShell.Header>
                <Group h="100%" px="md">
                    <Burger opened={opened} onClick={toggle} hiddenFrom="sm" size="sm"/>
                    Logo
                </Group>
            </AppShell.Header>
            <AppShell.Navbar p="md">
                Navbar
                {Array(15)
                    .fill(0)
                    .map((_, index) => (
                        <Skeleton key={index} h={28} mt="sm" animate={false} />
                    ))}
            </AppShell.Navbar>
            <AppShell.Main>
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
            </AppShell.Main>
        </AppShell>
    );
}
