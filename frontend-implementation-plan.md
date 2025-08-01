# 前端管理页面实施计划

## 项目概述
根据后端提供的controller API接口，实现前端管理页面的Vue组件，包含常见的增删改查功能。

## 后端API接口分析

### 1. 商品管理 API
- **路径**: `/api/products`
- **主要功能**:
  - `GET /` - 获取商品列表（支持查询条件）
  - `GET /count` - 获取商品总数
  - `GET /{id}` - 获取商品详情
  - `POST /` - 创建商品
  - `PUT /{id}` - 更新商品
  - `DELETE /{id}` - 删除商品
  - `PUT /{id}/enable` - 上架商品
  - `PUT /{id}/disable` - 下架商品
  - `GET /{id}/inventory` - 获取库存信息
  - `PUT /{id}/inventory` - 更新库存
  - `PUT /{id}/inventory/increase` - 增加库存
  - `PUT /{id}/inventory/decrease` - 减少库存

### 2. 分类管理 API
- **路径**: `/api/categories`
- **主要功能**:
  - `GET /` - 获取分类列表（分页）
  - `GET /count` - 获取分类总数
  - `GET /{id}` - 获取分类详情
  - `POST /` - 创建分类
  - `PUT /{id}` - 更新分类
  - `DELETE /{id}` - 删除分类
  - `PUT /{id}/enable` - 启用分类
  - `PUT /{id}/disable` - 禁用分类
  - `GET /tree` - 获取分类树
  - `PUT /{id}/move` - 移动分类
  - `PUT /{id}/sort-order` - 更新排序

### 3. 订单管理 API
- **路径**: `/api/orders`
- **主要功能**:
  - `GET /` - 获取订单列表（分页）
  - `GET /search` - 条件查询订单
  - `GET /count` - 获取订单总数
  - `GET /{id}` - 获取订单详情
  - `PUT /{id}` - 更新订单
  - `DELETE /{id}` - 删除订单
  - `PUT /{id}/status` - 更新订单状态
  - `PUT /{id}/ship` - 订单发货
  - `PUT /{id}/cancel` - 取消订单
  - `PUT /{id}/confirm` - 确认收货

## 前端实施计划

### 第一步：更新API服务接口
需要扩展 `frontend/src/services/api.ts` 文件，添加以下API模块：

#### 1. 商品管理API
```typescript
export const productApi = {
  // 获取商品列表
  getProductList: (params?: ProductQueryRequest) => {
    return request.get<Result<ProductDTO[]>>('/products', { params })
  },
  
  // 获取商品总数
  getProductCount: (params?: ProductQueryRequest) => {
    return request.get<Result<number>>('/products/count', { params })
  },
  
  // 获取商品详情
  getProduct: (id: number) => {
    return request.get<Result<ProductDTO>>(`/products/${id}`)
  },
  
  // 创建商品
  createProduct: (data: ProductForm) => {
    return request.post<Result<ProductDTO>>('/products', data)
  },
  
  // 更新商品
  updateProduct: (id: number, data: ProductForm) => {
    return request.put<Result<ProductDTO>>(`/products/${id}`, data)
  },
  
  // 删除商品
  deleteProduct: (id: number) => {
    return request.delete<Result<void>>(`/products/${id}`)
  },
  
  // 上架商品
  enableProduct: (id: number) => {
    return request.put<Result<ProductDTO>>(`/products/${id}/enable`)
  },
  
  // 下架商品
  disableProduct: (id: number) => {
    return request.put<Result<ProductDTO>>(`/products/${id}/disable`)
  },
  
  // 获取库存信息
  getProductInventory: (id: number) => {
    return request.get<Result<ProductInventory>>(`/products/${id}/inventory`)
  },
  
  // 更新库存
  updateProductInventory: (id: number, quantity: number) => {
    return request.put<Result<ProductInventory>>(`/products/${id}/inventory`, null, {
      params: { quantity }
    })
  }
}
```

#### 2. 分类管理API
```typescript
export const categoryApi = {
  // 获取分类列表
  getCategoryList: (page?: number, size?: number) => {
    return request.get<Result<CategoryDTO[]>>('/categories', {
      params: { page, size }
    })
  },
  
  // 获取分类总数
  getCategoryCount: () => {
    return request.get<Result<number>>('/categories/count')
  },
  
  // 获取分类详情
  getCategory: (id: number) => {
    return request.get<Result<CategoryDTO>>(`/categories/${id}`)
  },
  
  // 创建分类
  createCategory: (data: CategoryForm) => {
    return request.post<Result<CategoryDTO>>('/categories', data)
  },
  
  // 更新分类
  updateCategory: (id: number, data: CategoryForm) => {
    return request.put<Result<CategoryDTO>>(`/categories/${id}`, data)
  },
  
  // 删除分类
  deleteCategory: (id: number) => {
    return request.delete<Result<void>>(`/categories/${id}`)
  },
  
  // 启用分类
  enableCategory: (id: number) => {
    return request.put<Result<CategoryDTO>>(`/categories/${id}/enable`)
  },
  
  // 禁用分类
  disableCategory: (id: number) => {
    return request.put<Result<CategoryDTO>>(`/categories/${id}/disable`)
  },
  
  // 获取分类树
  getCategoryTree: () => {
    return request.get<Result<CategoryTreeNodeDTO[]>>('/categories/tree')
  },
  
  // 移动分类
  moveCategory: (id: number, newParentId?: number) => {
    return request.put<Result<CategoryDTO>>(`/categories/${id}/move`, null, {
      params: { newParentId }
    })
  },
  
  // 更新排序
  updateCategorySortOrder: (id: number, sortOrder: number) => {
    return request.put<Result<CategoryDTO>>(`/categories/${id}/sort-order`, null, {
      params: { sortOrder }
    })
  }
}
```

#### 3. 订单管理API
```typescript
export const orderApi = {
  // 获取订单列表
  getOrderList: (page?: number, size?: number) => {
    return request.get<Result<OrderDTO[]>>('/orders', {
      params: { page, size }
    })
  },
  
  // 条件查询订单
  searchOrders: (params: OrderQueryRequest) => {
    return request.get<Result<OrderDTO[]>>('/orders/search', { params })
  },
  
  // 获取订单总数
  getOrderCount: (params?: OrderQueryRequest) => {
    return request.get<Result<number>>('/orders/count', { params })
  },
  
  // 获取订单详情
  getOrder: (id: number) => {
    return request.get<Result<OrderDTO>>(`/orders/${id}`)
  },
  
  // 创建订单
  createOrder: (data: OrderForm) => {
    return request.post<Result<OrderDTO>>('/orders', data)
  },
  
  // 更新订单
  updateOrder: (id: number, data: OrderForm) => {
    return request.put<Result<OrderDTO>>(`/orders/${id}`, data)
  },
  
  // 删除订单
  deleteOrder: (id: number) => {
    return request.delete<Result<void>>(`/orders/${id}`)
  },
  
  // 更新订单状态
  updateOrderStatus: (id: number, status: number) => {
    return request.put<Result<OrderDTO>>(`/orders/${id}/status`, null, {
      params: { status }
    })
  },
  
  // 订单发货
  shipOrder: (id: number, logisticsCompany: string, trackingNumber: string) => {
    return request.put<Result<OrderDTO>>(`/orders/${id}/ship`, null, {
      params: { logisticsCompany, trackingNumber }
    })
  },
  
  // 取消订单
  cancelOrder: (id: number, cancelReason?: string) => {
    return request.put<Result<OrderDTO>>(`/orders/${id}/cancel`, null, {
      params: { cancelReason }
    })
  },
  
  // 确认收货
  confirmOrder: (id: number) => {
    return request.put<Result<OrderDTO>>(`/orders/${id}/confirm`)
  }
}
```

### 第二步：创建类型定义文件
需要创建或更新以下类型定义文件：

#### 1. 商品相关类型
```typescript
// frontend/src/types/product.ts
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

export interface ProductInventory {
  id: number
  productId: number
  quantity: number
  reservedQuantity: number
  createdAt: string
  updatedAt: string
}
```

#### 2. 分类相关类型
```typescript
// frontend/src/types/category.ts
export interface Category {
  id: number
  name: string
  description?: string
  parentId: number
  sortOrder: number
  status: number
  createdAt: string
  updatedAt: string
}

export interface CategoryForm {
  name: string
  description?: string
  parentId: number
  sortOrder: number
  status: number
}

export interface CategoryTreeNode {
  id: number
  name: string
  children?: CategoryTreeNode[]
}

export interface CategoryTreeNodeDTO {
  id: number
  name: string
  description?: string
  parentId: number
  sortOrder: number
  status: number
  children?: CategoryTreeNodeDTO[]
}
```

#### 3. 订单相关类型
```typescript
// frontend/src/types/order.ts
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

export interface OrderForm {
  userId: number
  items: OrderItemForm[]
  shippingAddress?: string
  receiverName?: string
  receiverPhone?: string
  remark?: string
}

export interface OrderItemForm {
  productId: number
  quantity: number
  price: number
}

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
```

### 第三步：创建管理页面

#### 1. 商品管理页面
创建 `frontend/src/views/product/List.vue`：
- 商品列表展示（支持搜索、筛选、分页）
- 商品新增/编辑表单
- 商品删除功能
- 商品上下架管理
- 库存管理功能

#### 2. 商品详情页面
创建 `frontend/src/views/product/Detail.vue`：
- 商品详细信息展示
- 库存信息展示
- 商品图片画廊
- 商品操作按钮

#### 3. 分类管理页面
创建 `frontend/src/views/category/List.vue`：
- 分类树形列表展示
- 分类新增/编辑表单
- 分类删除功能
- 分类排序管理
- 分类移动功能

#### 4. 分类详情页面
创建 `frontend/src/views/category/Detail.vue`：
- 分类详细信息展示
- 子分类列表
- 分类操作按钮

#### 5. 订单管理页面
创建 `frontend/src/views/order/List.vue`：
- 订单列表展示（支持多条件搜索）
- 订单详情查看
- 订单状态更新
- 订单发货功能
- 订单取消功能

#### 6. 订单详情页面
创建 `frontend/src/views/order/Detail.vue`：
- 订单详细信息展示
- 订单项列表
- 收货信息
- 物流信息
- 订单操作按钮

### 第四步：更新路由配置
更新 `frontend/src/router/index.ts`，添加新的管理页面路由：
```typescript
{
  path: '/products',
  name: 'ProductList',
  component: () => import('@/views/product/List.vue'),
  meta: {
    title: '商品管理',
    requiresAuth: true,
    permissions: ['ADMIN']
  }
},
{
  path: '/products/detail/:id',
  name: 'ProductDetail',
  component: () => import('@/views/product/Detail.vue'),
  meta: {
    title: '商品详情',
    requiresAuth: true,
    permissions: ['ADMIN']
  }
},
{
  path: '/categories',
  name: 'CategoryList',
  component: () => import('@/views/category/List.vue'),
  meta: {
    title: '分类管理',
    requiresAuth: true,
    permissions: ['ADMIN']
  }
},
{
  path: '/categories/detail/:id',
  name: 'CategoryDetail',
  component: () => import('@/views/category/Detail.vue'),
  meta: {
    title: '分类详情',
    requiresAuth: true,
    permissions: ['ADMIN']
  }
},
{
  path: '/orders',
  name: 'OrderList',
  component: () => import('@/views/order/List.vue'),
  meta: {
    title: '订单管理',
    requiresAuth: true,
    permissions: ['ADMIN']
  }
},
{
  path: '/orders/detail/:id',
  name: 'OrderDetail',
  component: () => import('@/views/order/Detail.vue'),
  meta: {
    title: '订单详情',
    requiresAuth: true,
    permissions: ['ADMIN']
  }
}
```

### 第五步：更新侧边栏菜单
更新 `frontend/src/components/layout/Sidebar.vue`，添加新的菜单项：
```typescript
{
  key: 'product-management',
  title: '商品管理',
  icon: ShoppingCartOutlined,
  children: [
    {
      key: '/products',
      title: '商品列表',
      icon: ShoppingCartOutlined,
      path: '/products'
    },
    {
      key: '/categories',
      title: '商品分类',
      icon: FileOutlined,
      path: '/categories'
    }
  ]
},
{
  key: 'order-management',
  title: '订单管理',
  icon: FileOutlined,
  children: [
    {
      key: '/orders',
      title: '订单列表',
      icon: FileOutlined,
      path: '/orders'
    }
  ]
}
```

### 第六步：测试功能
完成所有页面开发后，进行功能测试：
1. 商品管理功能测试
2. 分类管理功能测试
3. 订单管理功能测试
4. 权限控制测试
5. 响应式设计测试

## 技术要点

### 组件复用
- 使用现有的 `CommonTable` 组件
- 复用用户管理页面的表单验证逻辑
- 统一的页面布局和样式

### 类型安全
- 完整的 TypeScript 类型定义
- 前后端数据类型一致性
- 严格的类型检查

### 权限控制
- 基于角色的访问控制
- 管理员权限验证
- 路由守卫保护

### 用户体验
- 加载状态管理
- 错误处理机制
- 操作确认对话框
- 成功/失败提示

## 预期成果

完成后的系统将具备完整的电商管理后台功能：
- **商品管理**：完整的商品生命周期管理
- **分类管理**：灵活的分类树结构管理
- **订单管理**：高效的订单处理流程
- **用户管理**：完善的用户权限管理

所有功能都将遵循现有的代码规范和设计模式，确保系统的一致性和可维护性。