import { Button } from '@/shared/components/button'
import { Modal } from '@/shared/components/modal'

interface DeleteNoteModalProps {
    open: boolean
    onClose: () => void
    onConfirm: () => void | Promise<void>
    pending?: boolean
}

export function DeleteNoteModal({ open, onClose, onConfirm, pending = false }: DeleteNoteModalProps) {
    return (
        <Modal open={open} title="Excluir nota" onClose={onClose}>
            <p className="text-body-md text-gray-800">Tem certeza que deseja excluir esta nota?</p>
            <div className="mt-4 flex justify-end gap-3">
                <Button type="button" variant="secondary" onClick={onClose}>
                    Cancelar
                </Button>
                <Button type="button" variant="primary" disabled={pending} onClick={() => onConfirm()}>
                    Excluir
                </Button>
            </div>
        </Modal>
    )
}
