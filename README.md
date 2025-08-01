# 电商后台管理系统

## 项目介绍

这是一个基于Spring Boot 3.1.0、MyBatis 3.0.2、Vue 3.3.4、TypeScript 5.1.3和Ant Design Vue 4.0.0的电商后台管理系统。该项目提供了完整的电商功能，包括用户管理、商品管理、订单管理、物流管理、购物车、购物卡、日志系统、文件存储和仪表盘等功能。

## 技术栈

### 后端
- Spring Boot 3.1.0
- MyBatis 3.0.2
- MySQL 8.0+
- JWT 0.11.5
- Maven 3.8+
- Java 21

### 前端
- Vue 3.3.4
- TypeScript 5.1.3
- Ant Design Vue 4.0.0
- Vue Router 4.2.2
- Pinia 2.1.4
- Axios 1.4.0
- Vite 4.3.9

## 功能模块

1. 用户管理
   - 用户注册、登录
   - 用户信息管理
   - 角色和权限管理（RBAC）

2. 商品管理
   - 商品分类管理
   - 商品信息管理
   - 商品库存管理

3. 订单管理
   - 订单创建
   - 订单状态管理
   - 支付集成（银联、微信、支付宝）

4. 物流管理
   - 物流信息跟踪
   - 物流状态更新

5. 购物车管理
   - 商品添加/删除
   - 数量修改

6. 购物卡管理
   - 购物卡生成
   - 购物卡兑换

7. 日志系统
   - 操作日志记录
   - 日志查询

8. 文件存储系统
   - 文件上传/下载

9. Dashboard
   - 数据统计
   - 图表展示

## 环境要求

- Java 21+
- Node.js 21.0+
- Maven 3.8+
- MySQL 8.0+
- Docker 20.0+ (可选，用于容器化部署)

## 快速开始

### 方式一：本地开发环境

1. 克隆项目
   ```
   git clone <项目地址>
   ```

2. 启动后端服务
   ```
   cd backend
   mvn clean install
   mvn spring-boot:run
   ```

3. 启动前端服务
   ```
   cd frontend
   npm install
   npm run dev
   ```

4. 访问应用
   - 前端: http://localhost:3000
   - 后端API: http://localhost:8080

### 方式二：Docker容器化部署

1. 克隆项目
   ```
   git clone <项目地址>
   ```

2. 构建并启动容器
   ```
   docker-compose up -d
   ```

3. 访问应用
   - 前端: http://localhost:3000
   - 后端API: http://localhost:8080

## 项目结构

```
project/
├── backend/                     # 后端项目
│   ├── src/                     # 源代码
│   ├── pom.xml                  # Maven配置文件
│   ├── Dockerfile               # Docker配置文件
│   └── README.md                # 后端说明文档
├── frontend/                    # 前端项目
│   ├── src/                     # 源代码
│   ├── package.json             # 依赖配置文件
│   ├── vite.config.ts           # 构建配置文件
│   ├── Dockerfile               # Docker配置文件
│   ├── nginx.conf               # Nginx配置文件
│   └── README.md                # 前端说明文档
├── docker-compose.yml           # Docker Compose配置文件
└── README.md                    # 项目说明文档
```

## 默认用户

- 用户名: admin
- 密码: admin123

## 开发规范

### 后端开发规范
1. 遵循RESTful API设计规范
2. 使用统一的响应格式
3. 使用JWT进行身份验证
4. 使用MyBatis进行数据访问
5. 使用Spring Security进行权限控制

### 前端开发规范
1. 组件命名：使用PascalCase命名法
2. 文件命名：使用kebab-case命名法
3. 样式命名：使用BEM命名法
4. TypeScript类型：尽量使用接口定义类型
5. 遵循Vue 3 Composition API规范

## 贡献指南

1. Fork项目
2. 创建功能分支
3. 提交更改
4. 推送分支
5. 创建Pull Request

## 许可证

本项目采用MIT许可证，详情请见[LICENSE](LICENSE)文件。