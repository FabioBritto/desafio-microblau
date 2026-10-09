import { NotesPage } from './features/notes/notes-page'
import { Sidebar } from './shared/components/sidebar'

export default function App() {
    return (
        <div className="flex min-h-screen bg-background">
            <Sidebar />
            <main className="min-w-0 flex-1">
                <NotesPage />
            </main>
        </div>
    )
}
