# 电商后台管理系统项目规范

## 1. 项目架构

### 1.1 整体架构
- **前后端分离架构**：前端采用Vue 3 + TypeScript，后端采用Spring Boot + MyBatis
- **权限控制**：基于RBAC（角色权限控制）模型，使用Spring Security + JWT实现
- **数据存储**：MySQL 8.0+关系型数据库
- **容器化部署**：支持Docker和docker-compose部署

### 1.2 后端架构
- **分层架构**：控制层(Controller)、服务层(Service)、数据访问层(Mapper)、实体层(Entity)、数据传输层(DTO/VO)
- **面向接口编程**：服务层采用接口+实现的方式
- **AOP支持**：通过aspect包提供切面功能
- **统一异常处理**：通过exception包处理各类异常

### 1.3 前端架构
- **组件化开发**：基于Vue 3组件系统
- **状态管理**：使用Pinia
- **路由管理**：使用Vue Router 4
- **UI组件库**：Ant Design Vue 4.0

## 2. 目录结构规范

### 2.1 整体目录
```
├── backend/           # 后端项目
├── frontend/          # 前端项目
├── nginx/             # Nginx配置
└── *.md               # 项目文档
```

### 2.2 后端目录结构
```
backend/
├── src/main/java/com/example/ecommerce/  # Java源码
│   ├── controller/    # 控制器层
│   ├── service/       # 服务层
│   │   └── impl/      # 服务实现
│   ├── mapper/        # MyBatis映射器
│   ├── entity/        # 实体类
│   ├── dto/           # 数据传输对象
│   ├── vo/            # 视图对象
│   ├── enums/         # 枚举类
│   ├── config/        # 配置类
│   ├── aspect/        # 切面
│   ├── exception/     # 异常处理
│   ├── util/          # 工具类
│   └── EcommerceApplication.java  # 应用入口
├── src/main/resources/  # 资源文件
│   ├── application.yml       # 主配置文件
│   ├── application-dev.yml   # 开发环境配置
│   ├── application-prod.yml  # 生产环境配置
│   ├── mapper/              # MyBatis XML映射文件
│   └── db/                  # 数据库脚本
└── pom.xml                # Maven依赖管理

```

### 2.3 前端目录结构
```
frontend/
├── src/
│   ├── components/    # 通用组件
│   ├── views/         # 页面组件
│   ├── router/        # 路由配置
│   ├── store/         # 状态管理
│   ├── services/      # API服务
│   ├── utils/         # 工具函数
│   ├── assets/        # 静态资源
│   ├── types/         # TypeScript类型定义
│   ├── App.vue        # 根组件
│   └── main.ts        # 入口文件
├── public/           # 静态资源
├── index.html        # HTML模板
├── package.json      # NPM依赖管理
└── vite.config.ts    # Vite配置
```

## 3. 编码规范

### 3.1 Java后端编码规范

#### 3.1.1 类和接口命名
- 类名：采用大驼峰命名法，如`UserController`、`BaseEntity`
- 接口名：采用大驼峰命名法，通常以`I`为前缀或直接使用业务名称，如`UserService`
- 方法名：采用小驼峰命名法，动词+名词形式，如`getUserList`、`updateProfile`
- 字段名：采用小驼峰命名法，如`username`、`createdAt`
- 常量：全大写字母，下划线分隔，如`MAX_PAGE_SIZE`

#### 3.1.2 注解规范
- 使用Lombok注解简化代码：`@Data`、`@EqualsAndHashCode`
- 构造器注入依赖，避免`@Autowired`字段注入
- RESTful接口使用`@RestController`和`@RequestMapping`
- 参数校验使用`@Valid`、`@Validated`、`@Min`等注解
- 权限控制使用`@PreAuthorize`注解

#### 3.1.3 注释规范
- 类和方法必须添加Javadoc注释，说明功能、参数、返回值
- 复杂业务逻辑必须添加行内注释

#### 3.1.4 数据类型规范
- 时间日期使用`LocalDateTime`类型
- 主键使用`Long`类型
- 金额使用`BigDecimal`类型（金融相关场景）
- 状态值使用枚举类型

### 3.2 TypeScript前端编码规范

#### 3.2.1 命名规范
- 组件名：大驼峰命名法，如`UserProfile`
- 变量名：小驼峰命名法，如`userList`、`currentPage`
- 常量：全大写字母，下划线分隔，如`MAX_PAGE_SIZE`
- 接口/类型：大驼峰命名法，使用`I`前缀或无前缀，如`UserInfo`或`IUserInfo`

#### 3.2.2 组件规范
- 单文件组件（.vue）包含template、script、style
- 使用Composition API编写组件
- 组件Props和Emits必须使用TypeScript类型定义

#### 3.2.3 注释规范
- 组件、接口、公共函数必须添加JSDoc注释
- 复杂业务逻辑必须添加行内注释

## 4. 数据库设计规范

### 4.1 命名规范
- 表名：小写字母，下划线分隔，如`users`、`user_roles`
- 字段名：小写字母，下划线分隔，如`user_id`、`created_at`
- 索引名：`idx_表名_字段名`，如`idx_users_username`
- 外键名：`fk_表名_关联表名_字段名`，如`fk_user_roles_user_id`

### 4.2 表结构规范
- 所有表必须包含`id`（主键）、`created_at`（创建时间）、`updated_at`（更新时间）字段
- 表之间关系通过外键约束，使用`ON DELETE CASCADE`处理级联删除
- 多对多关系必须使用中间表，如`user_roles`、`role_permissions`
- 状态字段使用`tinyint`类型，如`status`字段（1表示正常，0表示禁用）
- 文本字段使用`varchar`或`text`，明确长度限制

### 4.3 索引规范
- 频繁查询的字段应建立索引
- 唯一约束字段应建立唯一索引
- 外键字段应建立索引
- 避免过多索引影响写入性能

## 5. API设计规范

### 5.1 接口风格
- 采用RESTful API设计风格
- 接口路径统一前缀`/api`
- 使用合适的HTTP方法：GET（查询）、POST（创建）、PUT（更新）、DELETE（删除）

### 5.2 接口命名
- 资源命名使用复数形式，如`/api/users`、`/api/products`
- 操作用资源ID标识，如`/api/users/{id}`
- 特殊操作使用动词+资源，如`/api/users/{id}/disable`

### 5.3 请求规范
- 查询参数：`GET /api/users?page=1&size=10`
- 分页参数：统一使用`page`（页码）和`size`（每页数量）
- 排序参数：`sortBy`（排序字段）和`sortOrder`（排序方向）
- 条件查询：支持多字段组合查询

### 5.4 响应规范
- 统一响应格式：使用`ResultVO`包装响应数据
- 响应包含状态码、消息和数据
- 分页数据包含总数、页码、每页数量和数据列表

## 6. 安全规范

### 6.1 认证授权
- 使用JWT进行用户认证
- 基于角色的访问控制（RBAC）
- 敏感接口添加权限验证

### 6.2 数据安全
- 密码加密存储（BCrypt等）
- 防止SQL注入、XSS攻击
- 接口请求参数校验

### 6.3 日志记录
- 操作日志记录关键用户行为
- 异常日志记录错误信息
- 安全日志记录敏感操作

## 7. 环境配置规范

### 7.1 配置文件
- 开发环境：`application-dev.yml`
- 生产环境：`application-prod.yml`
- 公共配置：`application.yml`

### 7.2 环境变量
- 敏感信息（数据库密码、密钥等）通过环境变量配置
- 不同环境使用不同的配置值

## 8. 开发与部署规范

### 8.1 开发流程
- 本地开发环境搭建：JDK 21+, Node.js 21.0+, Maven 3.8+, MySQL 8.0+
- 代码提交前必须通过编译和基本测试
- 遵循Git工作流（如Feature Branch Workflow）

### 8.2 构建与部署
- 后端：使用Maven构建
- 前端：使用npm/yarn构建
- Docker容器化部署
- 支持docker-compose一键部署

## 9. 文档规范

### 9.1 项目文档
- `README.md`：项目介绍、技术栈、快速开始
- `api_design.md`：API接口设计文档
- `database_design.md`：数据库设计文档
- `frontend_design.md`：前端页面设计文档

### 9.2 代码文档
- 类、方法、接口必须添加规范的注释
- 复杂业务逻辑必须有详细的注释说明

## 10. 其他规范

### 10.1 版本控制
- `.gitignore`文件必须包含IDE配置、构建产物、日志文件等
- 提交信息必须清晰、简洁，说明修改内容

### 10.2 性能优化
- 数据库查询优化：合理使用索引、避免全表扫描
- 接口响应优化：合理缓存、异步处理
- 前端性能优化：组件懒加载、资源压缩

### 10.3 代码审查
- 定期进行代码审查
- 遵循代码规范，保持代码风格一致性
- 及时修复潜在问题和技术债务