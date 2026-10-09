import type { ComponentProps } from 'react'

interface ButtonProps extends ComponentProps<'button'> {
    variant?: 'primary' | 'secondary'
    size?: 'sm'
}

const variants = {
    primary: 'bg-primary-500 text-white',
    secondary: 'border border-gray-100 bg-white text-gray-800',
}

const sizes = {
    sm: 'h-9 px-4 text-body-sm',
}

export function Button({
    variant = 'primary',
    size = 'sm',
    className,
    type = 'button',
    children,
    ...props
}: ButtonProps) {
    return (
        <button
            type={type}
            className={`inline-flex shrink-0 items-center gap-2 rounded font-semibold ${variants[variant]} ${sizes[size]} ${className ?? ''}`}
            {...props}
        >
            {children}
        </button>
    )
}
