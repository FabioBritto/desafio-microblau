import { useState, type FormEvent, type ReactNode } from 'react'
import { Button } from '@/shared/components/button'
import { Modal } from '@/shared/components/modal'
import { Select, type SelectOption } from '@/shared/components/select'
import {
    createNoteSchema,
    updateNoteSchema,
    type CreateNoteDTO,
    type UpdateNoteDTO,
} from '../schmeas'

interface NoteFormBaseProps {
    open: boolean
    onClose: () => void
    equipmentOptions: SelectOption[]
    variableOptions: SelectOption[]
    pending?: boolean
}

interface NoteFormCreateProps extends NoteFormBaseProps {
    mode?: 'create'
    defaultValues?: Partial<CreateNoteDTO>
    onSubmit: (values: CreateNoteDTO) => void | Promise<void>
}

interface NoteFormUpdateProps extends NoteFormBaseProps {
    mode: 'update'
    defaultValues?: Partial<UpdateNoteDTO>
    onSubmit: (values: UpdateNoteDTO) => void | Promise<void>
}

type NoteFormModalProps = NoteFormCreateProps | NoteFormUpdateProps

const emptyValues: CreateNoteDTO = {
    site: '',
    equipment: '',
    variable: '',
    author: '',
    message: '',
}

type FieldErrors = Partial<Record<keyof CreateNoteDTO, string>>

const fieldClassName =
    'w-full rounded-md border bg-white px-4 text-body-sm text-gray-800 shadow-sm focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-primary-500'

export function NoteFormModal({ open, onClose, ...formProps }: NoteFormModalProps) {
    const mode = formProps.mode ?? 'create'

    return (
        <Modal open={open} title={mode === 'update' ? 'Editar nota' : 'Nova nota'} onClose={onClose}>
            {open ? <NoteForm onClose={onClose} {...formProps} /> : null}
        </Modal>
    )
}

function NoteForm({
    onClose,
    defaultValues,
    equipmentOptions,
    variableOptions,
    pending = false,
    ...modeProps
}: Omit<NoteFormModalProps, 'open'>) {
    const mode = modeProps.mode ?? 'create'
    const [values, setValues] = useState<CreateNoteDTO>({ ...emptyValues, ...defaultValues })
    const [errors, setErrors] = useState<FieldErrors>({})

    async function handleSubmit(event: FormEvent<HTMLFormElement>) {
        event.preventDefault()
        if (modeProps.mode === 'update') {
            const parsed = updateNoteSchema.safeParse(values)
            if (!parsed.success) {
                setErrors(toFieldErrors(parsed.error.issues))
                return
            }
            setErrors({})
            await modeProps.onSubmit(parsed.data)
            return
        }
        const parsed = createNoteSchema.safeParse(values)
        if (!parsed.success) {
            setErrors(toFieldErrors(parsed.error.issues))
            return
        }
        setErrors({})
        await modeProps.onSubmit(parsed.data)
    }

    return (
            <form className="flex flex-col gap-4" onSubmit={handleSubmit} noValidate>
                <Field label="Site" error={errors.site}>
                    <input
                        aria-label="Site"
                        value={values.site}
                        aria-invalid={errors.site ? true : undefined}
                        onChange={(event) => setValues((prev) => ({ ...prev, site: event.target.value }))}
                        className={`h-9 ${fieldClassName} ${errors.site ? 'border-danger-main' : 'border-gray-100'}`}
                    />
                </Field>
                <Field label="Equipamento">
                    <Select
                        aria-label="Equipamento"
                        placeholder="Selecione um equipamento"
                        options={equipmentOptions}
                        value={values.equipment}
                        error={errors.equipment}
                        onChange={(event) =>
                            setValues((prev) => ({ ...prev, equipment: event.target.value }))
                        }
                    />
                </Field>
                <Field label="Monitoração">
                    <Select
                        aria-label="Monitoração"
                        placeholder="Selecione uma monitoração"
                        options={variableOptions}
                        value={values.variable}
                        error={errors.variable}
                        onChange={(event) =>
                            setValues((prev) => ({ ...prev, variable: event.target.value }))
                        }
                    />
                </Field>
                {mode === 'create' && (
                    <Field label="Autor" error={errors.author}>
                        <input
                            aria-label="Autor"
                            value={values.author}
                            aria-invalid={errors.author ? true : undefined}
                            onChange={(event) =>
                                setValues((prev) => ({ ...prev, author: event.target.value }))
                            }
                            className={`h-9 ${fieldClassName} ${errors.author ? 'border-danger-main' : 'border-gray-100'}`}
                        />
                    </Field>
                )}
                <Field label="Mensagem" error={errors.message}>
                    <textarea
                        aria-label="Mensagem"
                        value={values.message}
                        aria-invalid={errors.message ? true : undefined}
                        onChange={(event) =>
                            setValues((prev) => ({ ...prev, message: event.target.value }))
                        }
                        className={`h-36 resize-none overflow-y-auto ${fieldClassName} ${
                            errors.message ? 'border-danger-main' : 'border-gray-100'
                        }`}
                    />
                </Field>
                <div className="flex justify-end gap-3">
                    <Button type="button" variant="secondary" onClick={onClose}>
                        Cancelar
                    </Button>
                    <Button type="submit" variant="primary" disabled={pending}>
                        {mode === 'update' ? 'Salvar' : 'Criar Nota'}
                    </Button>
                </div>
            </form>
    )
}

function Field({
    label,
    error,
    children,
}: {
    label: string
    error?: string
    children: ReactNode
}) {
    return (
        <label className="flex flex-col gap-1 text-body-sm text-gray-800">
            {label}
            {children}
            {error && (
                <span role="alert" className="text-body-xs text-danger-main">
                    {error}
                </span>
            )}
        </label>
    )
}

function toFieldErrors(issues: { path: PropertyKey[]; message: string }[]) {
    const errors: FieldErrors = {}
    for (const issue of issues) {
        const key = issue.path[0]
        if (typeof key === 'string' && errors[key as keyof CreateNoteDTO] == null) {
            errors[key as keyof CreateNoteDTO] = issue.message
        }
    }
    return errors
}
