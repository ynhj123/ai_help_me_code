<template>
  <PageContainer title="订单管理" subTitle="管理系统中的订单">
    <template #extra>
      <a-button type="primary" @click="handleSearch">
        <SearchOutlined />
        搜索
      </a-button>
      <a-button @click="handleReset">
        <ReloadOutlined />
        重置
      </a-button>
    </template>
    
    <div class="order-list">
      <!-- 搜索表单 -->
      <a-form
        :model="searchForm"
        :layout="'inline'"
        class="search-form"
      >
        <a-form-item label="订单编号">
          <a-input
            v-model:value="searchForm.orderNo"
            placeholder="请输入订单编号"
            allow-clear
          />
        </a-form-item>
        
        <a-form-item label="订单状态">
          <a-select
            v-model:value="searchForm.status"
            placeholder="请选择状态"
            allow-clear
            style="width: 120px"
          >
            <a-select-option :value="1">待付款</a-select-option>
            <a-select-option :value="2">已付款</a-select-option>
            <a-select-option :value="3">已发货</a-select-option>
            <a-select-option :value="4">已完成</a-select-option>
            <a-select-option :value="5">已取消</a-select-option>
          </a-select>
        </a-form-item>
        
        <a-form-item label="下单时间">
          <a-range-picker
            v-model:value="searchForm.timeRange"
            :show-time="{ format: 'HH:mm' }"
            format="YYYY-MM-DD HH:mm:ss"
            :placeholder="['开始时间', '结束时间']"
          />
        </a-form-item>
        
        <a-form-item label="用户ID">
          <a-input-number
            v-model:value="searchForm.userId"
            placeholder="请输入用户ID"
            style="width: 120px"
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
      
      <!-- 订单表格 -->
      <CommonTable
        :columns="columns"
        :data-source="orderList"
        :loading="loading"
        :pagination="pagination"
        @change="handleTableChange"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'status'">
            <a-tag :color="getStatusColor(record.status)">
              {{ getStatusText(record.status) }}
            </a-tag>
          </template>
          
          <template v-else-if="column.key === 'totalAmount'">
            <span class="price">¥{{ record.totalAmount.toFixed(2) }}</span>
          </template>
          
          <template v-else-if="column.key === 'payAmount'">
            <span class="pay-amount">¥{{ record.payAmount.toFixed(2) }}</span>
          </template>
          
          <template v-else-if="column.key === 'createdAt'">
            <span>{{ record.createdAt }}</span>
          </template>
          
          <template v-else-if="column.key === 'action'">
            <a-space>
              <a-button type="link" size="small" @click="handleDetail(record)">
                详情
              </a-button>
              <a-button v-if="record.status === 1" type="link" size="small" @click="handleCancel(record)">
                取消
              </a-button>
              <a-button v-if="record.status === 2" type="link" size="small" @click="handleShip(record)">
                发货
              </a-button>
              <a-button v-if="record.status === 3" type="link" size="small" @click="handleConfirm(record)">
                确认收货
              </a-button>
              <a-button v-if="record.status === 5" type="link" size="small" @click="handleDelete(record)">
                删除
              </a-button>
            </a-space>
          </template>
        </template>
      </CommonTable>
    </div>
    
    <!-- 订单详情模态框 -->
    <a-modal
      v-model:open="detailModalVisible"
      title="订单详情"
      :width="1000"
      :footer="null"
      @cancel="handleDetailCancel"
    >
      <div v-if="currentOrder" class="order-detail">
        <a-descriptions title="订单信息" :column="2" bordered>
          <a-descriptions-item label="订单编号">{{ currentOrder.orderNo }}</a-descriptions-item>
          <a-descriptions-item label="订单状态">
            <a-tag :color="getStatusColor(currentOrder.status)">
              {{ getStatusText(currentOrder.status) }}
            </a-tag>
          </a-descriptions-item>
          <a-descriptions-item label="用户ID">{{ currentOrder.userId }}</a-descriptions-item>
          <a-descriptions-item label="订单金额">
            <span class="price">¥{{ currentOrder.totalAmount.toFixed(2) }}</span>
          </a-descriptions-item>
          <a-descriptions-item label="优惠金额">
            <span class="discount">-¥{{ currentOrder.discountAmount.toFixed(2) }}</span>
          </a-descriptions-item>
          <a-descriptions-item label="运费">
            <span class="shipping">¥{{ currentOrder.shippingAmount.toFixed(2) }}</span>
          </a-descriptions-item>
          <a-descriptions-item label="实付金额">
            <span class="pay-amount">¥{{ currentOrder.payAmount.toFixed(2) }}</span>
          </a-descriptions-item>
          <a-descriptions-item label="支付方式">
            {{ getPaymentMethodText(currentOrder.paymentMethod) }}
          </a-descriptions-item>
          <a-descriptions-item label="下单时间">{{ currentOrder.createdAt }}</a-descriptions-item>
          <a-descriptions-item label="支付时间">{{ currentOrder.paymentTime || '未支付' }}</a-descriptions-item>
          <a-descriptions-item label="发货时间">{{ currentOrder.shippingTime || '未发货' }}</a-descriptions-item>
          <a-descriptions-item label="完成时间">{{ currentOrder.completedTime || '未完成' }}</a-descriptions-item>
        </a-descriptions>
        
        <a-divider />
        
        <div class="order-items">
          <h4>订单商品</h4>
          <a-table
            :columns="itemColumns"
            :data-source="currentOrder.items || []"
            :pagination="false"
            size="small"
          >
            <template #bodyCell="{ column, record }">
              <template v-if="column.key === 'image'">
                <a-image
                  :src="record.productImage || '/default-product.png'"
                  :width="40"
                  :height="40"
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
        </div>
        
        <a-divider />
        
        <div class="order-address">
          <h4>收货信息</h4>
          <p><strong>收货人：</strong>{{ currentOrder.receiverName }}</p>
          <p><strong>联系电话：</strong>{{ currentOrder.receiverPhone }}</p>
          <p><strong>收货地址：</strong>{{ currentOrder.shippingAddress }}</p>
        </div>
        
        <a-divider />
        
        <div class="order-remark">
          <h4>订单备注</h4>
          <p>{{ currentOrder.remark || '无' }}</p>
        </div>
      </div>
    </a-modal>
    
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
import { useRouter } from 'vue-router'
import { message, Modal } from 'ant-design-vue'
import {
  SearchOutlined,
  ReloadOutlined
} from '@ant-design/icons-vue'
import PageContainer from '@/components/common/PageContainer.vue'
import CommonTable from '@/components/common/Table.vue'
import { orderApi } from '@/services/api'
import type { Order, OrderQueryRequest, OrderItem } from '@/types/order'

// 订单列表
const orderList = ref<Order[]>([])

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
    getOrderList()
  }
})

// 搜索表单
const searchForm = reactive<OrderQueryRequest>({
  orderNo: '',
  status: undefined,
  userId: undefined,
  startTime: undefined,
  endTime: undefined
})

// 表格列定义
const columns = [
  {
    title: '订单编号',
    dataIndex: 'orderNo',
    key: 'orderNo',
    ellipsis: true,
    width: 180
  },
  {
    title: '用户ID',
    dataIndex: 'userId',
    key: 'userId',
    width: 80,
    align: 'center'
  },
  {
    title: '订单状态',
    dataIndex: 'status',
    key: 'status',
    width: 100,
    align: 'center'
  },
  {
    title: '订单金额',
    dataIndex: 'totalAmount',
    key: 'totalAmount',
    width: 120,
    align: 'right'
  },
  {
    title: '实付金额',
    dataIndex: 'payAmount',
    key: 'payAmount',
    width: 120,
    align: 'right'
  },
  {
    title: '下单时间',
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

// 订单商品列定义
const itemColumns = [
  {
    title: '商品图片',
    dataIndex: 'productImage',
    key: 'image',
    width: 60,
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
    width: 80,
    align: 'right'
  },
  {
    title: '数量',
    dataIndex: 'quantity',
    key: 'quantity',
    width: 60,
    align: 'center'
  },
  {
    title: '小计',
    dataIndex: 'totalAmount',
    key: 'totalAmount',
    width: 80,
    align: 'right'
  }
]

// 订单详情相关
const detailModalVisible = ref(false)
const currentOrder = ref<Order | null>(null)

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

// 获取订单列表
const getOrderList = async () => {
  try {
    loading.value = true
    
    // 构造查询参数
    const params = {
      page: pagination.current,
      size: pagination.pageSize,
      ...searchForm
    }
    
    // 调用接口获取订单列表
    const response = await orderApi.getOrderList(params)
    orderList.value = response.data.content || response.data
    
    // 获取总数
    const countResponse = await orderApi.getOrderCount(params)
    pagination.total = countResponse.data
  } catch (error) {
    console.error('获取订单列表失败:', error)
    message.error('获取订单列表失败')
  } finally {
    loading.value = false
  }
}

// 处理搜索
const handleSearch = () => {
  pagination.current = 1
  getOrderList()
}

// 处理重置
const handleReset = () => {
  searchForm.orderNo = ''
  searchForm.status = undefined
  searchForm.userId = undefined
  searchForm.startTime = undefined
  searchForm.endTime = undefined
  pagination.current = 1
  getOrderList()
}

// 处理表格变化
const handleTableChange = (page: any) => {
  pagination.current = page.current
  pagination.pageSize = page.pageSize
  getOrderList()
}

// 处理详情
const handleDetail = (record: Order) => {
  // 跳转到详情页面
  router.push(`/order/detail/${record.id}`)
}

// 处理详情取消
const handleDetailCancel = () => {
  detailModalVisible.value = false
  currentOrder.value = null
}

// 处理发货
const handleShip = (record: Order) => {
  currentOrder.value = record
  shipForm.logisticsCompany = ''
  shipForm.trackingNumber = ''
  shipModalVisible.value = true
}

// 处理发货确认
const handleShipOk = () => {
  shipFormRef.value?.validate().then(async () => {
    try {
      if (currentOrder.value) {
        await orderApi.shipOrder(
          currentOrder.value.id,
          shipForm.logisticsCompany,
          shipForm.trackingNumber
        )
        message.success('发货成功')
        shipModalVisible.value = false
        getOrderList()
      }
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
const handleCancel = (record: Order) => {
  currentOrder.value = record
  cancelForm.cancelReason = ''
  cancelModalVisible.value = true
}

// 处理取消确认
const handleCancelOk = () => {
  cancelFormRef.value?.validate().then(async () => {
    try {
      if (currentOrder.value) {
        await orderApi.cancelOrder(currentOrder.value.id, cancelForm.cancelReason)
        message.success('取消成功')
        cancelModalVisible.value = false
        getOrderList()
      }
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
const handleConfirm = async (record: Order) => {
  try {
    await orderApi.confirmOrder(record.id)
    message.success('确认收货成功')
    getOrderList()
  } catch (error) {
    console.error('确认收货失败:', error)
    message.error('确认收货失败')
  }
}

// 处理删除
const handleDelete = (record: Order) => {
  Modal.confirm({
    title: '确认删除',
    content: `确定要删除订单 "${record.orderNo}" 吗？`,
    okText: '确认',
    cancelText: '取消',
    onOk: async () => {
      try {
        await orderApi.deleteOrder(record.id)
        message.success('删除成功')
        getOrderList()
      } catch (error) {
        console.error('删除订单失败:', error)
        message.error('删除订单失败')
      }
    }
  })
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
  getOrderList()
})
</script>

<style scoped>
.order-list {
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

.order-detail {
  padding: 0;
}

.order-items {
  margin-top: 16px;
}

.order-items h4 {
  margin-bottom: 8px;
  color: #333;
}

.order-address {
  margin-top: 16px;
}

.order-address h4 {
  margin-bottom: 8px;
  color: #333;
}

.order-remark {
  margin-top: 16px;
}

.order-remark h4 {
  margin-bottom: 8px;
  color: #333;
}

:deep(.custom-image-preview .ant-image-mask) {
  background: rgba(0, 0, 0, 0.5);
}
</style>