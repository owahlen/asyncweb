import {Burger, Group, Image} from '@mantine/core';
import Link from "next/link";

export interface HeaderProps {
    opened?: boolean;
    toggle?: () => void;
}
export function Header(headerProps: HeaderProps) {
    const {opened, toggle} = headerProps;

    return (
        <Group h="100%" px="md">
            <Burger opened={opened} onClick={toggle} hiddenFrom="sm" size="sm"/>

            <Link href="/">
            <Image height={30}
                   style={{cursor:"pointer"}}
                   src="images/logo.png"
                   alt="Mantine logo"/>
            </Link>
        </Group>
    );
}
