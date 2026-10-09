import { ChevronLeft, ChevronRight, ChevronsLeft, ChevronsRight } from 'lucide-react'
import type { ReactNode } from 'react'

interface PaginationProps {
    page: number
    totalPages: number
    onPageChange: (page: number) => void
    maxVisible?: number
}

function clamp(value: number, min: number, max: number) {
    return Math.min(Math.max(value, min), max)
}

function visiblePages(page: number, totalPages: number, maxVisible: number) {
    const count = Math.min(maxVisible, totalPages)
    const start =
        totalPages <= maxVisible
            ? 1
            : clamp(page - Math.floor(maxVisible / 2), 1, totalPages - maxVisible + 1)

    return Array.from({ length: count }, (_, index) => start + index)
}

export function Pagination({ page, totalPages, onPageChange, maxVisible = 5 }: PaginationProps) {
    if (totalPages <= 1) return null

    const pages = visiblePages(page, totalPages, maxVisible)
    const atStart = page <= 1
    const atEnd = page >= totalPages

    return (
        <nav aria-label="Paginação" className="mt-4 flex items-center justify-end gap-3">
            <ChevronButton
                label="Primeira página"
                disabled={atStart}
                onClick={() => onPageChange(1)}
            >
                <ChevronsLeft className="size-4" />
            </ChevronButton>
            <ChevronButton
                label="Página anterior"
                disabled={atStart}
                onClick={() => onPageChange(page - 1)}
            >
                <ChevronLeft className="size-4" />
            </ChevronButton>
            {pages.map((pageNumber) => {
                const isCurrent = pageNumber === page

                return (
                    <button
                        key={pageNumber}
                        type="button"
                        aria-label={`Página ${pageNumber}`}
                        aria-current={isCurrent ? 'page' : undefined}
                        onClick={() => onPageChange(pageNumber)}
                        className={`inline-flex size-8 items-center justify-center rounded text-body-sm text-gray-800 ${
                            isCurrent ? 'bg-primary-200' : 'hover:bg-primary-50'
                        }`}
                    >
                        {pageNumber}
                    </button>
                )
            })}
            <ChevronButton
                label="Próxima página"
                disabled={atEnd}
                onClick={() => onPageChange(page + 1)}
            >
                <ChevronRight className="size-4" />
            </ChevronButton>
            <ChevronButton
                label="Última página"
                disabled={atEnd}
                onClick={() => onPageChange(totalPages)}
            >
                <ChevronsRight className="size-4" />
            </ChevronButton>
        </nav>
    )
}

function ChevronButton({
    label,
    disabled,
    onClick,
    children,
}: {
    label: string
    disabled: boolean
    onClick: () => void
    children: ReactNode
}) {
    return (
        <button
            type="button"
            aria-label={label}
            disabled={disabled}
            onClick={onClick}
            className="inline-flex size-8 items-center justify-center rounded text-primary-400 disabled:cursor-not-allowed disabled:opacity-40"
        >
            {children}
        </button>
    )
}
