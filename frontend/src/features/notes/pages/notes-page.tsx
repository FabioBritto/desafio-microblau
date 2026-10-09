import { Funnel } from 'lucide-react'
import { useMemo, useState } from 'react'
import { Button } from '@/shared/components/button'
import { Pagination } from '@/shared/components/pagination'
import { Select, type SelectOption } from '@/shared/components/select'
import { DeleteNoteModal } from '../components/delete-note-modal'
import { NoteFormModal } from '../components/note-form-modal'
import { NotesTable } from '../components/notes-table'
import { PeriodFilterModal } from '../components/period-filter-modal'
import { useCreateNote, useDeleteNote, useNotesList, useUpdateNote } from '../queries'
import type { Note, NotesFilters } from '../schmeas'

const optionFilters: NotesFilters = { page: 1, size: 100 }

function toSelectOptions(values: Array<string | undefined>, selected?: string): SelectOption[] {
    const unique = [...new Set(values.filter((value): value is string => Boolean(value)))]
    if (selected && !unique.includes(selected)) unique.unshift(selected)
    return unique.map((value) => ({ value, label: value }))
}

export function NotesPage() {
    const [filters, setFilters] = useState<NotesFilters>({ page: 1, size: 5 })
    const [createOpen, setCreateOpen] = useState(false)
    const [editingNote, setEditingNote] = useState<Note | null>(null)
    const [deletingNote, setDeletingNote] = useState<Note | null>(null)
    const [periodOpen, setPeriodOpen] = useState(false)
    const { data, isLoading, isPlaceholderData, isError } = useNotesList(filters)
    const { data: optionSource } = useNotesList(optionFilters)
    const createNote = useCreateNote()
    const updateNote = useUpdateNote()
    const deleteNote = useDeleteNote()

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
                <Button size="sm" variant="primary" onClick={() => setPeriodOpen(true)}>
                    <Funnel size={14} /> Filtrar por período
                </Button>
            </div>
            <NotesTable
                notes={data?.content ?? []}
                isLoading={isLoading || isPlaceholderData}
                isError={isError}
                totalPages={data?.totalPages}
                onEdit={setEditingNote}
                onDelete={setDeletingNote}
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
            <NoteFormModal
                mode="update"
                open={editingNote != null}
                onClose={() => setEditingNote(null)}
                defaultValues={
                    editingNote
                        ? {
                              site: editingNote.site,
                              equipment: editingNote.equipment,
                              variable: editingNote.variable,
                              message: editingNote.message,
                          }
                        : undefined
                }
                equipmentOptions={toSelectOptions(
                    optionSource?.content.map((note) => note.equipment) ?? [],
                    editingNote?.equipment,
                )}
                variableOptions={toSelectOptions(
                    optionSource?.content.map((note) => note.variable) ?? [],
                    editingNote?.variable,
                )}
                pending={updateNote.isPending}
                onSubmit={async (values) => {
                    if (!editingNote) return
                    await updateNote.mutateAsync({ id: editingNote.id, note: values })
                    setEditingNote(null)
                }}
            />
            <PeriodFilterModal
                open={periodOpen}
                onClose={() => setPeriodOpen(false)}
                startDate={filters.startDate}
                endDate={filters.endDate}
                onApply={(values) => {
                    setFilters((prev) => ({
                        ...prev,
                        startDate: values.startDate,
                        endDate: values.endDate,
                        page: 1,
                    }))
                }}
                onClear={() => {
                    setFilters((prev) => ({
                        ...prev,
                        startDate: undefined,
                        endDate: undefined,
                        page: 1,
                    }))
                    setPeriodOpen(false)
                }}
            />
            <DeleteNoteModal
                open={deletingNote != null}
                onClose={() => setDeletingNote(null)}
                pending={deleteNote.isPending}
                onConfirm={async () => {
                    if (!deletingNote) return
                    await deleteNote.mutateAsync(deletingNote.id)
                    setDeletingNote(null)
                }}
            />
        </div>
    )
}
