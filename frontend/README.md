# 运营后台前端系统

基于React + TypeScript + Ant Design的企业级运营后台管理系统。

## 技术栈

- **框架**: React 18 + TypeScript 5
- **构建工具**: Vite 5
- **状态管理**: Zustand + React Query
- **UI框架**: Ant Design 5
- **路由**: React Router 6
- **HTTP客户端**: Axios
- **图表**: ECharts
- **测试**: Vitest

## 项目结构

```
frontend/
├── src/
│   ├── api/           # API接口封装
│   ├── components/    # 公共组件
│   ├── hooks/         # 自定义Hooks
│   ├── layouts/       # 布局组件
│   ├── pages/         # 页面组件
│   ├── stores/        # 状态管理
│   ├── types/         # TypeScript类型定义
│   ├── utils/         # 工具函数
│   └── styles/        # 样式文件
├── public/            # 静态资源
├── .env.development   # 开发环境配置
├── .env.production    # 生产环境配置
└── vite.config.ts     # Vite配置
```

## 功能模块

### 1. 认证授权
- 用户登录/登出
- JWT Token管理
- 权限控制

### 2. 用户管理
- 用户列表展示
- 用户新增/编辑/删除
- 用户状态管理

### 3. 商品中心
- 商品列表管理
- 商品分类管理
- SKU管理
- 商品图片管理

### 4. 订单中心
- 订单列表展示
- 订单状态管理
- 订单详情查看

### 5. 物流中心
- 物流订单管理
- 物流状态跟踪
- 承运商管理

### 6. 数据仪表盘
- 数据统计展示
- 销售趋势图表
- 关键指标监控

## 快速开始

### 安装依赖
```bash
npm install
```

### 开发环境运行
```bash
npm run dev
```

### 生产环境构建
```bash
npm run build
```

### 代码检查
```bash
npm run lint
```

### 代码格式化
```bash
npm run format
```

## 环境变量

### 开发环境 (.env.development)
```
VITE_API_BASE_URL=http://localhost:8080/api
```

### 生产环境 (.env.production)
```
VITE_API_BASE_URL=https://your-domain.com/api
```

## API接口

所有API接口都经过统一封装，位于 `src/api/` 目录下：
- `auth.ts` - 认证相关接口
- `user.ts` - 用户管理接口
- `product.ts` - 商品管理接口
- `order.ts` - 订单管理接口
- `logistics.ts` - 物流管理接口

## 状态管理

使用Zustand进行状态管理，主要store包括：
- `authStore` - 认证状态
- `userStore` - 用户状态
- `appStore` - 应用全局状态

## 权限控制

基于角色的权限控制(RBAC)，支持以下角色：
- ADMIN - 管理员
- USER - 普通用户

## 响应式设计

系统采用响应式设计，支持多种屏幕尺寸：
- 桌面端 (≥1200px)
- 平板端 (≥768px)
- 移动端 (<768px)

## 开发规范

- 使用TypeScript进行类型检查
- 遵循ESLint代码规范
- 使用Prettier进行代码格式化
- 组件采用函数式组件和Hooks
- API调用统一封装处理错误

## 部署说明

1. 构建生产版本
```bash
npm run build
```

2. 部署到服务器
将 `dist/` 目录下的文件部署到Web服务器

3. 配置反向代理
确保API请求正确代理到后端服务
