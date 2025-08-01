import { defineStore } from 'pinia'
import { userApi } from '@/services/api'
import type { User } from '@/types/user'

// 定义状态接口
interface UserState {
  token: string | null
  userInfo: User | null
}

// 创建用户状态管理
export const useUserStore = defineStore('user', {
  state: (): UserState => ({
    token: localStorage.getItem('token'),
    userInfo: null
  }),
  
  getters: {
    // 是否已登录
    isLogin: (state) => !!state.token,
    
    // 用户角色
    userRole: (state) => state.userInfo?.role
  },
  
  actions: {
    // 设置token
    setToken(token: string) {
      this.token = token
      localStorage.setItem('token', token)
    },
    
    // 清除token
    clearToken() {
      this.token = null
      this.userInfo = null
      localStorage.removeItem('token')
    },
    
    // 获取用户信息
    async getUserInfo() {
      try {
        const response = await userApi.getProfile()
        this.userInfo = response.data
        return response.data
      } catch (error) {
        console.error('获取用户信息失败:', error)
        this.clearToken()
        throw error
      }
    },
    
    // 登出
    async logout() {
      try {
        await userApi.logout()
      } catch (error) {
        console.error('登出失败:', error)
      } finally {
        this.clearToken()
      }
    }
  }
})