import { useAuth } from '../auth/AuthProvider'

export function RegisterPage() {
  const { register } = useAuth()
  return <button onClick={() => register('me4@test.com', 'password123')}>test register</button>
}