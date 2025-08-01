# 后端代码修复计划

## 📋 项目现状分析

### ✅ 已完成部分
1. **用户模块** - 完整实现了用户注册、登录、管理功能
2. **认证授权** - JWT认证、Spring Security配置完整
3. **数据访问层** - MyBatis Mapper和XML配置完整
4. **异常处理** - 全局异常处理器已实现
5. **配置文件** - 应用配置、数据库配置、JWT配置完整
6. **数据库设计** - 用户表、角色表、商品表等已设计
7. **工具类** - 密码加密、JWT工具类完整
8. **DTO/VO** - 数据传输对象和响应对象完整

### ❌ 关键问题识别
1. **UserDetailsServiceImpl缺失** - Spring Security认证的关键组件
2. **数据库字段类型不匹配** - 用户状态字段类型问题
3. **核心业务模块缺失** - 商品、订单、购物车、分类管理
4. **功能不完整** - 分页、文件上传、缓存、日志等

## 🔧 修复计划详情

### 阶段一：修复关键问题（优先级：高）

#### 1.1 实现UserDetailsServiceImpl
**问题**: 在UserServiceImpl中注入了UserDetailsService，但该类未实现
**影响**: Spring Security认证无法正常工作
**解决方案**:
```java
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    
    @Autowired
    private UserMapper userMapper;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在: " + username);
        }
        
        return org.springframework.security.core.userdetails.User
            .withUsername(user.getUsername())
            .password(user.getPassword())
            .roles(user.getRole().name())
            .accountExpired(false)
            .accountLocked(false)
            .credentialsExpired(false)
            .disabled(user.getStatus() == UserStatus.DISABLED)
            .build();
    }
}
```

#### 1.2 修复数据库schema字段类型不匹配
**问题**: schema.sql中用户状态字段定义为TINYINT，但UserStatus枚举使用int
**影响**: 可能导致类型转换异常
**解决方案**:
- 修改schema.sql中用户状态字段为INT类型
- 确保UserStatus枚举与数据库字段类型一致

### 阶段二：实现核心业务模块（优先级：高）

#### 2.1 商品管理模块
**需要实现的组件**:
- `Product` 实体类
- `ProductDTO` / `ProductVO` 数据传输对象
- `ProductMapper` 接口和XML映射文件
- `ProductService` 接口和实现类
- `ProductController` 控制器

**API接口**:
- `GET /api/products` - 获取商品列表（分页、搜索、筛选）
- `GET /api/products/{id}` - 获取商品详情
- `POST /api/products` - 创建商品
- `PUT /api/products/{id}` - 更新商品
- `DELETE /api/products/{id}` - 删除商品
- `PUT /api/products/{id}/enable` - 上架商品
- `PUT /api/products/{id}/disable` - 下架商品

#### 2.2 分类管理模块
**需要实现的组件**:
- `Category` 实体类
- `CategoryDTO` / `CategoryVO` 数据传输对象
- `CategoryMapper` 接口和XML映射文件
- `CategoryService` 接口和实现类
- `CategoryController` 控制器

**API接口**:
- `GET /api/categories` - 获取分类列表
- `GET /api/categories/tree` - 获取分类树
- `GET /api/categories/{id}` - 获取分类详情
- `POST /api/categories` - 创建分类
- `PUT /api/categories/{id}` - 更新分类
- `DELETE /api/categories/{id}` - 删除分类

#### 2.3 订单管理模块
**需要实现的组件**:
- `Order` 实体类
- `OrderItem` 实体类
- `OrderDTO` / `OrderVO` 数据传输对象
- `OrderMapper` 接口和XML映射文件
- `OrderService` 接口和实现类
- `OrderController` 控制器

**API接口**:
- `POST /api/orders` - 创建订单
- `GET /api/orders` - 获取订单列表
- `GET /api/orders/{id}` - 获取订单详情
- `PUT /api/orders/{id}` - 更新订单
- `DELETE /api/orders/{id}` - 删除订单
- `PUT /api/orders/{id}/cancel` - 取消订单
- `PUT /api/orders/{id}/pay` - 确认付款
- `PUT /api/orders/{id}/ship` - 发货
- `PUT /api/orders/{id}/complete` - 完成订单

#### 2.4 购物车管理模块
**需要实现的组件**:
- `ShoppingCart` 实体类
- `CartItem` 实体类
- `CartDTO` / `CartVO` 数据传输对象
- `CartMapper` 接口和XML映射文件
- `CartService` 接口和实现类
- `CartController` 控制器

**API接口**:
- `GET /api/cart` - 获取当前用户购物车
- `POST /api/cart` - 添加商品到购物车
- `PUT /api/cart/{productId}` - 更新购物车商品数量
- `DELETE /api/cart/{productId}` - 删除购物车商品
- `DELETE /api/cart` - 清空购物车

### 阶段三：完善功能模块（优先级：中）

#### 3.1 完善分页查询功能
**问题**: 当前UserMapper.xml中的分页查询功能较简单
**解决方案**:
- 添加动态查询条件
- 支持多字段排序
- 添加总数查询
- 实现通用分页工具类

#### 3.2 实现文件上传功能
**需要实现的组件**:
- `File` 实体类
- `FileDTO` / `FileVO` 数据传输对象
- `FileService` 文件上传服务
- `FileController` 文件控制器
- 文件存储配置

**API接口**:
- `POST /api/files` - 上传文件
- `GET /api/files/{id}` - 获取文件信息
- `GET /api/files/{id}/download` - 下载文件

#### 3.3 添加Redis缓存支持
**需要添加的依赖**:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

**缓存策略**:
- 用户信息缓存
- 商品信息缓存
- 分类树缓存
- 热门数据缓存

### 阶段四：优化和增强（优先级：低）

#### 4.1 添加数据库索引优化
**需要优化的索引**:
- 用户表相关索引
- 商品表相关索引
- 订单表相关索引
- 购物车表相关索引

#### 4.2 实现日志记录功能
**需要实现的组件**:
- `OperationLog` 实体类
- `LogAspect` 切面类
- `LogService` 日志服务
- 日志配置

#### 4.3 添加API文档（Swagger配置）
**需要添加的依赖**:
```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.1.0</version>
</dependency>
```

**配置内容**:
- API文档基本信息
- 全局参数配置
- 接口分组
- 安全配置

## 📅 实施时间表

### 第1周：修复关键问题
- [ ] 实现UserDetailsServiceImpl
- [ ] 修复数据库字段类型不匹配
- [ ] 测试认证功能

### 第2-3周：实现核心业务模块
- [ ] 商品管理模块
- [ ] 分类管理模块
- [ ] 订单管理模块
- [ ] 购物车管理模块

### 第4周：完善功能模块
- [ ] 完善分页查询功能
- [ ] 实现文件上传功能
- [ ] 添加Redis缓存支持

### 第5周：优化和增强
- [ ] 数据库索引优化
- [ ] 实现日志记录功能
- [ ] 添加API文档

## 🎯 预期成果

1. **系统稳定性提升** - 修复关键问题，确保系统正常运行
2. **功能完整性** - 实现电商核心业务功能
3. **性能优化** - 添加缓存和索引优化
4. **可维护性** - 完善日志和文档
5. **开发效率** - 提供完整的API文档

## 🔍 验收标准

1. **认证功能正常** - 用户可以正常注册、登录、登出
2. **业务功能完整** - 所有API接口按照设计文档实现
3. **数据一致性** - 数据库操作正确，事务处理正常
4. **性能达标** - 响应时间符合要求，缓存生效
5. **文档完善** - API文档完整，代码注释清晰