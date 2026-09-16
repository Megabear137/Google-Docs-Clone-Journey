import { Routes, Route, Navigate } from 'react-router-dom'
import { LoginPage } from './pages/LoginPage'
import { NotesListPage } from './pages/NotesListPage'
import { RegisterPage } from './pages/RegisterPage'
import { NotePage } from './pages/NotePage'
import { type ReactNode } from 'react'
import { useAuth } from './auth/AuthProvider'

function App() {

  return (
    <Routes>
      <Route path="/" element={<Navigate to="/notes" replace />} />
      <Route path="/login" element={<LoginPage />} />
      <Route path="/register" element={<RegisterPage />} />
      <Route path="/notes" element={
        <ProtectedRoute>
          <NotesListPage />
        </ProtectedRoute>
        } />
      <Route path="/notes/new" element={
        <ProtectedRoute>
          <NotePage />
        </ProtectedRoute>
        } />
      <Route path="/notes/:id" element={
        <ProtectedRoute>
          <NotePage />
        </ProtectedRoute>
        } />
      <Route path="*" element={<Navigate to="/notes" replace />} />
    </Routes>
  )
}

function ProtectedRoute( {children}: {children : ReactNode} ) {
  const { user } = useAuth()
  if (!user) return <Navigate to="/login" replace/>
  return <>{children}</>
}

export default App
