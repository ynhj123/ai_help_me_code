// 订单信息接口
export interface Order {
  id: number
  orderNo: string
  userId: number
  status: number
  totalAmount: number
  discountAmount: number
  shippingAmount: number
  payAmount: number
  paymentMethod?: number
  paymentTime?: string
  shippingAddress?: string
  receiverName?: string
  receiverPhone?: string
  shippingTime?: string
  completedTime?: string
  remark?: string
  createdAt: string
  updatedAt: string
}

// 订单表单接口
export interface OrderForm {
  userId: number
  items: OrderItemForm[]
  shippingAddress?: string
  receiverName?: string
  receiverPhone?: string
  remark?: string
}

// 订单项表单接口
export interface OrderItemForm {
  productId: number
  quantity: number
  price: number
}

// 订单查询请求接口
export interface OrderQueryRequest {
  orderNo?: string
  userId?: number
  status?: number
  startTime?: string
  endTime?: string
  page?: number
  size?: number
  sortBy?: string
  sortOrder?: 'asc' | 'desc'
}

// 订单项接口
export interface OrderItem {
  id: number
  orderId: number
  productId: number
  productName: string
  productSku?: string
  productImage?: string
  quantity: number
  price: number
  totalAmount: number
  createdAt: string
}

// 支付记录接口
export interface Payment {
  id: number
  orderNo: string
  paymentMethod: number
  amount: number
  status: number
  transactionNo?: string
  paymentTime?: string
  createdAt: string
  updatedAt: string
}

// 订单状态枚举
export enum OrderStatus {
  PENDING_PAYMENT = 1,  // 待付款
  PAID = 2,             // 已付款
  SHIPPED = 3,          // 已发货
  COMPLETED = 4,        // 已完成
  CANCELLED = 5         // 已取消
}

// 支付方式枚举
export enum PaymentMethod {
  UNIONPAY = 1,   // 银联
  WECHAT = 2,     // 微信
  ALIPAY = 3      // 支付宝
}

// 支付状态枚举
export enum PaymentStatus {
  PENDING = 0,    // 待支付
  PAID = 1,       // 已支付
  CANCELLED = 2,  // 已取消
  FAILED = 3      // 支付失败
}