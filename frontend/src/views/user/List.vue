<template>
  <PageContainer title="用户管理" subTitle="管理系统中的用户">
    <template #extra>
      <a-button type="primary" @click="handleCreate">
        <PlusOutlined />
        新增用户
      </a-button>
    </template>
    
    <div class="user-list">
      <!-- 搜索表单 -->
      <a-form
        :model="searchForm"
        :layout="'inline'"
        class="search-form"
      >
        <a-form-item label="用户名">
          <a-input
            v-model:value="searchForm.username"
            placeholder="请输入用户名"
            allow-clear
          />
        </a-form-item>
        
        <a-form-item label="邮箱">
          <a-input
            v-model:value="searchForm.email"
            placeholder="请输入邮箱"
            allow-clear
          />
        </a-form-item>
        
        <a-form-item>
          <a-button type="primary" @click="handleSearch">
            <SearchOutlined />
            搜索
          </a-button>
          <a-button style="margin-left: 8px" @click="handleReset">
            重置
          </a-button>
        </a-form-item>
      </a-form>
      
      <!-- 用户表格 -->
      <CommonTable
        :columns="columns"
        :data-source="userList"
        :loading="loading"
        :pagination="pagination"
        @change="handleTableChange"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'status'">
            <a-tag :color="record.status === 'ACTIVE' ? 'green' : 'red'">
              {{ record.status === 'ACTIVE' ? '正常' : '禁用' }}
            </a-tag>
          </template>
          
          <template v-else-if="column.key === 'role'">
            <a-tag :color="getRoleColor(record.role)">
              {{ getRoleText(record.role) }}
            </a-tag>
          </template>
          
          <template v-else-if="column.key === 'action'">
            <a-space>
              <a-button type="link" size="small" @click="handleEdit(record)">
                编辑
              </a-button>
              <a-button type="link" size="small" @click="handleDelete(record)">
                删除
              </a-button>
            </a-space>
          </template>
        </template>
      </CommonTable>
    </div>
    
    <!-- 用户编辑模态框 -->
    <a-modal
      v-model:open="modalVisible"
      :title="modalTitle"
      :confirm-loading="confirmLoading"
      @ok="handleModalOk"
      @cancel="handleModalCancel"
    >
      <a-form
        ref="userFormRef"
        :model="userForm"
        :rules="userFormRules"
        layout="vertical"
      >
        <a-form-item label="用户名" name="username">
          <a-input
            v-model:value="userForm.username"
            placeholder="请输入用户名"
          />
        </a-form-item>
        
        <a-form-item label="邮箱" name="email">
          <a-input
            v-model:value="userForm.email"
            placeholder="请输入邮箱"
          />
        </a-form-item>
        
        <a-form-item label="手机号" name="phone">
          <a-input
            v-model:value="userForm.phone"
            placeholder="请输入手机号"
          />
        </a-form-item>
        
        <a-form-item label="昵称" name="nickname">
          <a-input
            v-model:value="userForm.nickname"
            placeholder="请输入昵称"
          />
        </a-form-item>
        
        <a-form-item label="状态" name="status">
          <a-select v-model:value="userForm.status" placeholder="请选择状态">
            <a-select-option value="ACTIVE">正常</a-select-option>
            <a-select-option value="INACTIVE">禁用</a-select-option>
          </a-select>
        </a-form-item>
        
        <a-form-item label="角色" name="role">
          <a-select v-model:value="userForm.role" placeholder="请选择角色">
            <a-select-option value="ADMIN">管理员</a-select-option>
            <a-select-option value="DISTRIBUTOR">分销商</a-select-option>
            <a-select-option value="DELIVERY_PERSONNEL">运输人员</a-select-option>
            <a-select-option value="USER">普通用户</a-select-option>
          </a-select>
        </a-form-item>
      </a-form>
    </a-modal>
  </PageContainer>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { message, Modal } from 'ant-design-vue'
import {
  PlusOutlined,
  SearchOutlined
} from '@ant-design/icons-vue'
import PageContainer from '@/components/common/PageContainer.vue'
import CommonTable from '@/components/common/Table.vue'
import { userApi } from '@/services/api'
import type { User, UserForm, UserStatus, UserRole } from '@/types/user'

// 用户列表
const userList = ref<User[]>([])

// 加载状态
const loading = ref(false)

// 分页信息
const pagination = reactive({
  current: 1,
  pageSize: 10,
  total: 0,
  showSizeChanger: true,
  showQuickJumper: true,
  showTotal: (total: number) => `共 ${total} 条`,
  onChange: (page: number) => {
    pagination.current = page
    getUserList()
  }
})

// 搜索表单
const searchForm = reactive({
  username: '',
  email: ''
})

// 表格列定义
const columns = [
  {
    title: '用户名',
    dataIndex: 'username',
    key: 'username'
  },
  {
    title: '邮箱',
    dataIndex: 'email',
    key: 'email'
  },
  {
    title: '手机号',
    dataIndex: 'phone',
    key: 'phone'
  },
  {
    title: '昵称',
    dataIndex: 'nickname',
    key: 'nickname'
  },
  {
    title: '状态',
    dataIndex: 'status',
    key: 'status'
  },
  {
    title: '角色',
    dataIndex: 'role',
    key: 'role'
  },
  {
    title: '创建时间',
    dataIndex: 'createdAt',
    key: 'createdAt'
  },
  {
    title: '操作',
    key: 'action',
    fixed: 'right',
    width: 150
  }
]

// 获取角色颜色
const getRoleColor = (role: UserRole) => {
  switch (role) {
    case UserRole.ADMIN:
      return 'blue'
    case UserRole.DISTRIBUTOR:
      return 'green'
    case UserRole.DELIVERY_PERSONNEL:
      return 'orange'
    case UserRole.USER:
      return 'gray'
    default:
      return 'default'
  }
}

// 获取角色文本
const getRoleText = (role: UserRole) => {
  switch (role) {
    case UserRole.ADMIN:
      return '管理员'
    case UserRole.DISTRIBUTOR:
      return '分销商'
    case UserRole.DELIVERY_PERSONNEL:
      return '运输人员'
    case UserRole.USER:
      return '普通用户'
    default:
      return role
  }
}

// 获取用户列表
const getUserList = async () => {
  try {
    loading.value = true
    
    // 构造查询参数
    const params = {
      page: pagination.current,
      size: pagination.pageSize,
      ...searchForm
    }
    
    // 调用接口获取用户列表
    const response = await userApi.getUserList(params)
    userList.value = response.data.content || response.data
    pagination.total = response.data.total || response.data.length
  } catch (error) {
    console.error('获取用户列表失败:', error)
    message.error('获取用户列表失败')
  } finally {
    loading.value = false
  }
}

// 处理搜索
const handleSearch = () => {
  pagination.current = 1
  getUserList()
}

// 处理重置
const handleReset = () => {
  searchForm.username = ''
  searchForm.email = ''
  pagination.current = 1
  getUserList()
}

// 处理表格变化
const handleTableChange = (page: any) => {
  pagination.current = page.current
  pagination.pageSize = page.pageSize
  getUserList()
}

// 模态框相关
const modalVisible = ref(false)
const confirmLoading = ref(false)
const modalTitle = ref('新增用户')
const isEdit = ref(false)
const editUserId = ref<number | null>(null)

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

// 处理新增
const handleCreate = () => {
  modalTitle.value = '新增用户'
  isEdit.value = false
  editUserId.value = null
  userForm.value = {
    username: '',
    email: '',
    phone: '',
    nickname: '',
    avatar: '',
    status: UserStatus.ACTIVE,
    role: UserRole.USER
  }
  modalVisible.value = true
}

// 处理编辑
const handleEdit = (record: User) => {
  modalTitle.value = '编辑用户'
  isEdit.value = true
  editUserId.value = record.id
  userForm.value = {
    username: record.username,
    email: record.email,
    phone: record.phone,
    nickname: record.nickname,
    avatar: record.avatar,
    status: record.status,
    role: record.role
  }
  modalVisible.value = true
}

// 处理删除
const handleDelete = (record: User) => {
  Modal.confirm({
    title: '确认删除',
    content: `确定要删除用户 "${record.username}" 吗？`,
    okText: '确认',
    cancelText: '取消',
    onOk: async () => {
      try {
        await userApi.deleteUser(record.id)
        message.success('删除成功')
        getUserList()
      } catch (error) {
        console.error('删除用户失败:', error)
        message.error('删除用户失败')
      }
    }
  })
}

// 处理模态框确认
const handleModalOk = () => {
  userFormRef.value?.validate().then(async () => {
    try {
      confirmLoading.value = true
      
      if (isEdit.value && editUserId.value) {
        // 编辑用户
        await userApi.updateUser(editUserId.value, userForm.value)
        message.success('更新成功')
      } else {
        // 新增用户
        await userApi.createUser(userForm.value)
        message.success('创建成功')
      }
      
      modalVisible.value = false
      getUserList()
    } catch (error) {
      console.error('保存用户失败:', error)
      message.error('保存用户失败')
    } finally {
      confirmLoading.value = false
    }
  })
}

// 处理模态框取消
const handleModalCancel = () => {
  modalVisible.value = false
}

// 组件挂载时获取用户列表
onMounted(() => {
  getUserList()
})
</script>

<style scoped>
.user-list {
  background: #fff;
  padding: 24px;
  border-radius: 8px;
}

.search-form {
  margin-bottom: 24px;
}

:deep(.ant-form-item) {
  margin-bottom: 16px;
}
</style>