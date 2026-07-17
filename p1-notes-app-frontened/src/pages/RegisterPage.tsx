import { useAuth } from '../auth/AuthProvider'
import { useState } from "react"
import { useNavigate } from 'react-router-dom'


export function RegisterPage() {
  const { register } = useAuth()
  const [email, setEmail] = useState("")
  const [password, setPassword] = useState("")
  const [error, setError] = useState("")
  const navigate = useNavigate()

  async function handleSubmit() {
    setError('')
    try {
      await register(email, password)
      navigate('/notes')
    } catch {
      setError("Oops! Something went wrong!")
    }
  }

  return (
    // email input, password input, a submit button, and {error && <p>...</p>}
    <form onSubmit={(e) => {e.preventDefault(); handleSubmit();}}>
      <input
        type="email"
        value={email}
        onChange={(e) => setEmail(e.target.value)}
        placeholder="you@example.com"
      />
      <input
        type="password"
        value={password}
        onChange={(p) => setPassword(p.target.value)}
        placeholder=""
      />
      <button type="submit">Register</button>
      {error && <p className="text-red-600">{error}</p>}
    </form>
  )
}