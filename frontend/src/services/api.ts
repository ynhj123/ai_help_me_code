import request from '@/utils/request'
import type {
  LoginRequest,
  RegisterRequest,
  User,
  UserForm
} from '@/types/user'
import type { Result } from '@/types/common'
import type {
  Product,
  ProductForm,
  ProductQueryRequest,
  ProductInventory
} from '@/types/product'
import type {
  Category,
  CategoryForm,
  CategoryTreeNode,
  CategoryTreeNodeDTO
} from '@/types/category'
import type {
  Order,
  OrderForm,
  OrderQueryRequest,
  OrderItem,
  Payment
} from '@/types/order'

// 认证相关API
export const authApi = {
  /**
   * 用户登录
   * @param data 登录请求数据
   * @returns JWT token
   */
  login: (data: LoginRequest) => {
    return request.post<Result<string>>('/auth/login', data)
  },

  /**
   * 用户注册
   * @param data 注册请求数据
   * @returns 注册结果
   */
  register: (data: RegisterRequest) => {
    return request.post<Result<void>>('/auth/register', data)
  },

  /**
   * 用户登出
   * @returns 登出结果
   */
  logout: () => {
    return request.post<Result<void>>('/auth/logout')
  }
}

// 用户相关API
export const userApi = {
  /**
   * 获取当前用户信息
   * @returns 用户信息
   */
  getProfile: () => {
    return request.get<Result<User>>('/users/profile')
  },

  /**
   * 获取用户列表
   * @param params 查询参数
   * @returns 用户列表
   */
  getUserList: (params?: any) => {
    return request.get<Result<User[]>>('/users', { params })
  },

  /**
   * 获取用户详情
   * @param id 用户ID
   * @returns 用户详情
   */
  getUser: (id: number) => {
    return request.get<Result<User>>(`/users/${id}`)
  },

  /**
   * 创建用户
   * @param data 用户数据
   * @returns 创建的用户
   */
  createUser: (data: UserForm) => {
    return request.post<Result<User>>('/users', data)
  },

  /**
   * 更新用户
   * @param id 用户ID
   * @param data 用户数据
   * @returns 更新后的用户
   */
  updateUser: (id: number, data: UserForm) => {
    return request.put<Result<User>>(`/users/${id}`, data)
  },

  /**
   * 删除用户
   * @param id 用户ID
   * @returns 删除结果
   */
  deleteUser: (id: number) => {
    return request.delete<Result<void>>(`/users/${id}`)
  }
}

// 商品相关API
export const productApi = {
  /**
   * 获取商品列表
   * @param params 查询参数
   * @returns 商品列表
   */
  getProductList: (params?: ProductQueryRequest) => {
    return request.get<Result<Product[]>>('/products', { params })
  },

  /**
   * 获取商品总数
   * @param params 查询参数
   * @returns 商品总数
   */
  getProductCount: (params?: ProductQueryRequest) => {
    return request.get<Result<number>>('/products/count', { params })
  },

  /**
   * 获取商品详情
   * @param id 商品ID
   * @returns 商品详情
   */
  getProduct: (id: number) => {
    return request.get<Result<Product>>(`/products/${id}`)
  },

  /**
   * 创建商品
   * @param data 商品数据
   * @returns 创建的商品
   */
  createProduct: (data: ProductForm) => {
    return request.post<Result<Product>>('/products', data)
  },

  /**
   * 更新商品
   * @param id 商品ID
   * @param data 商品数据
   * @returns 更新后的商品
   */
  updateProduct: (id: number, data: ProductForm) => {
    return request.put<Result<Product>>(`/products/${id}`, data)
  },

  /**
   * 删除商品
   * @param id 商品ID
   * @returns 删除结果
   */
  deleteProduct: (id: number) => {
    return request.delete<Result<void>>(`/products/${id}`)
  },

  /**
   * 上架商品
   * @param id 商品ID
   * @returns 更新后的商品
   */
  enableProduct: (id: number) => {
    return request.put<Result<Product>>(`/products/${id}/enable`)
  },

  /**
   * 下架商品
   * @param id 商品ID
   * @returns 更新后的商品
   */
  disableProduct: (id: number) => {
    return request.put<Result<Product>>(`/products/${id}/disable`)
  },

  /**
   * 获取商品库存信息
   * @param id 商品ID
   * @returns 库存信息
   */
  getProductInventory: (id: number) => {
    return request.get<Result<ProductInventory>>(`/products/${id}/inventory`)
  },

  /**
   * 更新商品库存
   * @param id 商品ID
   * @param quantity 库存数量
   * @returns 更新后的库存信息
   */
  updateProductInventory: (id: number, quantity: number) => {
    return request.put<Result<ProductInventory>>(`/products/${id}/inventory`, null, {
      params: { quantity }
    })
  }
}

// 分类相关API
export const categoryApi = {
  /**
   * 获取分类列表
   * @param params 查询参数
   * @returns 分类列表
   */
  getCategoryList: (params?: any) => {
    return request.get<Result<Category[]>>(`/categories`, { params })
  },

  /**
   * 获取分类总数
   * @returns 分类总数
   */
  getCategoryCount: () => {
    return request.get<Result<number>>('/categories/count')
  },

  /**
   * 获取分类详情
   * @param id 分类ID
   * @returns 分类详情
   */
  getCategory: (id: number) => {
    return request.get<Result<Category>>(`/categories/${id}`)
  },

  /**
   * 创建分类
   * @param data 分类数据
   * @returns 创建的分类
   */
  createCategory: (data: CategoryForm) => {
    return request.post<Result<Category>>('/categories', data)
  },

  /**
   * 更新分类
   * @param id 分类ID
   * @param data 分类数据
   * @returns 更新后的分类
   */
  updateCategory: (id: number, data: CategoryForm) => {
    return request.put<Result<Category>>(`/categories/${id}`, data)
  },

  /**
   * 删除分类
   * @param id 分类ID
   * @returns 删除结果
   */
  deleteCategory: (id: number) => {
    return request.delete<Result<void>>(`/categories/${id}`)
  },

  /**
   * 启用分类
   * @param id 分类ID
   * @returns 更新后的分类
   */
  enableCategory: (id: number) => {
    return request.put<Result<Category>>(`/categories/${id}/enable`)
  },

  /**
   * 禁用分类
   * @param id 分类ID
   * @returns 更新后的分类
   */
  disableCategory: (id: number) => {
    return request.put<Result<Category>>(`/categories/${id}/disable`)
  },

  /**
   * 获取分类树
   * @returns 分类树
   */
  getCategoryTree: () => {
    return request.get<Result<CategoryTreeNodeDTO[]>>('/categories/tree')
  },

  /**
   * 移动分类
   * @param id 分类ID
   * @param newParentId 新的父级分类ID
   * @returns 移动后的分类
   */
  moveCategory: (id: number, newParentId?: number) => {
    return request.put<Result<Category>>(`/categories/${id}/move`, null, {
      params: { newParentId }
    })
  },

  /**
   * 更新分类排序
   * @param id 分类ID
   * @param sortOrder 排序值
   * @returns 更新后的分类
   */
  updateCategorySortOrder: (id: number, sortOrder: number) => {
    return request.put<Result<Category>>(`/categories/${id}/sort-order`, null, {
      params: { sortOrder }
    })
  }
}

// 订单相关API
export const orderApi = {
  /**
   * 获取订单列表
   * @param params 查询参数
   * @returns 订单列表
   */
  getOrderList: (params?: any) => {
    return request.get<Result<Order[]>>(`/orders`, { params })
  },

  /**
   * 条件查询订单列表
   * @param params 查询参数
   * @returns 订单列表
   */
  searchOrders: (params: OrderQueryRequest) => {
    return request.get<Result<Order[]>>(('/orders/search'), { params })
  },

  /**
   * 获取订单总数
   * @param params 查询参数
   * @returns 订单总数
   */
  getOrderCount: (params?: OrderQueryRequest) => {
    return request.get<Result<number>>('/orders/count', { params })
  },

  /**
   * 获取订单详情
   * @param id 订单ID
   * @returns 订单详情
   */
  getOrder: (id: number) => {
    return request.get<Result<Order>>(`/orders/${id}`)
  },

  /**
   * 创建订单
   * @param data 订单数据
   * @returns 创建的订单
   */
  createOrder: (data: OrderForm) => {
    return request.post<Result<Order>>('/orders', data)
  },

  /**
   * 更新订单
   * @param id 订单ID
   * @param data 订单数据
   * @returns 更新后的订单
   */
  updateOrder: (id: number, data: OrderForm) => {
    return request.put<Result<Order>>(`/orders/${id}`, data)
  },

  /**
   * 删除订单
   * @param id 订单ID
   * @returns 删除结果
   */
  deleteOrder: (id: number) => {
    return request.delete<Result<void>>(`/orders/${id}`)
  },

  /**
   * 更新订单状态
   * @param id 订单ID
   * @param status 订单状态
   * @returns 更新后的订单
   */
  updateOrderStatus: (id: number, status: number) => {
    return request.put<Result<Order>>(`/orders/${id}/status`, null, {
      params: { status }
    })
  },

  /**
   * 订单发货
   * @param id 订单ID
   * @param logisticsCompany 物流公司
   * @param trackingNumber 物流单号
   * @returns 更新后的订单
   */
  shipOrder: (id: number, logisticsCompany: string, trackingNumber: string) => {
    return request.put<Result<Order>>(`/orders/${id}/ship`, null, {
      params: { logisticsCompany, trackingNumber }
    })
  },

  /**
   * 取消订单
   * @param id 订单ID
   * @param cancelReason 取消原因
   * @returns 更新后的订单
   */
  cancelOrder: (id: number, cancelReason?: string) => {
    return request.put<Result<Order>>(`/orders/${id}/cancel`, null, {
      params: { cancelReason }
    })
  },

  /**
   * 确认收货
   * @param id 订单ID
   * @returns 更新后的订单
   */
  confirmOrder: (id: number) => {
    return request.put<Result<Order>>(`/orders/${id}/confirm`)
  }
}