// 统计数据接口
export interface Statistics {
  id: number
  statDate: string
  dau: number
  orderCount: number
  orderAmount: number
  paidOrderCount: number
  paidOrderAmount: number
  createdAt: string
  updatedAt: string
}

// 销售图表数据接口
export interface SalesChartItem {
  date: string
  orderCount: number
  orderAmount: number
  paidOrderCount: number
  paidOrderAmount: number
}

// 用户活跃度数据接口
export interface UserActivityItem {
  date: string
  dau: number
}