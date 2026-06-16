import { useAuth } from '../auth/AuthProvider'

export function RegisterPage() {
  const { register } = useAuth()
  return <button onClick={() => register('me8@test.com', 'password123')}>test register</button>
}