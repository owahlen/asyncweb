'use client';

import {AppShell} from '@mantine/core';
import {Header} from "@/components/Header/Header";
import {Navbar} from "@/components/Navbar/Navbar";
import React from "react";
import {useDisclosure} from "@mantine/hooks";

export interface ShellProps {
    children?: React.ReactNode;
}

export function AppLayout(shellProps: ShellProps) {
    const [opened, {close, toggle}] = useDisclosure();
    const {children} = shellProps;

    return (
        <AppShell
            padding="md"
            header={{height: 60}}
            navbar={{width: 300, breakpoint: 'sm', collapsed: {mobile: !opened}}}
        >
            <AppShell.Header>
                <Header opened={opened} toggle={toggle}/>
            </AppShell.Header>
            <AppShell.Navbar p="md">
                <Navbar close={close}/>
            </AppShell.Navbar>
            <AppShell.Main>
                {children}
            </AppShell.Main>
        </AppShell>
    );
}
