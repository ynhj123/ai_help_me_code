# Spring Boot 项目编译错误修复计划

## 问题分析

根据编译错误，项目存在以下主要问题：

### 1. MapStruct映射错误
- **文件**: `backend/src/main/java/com/admin/modules/order/mapper/OrderMapper.java:19`
- **错误**: No property named "user.username" exists in source parameter(s)
- **原因**: Order实体中没有user对象，而是直接有userName和userPhone字段

### 2. 缺失的Repository类
- **缺失文件**:
  - `OrderRepository` - 在order模块中缺失
  - `ProductImageRepository` - 在product模块中缺失
  - `ProductAttributeRepository` - 在product模块中缺失

### 3. 缺失的依赖导入
- **文件**: `backend/src/main/java/com/admin/modules/product/dto/ProductCreateRequest.java`
- **错误**: 找不到符号 @Valid
- **原因**: 缺少jakarta.validation.Valid的导入

## 修复步骤

### 步骤1: 修复OrderMapper映射问题
**文件**: `backend/src/main/java/com/admin/modules/order/mapper/OrderMapper.java`
```java
// 修改前
@Mapping(target = "userName", source = "user.username")
@Mapping(target = "userPhone", source = "user.phone")

// 修改后
@Mapping(target = "userName", source = "userName")
@Mapping(target = "userPhone", source = "userPhone")
```

### 步骤2: 创建缺失的Repository接口

#### 2.1 创建OrderRepository
**文件**: `backend/src/main/java/com/admin/modules/order/repository/OrderRepository.java`
```java
package com.admin.modules.order.repository;

import com.admin.modules.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
}
```

#### 2.2 创建ProductImageRepository
**文件**: `backend/src/main/java/com/admin/modules/product/repository/ProductImageRepository.java`
```java
package com.admin.modules.product.repository;

import com.admin.modules.product.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    List<ProductImage> findByProductId(Long productId);
}
```

#### 2.3 创建ProductAttributeRepository
**文件**: `backend/src/main/java/com/admin/modules/product/repository/ProductAttributeRepository.java`
```java
package com.admin.modules.product.repository;

import com.admin.modules.product.entity.ProductAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductAttributeRepository extends JpaRepository<ProductAttribute, Long> {
    List<ProductAttribute> findByProductId(Long productId);
}
```

### 步骤3: 修复导入问题
**文件**: `backend/src/main/java/com/admin/modules/product/dto/ProductCreateRequest.java`
```java
// 添加缺失的导入
import jakarta.validation.Valid;
```

### 步骤4: 验证实体类
需要确认以下实体类存在：
- `ProductImage` - 用于ProductImageRepository
- `ProductAttribute` - 用于ProductAttributeRepository

### 步骤5: 重新编译和测试
```bash
cd backend
./gradlew clean build
```

## 验证清单

- [ ] OrderMapper中的映射已修复
- [ ] OrderRepository已创建
- [ ] ProductImageRepository已创建
- [ ] ProductAttributeRepository已创建
- [ ] ProductCreateRequest中的@Valid导入已修复
- [ ] 项目成功编译
- [ ] 所有单元测试通过

## 注意事项

1. 确保所有实体类都有正确的JPA注解
2. 检查数据库连接配置
3. 验证Spring Data JPA依赖是否正确配置
4. 确认Lombok和MapStruct注解处理器工作正常