<template>
  <PageContainer :title="`商品详情 - ${product.name}`">
    <div v-if="loading" class="loading-container">
      <a-spin size="large" />
    </div>
    
    <div v-else class="product-detail">
      <a-row :gutter="24">
        <!-- 商品图片 -->
        <a-col :span="8">
          <div class="product-images">
            <a-image
              :src="product.image || '/default-product.png'"
              :height="300"
              :preview="{
                mask: true,
                maskClassName: 'custom-image-preview'
              }"
            />
            <div class="image-gallery" v-if="product.gallery && product.gallery.length > 0">
              <a-image
                v-for="(image, index) in product.gallery"
                :key="index"
                :src="image"
                :height="80"
                :width="80"
                :style="{ marginRight: '8px', marginBottom: '8px' }"
                :preview="{
                  mask: true,
                  maskClassName: 'custom-image-preview'
                }"
              />
            </div>
          </div>
        </a-col>
        
        <!-- 商品基本信息 -->
        <a-col :span="16">
          <a-card :bordered="false">
            <a-descriptions title="商品信息" :column="2" bordered>
              <a-descriptions-item label="商品名称">{{ product.name }}</a-descriptions-item>
              <a-descriptions-item label="商品分类">{{ product.categoryName }}</a-descriptions-item>
              <a-descriptions-item label="SKU">{{ product.sku }}</a-descriptions-item>
              <a-descriptions-item label="条形码">{{ product.barcode || '无' }}</a-descriptions-item>
              <a-descriptions-item label="商品价格">
                <span class="price">¥{{ product.price.toFixed(2) }}</span>
              </a-descriptions-item>
              <a-descriptions-item label="市场价">
                <span class="market-price">¥{{ product.marketPrice?.toFixed(2) || '0.00' }}</span>
              </a-descriptions-item>
              <a-descriptions-item label="成本价">
                <span class="cost-price">¥{{ product.costPrice?.toFixed(2) || '0.00' }}</span>
              </a-descriptions-item>
              <a-descriptions-item label="商品状态">
                <a-tag :color="product.status === 1 ? 'green' : 'red'">
                  {{ product.status === 1 ? '上架' : '下架' }}
                </a-tag>
              </a-descriptions-item>
              <a-descriptions-item label="创建时间">{{ product.createdAt }}</a-descriptions-item>
              <a-descriptions-item label="更新时间">{{ product.updatedAt }}</a-descriptions-item>
            </a-descriptions>
            
            <a-divider />
            
            <div class="product-description">
              <h4>商品描述</h4>
              <p>{{ product.description || '暂无描述' }}</p>
            </div>
            
            <a-divider />
            
            <div class="product-detail-content">
              <h4>商品详情</h4>
              <div v-html="product.detail || '暂无详情'" />
            </div>
          </a-card>
        </a-col>
      </a-row>
      
      <!-- 库存信息 -->
      <a-row :gutter="24" style="margin-top: 24px">
        <a-col :span="24">
          <a-card title="库存信息" :bordered="false">
            <a-descriptions :column="4" bordered>
              <a-descriptions-item label="当前库存">{{ inventory?.quantity || 0 }}</a-descriptions-item>
              <a-descriptions-item label="预留库存">{{ inventory?.reservedQuantity || 0 }}</a-descriptions-item>
              <a-descriptions-item label="可用库存">{{ (inventory?.quantity || 0) - (inventory?.reservedQuantity || 0) }}</a-descriptions-item>
              <a-descriptions-item label="库存状态">
                <a-tag :color="(inventory?.quantity || 0) > 0 ? 'green' : 'red'">
                  {{ (inventory?.quantity || 0) > 0 ? '有库存' : '无库存' }}
                </a-tag>
              </a-descriptions-item>
            </a-descriptions>
            
            <div class="inventory-actions" style="margin-top: 16px">
              <a-space>
                <a-button type="primary" @click="showInventoryModal = true">
                  <PlusOutlined />
                  调整库存
                </a-button>
                <a-button @click="showInventoryModal = true">
                  <PlusOutlined />
                  增加库存
                </a-button>
                <a-button @click="showInventoryModal = true">
                  <MinusOutlined />
                  减少库存
                </a-button>
              </a-space>
            </div>
          </a-card>
        </a-col>
      </a-row>
      
      <!-- 操作按钮 -->
      <div class="actions" style="margin-top: 24px">
        <a-space>
          <a-button type="primary" @click="handleEdit">
            <EditOutlined />
            编辑商品
          </a-button>
          <a-button v-if="product.status === 1" @click="handleDisable">
            <StopOutlined />
            下架商品
          </a-button>
          <a-button v-else type="primary" @click="handleEnable">
            <CheckOutlined />
            上架商品
          </a-button>
          <a-button @click="handleBack">
            <ArrowLeftOutlined />
            返回列表
          </a-button>
        </a-space>
      </div>
    </div>
    
    <!-- 库存调整模态框 -->
    <a-modal
      v-model:open="showInventoryModal"
      title="库存调整"
      :width="500"
      @ok="handleInventoryOk"
      @cancel="handleInventoryCancel"
    >
      <a-form
        ref="inventoryFormRef"
        :model="inventoryForm"
        :rules="inventoryFormRules"
        layout="vertical"
      >
        <a-form-item label="调整类型" name="type">
          <a-radio-group v-model:value="inventoryForm.type">
            <a-radio value="set">设置库存</a-radio>
            <a-radio value="increase">增加库存</a-radio>
            <a-radio value="decrease">减少库存</a-radio>
          </a-radio-group>
        </a-form-item>
        
        <a-form-item label="数量" name="quantity">
          <a-input-number
            v-model:value="inventoryForm.quantity"
            :min="0"
            :precision="0"
            style="width: 100%"
          />
        </a-form-item>
        
        <a-form-item label="备注" name="remark">
          <a-textarea
            v-model:value="inventoryForm.remark"
            placeholder="请输入备注信息"
            :rows="3"
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
  PlusOutlined,
  MinusOutlined,
  EditOutlined,
  StopOutlined,
  CheckOutlined,
  ArrowLeftOutlined
} from '@ant-design/icons-vue'
import PageContainer from '@/components/common/PageContainer.vue'
import { productApi } from '@/services/api'
import type { Product, ProductInventory } from '@/types/product'

const router = useRouter()
const route = useRoute()

// 商品信息
const product = ref<Product>({
  id: 0,
  name: '',
  description: '',
  categoryId: 0,
  categoryName: '',
  price: 0,
  marketPrice: 0,
  costPrice: 0,
  sku: '',
  barcode: '',
  image: '',
  gallery: [],
  detail: '',
  status: 1,
  createdAt: '',
  updatedAt: ''
})

// 库存信息
const inventory = ref<ProductInventory | null>(null)

// 加载状态
const loading = ref(false)

// 库存调整模态框
const showInventoryModal = ref(false)
const inventoryFormRef = ref<any>(null)

// 库存调整表单
const inventoryForm = reactive({
  type: 'set',
  quantity: 0,
  remark: ''
})

// 库存调整表单规则
const inventoryFormRules = {
  type: [
    { required: true, message: '请选择调整类型', trigger: 'change' }
  ],
  quantity: [
    { required: true, message: '请输入数量', trigger: 'blur' },
    { type: 'number', min: 0, message: '数量不能小于0', trigger: 'blur' }
  ]
}

// 获取商品详情
const getProductDetail = async () => {
  try {
    loading.value = true
    const productId = Number(route.params.id)
    
    // 获取商品详情
    const response = await productApi.getProduct(productId)
    product.value = response.data
    
    // 获取库存信息
    const inventoryResponse = await productApi.getProductInventory(productId)
    inventory.value = inventoryResponse.data
  } catch (error) {
    console.error('获取商品详情失败:', error)
    message.error('获取商品详情失败')
    router.push('/products')
  } finally {
    loading.value = false
  }
}

// 处理编辑
const handleEdit = () => {
  router.push(`/products/edit/${product.value.id}`)
}

// 处理上架
const handleEnable = async () => {
  try {
    await productApi.enableProduct(product.value.id)
    message.success('上架成功')
    getProductDetail()
  } catch (error) {
    console.error('上架失败:', error)
    message.error('上架失败')
  }
}

// 处理下架
const handleDisable = async () => {
  try {
    await productApi.disableProduct(product.value.id)
    message.success('下架成功')
    getProductDetail()
  } catch (error) {
    console.error('下架失败:', error)
    message.error('下架失败')
  }
}

// 处理返回
const handleBack = () => {
  router.push('/products')
}

// 处理库存调整确认
const handleInventoryOk = () => {
  inventoryFormRef.value?.validate().then(async () => {
    try {
      const productId = Number(route.params.id)
      
      switch (inventoryForm.type) {
        case 'set':
          await productApi.updateProductInventory(productId, inventoryForm.quantity)
          message.success('库存设置成功')
          break
        case 'increase':
          await productApi.updateProductInventory(productId, (inventory.value?.quantity || 0) + inventoryForm.quantity)
          message.success('库存增加成功')
          break
        case 'decrease':
          await productApi.updateProductInventory(productId, Math.max(0, (inventory.value?.quantity || 0) - inventoryForm.quantity))
          message.success('库存减少成功')
          break
      }
      
      showInventoryModal.value = false
      getProductDetail()
    } catch (error) {
      console.error('库存调整失败:', error)
      message.error('库存调整失败')
    }
  })
}

// 处理库存调整取消
const handleInventoryCancel = () => {
  showInventoryModal.value = false
  inventoryForm.type = 'set'
  inventoryForm.quantity = 0
  inventoryForm.remark = ''
}

// 组件挂载时获取数据
onMounted(() => {
  getProductDetail()
})
</script>

<style scoped>
.loading-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 400px;
}

.product-detail {
  padding: 0;
}

.product-images {
  text-align: center;
}

.image-gallery {
  margin-top: 16px;
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
}

.price {
  color: #f5222d;
  font-size: 24px;
  font-weight: bold;
}

.market-price {
  color: #999;
  text-decoration: line-through;
}

.cost-price {
  color: #666;
}

.product-description {
  margin-top: 16px;
}

.product-description h4 {
  margin-bottom: 8px;
  color: #333;
}

.product-detail-content {
  margin-top: 16px;
}

.product-detail-content h4 {
  margin-bottom: 8px;
  color: #333;
}

.actions {
  text-align: center;
}

:deep(.custom-image-preview .ant-image-mask) {
  background: rgba(0, 0, 0, 0.5);
}
</style>