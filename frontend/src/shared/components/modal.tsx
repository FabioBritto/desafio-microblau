import { useEffect, useId, type ReactNode } from 'react'

interface ModalProps {
    open: boolean
    title: string
    onClose: () => void
    children: ReactNode
}

export function Modal({ open, title, onClose, children }: ModalProps) {
    const titleId = useId()

    useEffect(() => {
        if (!open) return

        function onKeyDown(event: KeyboardEvent) {
            if (event.key === 'Escape') onClose()
        }

        document.addEventListener('keydown', onKeyDown)
        return () => document.removeEventListener('keydown', onKeyDown)
    }, [open, onClose])

    if (!open) return null

    return (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-gray-800/40 p-4">
            <div
                role="dialog"
                aria-modal="true"
                aria-labelledby={titleId}
                className="max-h-[calc(100vh-2rem)] w-full max-w-lg overflow-y-auto rounded-lg bg-white p-6 shadow-sm"
            >
                <h2 id={titleId} className="mb-4 text-h6 text-gray-800">
                    {title}
                </h2>
                {children}
            </div>
        </div>
    )
}
