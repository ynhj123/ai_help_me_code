// 用户角色枚举
export enum UserRole {
  ADMIN = 'ADMIN',
  DISTRIBUTOR = 'DISTRIBUTOR',
  DELIVERY_PERSONNEL = 'DELIVERY_PERSONNEL',
  USER = 'USER'
}

// 用户状态枚举
export enum UserStatus {
  ACTIVE = 'ACTIVE',
  INACTIVE = 'INACTIVE'
}

// 用户接口
export interface User {
  id: number
  username: string
  email: string
  phone: string
  nickname: string
  avatar: string
  status: UserStatus
  role: UserRole
  createdAt: string
  updatedAt: string
}

// 登录请求接口
export interface LoginRequest {
  username: string
  password: string
}

// 注册请求接口
export interface RegisterRequest {
  username: string
  email: string
  phone: string
  password: string
}

// 用户表单接口
export interface UserForm {
  id?: number
  username: string
  email: string
  phone: string
  nickname: string
  avatar: string
  status: UserStatus
  role: UserRole
}