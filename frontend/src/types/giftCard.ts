// 购物卡接口
export interface GiftCard {
  id: number
  cardNo: string
  balance: number
  status: number
  expiredAt?: string
  createdAt: string
  updatedAt: string
}

// 购物卡使用记录接口
export interface GiftCardUsage {
  id: number
  cardId: number
  userId: number
  orderId?: number
  amount: number
  type: number
  remark?: string
  createdAt: string
}

// 购物卡状态枚举
export enum GiftCardStatus {
  ACTIVE = 1,     // 有效
  USED = 0,       // 已使用
  EXPIRED = -1    // 已过期
}

// 购物卡使用类型枚举
export enum GiftCardUsageType {
  RECHARGE = 1,   // 充值
  CONSUME = 2     // 消费
}