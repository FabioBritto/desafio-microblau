import { Sidebar } from './shared/components/sidebar'

export default function App() {
    return (
        <div className="flex min-h-screen bg-background">
            <Sidebar />
            <main className="flex-1" />
        </div>
    )
}
