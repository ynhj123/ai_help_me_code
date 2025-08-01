# 电商后台管理系统 - 前端

## 项目介绍

这是一个基于Vue 3.3.4、TypeScript 5.1.3和Ant Design Vue 4.0.0的电商后台管理系统前端项目。该项目提供了完整的电商功能，包括用户管理、商品管理、订单管理、物流管理、购物车、购物卡、日志系统、文件存储和仪表盘等功能。

## 技术栈

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

- Node.js 21.0+
- npm 8.0+

## 快速开始

1. 克隆项目
   ```
   git clone <项目地址>
   ```

2. 安装依赖
   ```
   npm install
   ```

3. 启动开发服务器
   ```
   npm run dev
   ```

4. 构建生产版本
   ```
   npm run build
   ```

## 项目结构

```
src/
├── assets/                      # 静态资源
│   ├── images/                  # 图片资源
│   └── styles/                  # 样式文件
├── components/                  # 公共组件
│   ├── layout/                  # 布局组件
│   ├── common/                  # 通用组件
│   └── business/                # 业务组件
├── views/                       # 页面组件
│   ├── auth/                    # 认证相关页面
│   ├── user/                    # 用户管理页面
│   ├── product/                 # 商品管理页面
│   ├── order/                   # 订单管理页面
│   ├── shipping/                # 物流管理页面
│   ├── cart/                    # 购物车页面
│   ├── gift-card/               # 购物卡页面
│   ├── log/                     # 日志页面
│   ├── file/                    # 文件管理页面
│   └── dashboard/               # 仪表盘页面
├── router/                      # 路由配置
├── store/                       # 状态管理
├── services/                    # API服务
├── utils/                       # 工具函数
├── types/                       # TypeScript类型定义
├── plugins/                     # 插件
├── App.vue                      # 根组件
└── main.ts                      # 入口文件
```

## 开发规范

1. 组件命名：使用PascalCase命名法
2. 文件命名：使用kebab-case命名法
3. 样式命名：使用BEM命名法
4. TypeScript类型：尽量使用接口定义类型

## 默认用户

- 用户名: admin
- 密码: admin123