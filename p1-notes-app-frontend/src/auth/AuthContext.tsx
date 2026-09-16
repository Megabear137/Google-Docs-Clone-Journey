export type User = { id : number; email: string}

export type AuthContextValue = {
    user: User | null
    token : string | null
    login: (email: string, password:string) => Promise<void>
    register: (email: string, password:string) => Promise<void>
    logout: () => void
}