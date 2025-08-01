# 电商后台管理系统 - 后端

## 项目介绍

这是一个基于Spring Boot 3.1.0和MyBatis 3.0.2的电商后台管理系统后端项目。该项目提供了完整的电商功能，包括用户管理、商品管理、订单管理、物流管理、购物车、购物卡、日志系统、文件存储和仪表盘等功能。

## 技术栈

- Spring Boot 3.1.0
- MyBatis 3.0.2
- MySQL 8.0+
- JWT 0.11.5
- Maven 3.8+
- Java 21

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
- Maven 3.8+
- MySQL 8.0+

## 快速开始

1. 克隆项目
   ```
   git clone <项目地址>
   ```

2. 创建数据库
   ```sql
   CREATE DATABASE ecommerce CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

3. 配置数据库
   在`application.yml`中修改数据库连接信息

4. 构建项目
   ```
   mvn clean install
   ```

5. 运行项目
   ```
   mvn spring-boot:run
   ```

## API文档

API文档使用Swagger生成，启动项目后访问：http://localhost:8080/swagger-ui.html

## 配置文件

- `application.yml`：主配置文件
- `application-dev.yml`：开发环境配置
- `application-prod.yml`：生产环境配置

## 项目结构

```
src/
├── main/
│   ├── java/
│   │   └── com/example/ecommerce/
│   │       ├── EcommerceApplication.java
│   │       ├── config/          # 配置类
│   │       ├── controller/      # 控制器
│   │       ├── service/         # 业务逻辑层
│   │       ├── mapper/          # 数据访问层
│   │       ├── entity/          # 实体类
│   │       ├── dto/             # 数据传输对象
│   │       ├── vo/              # 视图对象
│   │       ├── enums/           # 枚举类
│   │       ├── exception/       # 异常处理
│   │       ├── util/            # 工具类
│   │       └── aspect/          # 切面类
│   └── resources/
│       ├── application.yml      # 配置文件
│       ├── db/                  # 数据库脚本
│       ├── mapper/              # MyBatis映射文件
│       ├── static/              # 静态资源
│       └── templates/           # 模板文件
└── test/
    └── java/                    # 测试代码
```

## 默认用户

- 用户名: admin
- 密码: admin123