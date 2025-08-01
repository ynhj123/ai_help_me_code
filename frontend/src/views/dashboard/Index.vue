<template>
  <PageContainer title="仪表盘" subTitle="系统概览">
    <div class="dashboard">
      <!-- 统计卡片 -->
      <a-row :gutter="16" class="stats-cards">
        <a-col :span="6">
          <a-card class="stats-card">
            <a-skeleton v-if="loading" active />
            <div v-else class="stats-content">
              <div class="stats-icon" style="background-color: #1890ff">
                <UserOutlined />
              </div>
              <div class="stats-info">
                <div class="stats-value">{{ stats.dau }}</div>
                <div class="stats-label">日活跃用户</div>
              </div>
            </div>
          </a-card>
        </a-col>
        
        <a-col :span="6">
          <a-card class="stats-card">
            <a-skeleton v-if="loading" active />
            <div v-else class="stats-content">
              <div class="stats-icon" style="background-color: #52c41a">
                <ShoppingCartOutlined />
              </div>
              <div class="stats-info">
                <div class="stats-value">{{ stats.orderCount }}</div>
                <div class="stats-label">订单数</div>
              </div>
            </div>
          </a-card>
        </a-col>
        
        <a-col :span="6">
          <a-card class="stats-card">
            <a-skeleton v-if="loading" active />
            <div v-else class="stats-content">
              <div class="stats-icon" style="background-color: #faad14">
                <DollarOutlined />
              </div>
              <div class="stats-info">
                <div class="stats-value">¥{{ stats.orderAmount }}</div>
                <div class="stats-label">订单金额</div>
              </div>
            </div>
          </a-card>
        </a-col>
        
        <a-col :span="6">
          <a-card class="stats-card">
            <a-skeleton v-if="loading" active />
            <div v-else class="stats-content">
              <div class="stats-icon" style="background-color: #722ed1">
                <BarChartOutlined />
              </div>
              <div class="stats-info">
                <div class="stats-value">{{ stats.paidOrderCount }}</div>
                <div class="stats-label">已支付订单</div>
              </div>
            </div>
          </a-card>
        </a-col>
      </a-row>
      
      <!-- 图表区域 -->
      <a-row :gutter="16" class="charts">
        <a-col :span="12">
          <a-card title="销售趋势" class="chart-card">
            <div ref="salesChartRef" class="chart-container"></div>
          </a-card>
        </a-col>
        
        <a-col :span="12">
          <a-card title="用户活跃度" class="chart-card">
            <div ref="userActivityChartRef" class="chart-container"></div>
          </a-card>
        </a-col>
      </a-row>
    </div>
  </PageContainer>
</template>

<script setup lang="ts">
import { ref, onMounted, onBeforeUnmount } from 'vue'
import * as echarts from 'echarts'
import {
  UserOutlined,
  ShoppingCartOutlined,
  DollarOutlined,
  BarChartOutlined
} from '@ant-design/icons-vue'
import PageContainer from '@/components/common/PageContainer.vue'

// 加载状态
const loading = ref(false)

// 统计数据
const stats = ref({
  dau: 0,
  orderCount: 0,
  orderAmount: 0,
  paidOrderCount: 0,
  paidOrderAmount: 0
})

// 图表引用
const salesChartRef = ref<HTMLDivElement | null>(null)
const userActivityChartRef = ref<HTMLDivElement | null>(null)

// 销售图表实例
let salesChart: echarts.ECharts | null = null

// 用户活跃度图表实例
let userActivityChart: echarts.ECharts | null = null

// 获取统计数据
const getStats = async () => {
  try {
    loading.value = true
    
    // 模拟数据
    stats.value = {
      dau: 1234,
      orderCount: 567,
      orderAmount: 89012.34,
      paidOrderCount: 456,
      paidOrderAmount: 78901.23
    }
  } catch (error) {
    console.error('获取统计数据失败:', error)
  } finally {
    loading.value = false
  }
}

// 初始化销售图表
const initSalesChart = () => {
  if (salesChartRef.value) {
    salesChart = echarts.init(salesChartRef.value)
    
    // 模拟数据
    const option = {
      tooltip: {
        trigger: 'axis'
      },
      xAxis: {
        type: 'category',
        data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
      },
      yAxis: {
        type: 'value'
      },
      series: [
        {
          data: [120, 200, 150, 80, 70, 110, 130],
          type: 'line',
          smooth: true
        }
      ]
    }
    
    salesChart.setOption(option)
  }
}

// 初始化用户活跃度图表
const initUserActivityChart = () => {
  if (userActivityChartRef.value) {
    userActivityChart = echarts.init(userActivityChartRef.value)
    
    // 模拟数据
    const option = {
      tooltip: {
        trigger: 'axis'
      },
      xAxis: {
        type: 'category',
        data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
      },
      yAxis: {
        type: 'value'
      },
      series: [
        {
          data: [1200, 1500, 1800, 1600, 1400, 2000, 2200],
          type: 'bar'
        }
      ]
    }
    
    userActivityChart.setOption(option)
  }
}

// 调整图表大小
const resizeCharts = () => {
  salesChart?.resize()
  userActivityChart?.resize()
}

// 组件挂载时初始化
onMounted(() => {
  getStats()
  initSalesChart()
  initUserActivityChart()
  
  // 监听窗口大小变化
  window.addEventListener('resize', resizeCharts)
})

// 组件卸载前清理
onBeforeUnmount(() => {
  // 销毁图表实例
  salesChart?.dispose()
  userActivityChart?.dispose()
  
  // 移除事件监听
  window.removeEventListener('resize', resizeCharts)
})
</script>

<style scoped>
.dashboard {
  background: #fff;
  padding: 24px;
  border-radius: 8px;
}

.stats-cards {
  margin-bottom: 24px;
}

.stats-card {
  height: 120px;
}

.stats-content {
  display: flex;
  align-items: center;
  height: 100%;
}

.stats-icon {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  color: #fff;
  margin-right: 16px;
}

.stats-info {
  flex: 1;
}

.stats-value {
  font-size: 24px;
  font-weight: 600;
  color: #333;
  margin-bottom: 4px;
}

.stats-label {
  font-size: 14px;
  color: #999;
}

.charts {
  margin-top: 24px;
}

.chart-card {
  height: 400px;
}

.chart-container {
  width: 100%;
  height: 320px;
}
</style>