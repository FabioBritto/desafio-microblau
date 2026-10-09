import { z } from "zod";

export const noteSchema = z.object({
    id: z.string(),
    site: z.string(),
    equipment: z.string(),
    variable: z.string(),
    timestamp: z.string(),
    author: z.string(),
    message: z.string(),
});

export type Note = z.infer<typeof noteSchema>;

export const createNoteSchema = z.object({
    site: z.string().min(10, "O endereço do site precisa ter ao menos 10 caracteres").max(255),
    equipment: z.string().min(5, "O nome do equipamento precisa ter ao menos 5 caracteres").max(255),
    variable: z.string().min(3, "O nome da variável precisa ter ao menos 3 caracteres").max(255),
    author: z.string().min(5, "O nome do(a) autor(a) precisa ter ao menos 5 caracteres").max(255),
    message: z.string().min(1, "A mensagem é obrigatória").max(2500),
});

export type CreateNoteDTO = z.infer<typeof createNoteSchema>;

export const updateNoteSchema = z.object({
    site: z.string().min(10, "O endereço do site precisa ter ao menos 10 caracteres").max(255),
    equipment: z.string().min(5, "O nome do equipamento precisa ter ao menos 5 caracteres").max(255),
    variable: z.string().min(3, "O nome da variável precisa ter ao menos 3 caracteres").max(255),
    message: z.string().min(1, "A mensagem é obrigatória").max(2500),
});

export type UpdateNoteDTO = z.infer<typeof updateNoteSchema>;

export const paginatedNoteSchema = z.object({
    content: z.array(noteSchema),
    totalElements: z.number(),
    totalPages: z.number(),
    number: z.number(),
    size: z.number(),
});

export type PaginatedNote = z.infer<typeof paginatedNoteSchema>;

export const notesFilters = z.object({
    site: z.string().optional(),
    equipment: z.string().optional(),
    startDate: z.string().optional(),
    endDate: z.string().optional(),
    page: z.number().optional(),
    size: z.number().optional(),
});

export type NotesFilters = z.infer<typeof notesFilters>;

