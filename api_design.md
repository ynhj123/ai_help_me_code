# 后台管理系统API接口设计

## 1. 用户管理模块

### 认证相关
| 接口 | 方法 | 路径 | 描述 |
|------|------|------|------|
| 用户注册 | POST | /api/auth/register | 用户注册 |
| 用户登录 | POST | /api/auth/login | 用户登录 |
| 用户登出 | POST | /api/auth/logout | 用户登出 |
| 刷新令牌 | POST | /api/auth/refresh | 刷新访问令牌 |

### 用户相关
| 接口 | 方法 | 路径 | 描述 |
|------|------|------|------|
| 获取当前用户信息 | GET | /api/users/profile | 获取当前用户信息 |
| 更新当前用户信息 | PUT | /api/users/profile | 更新当前用户信息 |
| 修改密码 | PUT | /api/users/password | 修改密码 |
| 获取用户列表 | GET | /api/users | 获取用户列表（管理员） |
| 获取用户详情 | GET | /api/users/{id} | 获取用户详情（管理员） |
| 创建用户 | POST | /api/users | 创建用户（管理员） |
| 更新用户 | PUT | /api/users/{id} | 更新用户（管理员） |
| 删除用户 | DELETE | /api/users/{id} | 删除用户（管理员） |

### 角色相关
| 接口 | 方法 | 路径 | 描述 |
|------|------|------|------|
| 获取角色列表 | GET | /api/roles | 获取角色列表 |
| 获取角色详情 | GET | /api/roles/{id} | 获取角色详情 |
| 创建角色 | POST | /api/roles | 创建角色 |
| 更新角色 | PUT | /api/roles/{id} | 更新角色 |
| 删除角色 | DELETE | /api/roles/{id} | 删除角色 |
| 获取角色权限 | GET | /api/roles/{id}/permissions | 获取角色权限 |
| 分配角色权限 | POST | /api/roles/{id}/permissions | 分配角色权限 |

### 权限相关
| 接口 | 方法 | 路径 | 描述 |
|------|------|------|------|
| 获取权限列表 | GET | /api/permissions | 获取权限列表 |
| 获取权限详情 | GET | /api/permissions/{id} | 获取权限详情 |
| 创建权限 | POST | /api/permissions | 创建权限 |
| 更新权限 | PUT | /api/permissions/{id} | 更新权限 |
| 删除权限 | DELETE | /api/permissions/{id} | 删除权限 |

## 2. 商品管理模块

### 商品分类
| 接口 | 方法 | 路径 | 描述 |
|------|------|------|------|
| 获取分类列表 | GET | /api/categories | 获取分类列表 |
| 获取分类树 | GET | /api/categories/tree | 获取分类树 |
| 获取分类详情 | GET | /api/categories/{id} | 获取分类详情 |
| 创建分类 | POST | /api/categories | 创建分类 |
| 更新分类 | PUT | /api/categories/{id} | 更新分类 |
| 删除分类 | DELETE | /api/categories/{id} | 删除分类 |

### 商品管理
| 接口 | 方法 | 路径 | 描述 |
|------|------|------|------|
| 获取商品列表 | GET | /api/products | 获取商品列表 |
| 获取商品详情 | GET | /api/products/{id} | 获取商品详情 |
| 创建商品 | POST | /api/products | 创建商品 |
| 更新商品 | PUT | /api/products/{id} | 更新商品 |
| 删除商品 | DELETE | /api/products/{id} | 删除商品 |
| 上架商品 | PUT | /api/products/{id}/enable | 上架商品 |
| 下架商品 | PUT | /api/products/{id}/disable | 下架商品 |

### 商品库存
| 接口 | 方法 | 路径 | 描述 |
|------|------|------|------|
| 获取库存信息 | GET | /api/inventory/{productId} | 获取商品库存信息 |
| 更新库存 | PUT | /api/inventory/{productId} | 更新商品库存 |

## 3. 订单管理模块

| 接口 | 方法 | 路径 | 描述 |
|------|------|------|------|
| 创建订单 | POST | /api/orders | 创建订单 |
| 获取订单列表 | GET | /api/orders | 获取订单列表 |
| 获取订单详情 | GET | /api/orders/{id} | 获取订单详情 |
| 更新订单 | PUT | /api/orders/{id} | 更新订单 |
| 删除订单 | DELETE | /api/orders/{id} | 删除订单 |
| 取消订单 | PUT | /api/orders/{id}/cancel | 取消订单 |
| 确认付款 | PUT | /api/orders/{id}/pay | 确认付款 |
| 发货 | PUT | /api/orders/{id}/ship | 发货 |
| 完成订单 | PUT | /api/orders/{id}/complete | 完成订单 |

## 4. 物流管理模块

| 接口 | 方法 | 路径 | 描述 |
|------|------|------|------|
| 获取物流信息 | GET | /api/shipping/{orderId} | 获取订单物流信息 |
| 更新物流信息 | PUT | /api/shipping/{orderId} | 更新物流信息 |
| 获取待发货订单 | GET | /api/shipping/pending | 获取待发货订单列表 |
| 获取运输中订单 | GET | /api/shipping/in-transit | 获取运输中订单列表 |

## 5. 购物车管理模块

| 接口 | 方法 | 路径 | 描述 |
|------|------|------|------|
| 获取购物车 | GET | /api/cart | 获取当前用户购物车 |
| 添加商品到购物车 | POST | /api/cart | 添加商品到购物车 |
| 更新购物车商品数量 | PUT | /api/cart/{productId} | 更新购物车商品数量 |
| 删除购物车商品 | DELETE | /api/cart/{productId} | 删除购物车商品 |
| 清空购物车 | DELETE | /api/cart | 清空购物车 |

## 6. 购物卡管理模块

| 接口 | 方法 | 路径 | 描述 |
|------|------|------|------|
| 获取购物卡列表 | GET | /api/gift-cards | 获取购物卡列表 |
| 获取购物卡详情 | GET | /api/gift-cards/{id} | 获取购物卡详情 |
| 创建购物卡 | POST | /api/gift-cards | 创建购物卡 |
| 兑换购物卡 | POST | /api/gift-cards/redeem | 兑换购物卡 |
| 获取购物卡使用记录 | GET | /api/gift-cards/{id}/usage | 获取购物卡使用记录 |

## 7. 日志系统模块

| 接口 | 方法 | 路径 | 描述 |
|------|------|------|------|
| 获取操作日志列表 | GET | /api/logs | 获取操作日志列表 |
| 获取日志详情 | GET | /api/logs/{id} | 获取日志详情 |

## 8. 文件存储系统模块

| 接口 | 方法 | 路径 | 描述 |
|------|------|------|------|
| 上传文件 | POST | /api/files | 上传文件 |
| 获取文件信息 | GET | /api/files/{id} | 获取文件信息 |
| 下载文件 | GET | /api/files/{id}/download | 下载文件 |

## 9. Dashboard模块

| 接口 | 方法 | 路径 | 描述 |
|------|------|------|------|
| 获取统计数据 | GET | /api/dashboard/stats | 获取统计数据 |
| 获取销售图表数据 | GET | /api/dashboard/sales-chart | 获取销售图表数据 |
| 获取用户活跃度数据 | GET | /api/dashboard/user-activity | 获取用户活跃度数据 |