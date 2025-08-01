<template>
  <a-layout class="layout">
    <!-- 侧边栏 -->
    <Sidebar v-model:collapsed="collapsed" />
    
    <a-layout>
      <!-- 头部 -->
      <Header v-model:collapsed="collapsed" />
      
      <!-- 内容区域 -->
      <a-layout-content class="content">
        <router-view />
      </a-layout-content>
      
      <!-- 页脚 -->
      <a-layout-footer class="footer">
        <div class="footer-content">
          <span>电商管理系统 ©2023</span>
        </div>
      </a-layout-footer>
    </a-layout>
  </a-layout>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useUserStore } from '@/store/user'
import Sidebar from './Sidebar.vue'
import Header from './Header.vue'

// 折叠状态
const collapsed = ref(false)

// 用户状态
const userStore = useUserStore()

// 组件挂载时获取用户信息
onMounted(async () => {
  if (userStore.isLogin) {
    try {
      await userStore.getUserInfo()
    } catch (error) {
      console.error('获取用户信息失败:', error)
    }
  }
})
</script>

<style scoped>
.layout {
  height: 100vh;
}

.content {
  margin: 24px;
  padding: 24px;
  background: #fff;
  border-radius: 8px;
  overflow: auto;
}

.footer {
  text-align: center;
  padding: 16px 0;
  background: #f0f2f5;
}

.footer-content {
  color: #999;
  font-size: 14px;
}
</style>