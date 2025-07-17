// 商品分类
export interface Category {
  id: number
  name: string
  description?: string
  parentId?: number
  sortOrder: number
  status: 'ACTIVE' | 'INACTIVE'
  children?: Category[]
  createdAt: string
  updatedAt: string
}

// 商品属性
export interface ProductAttribute {
  id: number
  name: string
  value: string
  type: 'TEXT' | 'NUMBER' | 'BOOLEAN' | 'SELECT'
  required: boolean
  sortOrder: number
}

// SKU
export interface Sku {
  id: number
  skuCode: string
  price: number
  stock: number
  attributes: Record<string, string>
  status: 'ACTIVE' | 'INACTIVE'
  images: string[]
}

// 商品图片
export interface ProductImage {
  id: number
  url: string
  alt?: string
  sortOrder: number
  isPrimary: boolean
}

// 商品
export interface Product {
  id: number
  name: string
  description?: string
  category: Category
  skus: Sku[]
  images: ProductImage[]
  attributes: ProductAttribute[]
  status: 'ACTIVE' | 'INACTIVE' | 'DRAFT'
  createdAt: string
  updatedAt: string
}

// 商品创建请求
export interface ProductCreateRequest {
  name: string
  description?: string
  categoryId: number
  attributes: ProductAttribute[]
  images: string[]
}

// 商品更新请求
export interface ProductUpdateRequest {
  name?: string
  description?: string
  categoryId?: number
  attributes?: ProductAttribute[]
  images?: string[]
  status?: 'ACTIVE' | 'INACTIVE' | 'DRAFT'
}