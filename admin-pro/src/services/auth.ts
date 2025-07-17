import { request } from '@umijs/max';
import type { ApiResponse, LoginRequest, LoginResponse, User } from '@/types/api';

// 登录
export async function login(data: LoginRequest) {
  const response = await request<ApiResponse<LoginResponse>>('/auth/login', {
    method: 'POST',
    data,
  });
  
  if (response.code === 200 && response.data.token) {
    localStorage.setItem('token', response.data.token);
    localStorage.setItem('user', JSON.stringify(response.data.user));
  }
  
  return response;
}

// 登出
export async function logout() {
  const response = await request<ApiResponse<void>>('/auth/logout', {
    method: 'POST',
  });
  
  localStorage.removeItem('token');
  localStorage.removeItem('user');
  
  return response;
}

// 获取当前用户信息
export async function getCurrentUser() {
  const response = await request<ApiResponse<User>>('/auth/profile');
  if (response.code === 200 && response.data) {
    localStorage.setItem('user', JSON.stringify(response.data));
  }
  return response;
}

// 获取token
export function getToken() {
  return localStorage.getItem('token');
}

// 获取用户信息
export function getUserInfo() {
  const userStr = localStorage.getItem('user');
  return userStr ? JSON.parse(userStr) : null;
}

// 检查是否登录
export function isLoggedIn() {
  return !!getToken();
}

// 检查权限
export function hasPermission(permission: string) {
  const user = getUserInfo();
  if (!user) return false;
  
  // 管理员拥有所有权限
  if (user.role === 'admin') return true;
  
  // 普通用户权限检查
  const userPermissions = user.permissions || [];
  return userPermissions.includes(permission);
}