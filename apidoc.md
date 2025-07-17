# 管理后台系统 API 文档

## 认证授权模块

### 用户注册
**POST** `/api/auth/signup`

**请求体:**
```json
{
  "username": "string",
  "email": "user@example.com",
  "password": "string",
  "role": ["user", "admin", "moderator"]
}
```

**响应:**
```json
{
  "message": "用户注册成功！"
}
```

### 用户登录
**POST** `/api/auth/signin`

**请求体:**
```json
{
  "username": "string",
  "password": "string"
}
```

**响应:**
```json
{
  "accessToken": "jwt-token-string",
  "tokenType": "Bearer",
  "expiresIn": 86400000
}
```

### 获取当前用户信息
**GET** `/api/users/me`

**Headers:**
```
Authorization: Bearer {jwt-token}
```

**响应:**
```json
{
  "id": 1,
  "username": "admin",
  "email": "admin@example.com",
  "roles": ["ROLE_ADMIN"],
  "createdAt": "2024-01-01T00:00:00Z"
}
```

### 更新用户信息
**PUT** `/api/users/{id}`

**Headers:**
```
Authorization: Bearer {jwt-token}
```

**请求体:**
```json
{
  "username": "string",
  "email": "user@example.com",
  "roles": [1, 2]
}
```

**响应:**
```json
{
  "id": 1,
  "username": "updated-username",
  "email": "updated@example.com",
  "roles": ["ROLE_USER"],
  "updatedAt": "2024-01-01T12:00:00Z"
}
```

### 删除用户
**DELETE** `/api/users/{id}`

**Headers:**
```
Authorization: Bearer {jwt-token}
```

**响应:**
```
204 No Content
```

### 获取用户列表
**GET** `/api/users`

**Headers:**
```
Authorization: Bearer {jwt-token}
```

**查询参数:**
- `page` (默认: 0)
- `size` (默认: 20)
- `sort` (默认: id,desc)

**响应:**
```json
{
  "content": [
    {
      "id": 1,
      "username": "admin",
      "email": "admin@example.com",
      "roles": ["ROLE_ADMIN"],
      "createdAt": "2024-01-01T00:00:00Z"
    }
  ],
  "totalElements": 100,
  "totalPages": 5,
  "number": 0,
  "size": 20
}
```

## 商品中心模块

### 商品分类管理

#### 获取分类树
**GET** `/api/categories/tree`

**响应:**
```json
[
  {
    "id": 1,
    "name": "电子产品",
    "description": "各类电子产品",
    "imageUrl": "https://example.com/image.jpg",
    "sortOrder": 1,
    "status": "ACTIVE",
    "children": [
      {
        "id": 2,
        "name": "手机",
        "parentId": 1,
        "children": []
      }
    ]
  }
]
```

#### 创建分类
**POST** `/api/categories`

**Headers:**
```
Authorization: Bearer {jwt-token}
```

**请求体:**
```json
{
  "name": "string",
  "description": "string",
  "imageUrl": "string",
  "sortOrder": 1,
  "status": "ACTIVE",
  "parentId": null
}
```

**响应:**
```json
{
  "id": 1,
  "name": "string",
  "description": "string",
  "imageUrl": "string",
  "sortOrder": 1,
  "status": "ACTIVE",
  "parentId": null
}
```

#### 更新分类
**PUT** `/api/categories/{id}`

**Headers:**
```
Authorization: Bearer {jwt-token}
```

**请求体:** 同创建分类

#### 删除分类
**DELETE** `/api/categories/{id}`

**Headers:**
```
Authorization: Bearer {jwt-token}
```

### 商品管理

#### 获取商品列表
**GET** `/api/products`

**查询参数:**
- `page` (默认: 0)
- `size` (默认: 20)
- `categoryId` (可选)
- `status` (可选: DRAFT, ACTIVE, INACTIVE)
- `search` (可选: 按名称搜索)

**响应:**
```json
{
  "content": [
    {
      "id": 1,
      "name": "iPhone 15",
      "code": "IPHONE15",
      "category": {
        "id": 2,
        "name": "手机"
      },
      "brand": "Apple",
      "description": "最新款iPhone",
      "status": "ACTIVE",
      "skus": [...],
      "images": [...]
    }
  ],
  "totalElements": 50,
  "totalPages": 3
}
```

#### 创建商品
**POST** `/api/products`

**Headers:**
```
Authorization: Bearer {jwt-token}
Content-Type: multipart/form-data
```

**请求体:**
```json
{
  "name": "string",
  "code": "string",
  "categoryId": 1,
  "brand": "string",
  "description": "string",
  "richContent": "HTML内容",
  "status": "DRAFT"
}
```

#### 获取商品详情
**GET** `/api/products/{id}`

**响应:**
```json
{
  "id": 1,
  "name": "iPhone 15",
  "code": "IPHONE15",
  "category": {...},
  "brand": "Apple",
  "description": "最新款iPhone",
  "richContent": "<p>详细描述</p>",
  "status": "ACTIVE",
  "skus": [
    {
      "id": 1,
      "skuCode": "IPHONE15-128GB-BLACK",
      "price": 5999.00,
      "stock": 100,
      "warningStock": 10,
      "specifications": {"color": "黑色", "storage": "128GB"}
    }
  ],
  "images": [
    {
      "id": 1,
      "url": "https://example.com/image1.jpg",
      "alt": "商品主图",
      "sortOrder": 1,
      "isMain": true
    }
  ],
  "attributes": [
    {
      "id": 1,
      "name": "屏幕尺寸",
      "value": "6.1英寸",
      "type": "SPEC"
    }
  ]
}
```

#### 更新商品
**PUT** `/api/products/{id}`

**Headers:**
```
Authorization: Bearer {jwt-token}
```

**请求体:** 同创建商品

#### 删除商品
**DELETE** `/api/products/{id}`

**Headers:**
```
Authorization: Bearer {jwt-token}
```

### SKU管理

#### 创建SKU
**POST** `/api/products/{productId}/skus`

**Headers:**
```
Authorization: Bearer {jwt-token}
```

**请求体:**
```json
{
  "skuCode": "string",
  "price": 999.99,
  "stock": 100,
  "warningStock": 10,
  "barcode": "string",
  "weight": 0.5,
  "specifications": {"color": "红色", "size": "XL"}
}
```

#### 更新SKU库存
**PUT** `/api/skus/{id}/stock`

**Headers:**
```
Authorization: Bearer {jwt-token}
```

**请求体:**
```json
{
  "quantity": 50,
  "operationType": "INCREASE" // INCREASE, DECREASE, SET
}
```

## 订单中心模块

### 创建订单
**POST** `/api/orders`

**Headers:**
```
Authorization: Bearer {jwt-token}
```

**请求体:**
```json
{
  "items": [
    {
      "skuId": 1,
      "quantity": 2,
      "price": 5999.00
    }
  ],
  "address": {
    "receiverName": "张三",
    "phone": "13800138000",
    "province": "广东省",
    "city": "深圳市",
    "district": "南山区",
    "detail": "科技园xxx号"
  },
  "paymentMethod": "ALIPAY",
  "remark": "请尽快发货"
}
```

**响应:**
```json
{
  "id": 1,
  "orderNo": "20240101120001",
  "status": "PENDING_PAYMENT",
  "totalAmount": 11998.00,
  "items": [...],
  "address": {...},
  "createdAt": "2024-01-01T12:00:00Z"
}
```

### 获取订单列表
**GET** `/api/orders`

**Headers:**
```
Authorization: Bearer {jwt-token}
```

**查询参数:**
- `page` (默认: 0)
- `size` (默认: 20)
- `status` (可选: PENDING_PAYMENT, PAID, SHIPPED, COMPLETED, CANCELLED)
- `startDate` (可选: ISO格式)
- `endDate` (可选: ISO格式)

### 获取订单详情
**GET** `/api/orders/{id}`

### 更新订单状态
**PUT** `/api/orders/{id}/status`

**Headers:**
```
Authorization: Bearer {jwt-token}
```

**请求体:**
```json
{
  "status": "PAID",
  "remark": "用户已付款"
}
```

### 取消订单
**PUT** `/api/orders/{id}/cancel`

**Headers:**
```
Authorization: Bearer {jwt-token}
```

**请求体:**
```json
{
  "reason": "不想要了"
}
```

## 物流中心模块

### 承运商管理

#### 获取承运商列表
**GET** `/api/carriers`

**响应:**
```json
[
  {
    "id": 1,
    "name": "顺丰速运",
    "code": "SF",
    "contact": "400-811-1111",
    "website": "https://www.sf-express.com",
    "status": "ACTIVE"
  }
]
```

#### 创建承运商
**POST** `/api/carriers`

**Headers:**
```
Authorization: Bearer {jwt-token}
```

**请求体:**
```json
{
  "name": "string",
  "code": "string",
  "contact": "string",
  "website": "string",
  "status": "ACTIVE"
}
```

### 发货管理

#### 创建发货单
**POST** `/api/shipping-orders`

**Headers:**
```
Authorization: Bearer {jwt-token}
```

**请求体:**
```json
{
  "orderId": 1,
  "carrierId": 1,
  "trackingNumber": "SF123456789",
  "deliveryMethod": "EXPRESS",
  "estimatedDelivery": "2024-01-03",
  "shippingFee": 12.00
}
```

#### 更新物流状态
**PUT** `/api/shipping-orders/{id}/status`

**Headers:**
```
Authorization: Bearer {jwt-token}
```

**请求体:**
```json
{
  "status": "IN_TRANSIT",
  "location": "深圳市",
  "description": "包裹已到达深圳分拨中心"
}
```

#### 获取物流跟踪信息
**GET** `/api/shipping-orders/{id}/tracks`

**响应:**
```json
[
  {
    "id": 1,
    "status": "IN_TRANSIT",
    "location": "深圳市",
    "description": "包裹已到达深圳分拨中心",
    "timestamp": "2024-01-02T10:30:00Z"
  }
]
```

## 数据仪表盘模块

### 获取仪表盘统计数据
**GET** `/api/dashboard/stats`

**Headers:**
```
Authorization: Bearer {jwt-token}
```

**响应:**
```json
{
  "totalUsers": 1250,
  "activeUsersToday": 89,
  "totalProducts": 568,
  "totalOrders": 3421,
  "totalRevenue": 156789.50,
  "pendingOrders": 23,
  "lowStockProducts": 12,
  "orderTrend": [
    {"date": "2024-01-01", "orders": 45, "revenue": 12345.00},
    {"date": "2024-01-02", "orders": 52, "revenue": 15678.00}
  ],
  "topSellingProducts": [
    {"productId": 1, "productName": "iPhone 15", "sales": 156, "revenue": 935844.00}
  ],
  "categoryStats": [
    {"categoryId": 1, "categoryName": "电子产品", "productCount": 156, "sales": 2345}
  ]
}
```

## 错误响应格式

### 400 Bad Request
```json
{
  "timestamp": "2024-01-01T12:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "请求参数错误",
  "path": "/api/products"
}
```

### 401 Unauthorized
```json
{
  "timestamp": "2024-01-01T12:00:00Z",
  "status": 401,
  "error": "Unauthorized",
  "message": "未提供有效的认证信息",
  "path": "/api/users/me"
}
```

### 403 Forbidden
```json
{
  "timestamp": "2024-01-01T12:00:00Z",
  "status": 403,
  "error": "Forbidden",
  "message": "权限不足",
  "path": "/api/admin/users"
}
```

### 404 Not Found
```json
{
  "timestamp": "2024-01-01T12:00:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "资源不存在",
  "path": "/api/products/999"
}
```

### 429 Too Many Requests
```json
{
  "timestamp": "2024-01-01T12:00:00Z",
  "status": 429,
  "error": "Too Many Requests",
  "message": "请求过于频繁，请稍后再试",
  "path": "/api/auth/signin"
}
```

### 500 Internal Server Error
```json
{
  "timestamp": "2024-01-01T12:00:00Z",
  "status": 500,
  "error": "Internal Server Error",
  "message": "服务器内部错误",
  "path": "/api/orders"
}
```

## 分页响应格式

所有列表接口都使用统一的分页格式：

```json
{
  "content": [...],           // 数据列表
  "totalElements": 100,       // 总记录数
  "totalPages": 5,           // 总页数
  "number": 0,               // 当前页码（从0开始）
  "size": 20,                // 每页大小
  "first": true,             // 是否第一页
  "last": false,             // 是否最后一页
  "numberOfElements": 20     // 当前页实际记录数
}
```

## 枚举值说明

### 商品状态
- `DRAFT` - 草稿
- `ACTIVE` - 上架
- `INACTIVE` - 下架

### 订单状态
- `PENDING_PAYMENT` - 待支付
- `PAID` - 已支付
- `SHIPPED` - 已发货
- `COMPLETED` - 已完成
- `CANCELLED` - 已取消

### 支付状态
- `PENDING` - 待支付
- `PAID` - 已支付
- `REFUNDED` - 已退款
- `FAILED` - 支付失败

### 支付方式
- `ALIPAY` - 支付宝
- `WECHAT` - 微信支付
- `UNIONPAY` - 银联支付
- `CREDIT_CARD` - 信用卡

### 物流状态
- `PENDING` - 待发货
- `SHIPPED` - 已发货
- `IN_TRANSIT` - 运输中
- `DELIVERED` - 已送达
- `RETURNED` - 已退回

### 配送方式
- `EXPRESS` - 快递配送
- `LOCAL_DELIVERY` - 本地配送
- `SELF_PICKUP` - 门店自取

## 认证说明

所有需要认证的接口都需要在请求头中添加：
```
Authorization: Bearer {jwt-token}
```

JWT Token有效期为24小时，过期后需要重新登录获取新的Token。

## 速率限制

- 登录接口：每分钟最多5次尝试
- 注册接口：每小时最多10次
- 其他接口：每分钟最多100次请求

超过限制将返回429状态码。

## 数据格式

- 所有时间使用ISO 8601格式（UTC）
- 金额使用Decimal类型，精确到分
- 图片URL使用HTTPS协议
- 状态字段使用枚举值，区分大小写