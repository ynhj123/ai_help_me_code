// 商品信息接口
export interface Product {
  id: number
  name: string
  description?: string
  categoryId: number
  price: number
  marketPrice?: number
  costPrice?: number
  sku: string
  barcode?: string
  image?: string
  gallery?: string[]
  detail?: string
  status: number
  createdAt: string
  updatedAt: string
}

// 商品表单接口
export interface ProductForm {
  name: string
  description?: string
  categoryId: number
  price: number
  marketPrice?: number
  costPrice?: number
  sku: string
  barcode?: string
  image?: string
  gallery?: string[]
  detail?: string
  status: number
}

// 商品查询请求接口
export interface ProductQueryRequest {
  name?: string
  categoryId?: number
  status?: number
  minPrice?: number
  maxPrice?: number
  page?: number
  size?: number
  sortBy?: string
  sortOrder?: 'asc' | 'desc'
}

// 商品库存接口
export interface ProductInventory {
  id: number
  productId: number
  quantity: number
  reservedQuantity: number
  createdAt: string
  updatedAt: string
}

// 商品分类树节点接口
export interface CategoryTreeNode {
  id: number
  name: string
  children?: CategoryTreeNode[]
}