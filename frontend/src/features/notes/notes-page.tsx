import { Funnel } from 'lucide-react'
import { useMemo, useState } from 'react'
import { Button } from '@/shared/components/button'
import { Pagination } from '@/shared/components/pagination'
import { Select, type SelectOption } from '@/shared/components/select'
import { NoteFormModal } from './components/note-form-modal'
import { NotesTable } from './components/notes-table'
import { useCreateNote, useNotesList } from './queries'
import type { NotesFilters } from './schmeas'

const optionFilters: NotesFilters = { page: 1, size: 100 }

function toSelectOptions(values: Array<string | undefined>, selected?: string): SelectOption[] {
    const unique = [...new Set(values.filter((value): value is string => Boolean(value)))]
    if (selected && !unique.includes(selected)) unique.unshift(selected)
    return unique.map((value) => ({ value, label: value }))
}

export function NotesPage() {
    const [filters, setFilters] = useState<NotesFilters>({ page: 1, size: 5 })
    const [createOpen, setCreateOpen] = useState(false)
    const { data, isLoading, isPlaceholderData, isError } = useNotesList(filters)
    const { data: optionSource } = useNotesList(optionFilters)
    const createNote = useCreateNote()

    const siteOptions = useMemo(
        () => toSelectOptions(optionSource?.content.map((note) => note.site) ?? [], filters.site),
        [optionSource?.content, filters.site],
    )
    const equipmentOptions = useMemo(
        () => toSelectOptions(optionSource?.content.map((note) => note.equipment) ?? [], filters.equipment),
        [optionSource?.content, filters.equipment],
    )
    const formEquipmentOptions = useMemo(
        () => toSelectOptions(optionSource?.content.map((note) => note.equipment) ?? []),
        [optionSource?.content],
    )
    const variableOptions = useMemo(
        () => toSelectOptions(optionSource?.content.map((note) => note.variable) ?? []),
        [optionSource?.content],
    )

    return (
        <div className="px-6 py-6">
            <div className="mb-5 flex items-center justify-between gap-4">
                <h1 className="text-h5 text-gray-800">Notas</h1>
                <Button size="sm" variant="primary" onClick={() => setCreateOpen(true)}>
                    Nova nota
                </Button>
            </div>
            <div className="mb-4 flex items-center justify-between gap-4">
                <div className="flex min-w-0 flex-1 gap-3">
                    <Select
                        aria-label="Site"
                        className="max-w-md"
                        placeholder="Selecione um site"
                        options={siteOptions}
                        value={filters.site ?? ''}
                        onChange={(event) => {
                            const value = event.target.value
                            setFilters((prev) => ({ ...prev, site: value || undefined, page: 1 }))
                        }}
                    />
                    <Select
                        aria-label="Equipamento"
                        className="max-w-md"
                        placeholder="Selecione um equipamento"
                        options={equipmentOptions}
                        value={filters.equipment ?? ''}
                        onChange={(event) => {
                            const value = event.target.value
                            setFilters((prev) => ({
                                ...prev,
                                equipment: value || undefined,
                                page: 1,
                            }))
                        }}
                    />
                </div>
                <Button size="sm" variant="primary" onClick={() => {}}>
                    <Funnel size={14} /> Filtrar por período
                </Button>
            </div>
            <NotesTable
                notes={data?.content ?? []}
                isLoading={isLoading || isPlaceholderData}
                isError={isError}
                totalPages={data?.totalPages}
            />
            <Pagination
                page={filters.page ?? 1}
                totalPages={data?.totalPages ?? 0}
                onPageChange={(page) => setFilters((prev) => ({ ...prev, page }))}
            />
            <NoteFormModal
                open={createOpen}
                onClose={() => setCreateOpen(false)}
                equipmentOptions={formEquipmentOptions}
                variableOptions={variableOptions}
                pending={createNote.isPending}
                onSubmit={async (values) => {
                    await createNote.mutateAsync(values)
                    setCreateOpen(false)
                }}
            />
        </div>
    )
}
