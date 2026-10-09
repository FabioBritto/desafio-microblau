import { QueryCache, QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { toast, Toaster } from 'sonner'
import '@fontsource-variable/sora'
import './index.css'
import App from './App.tsx'
import { notesApi } from './features/notes/api.ts'
import { ApiError } from './shared/lib/api.ts'

console.log(import.meta.env.VITE_API_URL);
notesApi.list({ page: 1, size: 10 }).then(console.log).catch(console.error);

const queryClient = new QueryClient({
    queryCache: new QueryCache({
        onError: (error) => {
            if (error.name === 'AbortError') return
            const message = error instanceof ApiError ? error.message : 'Não foi possível carregar os dados'
            toast.error(message)
        },
    }),
})

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <QueryClientProvider client={queryClient}>
      <App />
      <Toaster />
    </QueryClientProvider>
  </StrictMode>,
)
