<template>
  <a-layout-header class="header">
    <div class="header-left">
      <div class="logo">
        <img src="@/assets/images/logo.png" alt="Logo" class="logo-img" />
        <span class="logo-text">电商管理系统</span>
      </div>
      
      <a-button
        type="text"
        class="trigger"
        @click="toggleCollapsed"
      >
        <MenuUnfoldOutlined v-if="collapsed" />
        <MenuFoldOutlined v-else />
      </a-button>
    </div>
    
    <div class="header-right">
      <a-dropdown>
        <a class="ant-dropdown-link" @click.prevent>
          <a-avatar
            :src="userStore.userInfo?.avatar || defaultAvatar"
            :size="32"
            class="user-avatar"
          />
          <span class="user-name">{{ userStore.userInfo?.nickname || userStore.userInfo?.username }}</span>
        </a>
        
        <template #overlay>
          <a-menu>
            <a-menu-item key="profile" @click="goToProfile">
              <UserOutlined />
              个人中心
            </a-menu-item>
            <a-menu-item key="logout" @click="handleLogout">
              <LogoutOutlined />
              退出登录
            </a-menu-item>
          </a-menu>
        </template>
      </a-dropdown>
    </div>
  </a-layout-header>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import {
  MenuUnfoldOutlined,
  MenuFoldOutlined,
  UserOutlined,
  LogoutOutlined
} from '@ant-design/icons-vue'
import { useUserStore } from '@/store/user'
import defaultAvatar from '@/assets/images/avatar.png'

// 定义属性
const props = defineProps<{
  collapsed: boolean
}>()

// 定义事件
const emit = defineEmits<{
  (e: 'update:collapsed', collapsed: boolean): void
}>()

// 路由
const router = useRouter()

// 用户状态
const userStore = useUserStore()

// 切换折叠状态
const toggleCollapsed = () => {
  emit('update:collapsed', !props.collapsed)
}

// 跳转到个人中心
const goToProfile = () => {
  router.push('/profile')
}

// 处理登出
const handleLogout = async () => {
  try {
    await userStore.logout()
    router.push('/login')
  } catch (error) {
    console.error('登出失败:', error)
  }
}
</script>

<style scoped>
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 24px;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  position: relative;
  z-index: 10;
}

.header-left {
  display: flex;
  align-items: center;
}

.logo {
  display: flex;
  align-items: center;
  margin-right: 24px;
}

.logo-img {
  width: 32px;
  height: 32px;
  margin-right: 8px;
}

.logo-text {
  font-size: 18px;
  font-weight: 600;
  color: #333;
}

.trigger {
  font-size: 18px;
  line-height: 64px;
  padding: 0 24px;
  cursor: pointer;
  transition: color 0.3s;
}

.trigger:hover {
  color: #1890ff;
}

.header-right {
  display: flex;
  align-items: center;
}

.user-avatar {
  margin-right: 8px;
}

.user-name {
  color: #333;
}
</style>