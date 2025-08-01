# 项目目录结构

## 整体项目结构
```
project/
├── backend/                                 # 后端项目
├── frontend/                                # 前端项目
├── docs/                                    # 文档
│   ├── database_design.md                   # 数据库设计
│   ├── api_design.md                        # API设计
│   └── frontend_design.md                   # 前端设计
├── README.md                                # 项目说明
└── docker-compose.yml                       # Docker配置
```

## 后端项目结构（Spring Boot）
```
backend/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── example/
│   │   │           └── ecommerce/
│   │   │               ├── EcommerceApplication.java
│   │   │               ├── config/          # 配置类
│   │   │               ├── controller/      # 控制器
│   │   │               ├── service/         # 业务逻辑层
│   │   │               ├── mapper/          # 数据访问层
│   │   │               ├── entity/          # 实体类
│   │   │               ├── dto/             # 数据传输对象
│   │   │               ├── vo/              # 视图对象
│   │   │               ├── enums/           # 枚举类
│   │   │               ├── exception/       # 异常处理
│   │   │               ├── util/            # 工具类
│   │   │               └── aspect/          # 切面类
│   │   └── resources/
│   │       ├── application.yml              # 配置文件
│   │       ├── application-dev.yml          # 开发环境配置
│   │       ├── application-prod.yml         # 生产环境配置
│   │       ├── mapper/                      # MyBatis映射文件
│   │       ├── static/                      # 静态资源
│   │       └── templates/                   # 模板文件
│   └── test/
│       └── java/
└── pom.xml                                  # Maven配置文件
```

## 前端项目结构（Vue 3 + TypeScript）
```
frontend/
├── public/                                  # 静态资源
│   ├── index.html                           # 主页面
│   └── favicon.ico                          # 网站图标
├── src/
│   ├── assets/                              # 静态资源
│   │   ├── images/                          # 图片资源
│   │   └── styles/                          # 样式文件
│   ├── components/                          # 公共组件
│   │   ├── layout/                          # 布局组件
│   │   ├── common/                          # 通用组件
│   │   └── business/                        # 业务组件
│   ├── views/                               # 页面组件
│   │   ├── auth/                            # 认证相关页面
│   │   ├── user/                            # 用户管理页面
│   │   ├── product/                         # 商品管理页面
│   │   ├── order/                           # 订单管理页面
│   │   ├── shipping/                        # 物流管理页面
│   │   ├── cart/                            # 购物车页面
│   │   ├── gift-card/                       # 购物卡页面
│   │   ├── log/                             # 日志页面
│   │   ├── file/                            # 文件管理页面
│   │   └── dashboard/                       # 仪表盘页面
│   ├── router/                              # 路由配置
│   ├── store/                               # 状态管理
│   ├── services/                            # API服务
│   ├── utils/                               # 工具函数
│   ├── types/                               # TypeScript类型定义
│   ├── plugins/                             # 插件
│   ├── App.vue                              # 根组件
│   └── main.ts                              # 入口文件
├── tests/                                   # 测试文件
├── package.json                             # 依赖配置
├── tsconfig.json                            # TypeScript配置
├── vite.config.ts                           # 构建配置
└── README.md                                # 项目说明