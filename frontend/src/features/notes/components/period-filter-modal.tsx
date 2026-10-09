import { useState, type FormEvent } from 'react'
import { Button } from '@/shared/components/button'
import { Modal } from '@/shared/components/modal'

interface PeriodFilterModalProps {
    open: boolean
    onClose: () => void
    startDate?: string
    endDate?: string
    onApply: (values: { startDate?: string; endDate?: string }) => void
    onClear: () => void
}

const fieldClassName =
    'h-9 w-full rounded-md border bg-white px-4 text-body-sm text-gray-800 shadow-sm focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-primary-500'

export function PeriodFilterModal({
    open,
    onClose,
    startDate,
    endDate,
    onApply,
    onClear,
}: PeriodFilterModalProps) {
    return (
        <Modal open={open} title="Filtrar por período" onClose={onClose}>
            {open ? (
                <PeriodFilterForm
                    startDate={startDate}
                    endDate={endDate}
                    onClose={onClose}
                    onApply={onApply}
                    onClear={onClear}
                />
            ) : null}
        </Modal>
    )
}

function PeriodFilterForm({
    startDate,
    endDate,
    onClose,
    onApply,
    onClear,
}: Omit<PeriodFilterModalProps, 'open'>) {
    const [start, setStart] = useState(isoToDateInput(startDate))
    const [end, setEnd] = useState(isoToDateInput(endDate))
    const [error, setError] = useState<string>()

    function handleSubmit(event: FormEvent<HTMLFormElement>) {
        event.preventDefault()
        if (!start && !end) {
            setError('Informe ao menos uma data.')
            return
        }
        if (start && end && start > end) {
            setError('A data inicial não pode ser posterior à data final.')
            return
        }
        setError(undefined)
        onApply({
            startDate: start ? dateInputToStartIso(start) : undefined,
            endDate: end ? dateInputToEndIso(end) : undefined,
        })
        onClose()
    }

    return (
        <form className="flex flex-col gap-4" onSubmit={handleSubmit} noValidate>
            <label className="flex flex-col gap-1 text-body-sm text-gray-800">
                Data inicial
                <input
                    type="date"
                    aria-label="Data inicial"
                    value={start}
                    aria-invalid={error ? true : undefined}
                    onChange={(event) => setStart(event.target.value)}
                    className={`${fieldClassName} ${error ? 'border-danger-main' : 'border-gray-100'}`}
                />
            </label>
            <label className="flex flex-col gap-1 text-body-sm text-gray-800">
                Data final
                <input
                    type="date"
                    aria-label="Data final"
                    value={end}
                    aria-invalid={error ? true : undefined}
                    onChange={(event) => setEnd(event.target.value)}
                    className={`${fieldClassName} ${error ? 'border-danger-main' : 'border-gray-100'}`}
                />
            </label>
            {error && (
                <span role="alert" className="text-body-xs text-danger-main">
                    {error}
                </span>
            )}
            <div className="flex justify-end gap-3">
                <Button type="button" variant="secondary" onClick={onClose}>
                    Cancelar
                </Button>
                <Button type="button" variant="secondary" onClick={onClear}>
                    Limpar
                </Button>
                <Button type="submit" variant="primary">
                    Filtrar
                </Button>
            </div>
        </form>
    )
}

function dateInputToStartIso(value: string) {
    const [year, month, day] = value.split('-').map(Number)
    return new Date(year, month - 1, day, 0, 0, 0, 0).toISOString()
}

function dateInputToEndIso(value: string) {
    const [year, month, day] = value.split('-').map(Number)
    return new Date(year, month - 1, day, 23, 59, 59, 999).toISOString()
}

function isoToDateInput(value?: string) {
    if (!value) return ''
    const date = new Date(value)
    if (Number.isNaN(date.getTime())) return ''
    const year = date.getFullYear()
    const month = String(date.getMonth() + 1).padStart(2, '0')
    const day = String(date.getDate()).padStart(2, '0')
    return `${year}-${month}-${day}`
}
