import { useEffect, useState } from "react";
import { apiClient } from "../api/apiClient";
import { type NoteResponse } from "./NotesListPage";
import { useParams } from 'react-router-dom'
import ReactMarkdown from 'react-markdown'
import { useNavigate } from 'react-router-dom'
import { useRef } from 'react'

export function NotePage() {

  type Tag = {id: number, name: string}

  const [note, setNote] = useState<NoteResponse | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState("")
  const [saveError, setSaveError] = useState("")
  const [body, setBody] = useState("")
  const [title, setTitle] = useState("")
  const [isSaving, setSaving] = useState(false)
  const [justSaved, setJustSaved] = useState(false)
  const [tags, setTags] = useState<Tag[]>([])
  const [tagInput, setTagInput] = useState("")
  const { id } = useParams()
  const navigate = useNavigate()
  const [isNew] = useState(id == undefined) // Checks if a new note is being made

  const bodyRef = useRef<HTMLTextAreaElement>(null)
  
  const isDirty = isNew
        ? title !== "" || body !== ""          
        : note?.title !== title || note?.body !== body 


  async function saveNote(): Promise<void> {
    
    setSaving(true)
    setSaveError("")

    if (title == "") {
      setSaveError("Save failed! Title cannot be empty!")
      setTimeout(() => { setSaveError("");}, 2000)
      setSaving(false)
      return
    }

    if (isNew) {
        try {
          const { data } = await apiClient.post('/api/notes', { title, body })
          navigate(`/notes/${data.id}`)
        } catch {
          setSaveError("Failed to save note")
          setSaving(false)
        }

    } else {
      // Flash brief Saved! message on successful save
      await apiClient.put(`/api/notes/${id}`, { title, body })
      .then((res) => {
        setJustSaved(true)
        setNote(res.data)
      })
      .catch( () => setSaveError("Failed to Save Note!") )
      .finally( () => { 
        setSaving(false);              
        setTimeout(() => {
          setJustSaved(false);
        }, 2000) } )
  
    }

    
  }

  async function addTag(): Promise<void> {
    
    if (tagInput == "") {
      setSaveError("Save failed! Tag name cannot be empty!")
      return
    }

    await apiClient.post(`/api/notes/${id}/tags`, {'name': tagInput})
    .then((res) => {
      setTags(res.data)
     })
    .catch(() => setSaveError("Failed to Save Tag!"))
    .finally( () => {   
      setTagInput("")})
  }

  async function removeTag(tagName: string): Promise<void> {
    
    await apiClient.delete(`/api/notes/${id}/tags/${tagName}`)
    .then(() => {
      setTags(tags.filter( (tag) => tag.name != tagName ) )
    })
    
  }

  function backToNotes() {
    if (isDirty) {
      if (window.confirm(`Unsaved changes will be lost.`)) {
        navigate('/notes')
      }
    }
    else {
      navigate('/notes')
    }
  }

  function wrapSelection(marker: string) {
    const el = bodyRef.current
    if (!el) return

    const start = el.selectionStart
    const end = el.selectionEnd
    const selected = body.slice(start, end)

    el.focus()
    el.setSelectionRange(start, end)
    document.execCommand('insertText', false, marker + selected + marker)
  
    if (start === end) {
      // nothing was selected — put cursor between the markers
        const mid = start + marker.length
        el.setSelectionRange(mid, mid)
    }
  }

  useEffect( () => {
    function handleKeyDown(e: KeyboardEvent) {
      // detect Ctrl+S / Cmd+S
      if (e.key == 'Escape') {
        e.preventDefault()      // stop the browser's "Save Page" dialog
        backToNotes()
      }

    }
    
    window.addEventListener('keydown', handleKeyDown)
    return () => window.removeEventListener('keydown', handleKeyDown)   // cleanup
  }, [isDirty] )

  useEffect(() => {
    function handleKeyDown(e: KeyboardEvent) {
      // detect Ctrl+S / Cmd+S
      if ((e.ctrlKey || e.metaKey) && e.key === 's') {
        e.preventDefault()      // stop the browser's "Save Page" dialog
        saveNote()
      }

      if ((e.ctrlKey || e.metaKey) && e.key === 'b') {
        e.preventDefault()
        wrapSelection('**')
      }

      if ((e.ctrlKey || e.metaKey) && e.key === 'i') {
        e.preventDefault()
        wrapSelection('*')
      }

    }

    window.addEventListener('keydown', handleKeyDown)
    return () => window.removeEventListener('keydown', handleKeyDown)   // cleanup
  }, [title, body])


  useEffect(() => {

      if (isNew) {
        setBody("")
        setTitle("")
        setTags([])
        setLoading(false)
      } else {
        apiClient.get(`/api/notes/${id}`)
        .then( (res) => {
            setNote(res.data)
            setBody(res.data.body)
            setTitle(res.data.title)
            setTags(res.data.tags)} )
        .catch( () => setError(`Could not load note`) )
        .finally( () => setLoading(false) ) 
      }
    }, [id]) //re-fetch if id changes
  
  if (loading) return <p>Loading...</p>
  if (error) return <p className="text-red-600">{error}</p>
  if (!note && !isNew) return <p className="text-red-600">Note not found</p>

  return (
    <>


      <button className="border p-1" onClick={() => {
            backToNotes()
          }}>Back to Notes</button>
    
      <div className="h-5"></div>
          
      {/* Title */}
      <div className="flex justify-center">
        <textarea className="flex-1 border rounded p-2 font-mono text-center field-sizing-content" 
                  value={title} onChange={e => setTitle(e.target.value)} />
      </div>

      <div className="h-5"></div>

      {/* Tag Carousel */}
      <div className="flex gap-4">  
        {tags.map((tag) => (
          <div key={tag.id} className="relative inline-block">
            <span className="border rounded px-2 py-1">{tag.name}</span>
            <button
              onClick={ () => removeTag(tag.name) }
              className="absolute -top-2 -right-2 bg-gray-200 rounded-full w-4 h-4 flex items-center justify-center text-xs leading-none"
            >×</button>
          </div>
        ))}
        <input
          value={tagInput}
          className="border rounded p-1"
          onChange={e => setTagInput(e.target.value)}
          onKeyDown={e => {
            if (e.key === 'Enter') {
            e.preventDefault()
            addTag()
          }}}/>
      </div>

      <div className="h-5"></div>

      {/* Markdown Editor and Preview */}
      <div className="flex gap-4">
        <textarea 
          className="flex-1 h-96 border rounded p-3 font-mono" 
          value={body}
          ref={bodyRef} 
          onChange={e => setBody(e.target.value)} />
          <div className="flex-1 h-96 border rounded p-3 overflow-auto prose">
            <ReactMarkdown>{body}</ReactMarkdown>
          </div>
      </div>

      <div className="h-5"></div>

      {/* Save Button */}
      <div className="flex justify-center">
        <button className="flex border rounded p-1 justify-center"
                disabled={isSaving}
                onClick={ () => { saveNote() } }>
                  {isSaving ? "Saving..." : "Save"}
                  </button>
      </div>

      {justSaved && <p className="text-green-600">Saved!</p>}
      {saveError && <p className="text-red-600">{saveError}</p>}
    </>
  )
}