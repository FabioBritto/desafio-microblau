import type { LucideIcon } from 'lucide-react'

interface NavItemProps {
    icon: LucideIcon
    label: string
    isActive: boolean
    onClick: () => void
}

export function NavItem({ icon: Icon, label, isActive, onClick }: NavItemProps) {
    return (
        <button
            type="button"
            onClick={onClick}
            aria-current={isActive ? 'page' : undefined}
            className={`flex w-full items-center gap-3 rounded-lg px-3 py-2 text-left text-body-md text-white transition-colors ${
                isActive ? 'bg-primary-400' : 'bg-gray-800'
            }`}
        >
            <Icon className="size-5 shrink-0" />
            <span className="truncate">{label}</span>
        </button>
    )
}
