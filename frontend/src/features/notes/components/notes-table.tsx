import { flexRender } from '@tanstack/react-table'
import { getCoreRowModel, useLegacyTable, type LegacyColumnDef } from '@tanstack/react-table/legacy'
import type { Note } from '../schmeas'

interface NotesTableProps {
    notes: Note[]
    isLoading?: boolean
    isError?: boolean
    totalPages?: number
}

const columns: LegacyColumnDef<Note>[] = [
    { accessorKey: 'site', header: 'Site' },
    { accessorKey: 'equipment', header: 'Equipamento' },
    { accessorKey: 'variable', header: 'Monitoração' },
    {
        accessorKey: 'timestamp',
        header: 'Data',
        cell: ({ getValue }) => formatNoteTimestamp(String(getValue() ?? '')),
    },
    { accessorKey: 'author', header: 'Autor' },
    {
        accessorKey: 'message',
        header: 'Mensagem',
        cell: ({ getValue }) => (
            <span className="break-words whitespace-normal">{String(getValue() ?? '')}</span>
        ),
    },
]

export function NotesTable({ notes, isLoading = false, isError = false, totalPages }: NotesTableProps) {
    const table = useLegacyTable({
        data: notes,
        columns,
        getCoreRowModel: getCoreRowModel(),
        manualPagination: true,
        pageCount: totalPages,
    })

    const rows = table.getRowModel().rows

    return (
        <div className={`overflow-x-auto rounded-lg bg-white shadow-sm ${isLoading ? 'opacity-60' : ''}`}>
            <table className="w-full min-w-[720px] table-fixed">
                <thead>
                    {table.getHeaderGroups().map((headerGroup) => (
                        <tr key={headerGroup.id} className="border-b border-gray-100">
                            {headerGroup.headers.map((header) => (
                                <th
                                    key={header.id}
                                    scope="col"
                                    className="h-14 px-4 text-left align-middle text-body-md font-semibold text-gray-800"
                                >
                                    {header.isPlaceholder
                                        ? null
                                        : flexRender(header.column.columnDef.header, header.getContext())}
                                </th>
                            ))}
                        </tr>
                    ))}
                </thead>
                <tbody>
                    {rows.length === 0 && !isLoading && !isError ? (
                        <tr>
                            <td
                                colSpan={columns.length}
                                className="px-4 py-8 text-center align-middle text-body-md text-gray-800"
                            >
                                Nenhuma nota encontrada
                            </td>
                        </tr>
                    ) : (
                        rows.map((row, index) => (
                            <tr
                                key={row.id}
                                className={index === rows.length - 1 ? undefined : 'border-b border-gray-100'}
                            >
                                {row.getVisibleCells().map((cell) => (
                                    <td
                                        key={cell.id}
                                        className="px-4 py-4 align-middle text-body-md break-words text-gray-800"
                                    >
                                        {flexRender(cell.column.columnDef.cell, cell.getContext())}
                                    </td>
                                ))}
                            </tr>
                        ))
                    )}
                </tbody>
            </table>
        </div>
    )
}

function formatNoteTimestamp(value: string) {
    const date = new Date(value)
    if (Number.isNaN(date.getTime())) return value

    const parts = new Intl.DateTimeFormat('pt-BR', {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit',
        hourCycle: 'h23',
    }).formatToParts(date)

    const part = (type: Intl.DateTimeFormatPartTypes) =>
        parts.find((item) => item.type === type)?.value ?? ''

    return `${part('day')}/${part('month')}/${part('year')} ${part('hour')}:${part('minute')}:${part('second')}`
}
