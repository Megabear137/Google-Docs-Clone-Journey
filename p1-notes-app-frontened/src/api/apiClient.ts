import axios from 'axios'

// Create a configured instance. baseURL is prepended to every request path.
export const apiClient = axios.create({
  baseURL: import.meta.env.VITE_API_URL,
})

// Attach the request interceptor: runs before every request leaves.
apiClient.interceptors.request.use((config) => {
  const isAuthRoute = config.url?.includes("/api/auth")
  const token = localStorage.getItem('token')
  if (token && !isAuthRoute) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

