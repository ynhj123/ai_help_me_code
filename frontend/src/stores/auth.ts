import { create } from 'zustand'
import { persist } from 'zustand/middleware'
import { User, LoginRequest, LoginResponse } from '@/types'

interface AuthState {
  user: User | null
  token: string | null
  isAuthenticated: boolean
  login: (credentials: LoginRequest) => Promise<void>
  logout: () => void
  setUser: (user: User) => void
  setToken: (token: string) => void
}

export const useAuthStore = create<AuthState>()(
  persist(
    (set) => ({
      user: null,
      token: null,
      isAuthenticated: false,

      login: async (credentials: LoginRequest) => {
        // 这里将集成实际的API调用
        // 暂时使用模拟数据
        const mockResponse: LoginResponse = {
          accessToken: 'mock-jwt-token',
          tokenType: 'Bearer',
          expiresIn: 86400,
          user: {
            id: 1,
            username: credentials.username,
            email: 'admin@example.com',
            firstName: 'Admin',
            lastName: 'User',
            status: 'ACTIVE',
            roles: [
              {
                id: 1,
                name: 'ADMIN',
                description: '管理员',
                permissions: [],
              },
            ],
            createdAt: new Date().toISOString(),
            updatedAt: new Date().toISOString(),
          },
        }

        set({
          user: mockResponse.user,
          token: mockResponse.accessToken,
          isAuthenticated: true,
        })
      },

      logout: () => {
        set({
          user: null,
          token: null,
          isAuthenticated: false,
        })
      },

      setUser: (user: User) => {
        set({ user })
      },

      setToken: (token: string) => {
        set({ token })
      },
    }),
    {
      name: 'auth-storage',
      partialize: (state) => ({
        user: state.user,
        token: state.token,
        isAuthenticated: state.isAuthenticated,
      }),
    }
  )
)