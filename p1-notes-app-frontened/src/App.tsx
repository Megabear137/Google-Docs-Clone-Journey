import React, { useState } from 'react'
import { Routes, Route, Navigate, useActionData } from 'react-router-dom'
import { LoginPage } from './pages/LoginPage'
import { NotesListPage } from './pages/NotesListPage'
import { RegisterPage } from './pages/RegisterPage'
import { NotePage } from './pages/NotePage'
import { type ReactNode } from 'react'
import { useAuth } from './auth/AuthProvider'

function App() {
  const [count, setCount] = useState(0)

  return (
    <Routes>
      <Route path="/login" element={<LoginPage />} />
      <Route path="/register" element={<RegisterPage />} />
      <Route path="/notes" element={
        <ProtectedRoute>
          <NotesListPage />
        </ProtectedRoute>
        } />
      <Route path="/notes/:id" element={
        <ProtectedRoute>
          <NotePage />
        </ProtectedRoute>
        } />
    </Routes>
  )
}

function ProtectedRoute( {children}: {children : ReactNode} ) {
  const { user } = useAuth()
  if (!user) return <Navigate to="/login" replace/>
  return <>{children}</>
}

export default App
