import Link from 'next/link';
import {NavLink} from '@mantine/core';
import {IconCategory, IconHome, IconShoppingCart} from '@tabler/icons-react';

export interface NavbarProps {
    close: () => void;
}
export function Navbar(navbarProps: NavbarProps) {
    const {close} = navbarProps;
    return (
        <>
            {/* Home link */}
            <Link href="/" passHref legacyBehavior>
                <NavLink label="Home" leftSection={<IconHome size={18}/>} onClick={close}/>
            </Link>

            {/* Product link */}
            <Link href="/products" passHref legacyBehavior>
                <NavLink label="Products" leftSection={<IconShoppingCart size={18}/>} onClick={close}/>
            </Link>

            {/* Categories link */}
            <Link href="/categories" passHref legacyBehavior>
                <NavLink label="Categories" leftSection={<IconCategory size={18}/>} onClick={close}/>
            </Link>
        </>
    );
}
