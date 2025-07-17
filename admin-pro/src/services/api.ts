import { request } from '@umijs/max';
import type { 
  ApiResponse, 
  PaginatedResponse, 
  User, 
  Product, 
  Order, 
  Shipment,
  LoginRequest,
  LoginResponse,
  PaginationParams 
} from '@/types/api';

// 认证相关API
export async function login(params: LoginRequest) {
  return request<ApiResponse<LoginResponse>>('/auth/login', {
    method: 'POST',
    data: params,
  });
}

export async function logout() {
  return request<ApiResponse<void>>('/auth/logout', {
    method: 'POST',
  });
}

export async function getCurrentUser() {
  return request<ApiResponse<User>>('/auth/profile');
}

// 用户管理API
export async function getUsers(params?: PaginationParams) {
  return request<ApiResponse<PaginatedResponse<User>>>('/users', {
    params,
  });
}

export async function getUser(id: number) {
  return request<ApiResponse<User>>(`/users/${id}`);
}

export async function createUser(data: Partial<User>) {
  return request<ApiResponse<User>>('/users', {
    method: 'POST',
    data,
  });
}

export async function updateUser(id: number, data: Partial<User>) {
  return request<ApiResponse<User>>(`/users/${id}`, {
    method: 'PUT',
    data,
  });
}

export async function deleteUser(id: number) {
  return request<ApiResponse<void>>(`/users/${id}`, {
    method: 'DELETE',
  });
}

// 商品管理API
export async function getProducts(params?: PaginationParams) {
  return request<ApiResponse<PaginatedResponse<Product>>>('/products', {
    params,
  });
}

export async function getProduct(id: number) {
  return request<ApiResponse<Product>>(`/products/${id}`);
}

export async function createProduct(data: Partial<Product>) {
  return request<ApiResponse<Product>>('/products', {
    method: 'POST',
    data,
  });
}

export async function updateProduct(id: number, data: Partial<Product>) {
  return request<ApiResponse<Product>>(`/products/${id}`, {
    method: 'PUT',
    data,
  });
}

export async function deleteProduct(id: number) {
  return request<ApiResponse<void>>(`/products/${id}`, {
    method: 'DELETE',
  });
}

// 订单管理API
export async function getOrders(params?: PaginationParams) {
  return request<ApiResponse<PaginatedResponse<Order>>>('/orders', {
    params,
  });
}

export async function getOrder(id: number) {
  return request<ApiResponse<Order>>(`/orders/${id}`);
}

export async function updateOrderStatus(id: number, status: string) {
  return request<ApiResponse<Order>>(`/orders/${id}/status`, {
    method: 'PUT',
    data: { status },
  });
}

// 物流管理API
export async function getShipments(params?: PaginationParams) {
  return request<ApiResponse<PaginatedResponse<Shipment>>>('/shipments', {
    params,
  });
}

export async function getShipment(id: number) {
  return request<ApiResponse<Shipment>>(`/shipments/${id}`);
}

export async function createShipment(data: Partial<Shipment>) {
  return request<ApiResponse<Shipment>>('/shipments', {
    method: 'POST',
    data,
  });
}

export async function updateShipment(id: number, data: Partial<Shipment>) {
  return request<ApiResponse<Shipment>>(`/shipments/${id}`, {
    method: 'PUT',
    data,
  });
}

// 数据统计API
export async function getDashboardStats() {
  return request<ApiResponse<{
    totalUsers: number;
    totalProducts: number;
    totalOrders: number;
    totalRevenue: number;
    todayOrders: number;
    pendingOrders: number;
  }>>('/dashboard/stats');
}

export async function getOrderChartData() {
  return request<ApiResponse<{
    date: string;
    orders: number;
    revenue: number;
  }[]>>('/dashboard/order-chart');
}

export async function getProductChartData() {
  return request<ApiResponse<{
    name: string;
    sales: number;
  }[]>>('/dashboard/product-chart');
}