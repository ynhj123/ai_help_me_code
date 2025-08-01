<template>
  <PageContainer :title="`订单详情 - ${order.orderNo}`">
    <div v-if="loading" class="loading-container">
      <a-spin size="large" />
    </div>
    
    <div v-else class="order-detail">
      <a-row :gutter="24">
        <!-- 订单基本信息 -->
        <a-col :span="24">
          <a-card :bordered="false">
            <a-descriptions title="订单信息" :column="2" bordered>
              <a-descriptions-item label="订单编号">{{ order.orderNo }}</a-descriptions-item>
              <a-descriptions-item label="订单状态">
                <a-tag :color="getStatusColor(order.status)">
                  {{ getStatusText(order.status) }}
                </a-tag>
              </a-descriptions-item>
              <a-descriptions-item label="用户ID">{{ order.userId }}</a-descriptions-item>
              <a-descriptions-item label="订单金额">
                <span class="price">¥{{ order.totalAmount.toFixed(2) }}</span>
              </a-descriptions-item>
              <a-descriptions-item label="优惠金额">
                <span class="discount">-¥{{ order.discountAmount.toFixed(2) }}</span>
              </a-descriptions-item>
              <a-descriptions-item label="运费">
                <span class="shipping">¥{{ order.shippingAmount.toFixed(2) }}</span>
              </a-descriptions-item>
              <a-descriptions-item label="实付金额">
                <span class="pay-amount">¥{{ order.payAmount.toFixed(2) }}</span>
              </a-descriptions-item>
              <a-descriptions-item label="支付方式">
                {{ getPaymentMethodText(order.paymentMethod) }}
              </a-descriptions-item>
              <a-descriptions-item label="下单时间">{{ order.createdAt }}</a-descriptions-item>
              <a-descriptions-item label="支付时间">{{ order.paymentTime || '未支付' }}</a-descriptions-item>
              <a-descriptions-item label="发货时间">{{ order.shippingTime || '未发货' }}</a-descriptions-item>
              <a-descriptions-item label="完成时间">{{ order.completedTime || '未完成' }}</a-descriptions-item>
            </a-descriptions>
            
            <a-divider />
            
            <div class="order-remark">
              <h4>订单备注</h4>
              <p>{{ order.remark || '无' }}</p>
            </div>
          </a-card>
        </a-col>
      </a-row>
      
      <!-- 收货信息 -->
      <a-row :gutter="24" style="margin-top: 24px">
        <a-col :span="24">
          <a-card title="收货信息" :bordered="false">
            <a-descriptions :column="1" bordered>
              <a-descriptions-item label="收货人">{{ order.receiverName }}</a-descriptions-item>
              <a-descriptions-item label="联系电话">{{ order.receiverPhone }}</a-descriptions-item>
              <a-descriptions-item label="收货地址">{{ order.shippingAddress }}</a-descriptions-item>
            </a-descriptions>
          </a-card>
        </a-col>
      </a-row>
      
      <!-- 订单商品 -->
      <a-row :gutter="24" style="margin-top: 24px">
        <a-col :span="24">
          <a-card title="订单商品" :bordered="false">
            <a-table
              :columns="itemColumns"
              :data-source="order.items || []"
              :pagination="false"
              size="small"
            >
              <template #bodyCell="{ column, record }">
                <template v-if="column.key === 'image'">
                  <a-image
                    :src="record.productImage || '/default-product.png'"
                    :width="60"
                    :height="60"
                    :preview="{
                      mask: true,
                      maskClassName: 'custom-image-preview'
                    }"
                  />
                </template>
                
                <template v-else-if="column.key === 'price'">
                  <span>¥{{ record.price.toFixed(2) }}</span>
                </template>
                
                <template v-else-if="column.key === 'totalAmount'">
                  <span>¥{{ record.totalAmount.toFixed(2) }}</span>
                </template>
              </template>
            </a-table>
          </a-card>
        </a-col>
      </a-row>
      
      <!-- 物流信息 -->
      <a-row v-if="order.logisticsCompany && order.trackingNumber" :gutter="24" style="margin-top: 24px">
        <a-col :span="24">
          <a-card title="物流信息" :bordered="false">
            <a-descriptions :column="2" bordered>
              <a-descriptions-item label="物流公司">{{ order.logisticsCompany }}</a-descriptions-item>
              <a-descriptions-item label="物流单号">{{ order.trackingNumber }}</a-descriptions-item>
              <a-descriptions-item label="发货时间">{{ order.shippingTime }}</a-descriptions-item>
              <a-descriptions-item label="预计到达时间">{{ order.estimatedArrivalTime || '待更新' }}</a-descriptions-item>
            </a-descriptions>
            
            <div class="logistics-trace" style="margin-top: 16px">
              <a-timeline>
                <a-timeline-item v-for="(trace, index) in logisticsTrace" :key="index" :color="trace.color">
                  <div class="trace-content">
                    <div class="trace-time">{{ trace.time }}</div>
                    <div class="trace-text">{{ trace.content }}</div>
                  </div>
                </a-timeline-item>
              </a-timeline>
            </div>
          </a-card>
        </a-col>
      </a-row>
      
      <!-- 操作按钮 -->
      <div class="actions" style="margin-top: 24px">
        <a-space>
          <a-button v-if="order.status === 1" @click="handleCancel">
            <StopOutlined />
            取消订单
          </a-button>
          <a-button v-if="order.status === 2" type="primary" @click="handleShip">
            <SendOutlined />
            发货
          </a-button>
          <a-button v-if="order.status === 3" @click="handleConfirm">
            <CheckOutlined />
            确认收货
          </a-button>
          <a-button v-if="order.status === 5" @click="handleDelete">
            <DeleteOutlined />
            删除订单
          </a-button>
          <a-button @click="handleBack">
            <ArrowLeftOutlined />
            返回列表
          </a-button>
        </a-space>
      </div>
    </div>
    
    <!-- 订单发货模态框 -->
    <a-modal
      v-model:open="shipModalVisible"
      title="订单发货"
      :width="500"
      @ok="handleShipOk"
      @cancel="handleShipCancel"
    >
      <a-form
        ref="shipFormRef"
        :model="shipForm"
        :rules="shipFormRules"
        layout="vertical"
      >
        <a-form-item label="物流公司" name="logisticsCompany">
          <a-select
            v-model:value="shipForm.logisticsCompany"
            placeholder="请选择物流公司"
          >
            <a-select-option value="顺丰快递">顺丰快递</a-select-option>
            <a-select-option value="中通快递">中通快递</a-select-option>
            <a-select-option value="圆通快递">圆通快递</a-select-option>
            <a-select-option value="韵达快递">韵达快递</a-select-option>
            <a-select-option value="申通快递">申通快递</a-select-option>
          </a-select>
        </a-form-item>
        
        <a-form-item label="物流单号" name="trackingNumber">
          <a-input
            v-model:value="shipForm.trackingNumber"
            placeholder="请输入物流单号"
          />
        </a-form-item>
      </a-form>
    </a-modal>
    
    <!-- 订单取消模态框 -->
    <a-modal
      v-model:open="cancelModalVisible"
      title="订单取消"
      :width="500"
      @ok="handleCancelOk"
      @cancel="handleCancelCancel"
    >
      <a-form
        ref="cancelFormRef"
        :model="cancelForm"
        :rules="cancelFormRules"
        layout="vertical"
      >
        <a-form-item label="取消原因" name="cancelReason">
          <a-textarea
            v-model:value="cancelForm.cancelReason"
            placeholder="请输入取消原因"
            :rows="4"
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
  StopOutlined,
  SendOutlined,
  CheckOutlined,
  DeleteOutlined,
  ArrowLeftOutlined
} from '@ant-design/icons-vue'
import PageContainer from '@/components/common/PageContainer.vue'
import { orderApi } from '@/services/api'
import type { Order, OrderItem } from '@/types/order'

const router = useRouter()
const route = useRoute()

// 订单信息
const order = ref<Order>({
  id: 0,
  orderNo: '',
  userId: 0,
  status: 1,
  totalAmount: 0,
  discountAmount: 0,
  shippingAmount: 0,
  payAmount: 0,
  paymentMethod: undefined,
  paymentTime: undefined,
  shippingAddress: '',
  receiverName: '',
  receiverPhone: '',
  shippingTime: undefined,
  completedTime: undefined,
  remark: '',
  createdAt: '',
  updatedAt: '',
  logisticsCompany: '',
  trackingNumber: '',
  estimatedArrivalTime: undefined
})

// 订单商品
const orderItems = ref<OrderItem[]>([])

// 物流追踪信息
const logisticsTrace = ref([
  {
    time: '2024-01-15 10:30:00',
    content: '【北京】快件已签收，签收人：本人',
    color: 'green'
  },
  {
    time: '2024-01-15 08:15:00',
    content: '【北京】快件已派送',
    color: 'blue'
  },
  {
    time: '2024-01-14 20:30:00',
    content: '【北京】快件到达北京转运中心',
    color: 'blue'
  },
  {
    time: '2024-01-14 15:20:00',
    content: '【上海】快件已发出',
    color: 'blue'
  }
])

// 加载状态
const loading = ref(false)

// 订单商品列定义
const itemColumns = [
  {
    title: '商品图片',
    dataIndex: 'productImage',
    key: 'image',
    width: 80,
    align: 'center'
  },
  {
    title: '商品名称',
    dataIndex: 'productName',
    key: 'productName',
    ellipsis: true
  },
  {
    title: 'SKU',
    dataIndex: 'productSku',
    key: 'productSku',
    ellipsis: true
  },
  {
    title: '单价',
    dataIndex: 'price',
    key: 'price',
    width: 100,
    align: 'right'
  },
  {
    title: '数量',
    dataIndex: 'quantity',
    key: 'quantity',
    width: 80,
    align: 'center'
  },
  {
    title: '小计',
    dataIndex: 'totalAmount',
    key: 'totalAmount',
    width: 100,
    align: 'right'
  }
]

// 订单发货相关
const shipModalVisible = ref(false)
const shipFormRef = ref<any>(null)
const shipForm = reactive({
  logisticsCompany: '',
  trackingNumber: ''
})

// 订单发货表单规则
const shipFormRules = {
  logisticsCompany: [
    { required: true, message: '请选择物流公司', trigger: 'change' }
  ],
  trackingNumber: [
    { required: true, message: '请输入物流单号', trigger: 'blur' }
  ]
}

// 订单取消相关
const cancelModalVisible = ref(false)
const cancelFormRef = ref<any>(null)
const cancelForm = reactive({
  cancelReason: ''
})

// 订单取消表单规则
const cancelFormRules = {
  cancelReason: [
    { required: true, message: '请输入取消原因', trigger: 'blur' }
  ]
}

// 获取订单详情
const getOrderDetail = async () => {
  try {
    loading.value = true
    const orderId = Number(route.params.id)
    
    // 获取订单详情
    const response = await orderApi.getOrder(orderId)
    order.value = response.data
    
    // 模拟获取订单商品信息
    orderItems.value = order.value.items || []
  } catch (error) {
    console.error('获取订单详情失败:', error)
    message.error('获取订单详情失败')
    router.push('/orders')
  } finally {
    loading.value = false
  }
}

// 处理发货
const handleShip = () => {
  shipForm.logisticsCompany = ''
  shipForm.trackingNumber = ''
  shipModalVisible.value = true
}

// 处理发货确认
const handleShipOk = () => {
  shipFormRef.value?.validate().then(async () => {
    try {
      await orderApi.shipOrder(
        order.value.id,
        shipForm.logisticsCompany,
        shipForm.trackingNumber
      )
      message.success('发货成功')
      shipModalVisible.value = false
      getOrderDetail()
    } catch (error) {
      console.error('发货失败:', error)
      message.error('发货失败')
    }
  })
}

// 处理发货取消
const handleShipCancel = () => {
  shipModalVisible.value = false
  shipForm.logisticsCompany = ''
  shipForm.trackingNumber = ''
}

// 处理取消
const handleCancel = () => {
  cancelForm.cancelReason = ''
  cancelModalVisible.value = true
}

// 处理取消确认
const handleCancelOk = () => {
  cancelFormRef.value?.validate().then(async () => {
    try {
      await orderApi.cancelOrder(order.value.id, cancelForm.cancelReason)
      message.success('取消成功')
      cancelModalVisible.value = false
      getOrderDetail()
    } catch (error) {
      console.error('取消失败:', error)
      message.error('取消失败')
    }
  })
}

// 处理取消取消
const handleCancelCancel = () => {
  cancelModalVisible.value = false
  cancelForm.cancelReason = ''
}

// 处理确认收货
const handleConfirm = async () => {
  try {
    await orderApi.confirmOrder(order.value.id)
    message.success('确认收货成功')
    getOrderDetail()
  } catch (error) {
    console.error('确认收货失败:', error)
    message.error('确认收货失败')
  }
}

// 处理删除
const handleDelete = () => {
  Modal.confirm({
    title: '确认删除',
    content: `确定要删除订单 "${order.value.orderNo}" 吗？`,
    okText: '确认',
    cancelText: '取消',
    onOk: async () => {
      try {
        await orderApi.deleteOrder(order.value.id)
        message.success('删除成功')
        router.push('/orders')
      } catch (error) {
        console.error('删除订单失败:', error)
        message.error('删除订单失败')
      }
    }
  })
}

// 处理返回
const handleBack = () => {
  router.push('/orders')
}

// 获取状态颜色
const getStatusColor = (status: number) => {
  const colors = {
    1: 'orange',    // 待付款
    2: 'blue',      // 已付款
    3: 'purple',    // 已发货
    4: 'green',     // 已完成
    5: 'red'        // 已取消
  }
  return colors[status as keyof typeof colors] || 'default'
}

// 获取状态文本
const getStatusText = (status: number) => {
  const texts = {
    1: '待付款',
    2: '已付款',
    3: '已发货',
    4: '已完成',
    5: '已取消'
  }
  return texts[status as keyof typeof texts] || '未知'
}

// 获取支付方式文本
const getPaymentMethodText = (method?: number) => {
  const texts = {
    1: '银联',
    2: '微信',
    3: '支付宝'
  }
  return method ? texts[method as keyof typeof texts] : '未支付'
}

// 组件挂载时获取数据
onMounted(() => {
  getOrderDetail()
})
</script>

<style scoped>
.loading-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 400px;
}

.order-detail {
  padding: 0;
}

.price {
  color: #f5222d;
  font-weight: bold;
}

.pay-amount {
  color: #1890ff;
  font-weight: bold;
}

.discount {
  color: #52c41a;
}

.shipping {
  color: #722ed1;
}

.order-remark {
  margin-top: 16px;
}

.order-remark h4 {
  margin-bottom: 8px;
  color: #333;
}

.logistics-trace {
  margin-top: 16px;
}

.trace-content {
  display: flex;
  flex-direction: column;
}

.trace-time {
  font-size: 12px;
  color: #999;
  margin-bottom: 4px;
}

.trace-text {
  font-size: 14px;
  color: #333;
}

.actions {
  text-align: center;
}

:deep(.custom-image-preview .ant-image-mask) {
  background: rgba(0, 0, 0, 0.5);
}
</style>