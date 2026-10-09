import { ChevronDown } from 'lucide-react'
import { useId, type ComponentProps } from 'react'

export interface SelectOption {
    value: string
    label: string
}

interface SelectProps extends Omit<ComponentProps<'select'>, 'children'> {
    options: SelectOption[]
    placeholder?: string
    error?: string
}

export function Select({
    options,
    placeholder,
    error,
    className,
    id,
    value,
    disabled,
    ...props
}: SelectProps) {
    const generatedId = useId()
    const selectId = id ?? generatedId
    const errorId = `${selectId}-error`
    const isEmpty = value === '' || value == null

    return (
        <div className={`w-full ${className ?? ''}`}>
            <div className="relative">
                <select
                    {...props}
                    id={selectId}
                    value={value}
                    disabled={disabled}
                    aria-invalid={error ? true : undefined}
                    aria-describedby={error ? errorId : undefined}
                    className={`h-9 w-full appearance-none rounded-md border bg-white px-4 pr-10 text-body-sm shadow-sm focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-primary-500 disabled:cursor-not-allowed disabled:border-gray-100 disabled:bg-gray-100 disabled:text-gray-400 ${
                        error ? 'border-danger-main' : 'border-gray-100'
                    } ${isEmpty ? 'text-gray-400' : 'text-gray-800'}`}
                >
                    {placeholder != null && <option value="">{placeholder}</option>}
                    {options.map((option) => (
                        <option key={option.value} value={option.value}>
                            {option.label}
                        </option>
                    ))}
                </select>
                <ChevronDown
                    aria-hidden
                    className={`pointer-events-none absolute top-1/2 right-3 size-4 -translate-y-1/2 text-gray-400 ${
                        disabled ? 'opacity-40' : ''
                    }`}
                />
            </div>
            {error && (
                <p id={errorId} role="alert" className="mt-1 text-body-xs text-danger-main">
                    {error}
                </p>
            )}
        </div>
    )
}
