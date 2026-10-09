import {
    ChartNoAxesColumn,
    ChartNoAxesCombined,
    FileText,
    House,
    MapPinCheckInside,
    MessageCircle,
    RotateCwFadingClock,
    type LucideIcon,
} from 'lucide-react'
import logo from '../../assets/icons/logo.svg'
import { NavItem } from './nav-item'

interface NavEntry {
    label: string
    icon: LucideIcon
}

const mainItems: NavEntry[] = [
    { label: 'Home', icon: House },
    { label: 'Análises', icon: ChartNoAxesCombined },
    { label: 'Notas', icon: MessageCircle },
    { label: 'Histórico', icon: RotateCwFadingClock },
    { label: 'Mapa', icon: MapPinCheckInside },
    { label: 'Dashboard', icon: ChartNoAxesColumn },
]

const optionItems: NavEntry[] = [
    { label: 'Logs', icon: FileText },
]

export function Sidebar() {
    return (
        <aside className="flex h-screen w-64 shrink-0 flex-col bg-gray-800 px-4 py-6">
            <img src={logo} alt="" width={50} height={39} className="self-center" />
            <nav className="mt-8 flex flex-col gap-6">
                <section className="flex flex-col gap-2">
                    <p className="text-h5 text-body-sm text-white">Menu Principal</p>
                    <ul className="flex flex-col gap-1">
                        {mainItems.map((item) => (
                            <li key={item.label}>
                                <NavItem
                                    icon={item.icon}
                                    label={item.label}
                                    isActive={item.label === 'Notas'}
                                    onClick={() => {}}
                                />
                            </li>
                        ))}
                    </ul>
                </section>
                <hr className="border-gray-400" />
                <section className="flex flex-col gap-2">
                    <p className="text-h5 text-body-sm text-white">Opções</p>
                    <ul className="flex flex-col gap-1">
                        {optionItems.map((item) => (
                            <li key={item.label}>
                                <NavItem
                                    icon={item.icon}
                                    label={item.label}
                                    isActive={false}
                                    onClick={() => {}}
                                />
                            </li>
                        ))}
                    </ul>
                </section>
            </nav>
        </aside>
    )
}
