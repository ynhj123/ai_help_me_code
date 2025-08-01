<template>
  <PageContainer :title="isEdit ? '编辑用户' : '新增用户'" back>
    <a-card class="user-edit-card">
      <a-form
        ref="userFormRef"
        :model="userForm"
        :rules="userFormRules"
        layout="vertical"
        @finish="handleFinish"
      >
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="用户名" name="username">
              <a-input
                v-model:value="userForm.username"
                placeholder="请输入用户名"
                :disabled="isEdit"
              />
            </a-form-item>
          </a-col>
          
          <a-col :span="12">
            <a-form-item label="邮箱" name="email">
              <a-input
                v-model:value="userForm.email"
                placeholder="请输入邮箱"
              />
            </a-form-item>
          </a-col>
        </a-row>
        
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="手机号" name="phone">
              <a-input
                v-model:value="userForm.phone"
                placeholder="请输入手机号"
              />
            </a-form-item>
          </a-col>
          
          <a-col :span="12">
            <a-form-item label="昵称" name="nickname">
              <a-input
                v-model:value="userForm.nickname"
                placeholder="请输入昵称"
              />
            </a-form-item>
          </a-col>
        </a-row>
        
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="状态" name="status">
              <a-select v-model:value="userForm.status" placeholder="请选择状态">
                <a-select-option value="ACTIVE">正常</a-select-option>
                <a-select-option value="INACTIVE">禁用</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          
          <a-col :span="12">
            <a-form-item label="角色" name="role">
              <a-select v-model:value="userForm.role" placeholder="请选择角色">
                <a-select-option value="ADMIN">管理员</a-select-option>
                <a-select-option value="DISTRIBUTOR">分销商</a-select-option>
                <a-select-option value="DELIVERY_PERSONNEL">运输人员</a-select-option>
                <a-select-option value="USER">普通用户</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
        </a-row>
        
        <a-row :gutter="24">
          <a-col :span="24">
            <a-form-item label="头像" name="avatar">
              <ImageUpload
                v-model:value="userForm.avatar"
                :max-count="1"
              />
            </a-form-item>
          </a-col>
        </a-row>
        
        <div class="form-actions">
          <a-button type="primary" html-type="submit" :loading="loading">
            {{ isEdit ? '更新' : '创建' }}
          </a-button>
          <a-button @click="handleCancel" style="margin-left: 12px;">
            取消
          </a-button>
        </div>
      </a-form>
    </a-card>
  </PageContainer>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import PageContainer from '@/components/common/PageContainer.vue'
import ImageUpload from '@/components/common/ImageUpload.vue'
import { userApi } from '@/services/api'
import type { User, UserForm, UserStatus, UserRole } from '@/types/user'

// 路由
const route = useRoute()
const router = useRouter()

// 是否为编辑模式
const isEdit = ref(false)

// 加载状态
const loading = ref(false)

// 用户表单
const userForm = ref<UserForm>({
  username: '',
  email: '',
  phone: '',
  nickname: '',
  avatar: '',
  status: UserStatus.ACTIVE,
  role: UserRole.USER
})

// 用户表单规则
const userFormRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '邮箱格式不正确', trigger: 'blur' }
  ],
  phone: [
    { pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' }
  ],
  status: [
    { required: true, message: '请选择状态', trigger: 'change' }
  ],
  role: [
    { required: true, message: '请选择角色', trigger: 'change' }
  ]
}

// 用户表单引用
const userFormRef = ref<any>(null)

// 获取用户详情
const getUserDetail = async (id: number) => {
  try {
    loading.value = true
    const response = await userApi.getUser(id)
    userForm.value = {
      ...response.data,
      status: response.data.status as UserStatus,
      role: response.data.role as UserRole
    }
  } catch (error) {
    console.error('获取用户详情失败:', error)
    message.error('获取用户详情失败')
  } finally {
    loading.value = false
  }
}

// 处理表单提交
const handleFinish = async () => {
  try {
    loading.value = true
    
    if (isEdit.value) {
      // 编辑用户
      await userApi.updateUser(userForm.value.id!, userForm.value)
      message.success('用户更新成功')
    } else {
      // 新增用户
      await userApi.createUser(userForm.value)
      message.success('用户创建成功')
    }
    
    // 返回用户列表
    router.push('/user')
  } catch (error) {
    console.error('保存用户失败:', error)
    message.error('保存用户失败')
  } finally {
    loading.value = false
  }
}

// 处理取消
const handleCancel = () => {
  router.back()
}

// 组件挂载时初始化
onMounted(() => {
  const userId = Number(route.params.id)
  if (userId) {
    isEdit.value = true
    getUserDetail(userId)
  }
})
</script>

<style scoped>
.user-edit-card {
  margin: 24px;
}

.form-actions {
  text-align: center;
  margin-top: 24px;
}
</style>