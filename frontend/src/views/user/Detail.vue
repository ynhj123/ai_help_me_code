<template>
  <PageContainer :title="`用户详情 - ${user?.username || ''}`" back>
    <a-spin :spinning="loading">
      <a-card v-if="user" class="user-detail-card">
        <a-row :gutter="24">
          <a-col :span="24" class="user-header">
            <a-avatar :size="80" :src="user.avatar || defaultAvatar" class="user-avatar" />
            <div class="user-info">
              <h2 class="user-name">{{ user.username }}</h2>
              <p class="user-nickname">{{ user.nickname }}</p>
              <a-tag :color="user.status === 'ACTIVE' ? 'green' : 'red'">
                {{ user.status === 'ACTIVE' ? '正常' : '禁用' }}
              </a-tag>
              <a-tag :color="getRoleColor(user.role)">
                {{ getRoleText(user.role) }}
              </a-tag>
            </div>
          </a-col>
        </a-row>
        
        <a-divider />
        
        <a-row :gutter="24">
          <a-col :span="12">
            <div class="detail-item">
              <span class="detail-label">邮箱:</span>
              <span class="detail-value">{{ user.email }}</span>
            </div>
          </a-col>
          <a-col :span="12">
            <div class="detail-item">
              <span class="detail-label">手机号:</span>
              <span class="detail-value">{{ user.phone }}</span>
            </div>
          </a-col>
        </a-row>
        
        <a-row :gutter="24">
          <a-col :span="12">
            <div class="detail-item">
              <span class="detail-label">创建时间:</span>
              <span class="detail-value">{{ formatDateTime(user.createdAt) }}</span>
            </div>
          </a-col>
          <a-col :span="12">
            <div class="detail-item">
              <span class="detail-label">更新时间:</span>
              <span class="detail-value">{{ formatDateTime(user.updatedAt) }}</span>
            </div>
          </a-col>
        </a-row>
        
        <a-divider />
        
        <div class="action-buttons">
          <a-button type="primary" @click="handleEdit">
            <EditOutlined />
            编辑
          </a-button>
          <a-button @click="handleBack" style="margin-left: 12px;">
            <ArrowLeftOutlined />
            返回
          </a-button>
        </div>
      </a-card>
    </a-spin>
  </PageContainer>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import {
  EditOutlined,
  ArrowLeftOutlined
} from '@ant-design/icons-vue'
import PageContainer from '@/components/common/PageContainer.vue'
import { userApi } from '@/services/api'
import type { User, UserRole } from '@/types/user'
import defaultAvatar from '@/assets/images/logo.png'

// 路由
const route = useRoute()
const router = useRouter()

// 用户信息
const user = ref<User | null>(null)

// 加载状态
const loading = ref(false)

// 获取角色颜色
const getRoleColor = (role: UserRole) => {
  switch (role) {
    case 'ADMIN':
      return 'blue'
    case 'DISTRIBUTOR':
      return 'green'
    case 'DELIVERY_PERSONNEL':
      return 'orange'
    case 'USER':
      return 'gray'
    default:
      return 'default'
  }
}

// 获取角色文本
const getRoleText = (role: UserRole) => {
  switch (role) {
    case 'ADMIN':
      return '管理员'
    case 'DISTRIBUTOR':
      return '分销商'
    case 'DELIVERY_PERSONNEL':
      return '运输人员'
    case 'USER':
      return '普通用户'
    default:
      return role
  }
}

// 格式化日期时间
const formatDateTime = (dateString: string) => {
  return new Date(dateString).toLocaleString('zh-CN')
}

// 获取用户详情
const getUserDetail = async (id: number) => {
  try {
    loading.value = true
    const response = await userApi.getUser(id)
    user.value = response.data
  } catch (error) {
    console.error('获取用户详情失败:', error)
    message.error('获取用户详情失败')
  } finally {
    loading.value = false
  }
}

// 处理编辑
const handleEdit = () => {
  if (user.value) {
    router.push(`/user/edit/${user.value.id}`)
  }
}

// 处理返回
const handleBack = () => {
  router.back()
}

// 组件挂载时获取用户详情
onMounted(() => {
  const userId = Number(route.params.id)
  if (userId) {
    getUserDetail(userId)
  }
})
</script>

<style scoped>
.user-detail-card {
  margin: 24px;
}

.user-header {
  display: flex;
  align-items: center;
  margin-bottom: 24px;
}

.user-avatar {
  margin-right: 24px;
}

.user-info {
  flex: 1;
}

.user-name {
  margin: 0 0 8px 0;
  font-size: 24px;
  font-weight: 600;
}

.user-nickname {
  margin: 0 0 16px 0;
  color: #666;
  font-size: 16px;
}

.detail-item {
  display: flex;
  margin-bottom: 16px;
}

.detail-label {
  width: 100px;
  font-weight: 600;
  color: #333;
}

.detail-value {
  flex: 1;
  color: #666;
}

.action-buttons {
  text-align: center;
  margin-top: 24px;
}
</style>