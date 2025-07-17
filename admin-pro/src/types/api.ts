// API响应基础类型
export interface ApiResponse<T = any> {
  code: number;
  message: string;
  data: T;
}

// 分页响应
export interface PaginatedResponse<T> {
  list: T[];
  total: number;
  page: number;
  pageSize: number;
}

// 用户相关类型
export interface User {
  id: number;
  username: string;
  email: string;
  phone?: string;
  avatar?: string;
  role: 'admin' | 'user';
  status: 'active' | 'inactive';
  createdAt: string;
  updatedAt: string;
}

// 商品相关类型
export interface Product {
  id: number;
  name: string;
  description?: string;
  price: number;
  stock: number;
  category: string;
  images: string[];
  status: 'active' | 'inactive';
  createdAt: string;
  updatedAt: string;
}

// 订单相关类型
export interface Order {
  id: number;
  orderNo: string;
  userId: number;
  user: User;
  items: OrderItem[];
  totalAmount: number;
  status: 'pending' | 'paid' | 'shipped' | 'delivered' | 'cancelled';
  shippingAddress: Address;
  createdAt: string;
  updatedAt: string;
}

export interface OrderItem {
  id: number;
  productId: number;
  product: Product;
  quantity: number;
  price: number;
}

// 地址类型
export interface Address {
  id: number;
  name: string;
  phone: string;
  province: string;
  city: string;
  district: string;
  address: string;
  zipCode?: string;
}

// 物流相关类型
export interface Shipment {
  id: number;
  orderId: number;
  order: Order;
  trackingNo: string;
  carrier: string;
  status: 'pending' | 'in_transit' | 'delivered' | 'failed';
  estimatedDelivery?: string;
  actualDelivery?: string;
  createdAt: string;
  updatedAt: string;
}

// 登录相关
export interface LoginRequest {
  username: string;
  password: string;
}

export interface LoginResponse {
  token: string;
  user: User;
}

// 分页参数
export interface PaginationParams {
  page?: number;
  pageSize?: number;
  keyword?: string;
  sort?: string;
  order?: 'asc' | 'desc';
}