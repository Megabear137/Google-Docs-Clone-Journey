import { useEffect, useState } from "react";
import { apiClient } from "../api/apiClient";
import { useNavigate } from 'react-router-dom'
import { useAuth } from "../auth/AuthProvider";
import { Link } from 'react-router-dom'


export type NoteResponse = {
  id: number,
  title: string,
  body: string,
  updatedAt: string
}

export function NotesListPage() {

  const [notes, setNotes] = useState<NoteResponse[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState("")
  const navigate = useNavigate()
  const { logout } = useAuth()

  useEffect(() => {
    apiClient.get('/api/notes')
      .then( (res) => setNotes(res.data) )
      .catch( () => setError("Failed to load notes") )
      .finally( () => setLoading(false) )
  }, [])

  if (loading) return <p>Loading...</p>
  if (error) return <p className="text-red-600">{error}</p>
  if (notes.length === 0) return <p>No notes yet.</p>

  return (
    <>
      <ul>
        {notes.map((note) => (
            <li key={note.id}>
              <Link to={`/notes/${note.id}`}>{note.title}</Link>
            </li>
        ))}
      </ul>
      <button onClick={() => {
        logout()
        navigate('/login')
        }}>Logout</button>
    </>
  )
}