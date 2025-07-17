// 订单状态
export type OrderStatus = 'PENDING' | 'CONFIRMED' | 'SHIPPED' | 'DELIVERED' | 'CANCELLED' | 'REFUNDED'

// 支付状态
export type PaymentStatus = 'PENDING' | 'PAID' | 'FAILED' | 'REFUNDED'

// 支付方式
export type PaymentMethod = 'ALIPAY' | 'WECHAT' | 'UNIONPAY' | 'CREDIT_CARD'

// 订单地址
export interface OrderAddress {
  id: number
  fullName: string
  phone: string
  province: string
  city: string
  district: string
  address: string
  postalCode?: string
}

// 订单项
export interface OrderItem {
  id: number
  productName: string
  skuCode: string
  quantity: number
  price: number
  totalAmount: number
  imageUrl?: string
}

// 订单
export interface Order {
  id: number
  orderNumber: string
  userId: number
  userName: string
  status: OrderStatus
  paymentStatus: PaymentStatus
  paymentMethod: PaymentMethod
  totalAmount: number
  shippingFee: number
  discountAmount: number
  finalAmount: number
  address: OrderAddress
  items: OrderItem[]
  notes?: string
  createdAt: string
  updatedAt: string
}

// 订单创建请求
export interface OrderCreateRequest {
  userId: number
  address: Omit<OrderAddress, 'id'>
  items: Array<{
    productId: number
    skuId: number
    quantity: number
  }>
  paymentMethod: PaymentMethod
  notes?: string
}

// 订单更新请求
export interface OrderUpdateRequest {
  status?: OrderStatus
  paymentStatus?: PaymentStatus
  notes?: string
}