<template>
  <div class="register-container">
    <div class="register-form">
      <div class="register-header">
        <img src="@/assets/images/logo.png" alt="Logo" class="logo" />
        <h2>电商管理系统</h2>
        <p>创建您的账户</p>
      </div>
      
      <a-form
        :model="registerForm"
        :rules="registerRules"
        @finish="handleRegister"
        @finishFailed="handleRegisterFailed"
        class="form"
      >
        <a-form-item name="username">
          <a-input
            v-model:value="registerForm.username"
            placeholder="请输入用户名"
            size="large"
          >
            <template #prefix>
              <UserOutlined />
            </template>
          </a-input>
        </a-form-item>
        
        <a-form-item name="email">
          <a-input
            v-model:value="registerForm.email"
            placeholder="请输入邮箱"
            size="large"
          >
            <template #prefix>
              <MailOutlined />
            </template>
          </a-input>
        </a-form-item>
        
        <a-form-item name="phone">
          <a-input
            v-model:value="registerForm.phone"
            placeholder="请输入手机号"
            size="large"
          >
            <template #prefix>
              <PhoneOutlined />
            </template>
          </a-input>
        </a-form-item>
        
        <a-form-item name="password">
          <a-input-password
            v-model:value="registerForm.password"
            placeholder="请输入密码"
            size="large"
          >
            <template #prefix>
              <LockOutlined />
            </template>
          </a-input-password>
        </a-form-item>
        
        <a-form-item name="confirmPassword">
          <a-input-password
            v-model:value="registerForm.confirmPassword"
            placeholder="请确认密码"
            size="large"
          >
            <template #prefix>
              <LockOutlined />
            </template>
          </a-input-password>
        </a-form-item>
        
        <a-form-item>
          <a-button
            type="primary"
            html-type="submit"
            size="large"
            block
            :loading="loading"
          >
            注册
          </a-button>
        </a-form-item>
        
        <div class="form-footer">
          <router-link to="/login">已有账号？立即登录</router-link>
        </div>
      </a-form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import { UserOutlined, MailOutlined, PhoneOutlined, LockOutlined } from '@ant-design/icons-vue'
import { authApi } from '@/services/api'
import type { RegisterRequest } from '@/types/user'

// 路由
const router = useRouter()

// 注册表单
const registerForm = ref<RegisterRequest & { confirmPassword: string }>({
  username: '',
  email: '',
  phone: '',
  password: '',
  confirmPassword: ''
})

// 注册规则
const registerRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '邮箱格式不正确', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    {
      validator: (_: any, value: string) => {
        if (value && value !== registerForm.value.password) {
          return Promise.reject('两次输入的密码不一致')
        }
        return Promise.resolve()
      },
      trigger: 'blur'
    }
  ]
}

// 加载状态
const loading = ref(false)

// 处理注册
const handleRegister = async () => {
  try {
    loading.value = true
    
    // 构造注册请求数据
    const requestData: RegisterRequest = {
      username: registerForm.value.username,
      email: registerForm.value.email,
      phone: registerForm.value.phone,
      password: registerForm.value.password
    }
    
    // 调用注册接口
    await authApi.register(requestData)
    
    // 提示消息
    message.success('注册成功')
    
    // 跳转到登录页
    router.push('/login')
  } catch (error) {
    console.error('注册失败:', error)
    message.error('注册失败，请稍后重试')
  } finally {
    loading.value = false
  }
}

// 处理注册失败
const handleRegisterFailed = () => {
  message.error('请检查表单填写是否正确')
}
</script>

<style scoped>
.register-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.register-form {
  width: 100%;
  max-width: 400px;
  padding: 40px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 15px 35px rgba(0, 0, 0, 0.1);
}

.register-header {
  text-align: center;
  margin-bottom: 30px;
}

.logo {
  width: 60px;
  height: 60px;
  margin-bottom: 15px;
}

.register-header h2 {
  margin: 0 0 10px 0;
  font-size: 24px;
  color: #333;
}

.register-header p {
  margin: 0;
  color: #666;
}

.form {
  margin-top: 20px;
}

.form-footer {
  text-align: center;
  margin-top: 20px;
}

.form-footer a {
  color: #1890ff;
  text-decoration: none;
}

.form-footer a:hover {
  text-decoration: underline;
}
</style>