import { api } from "@/shared/lib/api";
import type { CreateNoteDTO, Note, NotesFilters, PaginatedNote, UpdateNoteDTO } from "./schmeas";

export const notesApi = {
    list: (filters: NotesFilters, signal?: AbortSignal) => {
        const { page, ...rest } = filters
        return api.get<PaginatedNote>('notes', {
            ...rest,
            page: page == null ? undefined : Math.max(page - 1, 0),
        }, signal)
    },
    
    getById: (id: string, signal?: AbortSignal) =>
        api.get<Note>(`notes/${id}`,undefined, signal),
    
    create: (note: CreateNoteDTO, signal?: AbortSignal) =>
        api.post<Note>('notes', note, signal),

    update: (id: string, note: UpdateNoteDTO, signal?: AbortSignal) =>
        api.put<Note>(`notes/${id}`, note, signal),

    delete: (id: string, signal?: AbortSignal) =>
        api.delete<Note>(`notes/${id}`, signal),
}