import { Routes, Route, Navigate } from 'react-router-dom'
import { lazy, Suspense } from 'react'
import AuthGuard from '@/components/common/AuthGuard'
import Layout from '@/components/layout/Layout'
import Login from '@/pages/Login'

// 懒加载页面组件
const Dashboard = lazy(() => import('@/pages/Dashboard'))
const UserList = lazy(() => import('@/pages/User'))
const ProductList = lazy(() => import('@/pages/Product'))
const OrderList = lazy(() => import('@/pages/Order'))
const Logistics = lazy(() => import('@/pages/Logistics'))

const AppRouter = () => {
  return (
    <Suspense fallback={<div>加载中...</div>}>
      <Routes>
        <Route path="/login" element={<Login />} />
        
        <Route path="/" element={<AuthGuard />}>
          <Route element={<Layout />}>
            <Route index element={<Navigate to="/dashboard" replace />} />
            <Route path="/dashboard" element={<Dashboard />} />
            <Route path="/users" element={<UserList />} />
            <Route path="/products" element={<ProductList />} />
            <Route path="/orders" element={<OrderList />} />
            <Route path="/logistics" element={<Logistics />} />
          </Route>
        </Route>
        
        <Route path="*" element={<Navigate to="/dashboard" replace />} />
      </Routes>
    </Suspense>
  )
}

export default AppRouter