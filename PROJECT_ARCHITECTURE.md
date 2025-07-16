# 全栈后台管理系统架构设计

## 项目概述
基于模块化单体架构的企业级后台管理系统，采用Spring Boot + React技术栈。

## 技术栈

### 后端技术栈
- **框架**: Spring Boot 3.2 + Java 17
- **数据库**: PostgreSQL 15 (主从复制)
- **缓存**: Redis 7
- **消息队列**: RabbitMQ
- **搜索引擎**: Elasticsearch
- **安全**: Spring Security + JWT
- **API**: RESTful + GraphQL
- **文档**: SpringDoc OpenAPI 3
- **监控**: Micrometer + Prometheus

### 前端技术栈
- **框架**: React 18 + TypeScript 5
- **构建工具**: Vite 5
- **状态管理**: Zustand + React Query
- **UI框架**: Ant Design 5
- **图表**: ECharts
- **测试**: Vitest

## 项目结构

### 后端结构
```
backend/
├── src/main/java/com/admin/
│   ├── AdminApplication.java
│   ├── config/                 # 配置类
│   │   ├── SecurityConfig.java
│   │   ├── JwtConfig.java
│   │   ├── RedisConfig.java
│   │   └── GraphQLConfig.java
│   ├── common/                 # 公共模块
│   │   ├── entity/
│   │   ├── dto/
│   │   ├── exception/
│   │   ├── utils/
│   │   └── constants/
│   ├── modules/                # 业务模块
│   │   ├── auth/              # 认证授权
│   │   ├── user/              # 用户管理
│   │   ├── product/           # 商品中心
│   │   ├── order/             # 订单中心
│   │   ├── logistics/         # 物流中心
│   │   └── dashboard/         # 数据仪表盘
│   └── resources/
│       ├── application.yml
│       ├── application-dev.yml
│       ├── application-prod.yml
│       └── db/migration/       # Flyway迁移脚本
```

### 前端结构
```
frontend/
├── src/
│   ├── api/                   # API接口
│   ├── components/            # 公共组件
│   ├── hooks/                 # 自定义Hooks
│   ├── layouts/               # 布局组件
│   ├── pages/                 # 页面组件
│   │   ├── Dashboard/
│   │   ├── User/
│   │   ├── Product/
│   │   ├── Order/
│   │   └── Logistics/
│   ├── stores/                # 状态管理
│   ├── utils/                 # 工具函数
│   └── types/                 # TypeScript类型
```

## 数据库设计

### 核心表结构

#### 用户权限相关
```sql
-- 用户表
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    avatar_url VARCHAR(500),
    status VARCHAR(20) DEFAULT 'ACTIVE',
    user_type VARCHAR(20) DEFAULT 'USER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 角色表
CREATE TABLE roles (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL,
    code VARCHAR(50) UNIQUE NOT NULL,
    description TEXT,
    is_system BOOLEAN DEFAULT FALSE
);

-- 权限表
CREATE TABLE permissions (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    code VARCHAR(100) UNIQUE NOT NULL,
    resource VARCHAR(50) NOT NULL,
    action VARCHAR(50) NOT NULL
);
```

#### 商品中心
```sql
-- 商品分类
CREATE TABLE categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    parent_id BIGINT REFERENCES categories(id),
    level INTEGER DEFAULT 1,
    sort_order INTEGER DEFAULT 0,
    is_active BOOLEAN DEFAULT TRUE
);

-- 商品SPU
CREATE TABLE products (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    category_id BIGINT REFERENCES categories(id),
    brand VARCHAR(100),
    description TEXT,
    status VARCHAR(20) DEFAULT 'DRAFT',
    is_deleted BOOLEAN DEFAULT FALSE
);

-- 商品SKU
CREATE TABLE skus (
    id BIGSERIAL PRIMARY KEY,
    product_id BIGINT REFERENCES products(id),
    sku_code VARCHAR(50) UNIQUE NOT NULL,
    name VARCHAR(200) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    stock_quantity INTEGER DEFAULT 0,
    warning_stock INTEGER DEFAULT 10
);
```

#### 订单中心
```sql
-- 订单表
CREATE TABLE orders (
    id BIGSERIAL PRIMARY KEY,
    order_no VARCHAR(32) UNIQUE NOT NULL,
    user_id BIGINT REFERENCES users(id),
    total_amount DECIMAL(10,2) NOT NULL,
    status VARCHAR(20) DEFAULT 'PENDING',
    payment_method VARCHAR(20),
    delivery_address JSONB,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 订单项
CREATE TABLE order_items (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT REFERENCES orders(id),
    sku_id BIGINT REFERENCES skus(id),
    quantity INTEGER NOT NULL,
    price DECIMAL(10,2) NOT NULL
);
```

## 安全设计

### JWT认证
- Access Token: 15分钟有效期
- Refresh Token: 7天有效期
- 支持Token撤销
- 防暴力破解: 5次失败后锁定30分钟

### 防护措施
- SQL注入防护: JPA + 参数化查询
- XSS防护: Spring Security默认过滤器
- CSRF防护: 前后端分离 + Token验证
- 敏感数据脱敏: 自定义注解 + AOP

## 部署架构

### Docker配置
```yaml
# docker-compose.yml
version: '3.8'
services:
  app:
    build: ./backend
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=prod
    depends_on:
      - postgres
      - redis
      
  postgres:
    image: postgres:15
    environment:
      POSTGRES_DB: admin_system
      POSTGRES_USER: admin
      POSTGRES_PASSWORD: password
      
  redis:
    image: redis:7-alpine
    
  nginx:
    image: nginx:alpine
    ports:
      - "80:80"
      - "443:443"
```

## 开发规范

### 代码规范
- 遵循阿里巴巴Java开发手册
- 使用Checkstyle进行代码检查
- 单元测试覆盖率 > 80%
- 接口文档覆盖率 100%

### Git规范
- 分支管理: Git Flow
- Commit规范: Conventional Commits
- 代码审查: Pull Request + Code Review

## 监控告警

### 应用监控
- 健康检查: Spring Boot Actuator
- 性能监控: Micrometer + Prometheus
- 日志收集: ELK Stack
- 错误追踪: Sentry

### 业务监控
- 订单成功率 < 95% 告警
- 接口响应时间 > 2s 告警
- 库存预警: 库存 < 10% 告警