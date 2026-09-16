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
  const [debouncedQuery, setDebouncedQuery] = useState("")
  const [query, setQuery] = useState("")
  const [deleteError, setDeleteError] = useState("")

  const navigate = useNavigate()
  const { logout } = useAuth()

  // The debounce. Watches query; commits it to debouncedQuery after 300ms of quiet.
  useEffect(() => {
    const handle = setTimeout(() => setDebouncedQuery(query), 300)
    return () => clearTimeout(handle) 
  }, [query])

  // The fetch. Watches debouncedQuery; runs the actual API call.
  useEffect(() => {
    setError("")
    apiClient.get('/api/notes', {params: {search: debouncedQuery} })
      .then( (res) => {
          setNotes(res.data)
        } )
      .catch( () => setError("Failed to load notes") )
      .finally( () => setLoading(false) )
  }, [debouncedQuery])

  async function deleteNote(id: number): Promise<void> {

    await apiClient.delete(`/api/notes/${id}`)
    .then( () => setNotes( notes.filter( (note) => note.id != id ) )  )
    .catch( () => setDeleteError("Failed to delete note!") )

  }

  if (loading) return <p>Loading...</p>
  if (error) return <p className="text-red-600">{error}</p>
  if (notes.length === 0 && !debouncedQuery) return (
      <div>
        <p>No notes yet.</p>
        <button 
            className="border p-1"
            onClick={() => {
          navigate('/notes/new')
          }}>New Note</button>
      </div>
  )

  return (
    <>
      <input
          value={query}
          className="border rounded p-1"
          onChange={e => setQuery(e.target.value)}
          onKeyDown={e => {
            if (e.key === 'Enter') {
              e.preventDefault()
            }
          }}/>
      <ul>
        {notes.map((note) => (
            <li key={note.id}>
              <div className="flex gap-4">
                <Link to={`/notes/${note.id}`}>{note.title}</Link>
                <button 
                  className="text-red-600"
                  onClick={() => {
                    if (window.confirm(`Delete "${note.title}"?`)) {
                      deleteNote(note.id)}
                    }
                  }
                  >Delete</button>
              </div>
            </li>
        ))}
      </ul>

      <div>
        {notes.length == 0 && <p className="text-red-600">No notes found...</p>}
      </div>

      <div>
        <button 
            className="border p-1"
            onClick={() => {
          navigate('/notes/new')
          }}>New Note</button>
      </div>

      {deleteError && <p className="text-red-600">{deleteError}</p>}
        
      <div>
          <button onClick={() => {
            logout()
            navigate('/login')
            }}>Logout</button>
      </div>
    </>
  )
}