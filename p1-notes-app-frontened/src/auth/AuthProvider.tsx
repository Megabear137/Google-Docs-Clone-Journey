import { createContext, useContext, useEffect, useState, type ReactNode } from "react";
import { type User, type AuthContextValue } from "./AuthContext";
import { apiClient } from "../api/apiClient";
import { jwtDecode } from 'jwt-decode'

const AuthContext = createContext<AuthContextValue | null>(null)

export function AuthProvider( { children }: {children: ReactNode}) {

    const [token, setToken] = useState<string | null>(
        () => localStorage.getItem('token')
    )

    const [user, setUser] = useState<User | null>( () => {
        const saved = localStorage.getItem("token")
        if(!saved) return null

        try {
            
            const claims = jwtDecode<{sub: string, exp: number}>(saved)
            // Ensure token is not expired
            if(claims.exp * 1000 < Date.now()) {
                localStorage.removeItem('token')
                return null
            }

            return {id: Number(claims.sub), email : localStorage.getItem('email') ?? '' }

        } catch {
            return null
        }
    })

    async function login(email: string, password: string): Promise<void> {
        const { data } = await apiClient.post('/api/auth/login', { email, password })
        localStorage.setItem('token', data.token)
        localStorage.setItem('email', data.email)
        setToken(data.token)

        const claims = jwtDecode<{ sub: string }>(data.token)
        setUser({ id: Number(claims.sub), email })
    }

    async function register(email: string, password: string): Promise<void> {
        const { data } = await apiClient.post('/api/auth/register', { email, password })
        localStorage.setItem('token', data.token)
        localStorage.setItem('email', data.email)
        setToken(data.token)

        const claims = jwtDecode<{ sub: string }>(data.token)
        setUser({ id: Number(claims.sub), email })
    }

    function logout() {
        setUser(null)
        setToken(null)
        localStorage.removeItem("token")
    }

    return <AuthContext.Provider value={ 
                { user, 
                token, 
                login, 
                register, 
                logout }}> 
                {children} 
            </AuthContext.Provider>
}

export function useAuth() {
    const ctx = useContext(AuthContext)
    if (!ctx) throw new Error("useAuth must be used inside AuthProvider")
    return ctx
}