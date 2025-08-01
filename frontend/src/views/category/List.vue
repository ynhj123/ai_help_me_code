<template>
  <PageContainer title="分类管理" subTitle="管理系统中的商品分类">
    <template #extra>
      <a-button type="primary" @click="handleCreate">
        <PlusOutlined />
        新增分类
      </a-button>
    </template>
    
    <div class="category-list">
      <!-- 搜索表单 -->
      <a-form
        :model="searchForm"
        :layout="'inline'"
        class="search-form"
      >
        <a-form-item label="分类名称">
          <a-input
            v-model:value="searchForm.name"
            placeholder="请输入分类名称"
            allow-clear
          />
        </a-form-item>
        
        <a-form-item label="分类状态">
          <a-select
            v-model:value="searchForm.status"
            placeholder="请选择状态"
            allow-clear
            style="width: 120px"
          >
            <a-select-option :value="1">启用</a-select-option>
            <a-select-option :value="0">禁用</a-select-option>
          </a-select>
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
      
      <!-- 分类表格 -->
      <CommonTable
        :columns="columns"
        :data-source="categoryList"
        :loading="loading"
        :pagination="pagination"
        @change="handleTableChange"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'status'">
            <a-tag :color="record.status === 1 ? 'green' : 'red'">
              {{ record.status === 1 ? '启用' : '禁用' }}
            </a-tag>
          </template>
          
          <template v-else-if="column.key === 'sortOrder'">
            <a-input-number
              v-model:value="record.sortOrder"
              :min="0"
              :max="999"
              style="width: 80px"
              @change="handleSortOrderChange(record)"
            />
          </template>
          
          <template v-else-if="column.key === 'action'">
            <a-space>
              <a-button type="link" size="small" @click="handleEdit(record)">
                编辑
              </a-button>
              <a-button type="link" size="small" @click="handleDetail(record)">
                详情
              </a-button>
              <a-popconfirm
                v-if="record.status === 1"
                title="确定要禁用该分类吗？"
                @confirm="handleDisable(record)"
              >
                <a-button type="link" size="small" danger>
                  禁用
                </a-button>
              </a-popconfirm>
              <a-popconfirm
                v-else
                title="确定要启用该分类吗？"
                @confirm="handleEnable(record)"
              >
                <a-button type="link" size="small" type="primary">
                  启用
                </a-button>
              </a-popconfirm>
              <a-popconfirm
                title="确定要删除该分类吗？"
                @confirm="handleDelete(record)"
              >
                <a-button type="link" size="small" danger>
                  删除
                </a-button>
              </a-popconfirm>
            </a-space>
          </template>
        </template>
      </CommonTable>
    </div>
    
    <!-- 分类编辑模态框 -->
    <a-modal
      v-model:open="modalVisible"
      :title="modalTitle"
      :width="600"
      :confirm-loading="confirmLoading"
      @ok="handleModalOk"
      @cancel="handleModalCancel"
    >
      <a-form
        ref="categoryFormRef"
        :model="categoryForm"
        :rules="categoryFormRules"
        layout="vertical"
      >
        <a-form-item label="分类名称" name="name">
          <a-input
            v-model:value="categoryForm.name"
            placeholder="请输入分类名称"
          />
        </a-form-item>
        
        <a-form-item label="父级分类" name="parentId">
          <a-tree-select
            v-model:value="categoryForm.parentId"
            placeholder="请选择父级分类"
            :tree-data="categoryTreeData"
            :field-names="{ label: 'name', value: 'id', children: 'children' }"
            allow-clear
            style="width: 100%"
          />
        </a-form-item>
        
        <a-form-item label="排序值" name="sortOrder">
          <a-input-number
            v-model:value="categoryForm.sortOrder"
            placeholder="请输入排序值"
            :min="0"
            :max="999"
            style="width: 100%"
          />
        </a-form-item>
        
        <a-form-item label="分类状态" name="status">
          <a-radio-group v-model:value="categoryForm.status">
            <a-radio :value="1">启用</a-radio>
            <a-radio :value="0">禁用</a-radio>
          </a-radio-group>
        </a-form-item>
        
        <a-form-item label="分类描述" name="description">
          <a-textarea
            v-model:value="categoryForm.description"
            placeholder="请输入分类描述"
            :rows="4"
          />
        </a-form-item>
      </a-form>
    </a-modal>
    
    <!-- 分类移动模态框 -->
    <a-modal
      v-model:open="moveModalVisible"
      title="移动分类"
      :width="500"
      @ok="handleMoveOk"
      @cancel="handleMoveCancel"
    >
      <a-form layout="vertical">
        <a-form-item label="选择新的父级分类">
          <a-tree-select
            v-model:value="moveForm.newParentId"
            placeholder="请选择新的父级分类"
            :tree-data="categoryTreeData"
            :field-names="{ label: 'name', value: 'id', children: 'children' }"
            allow-clear
            style="width: 100%"
          />
        </a-form-item>
      </a-form>
    </a-modal>
  </PageContainer>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { message, Modal } from 'ant-design-vue'
import {
  PlusOutlined,
  SearchOutlined
} from '@ant-design/icons-vue'
import PageContainer from '@/components/common/PageContainer.vue'
import CommonTable from '@/components/common/Table.vue'
import { categoryApi } from '@/services/api'
import type { Category, CategoryForm, CategoryTreeNodeDTO } from '@/types/category'

// 分类列表
const categoryList = ref<Category[]>([])

// 分类树数据
const categoryTreeData = ref<CategoryTreeNodeDTO[]>([])

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
    getCategoryList()
  }
})

// 搜索表单
const searchForm = reactive({
  name: '',
  status: undefined
})

// 表格列定义
const columns = [
  {
    title: '分类名称',
    dataIndex: 'name',
    key: 'name',
    ellipsis: true
  },
  {
    title: '父级分类',
    dataIndex: 'parentName',
    key: 'parentName',
    ellipsis: true
  },
  {
    title: '排序值',
    dataIndex: 'sortOrder',
    key: 'sortOrder',
    width: 100,
    align: 'center'
  },
  {
    title: '分类状态',
    dataIndex: 'status',
    key: 'status',
    width: 80,
    align: 'center'
  },
  {
    title: '创建时间',
    dataIndex: 'createdAt',
    key: 'createdAt',
    width: 160,
    align: 'center'
  },
  {
    title: '操作',
    key: 'action',
    fixed: 'right',
    width: 200,
    align: 'center'
  }
]

// 模态框相关
const modalVisible = ref(false)
const confirmLoading = ref(false)
const modalTitle = ref('新增分类')
const isEdit = ref(false)
const editCategoryId = ref<number | null>(null)

// 分类表单
const categoryForm = ref<CategoryForm>({
  name: '',
  description: '',
  parentId: 0,
  sortOrder: 0,
  status: 1
})

// 分类表单规则
const categoryFormRules = {
  name: [
    { required: true, message: '请输入分类名称', trigger: 'blur' }
  ],
  parentId: [
    { required: true, message: '请选择父级分类', trigger: 'change' }
  ],
  sortOrder: [
    { required: true, message: '请输入排序值', trigger: 'blur' },
    { type: 'number', min: 0, max: 999, message: '排序值必须在0-999之间', trigger: 'blur' }
  ],
  status: [
    { required: true, message: '请选择分类状态', trigger: 'change' }
  ]
}

// 分类表单引用
const categoryFormRef = ref<any>(null)

// 移动分类相关
const moveModalVisible = ref(false)
const moveForm = reactive({
  categoryId: 0,
  newParentId: undefined as number | undefined
})

// 获取分类列表
const getCategoryList = async () => {
  try {
    loading.value = true
    
    // 构造查询参数
    const params = {
      page: pagination.current,
      size: pagination.pageSize,
      ...searchForm
    }
    
    // 调用接口获取分类列表
    const response = await categoryApi.getCategoryList(params)
    categoryList.value = response.data.content || response.data
    
    // 获取总数
    const countResponse = await categoryApi.getCategoryCount()
    pagination.total = countResponse.data
  } catch (error) {
    console.error('获取分类列表失败:', error)
    message.error('获取分类列表失败')
  } finally {
    loading.value = false
  }
}

// 获取分类树
const getCategoryTree = async () => {
  try {
    const response = await categoryApi.getCategoryTree()
    categoryTreeData.value = response.data
  } catch (error) {
    console.error('获取分类树失败:', error)
    message.error('获取分类树失败')
  }
}

// 处理搜索
const handleSearch = () => {
  pagination.current = 1
  getCategoryList()
}

// 处理重置
const handleReset = () => {
  searchForm.name = ''
  searchForm.status = undefined
  pagination.current = 1
  getCategoryList()
}

// 处理表格变化
const handleTableChange = (page: any) => {
  pagination.current = page.current
  pagination.pageSize = page.pageSize
  getCategoryList()
}

// 处理排序值变化
const handleSortOrderChange = async (record: Category) => {
  try {
    await categoryApi.updateCategorySortOrder(record.id, record.sortOrder)
    message.success('排序值更新成功')
  } catch (error) {
    console.error('更新排序值失败:', error)
    message.error('更新排序值失败')
    getCategoryList() // 重新获取列表以恢复原始值
  }
}

// 处理新增
const handleCreate = () => {
  modalTitle.value = '新增分类'
  isEdit.value = false
  editCategoryId.value = null
  categoryForm.value = {
    name: '',
    description: '',
    parentId: 0,
    sortOrder: 0,
    status: 1
  }
  modalVisible.value = true
}

// 处理编辑
const handleEdit = (record: Category) => {
  modalTitle.value = '编辑分类'
  isEdit.value = true
  editCategoryId.value = record.id
  categoryForm.value = {
    name: record.name,
    description: record.description || '',
    parentId: record.parentId,
    sortOrder: record.sortOrder,
    status: record.status
  }
  modalVisible.value = true
}

// 处理详情
const handleDetail = (record: Category) => {
  // 跳转到详情页面
  router.push(`/category/detail/${record.id}`)
}

// 处理移动
const handleMove = (record: Category) => {
  moveForm.categoryId = record.id
  moveForm.newParentId = undefined
  moveModalVisible.value = true
}

// 处理移动确认
const handleMoveOk = async () => {
  try {
    await categoryApi.moveCategory(moveForm.categoryId, moveForm.newParentId)
    message.success('移动成功')
    moveModalVisible.value = false
    getCategoryList()
  } catch (error) {
    console.error('移动分类失败:', error)
    message.error('移动分类失败')
  }
}

// 处理移动取消
const handleMoveCancel = () => {
  moveModalVisible.value = false
  moveForm.categoryId = 0
  moveForm.newParentId = undefined
}

// 处理启用
const handleEnable = async (record: Category) => {
  try {
    await categoryApi.enableCategory(record.id)
    message.success('启用成功')
    getCategoryList()
  } catch (error) {
    console.error('启用失败:', error)
    message.error('启用失败')
  }
}

// 处理禁用
const handleDisable = async (record: Category) => {
  try {
    await categoryApi.disableCategory(record.id)
    message.success('禁用成功')
    getCategoryList()
  } catch (error) {
    console.error('禁用失败:', error)
    message.error('禁用失败')
  }
}

// 处理删除
const handleDelete = (record: Category) => {
  Modal.confirm({
    title: '确认删除',
    content: `确定要删除分类 "${record.name}" 吗？`,
    okText: '确认',
    cancelText: '取消',
    onOk: async () => {
      try {
        await categoryApi.deleteCategory(record.id)
        message.success('删除成功')
        getCategoryList()
      } catch (error) {
        console.error('删除分类失败:', error)
        message.error('删除分类失败')
      }
    }
  })
}

// 处理模态框确认
const handleModalOk = () => {
  categoryFormRef.value?.validate().then(async () => {
    try {
      confirmLoading.value = true
      
      if (isEdit.value && editCategoryId.value) {
        // 编辑分类
        await categoryApi.updateCategory(editCategoryId.value, categoryForm.value)
        message.success('更新成功')
      } else {
        // 新增分类
        await categoryApi.createCategory(categoryForm.value)
        message.success('创建成功')
      }
      
      modalVisible.value = false
      getCategoryList()
    } catch (error) {
      console.error('保存分类失败:', error)
      message.error('保存分类失败')
    } finally {
      confirmLoading.value = false
    }
  })
}

// 处理模态框取消
const handleModalCancel = () => {
  modalVisible.value = false
}

// 路由
const router = useRouter()

// 组件挂载时获取数据
onMounted(() => {
  getCategoryList()
  getCategoryTree()
})
</script>

<style scoped>
.category-list {
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