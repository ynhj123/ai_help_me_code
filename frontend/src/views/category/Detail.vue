<template>
  <PageContainer :title="`分类详情 - ${category.name}`">
    <div v-if="loading" class="loading-container">
      <a-spin size="large" />
    </div>
    
    <div v-else class="category-detail">
      <a-row :gutter="24">
        <!-- 分类基本信息 -->
        <a-col :span="24">
          <a-card :bordered="false">
            <a-descriptions title="分类信息" :column="2" bordered>
              <a-descriptions-item label="分类名称">{{ category.name }}</a-descriptions-item>
              <a-descriptions-item label="父级分类">{{ category.parentName || '无' }}</a-descriptions-item>
              <a-descriptions-item label="排序值">{{ category.sortOrder }}</a-descriptions-item>
              <a-descriptions-item label="分类状态">
                <a-tag :color="category.status === 1 ? 'green' : 'red'">
                  {{ category.status === 1 ? '启用' : '禁用' }}
                </a-tag>
              </a-descriptions-item>
              <a-descriptions-item label="创建时间">{{ category.createdAt }}</a-descriptions-item>
              <a-descriptions-item label="更新时间">{{ category.updatedAt }}</a-descriptions-item>
            </a-descriptions>
            
            <a-divider />
            
            <div class="category-description">
              <h4>分类描述</h4>
              <p>{{ category.description || '暂无描述' }}</p>
            </div>
          </a-card>
        </a-col>
      </a-row>
      
      <!-- 分类统计信息 -->
      <a-row :gutter="24" style="margin-top: 24px">
        <a-col :span="24">
          <a-card title="分类统计" :bordered="false">
            <a-row :gutter="16">
              <a-col :span="6">
                <a-statistic
                  title="子分类数量"
                  :value="categoryStats.childrenCount"
                  :value-style="{ color: '#3f8600' }"
                />
              </a-col>
              <a-col :span="6">
                <a-statistic
                  title="商品数量"
                  :value="categoryStats.productCount"
                  :value-style="{ color: '#1890ff' }"
                />
              </a-col>
              <a-col :span="6">
                <a-statistic
                  title="启用商品数"
                  :value="categoryStats.enabledProductCount"
                  :value-style="{ color: '#52c41a' }"
                />
              </a-col>
              <a-col :span="6">
                <a-statistic
                  title="禁用商品数"
                  :value="categoryStats.disabledProductCount"
                  :value-style="{ color: '#ff4d4f' }"
                />
              </a-col>
            </a-row>
          </a-card>
        </a-col>
      </a-row>
      
      <!-- 子分类列表 -->
      <a-row :gutter="24" style="margin-top: 24px">
        <a-col :span="24">
          <a-card title="子分类列表" :bordered="false">
            <CommonTable
              :columns="childColumns"
              :data-source="childCategories"
              :loading="childLoading"
              :pagination="childPagination"
              @change="handleChildTableChange"
            >
              <template #bodyCell="{ column, record }">
                <template v-if="column.key === 'status'">
                  <a-tag :color="record.status === 1 ? 'green' : 'red'">
                    {{ record.status === 1 ? '启用' : '禁用' }}
                  </a-tag>
                </template>
                
                <template v-else-if="column.key === 'action'">
                  <a-space>
                    <a-button type="link" size="small" @click="handleEditChild(record)">
                      编辑
                    </a-button>
                    <a-button type="link" size="small" @click="handleDetailChild(record)">
                      详情
                    </a-button>
                    <a-popconfirm
                      v-if="record.status === 1"
                      title="确定要禁用该分类吗？"
                      @confirm="handleDisableChild(record)"
                    >
                      <a-button type="link" size="small" danger>
                        禁用
                      </a-button>
                    </a-popconfirm>
                    <a-popconfirm
                      v-else
                      title="确定要启用该分类吗？"
                      @confirm="handleEnableChild(record)"
                    >
                      <a-button type="link" size="small" type="primary">
                        启用
                      </a-button>
                    </a-popconfirm>
                    <a-popconfirm
                      title="确定要删除该分类吗？"
                      @confirm="handleDeleteChild(record)"
                    >
                      <a-button type="link" size="small" danger>
                        删除
                      </a-button>
                    </a-popconfirm>
                  </a-space>
                </template>
              </template>
            </CommonTable>
          </a-card>
        </a-col>
      </a-row>
      
      <!-- 操作按钮 -->
      <div class="actions" style="margin-top: 24px">
        <a-space>
          <a-button type="primary" @click="handleEdit">
            <EditOutlined />
            编辑分类
          </a-button>
          <a-button v-if="category.status === 1" @click="handleDisable">
            <StopOutlined />
            禁用分类
          </a-button>
          <a-button v-else type="primary" @click="handleEnable">
            <CheckOutlined />
            启用分类
          </a-button>
          <a-button @click="handleMove">
            <SwapOutlined />
            移动分类
          </a-button>
          <a-button @click="handleBack">
            <ArrowLeftOutlined />
            返回列表
          </a-button>
        </a-space>
      </div>
    </div>
    
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
import { ref, reactive, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { message } from 'ant-design-vue'
import {
  EditOutlined,
  StopOutlined,
  CheckOutlined,
  SwapOutlined,
  ArrowLeftOutlined
} from '@ant-design/icons-vue'
import PageContainer from '@/components/common/PageContainer.vue'
import CommonTable from '@/components/common/Table.vue'
import { categoryApi } from '@/services/api'
import type { Category, CategoryTreeNodeDTO } from '@/types/category'

const router = useRouter()
const route = useRoute()

// 分类信息
const category = ref<Category>({
  id: 0,
  name: '',
  description: '',
  parentId: 0,
  parentName: '',
  sortOrder: 0,
  status: 1,
  createdAt: '',
  updatedAt: ''
})

// 分类统计信息
const categoryStats = ref({
  childrenCount: 0,
  productCount: 0,
  enabledProductCount: 0,
  disabledProductCount: 0
})

// 子分类列表
const childCategories = ref<Category[]>([])

// 分类树数据
const categoryTreeData = ref<CategoryTreeNodeDTO[]>([])

// 加载状态
const loading = ref(false)
const childLoading = ref(false)

// 子分类分页信息
const childPagination = reactive({
  current: 1,
  pageSize: 10,
  total: 0,
  showSizeChanger: true,
  showQuickJumper: true,
  showTotal: (total: number) => `共 ${total} 条`,
  onChange: (page: number) => {
    childPagination.current = page
    getChildCategories()
  }
})

// 子分类表格列定义
const childColumns = [
  {
    title: '分类名称',
    dataIndex: 'name',
    key: 'name',
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

// 移动分类相关
const moveModalVisible = ref(false)
const moveForm = reactive({
  categoryId: 0,
  newParentId: undefined as number | undefined
})

// 获取分类详情
const getCategoryDetail = async () => {
  try {
    loading.value = true
    const categoryId = Number(route.params.id)
    
    // 获取分类详情
    const response = await categoryApi.getCategory(categoryId)
    category.value = response.data
    
    // 获取分类统计信息
    // 这里可以调用相应的API获取统计信息
    categoryStats.value = {
      childrenCount: 0,
      productCount: 0,
      enabledProductCount: 0,
      disabledProductCount: 0
    }
    
    // 获取子分类列表
    getChildCategories()
    
    // 获取分类树
    getCategoryTree()
  } catch (error) {
    console.error('获取分类详情失败:', error)
    message.error('获取分类详情失败')
    router.push('/categories')
  } finally {
    loading.value = false
  }
}

// 获取子分类列表
const getChildCategories = async () => {
  try {
    childLoading.value = true
    const categoryId = Number(route.params.id)
    
    // 调用接口获取子分类列表
    const response = await categoryApi.getCategoryList({
      parentId: categoryId,
      page: childPagination.current,
      size: childPagination.pageSize
    })
    childCategories.value = response.data.content || response.data
    
    // 获取总数
    const countResponse = await categoryApi.getCategoryCount()
    childPagination.total = countResponse.data
  } catch (error) {
    console.error('获取子分类列表失败:', error)
    message.error('获取子分类列表失败')
  } finally {
    childLoading.value = false
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

// 处理子分类表格变化
const handleChildTableChange = (page: any) => {
  childPagination.current = page.current
  childPagination.pageSize = page.pageSize
  getChildCategories()
}

// 处理编辑
const handleEdit = () => {
  router.push(`/categories/edit/${category.value.id}`)
}

// 处理启用
const handleEnable = async () => {
  try {
    await categoryApi.enableCategory(category.value.id)
    message.success('启用成功')
    getCategoryDetail()
  } catch (error) {
    console.error('启用失败:', error)
    message.error('启用失败')
  }
}

// 处理禁用
const handleDisable = async () => {
  try {
    await categoryApi.disableCategory(category.value.id)
    message.success('禁用成功')
    getCategoryDetail()
  } catch (error) {
    console.error('禁用失败:', error)
    message.error('禁用失败')
  }
}

// 处理移动
const handleMove = () => {
  moveForm.categoryId = category.value.id
  moveForm.newParentId = undefined
  moveModalVisible.value = true
}

// 处理移动确认
const handleMoveOk = async () => {
  try {
    await categoryApi.moveCategory(moveForm.categoryId, moveForm.newParentId)
    message.success('移动成功')
    moveModalVisible.value = false
    getCategoryDetail()
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

// 处理返回
const handleBack = () => {
  router.push('/categories')
}

// 处理编辑子分类
const handleEditChild = (record: Category) => {
  router.push(`/categories/edit/${record.id}`)
}

// 处理详情子分类
const handleDetailChild = (record: Category) => {
  router.push(`/categories/detail/${record.id}`)
}

// 处理启用子分类
const handleEnableChild = async (record: Category) => {
  try {
    await categoryApi.enableCategory(record.id)
    message.success('启用成功')
    getChildCategories()
  } catch (error) {
    console.error('启用失败:', error)
    message.error('启用失败')
  }
}

// 处理禁用子分类
const handleDisableChild = async (record: Category) => {
  try {
    await categoryApi.disableCategory(record.id)
    message.success('禁用成功')
    getChildCategories()
  } catch (error) {
    console.error('禁用失败:', error)
    message.error('禁用失败')
  }
}

// 处理删除子分类
const handleDeleteChild = (record: Category) => {
  Modal.confirm({
    title: '确认删除',
    content: `确定要删除子分类 "${record.name}" 吗？`,
    okText: '确认',
    cancelText: '取消',
    onOk: async () => {
      try {
        await categoryApi.deleteCategory(record.id)
        message.success('删除成功')
        getChildCategories()
      } catch (error) {
        console.error('删除子分类失败:', error)
        message.error('删除子分类失败')
      }
    }
  })
}

// 组件挂载时获取数据
onMounted(() => {
  getCategoryDetail()
})
</script>

<style scoped>
.loading-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 400px;
}

.category-detail {
  padding: 0;
}

.category-description {
  margin-top: 16px;
}

.category-description h4 {
  margin-bottom: 8px;
  color: #333;
}

.actions {
  text-align: center;
}
</style>