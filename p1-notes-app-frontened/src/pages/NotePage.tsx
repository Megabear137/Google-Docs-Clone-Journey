import { useEffect, useState } from "react";
import { apiClient } from "../api/apiClient";
import { type NoteResponse } from "./NotesListPage";
import { useParams } from 'react-router-dom'

export function NotePage() {

  const [note, setNote] = useState<NoteResponse | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState("")
  const { id } = useParams()

  useEffect(() => {
      apiClient.get(`/api/notes/${id}`)
        .then( (res) => setNote(res.data) )
        .catch( () => setError("Could not load note") )
        .finally( () => setLoading(false) )
    }, [id]) //re-fetch if id changes
  
  if (loading) return <p>Loading...</p>
  if (error) return <p className="text-red-600">{error}</p>
  if (!note) return <p className="text-red-600">Note not found</p>

  return (
    <>
    <p className="text-black-1000">{note.title}</p>
    <p className="text-black-600">{note.body}</p>
    </>
  )
}