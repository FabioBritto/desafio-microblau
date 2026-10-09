import { keepPreviousData, useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { notesApi } from "./api";
import type { CreateNoteDTO, NotesFilters, UpdateNoteDTO } from "./schmeas";

export const notesKeys = {
    all: ['notes'] as const,
    list: (filters: NotesFilters) => [...notesKeys.all, 'list', filters] as const,
    getById: (id: string) => [...notesKeys.all, 'getById', id] as const,
}

export function useNotesList(filters: NotesFilters) {
    return useQuery({
        queryKey: notesKeys.list(filters),
        queryFn: ({ signal }) => notesApi.list(filters, signal),
        placeholderData: keepPreviousData,
    })
}

export function useGetNoteById(id: string) {
    return useQuery({
        queryKey: notesKeys.getById(id),
        queryFn: ({ signal }) => notesApi.getById(id, signal),
    })
}

export function useCreateNote() {
    const queryClient = useQueryClient();

    return useMutation({
        mutationFn: (note: CreateNoteDTO) => notesApi.create(note),
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: notesKeys.all });
        },
    })
}

export function useUpdateNote() {
    const queryClient = useQueryClient();

    return useMutation({
        mutationFn: ({ id, note }: { id: string, note: UpdateNoteDTO }) => notesApi.update(id, note),
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: notesKeys.all });
        },
    })
}