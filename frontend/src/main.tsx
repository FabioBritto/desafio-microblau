import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.tsx'
import { notesApi } from './features/notes/api.ts'

console.log(import.meta.env.VITE_API_URL);
notesApi.list({ page: 1, limit: 10 }).then(console.log).catch(console.error);

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <App />
  </StrictMode>,
)
