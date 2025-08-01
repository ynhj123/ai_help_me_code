<template>
  <PageContainer title="商品管理" subTitle="管理系统中的商品">
    <template #extra>
      <a-button type="primary" @click="handleCreate">
        <PlusOutlined />
        新增商品
      </a-button>
    </template>
    
    <div class="product-list">
      <!-- 搜索表单 -->
      <a-form
        :model="searchForm"
        :layout="'inline'"
        class="search-form"
      >
        <a-form-item label="商品名称">
          <a-input
            v-model:value="searchForm.name"
            placeholder="请输入商品名称"
            allow-clear
          />
        </a-form-item>
        
        <a-form-item label="商品分类">
          <a-select
            v-model:value="searchForm.categoryId"
            placeholder="请选择分类"
            allow-clear
            style="width: 200px"
          >
            <a-select-option v-for="category in categoryList" :key="category.id" :value="category.id">
              {{ category.name }}
            </a-select-option>
          </a-select>
        </a-form-item>
        
        <a-form-item label="商品状态">
          <a-select
            v-model:value="searchForm.status"
            placeholder="请选择状态"
            allow-clear
            style="width: 120px"
          >
            <a-select-option :value="1">上架</a-select-option>
            <a-select-option :value="0">下架</a-select-option>
          </a-select>
        </a-form-item>
        
        <a-form-item label="价格区间">
          <a-input-number
            v-model:value="searchForm.minPrice"
            placeholder="最低价"
            style="width: 100px"
            :min="0"
          />
          <span style="margin: 0 8px">-</span>
          <a-input-number
            v-model:value="searchForm.maxPrice"
            placeholder="最高价"
            style="width: 100px"
            :min="0"
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
      
      <!-- 商品表格 -->
      <CommonTable
        :columns="columns"
        :data-source="productList"
        :loading="loading"
        :pagination="pagination"
        @change="handleTableChange"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'image'">
            <a-image
              :src="record.image || '/default-product.png'"
              :width="60"
              :height="60"
              :preview="{
                mask: true,
                maskClassName: 'custom-image-preview'
              }"
            />
          </template>
          
          <template v-else-if="column.key === 'status'">
            <a-tag :color="record.status === 1 ? 'green' : 'red'">
              {{ record.status === 1 ? '上架' : '下架' }}
            </a-tag>
          </template>
          
          <template v-else-if="column.key === 'price'">
            <span>¥{{ record.price.toFixed(2) }}</span>
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
                title="确定要下架该商品吗？"
                @confirm="handleDisable(record)"
              >
                <a-button type="link" size="small" danger>
                  下架
                </a-button>
              </a-popconfirm>
              <a-popconfirm
                v-else
                title="确定要上架该商品吗？"
                @confirm="handleEnable(record)"
              >
                <a-button type="link" size="small" type="primary">
                  上架
                </a-button>
              </a-popconfirm>
              <a-popconfirm
                title="确定要删除该商品吗？"
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
    
    <!-- 商品编辑模态框 -->
    <a-modal
      v-model:open="modalVisible"
      :title="modalTitle"
      :width="800"
      :confirm-loading="confirmLoading"
      @ok="handleModalOk"
      @cancel="handleModalCancel"
    >
      <a-form
        ref="productFormRef"
        :model="productForm"
        :rules="productFormRules"
        layout="vertical"
      >
        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item label="商品名称" name="name">
              <a-input
                v-model:value="productForm.name"
                placeholder="请输入商品名称"
              />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="商品分类" name="categoryId">
              <a-select
                v-model:value="productForm.categoryId"
                placeholder="请选择分类"
                :loading="categoryLoading"
              >
                <a-select-option v-for="category in categoryList" :key="category.id" :value="category.id">
                  {{ category.name }}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
        </a-row>
        
        <a-row :gutter="16">
          <a-col :span="8">
            <a-form-item label="商品价格" name="price">
              <a-input-number
                v-model:value="productForm.price"
                placeholder="请输入价格"
                :min="0"
                :precision="2"
                style="width: 100%"
              />
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="市场价" name="marketPrice">
              <a-input-number
                v-model:value="productForm.marketPrice"
                placeholder="请输入市场价"
                :min="0"
                :precision="2"
                style="width: 100%"
              />
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="成本价" name="costPrice">
              <a-input-number
                v-model:value="productForm.costPrice"
                placeholder="请输入成本价"
                :min="0"
                :precision="2"
                style="width: 100%"
              />
            </a-form-item>
          </a-col>
        </a-row>
        
        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item label="SKU" name="sku">
              <a-input
                v-model:value="productForm.sku"
                placeholder="请输入SKU"
              />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="条形码" name="barcode">
              <a-input
                v-model:value="productForm.barcode"
                placeholder="请输入条形码"
              />
            </a-form-item>
          </a-col>
        </a-row>
        
        <a-form-item label="商品图片" name="image">
          <ImageUpload
            v-model:value="productForm.image"
            :max-count="1"
            :size-limit="5"
          />
        </a-form-item>
        
        <a-form-item label="商品画廊">
          <ImageUpload
            v-model:value="productForm.gallery"
            :max-count="5"
            :size-limit="5"
          />
        </a-form-item>
        
        <a-form-item label="商品状态" name="status">
          <a-radio-group v-model:value="productForm.status">
            <a-radio :value="1">上架</a-radio>
            <a-radio :value="0">下架</a-radio>
          </a-radio-group>
        </a-form-item>
        
        <a-form-item label="商品描述" name="description">
          <a-textarea
            v-model:value="productForm.description"
            placeholder="请输入商品描述"
            :rows="4"
          />
        </a-form-item>
        
        <a-form-item label="商品详情" name="detail">
          <a-textarea
            v-model:value="productForm.detail"
            placeholder="请输入商品详情"
            :rows="6"
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
import ImageUpload from '@/components/common/ImageUpload.vue'
import { productApi, categoryApi } from '@/services/api'
import type { Product, ProductForm, ProductQueryRequest, Category } from '@/types/product'

// 商品列表
const productList = ref<Product[]>([])

// 分类列表
const categoryList = ref<Category[]>([])

// 加载状态
const loading = ref(false)
const categoryLoading = ref(false)

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
    getProductList()
  }
})

// 搜索表单
const searchForm = reactive<ProductQueryRequest>({
  name: '',
  categoryId: undefined,
  status: undefined,
  minPrice: undefined,
  maxPrice: undefined
})

// 表格列定义
const columns = [
  {
    title: '商品图片',
    dataIndex: 'image',
    key: 'image',
    width: 80,
    fixed: 'left'
  },
  {
    title: '商品名称',
    dataIndex: 'name',
    key: 'name',
    ellipsis: true
  },
  {
    title: '商品分类',
    dataIndex: 'categoryName',
    key: 'categoryName',
    ellipsis: true
  },
  {
    title: 'SKU',
    dataIndex: 'sku',
    key: 'sku',
    ellipsis: true
  },
  {
    title: '价格',
    dataIndex: 'price',
    key: 'price',
    width: 100,
    align: 'right'
  },
  {
    title: '市场价',
    dataIndex: 'marketPrice',
    key: 'marketPrice',
    width: 100,
    align: 'right'
  },
  {
    title: '状态',
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
const modalTitle = ref('新增商品')
const isEdit = ref(false)
const editProductId = ref<number | null>(null)

// 商品表单
const productForm = ref<ProductForm>({
  name: '',
  description: '',
  categoryId: 0,
  price: 0,
  marketPrice: 0,
  costPrice: 0,
  sku: '',
  barcode: '',
  image: '',
  gallery: [],
  detail: '',
  status: 1
})

// 商品表单规则
const productFormRules = {
  name: [
    { required: true, message: '请输入商品名称', trigger: 'blur' }
  ],
  categoryId: [
    { required: true, message: '请选择商品分类', trigger: 'change' }
  ],
  price: [
    { required: true, message: '请输入商品价格', trigger: 'blur' },
    { type: 'number', min: 0, message: '价格不能小于0', trigger: 'blur' }
  ],
  sku: [
    { required: true, message: '请输入SKU', trigger: 'blur' }
  ],
  status: [
    { required: true, message: '请选择商品状态', trigger: 'change' }
  ]
}

// 商品表单引用
const productFormRef = ref<any>(null)

// 获取商品列表
const getProductList = async () => {
  try {
    loading.value = true
    
    // 构造查询参数
    const params = {
      page: pagination.current,
      size: pagination.pageSize,
      ...searchForm
    }
    
    // 调用接口获取商品列表
    const response = await productApi.getProductList(params)
    productList.value = response.data.content || response.data
    
    // 获取总数
    const countResponse = await productApi.getProductCount(params)
    pagination.total = countResponse.data
  } catch (error) {
    console.error('获取商品列表失败:', error)
    message.error('获取商品列表失败')
  } finally {
    loading.value = false
  }
}

// 获取分类列表
const getCategoryList = async () => {
  try {
    categoryLoading.value = true
    const response = await categoryApi.getCategoryList()
    categoryList.value = response.data.content || response.data
  } catch (error) {
    console.error('获取分类列表失败:', error)
    message.error('获取分类列表失败')
  } finally {
    categoryLoading.value = false
  }
}

// 处理搜索
const handleSearch = () => {
  pagination.current = 1
  getProductList()
}

// 处理重置
const handleReset = () => {
  searchForm.name = ''
  searchForm.categoryId = undefined
  searchForm.status = undefined
  searchForm.minPrice = undefined
  searchForm.maxPrice = undefined
  pagination.current = 1
  getProductList()
}

// 处理表格变化
const handleTableChange = (page: any) => {
  pagination.current = page.current
  pagination.pageSize = page.pageSize
  getProductList()
}

// 处理新增
const handleCreate = () => {
  modalTitle.value = '新增商品'
  isEdit.value = false
  editProductId.value = null
  productForm.value = {
    name: '',
    description: '',
    categoryId: 0,
    price: 0,
    marketPrice: 0,
    costPrice: 0,
    sku: '',
    barcode: '',
    image: '',
    gallery: [],
    detail: '',
    status: 1
  }
  modalVisible.value = true
}

// 处理编辑
const handleEdit = (record: Product) => {
  modalTitle.value = '编辑商品'
  isEdit.value = true
  editProductId.value = record.id
  productForm.value = {
    name: record.name,
    description: record.description || '',
    categoryId: record.categoryId,
    price: record.price,
    marketPrice: record.marketPrice || 0,
    costPrice: record.costPrice || 0,
    sku: record.sku,
    barcode: record.barcode || '',
    image: record.image || '',
    gallery: record.gallery || [],
    detail: record.detail || '',
    status: record.status
  }
  modalVisible.value = true
}

// 处理详情
const handleDetail = (record: Product) => {
  // 跳转到详情页面
  router.push(`/product/detail/${record.id}`)
}

// 处理上架
const handleEnable = async (record: Product) => {
  try {
    await productApi.enableProduct(record.id)
    message.success('上架成功')
    getProductList()
  } catch (error) {
    console.error('上架失败:', error)
    message.error('上架失败')
  }
}

// 处理下架
const handleDisable = async (record: Product) => {
  try {
    await productApi.disableProduct(record.id)
    message.success('下架成功')
    getProductList()
  } catch (error) {
    console.error('下架失败:', error)
    message.error('下架失败')
  }
}

// 处理删除
const handleDelete = (record: Product) => {
  Modal.confirm({
    title: '确认删除',
    content: `确定要删除商品 "${record.name}" 吗？`,
    okText: '确认',
    cancelText: '取消',
    onOk: async () => {
      try {
        await productApi.deleteProduct(record.id)
        message.success('删除成功')
        getProductList()
      } catch (error) {
        console.error('删除商品失败:', error)
        message.error('删除商品失败')
      }
    }
  })
}

// 处理模态框确认
const handleModalOk = () => {
  productFormRef.value?.validate().then(async () => {
    try {
      confirmLoading.value = true
      
      if (isEdit.value && editProductId.value) {
        // 编辑商品
        await productApi.updateProduct(editProductId.value, productForm.value)
        message.success('更新成功')
      } else {
        // 新增商品
        await productApi.createProduct(productForm.value)
        message.success('创建成功')
      }
      
      modalVisible.value = false
      getProductList()
    } catch (error) {
      console.error('保存商品失败:', error)
      message.error('保存商品失败')
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
  getProductList()
})
</script>

<style scoped>
.product-list {
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

:deep(.custom-image-preview .ant-image-mask) {
  background: rgba(0, 0, 0, 0.5);
}
</style>