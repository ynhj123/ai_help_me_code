// 物流信息接口
export interface ShippingInfo {
  id: number
  orderId: number
  shippingCompany?: string
  trackingNumber?: string
  status: number
  shippedAt?: string
  deliveredAt?: string
  deliveryPersonnelId?: number
  remark?: string
  createdAt: string
  updatedAt: string
}

// 运输人员接口
export interface DeliveryPersonnel {
  id: number
  userId: number
  name: string
  phone: string
  status: number
  createdAt: string
  updatedAt: string
}

// 物流状态枚举
export enum ShippingStatus {
  PENDING = 1,     // 待发货
  SHIPPED = 2,     // 已发货
  IN_TRANSIT = 3,  // 运输中
  DELIVERED = 4    // 已送达
}