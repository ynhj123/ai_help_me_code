import { Navigate, Outlet } from 'react-router-dom'
import { useAuthStore } from '@/stores/auth'

const AuthGuard = () => {
  const { isAuthenticated } = useAuthStore()

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />
  }

  return <Outlet />
}

export default AuthGuard