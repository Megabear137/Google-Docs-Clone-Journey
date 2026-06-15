import { useState } from 'react'
import { Routes, Route } from 'react-router-dom'
import { LoginPage } from './pages/LoginPage'
import { NotesListPage } from './pages/NotesListPage'
import { RegisterPage } from './pages/RegisterPage'
import { NotePage } from './pages/NotePage'


function App() {
  const [count, setCount] = useState(0)

  return (
    <Routes>
      <Route path="/login" element={<LoginPage />} />
      <Route path="/register" element={<RegisterPage />} />
      <Route path="/notes" element={<NotesListPage />} />
      <Route path="/notes/:id" element={<NotePage />} />
    </Routes>
  )
}

export default App
