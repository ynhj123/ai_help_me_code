// 用户角色
export interface Role {
  id: number
  name: string
  description: string
  permissions: Permission[]
}

// 权限
export interface Permission {
  id: number
  name: string
  code: string
  description: string
}

// 用户信息
export interface User {
  id: number
  username: string
  email: string
  firstName: string
  lastName: string
  avatar?: string
  phone?: string
  status: 'ACTIVE' | 'INACTIVE'
  roles: Role[]
  createdAt: string
  updatedAt: string
}

// 登录请求
export interface LoginRequest {
  username: string
  password: string
}

// 登录响应
export interface LoginResponse {
  accessToken: string
  tokenType: string
  expiresIn: number
  user: User
}

// 注册请求
export interface RegisterRequest {
  username: string
  email: string
  password: string
  firstName: string
  lastName: string
  phone?: string
}

// 用户更新请求
export interface UserUpdateRequest {
  firstName?: string
  lastName?: string
  email?: string
  phone?: string
  avatar?: string
  status?: 'ACTIVE' | 'INACTIVE'
  roleIds?: number[]
}