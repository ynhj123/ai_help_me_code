// 购物车项接口
export interface CartItem {
  id: number
  userId: number
  productId: number
  quantity: number
  createdAt: string
  updatedAt: string
}

// 购物车商品详情接口
export interface CartItemDetail {
  id: number
  userId: number
  productId: number
  productName: string
  productImage?: string
  productPrice: number
  quantity: number
  subtotal: number
  createdAt: string
  updatedAt: string
}