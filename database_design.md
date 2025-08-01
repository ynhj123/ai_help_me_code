# 后台管理系统数据库设计

## 1. 用户相关表

### 用户表 (users)
```sql
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100),
    phone VARCHAR(20),
    nickname VARCHAR(100),
    avatar VARCHAR(255),
    status TINYINT DEFAULT 1 COMMENT '用户状态：1正常，0禁用',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### 角色表 (roles)
```sql
CREATE TABLE roles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) UNIQUE NOT NULL,
    description VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### 用户角色关联表 (user_roles)
```sql
CREATE TABLE user_roles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE,
    UNIQUE KEY uk_user_role (user_id, role_id)
);
```

### 权限表 (permissions)
```sql
CREATE TABLE permissions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) UNIQUE NOT NULL,
    description VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### 角色权限关联表 (role_permissions)
```sql
CREATE TABLE role_permissions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    role_id BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE,
    FOREIGN KEY (permission_id) REFERENCES permissions(id) ON DELETE CASCADE,
    UNIQUE KEY uk_role_permission (role_id, permission_id)
);
```

## 2. 商品相关表

### 商品分类表 (categories)
```sql
CREATE TABLE categories (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    parent_id BIGINT DEFAULT 0 COMMENT '父级分类ID，0表示顶级分类',
    sort_order INT DEFAULT 0,
    status TINYINT DEFAULT 1 COMMENT '分类状态：1显示，0隐藏',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### 商品表 (products)
```sql
CREATE TABLE products (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(200) NOT NULL,
    description TEXT,
    category_id BIGINT NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    market_price DECIMAL(10, 2),
    cost_price DECIMAL(10, 2),
    sku VARCHAR(100) UNIQUE,
    barcode VARCHAR(100),
    image VARCHAR(255),
    gallery TEXT COMMENT '商品图片集，JSON格式',
    detail TEXT COMMENT '商品详情',
    status TINYINT DEFAULT 1 COMMENT '商品状态：1上架，0下架',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES categories(id)
);
```

### 商品库存表 (product_inventory)
```sql
CREATE TABLE product_inventory (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL DEFAULT 0,
    reserved_quantity INT NOT NULL DEFAULT 0 COMMENT '预留库存',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    UNIQUE KEY uk_product (product_id)
);
```

## 3. 订单相关表

### 订单表 (orders)
```sql
CREATE TABLE orders (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_no VARCHAR(50) UNIQUE NOT NULL,
    user_id BIGINT NOT NULL,
    status TINYINT NOT NULL DEFAULT 1 COMMENT '订单状态：1待付款，2已付款，3已发货，4已完成，5已取消',
    total_amount DECIMAL(10, 2) NOT NULL,
    discount_amount DECIMAL(10, 2) DEFAULT 0,
    shipping_amount DECIMAL(10, 2) DEFAULT 0,
    pay_amount DECIMAL(10, 2) NOT NULL,
    payment_method TINYINT COMMENT '支付方式：1银联，2微信，3支付宝',
    payment_time TIMESTAMP NULL,
    shipping_address TEXT,
    receiver_name VARCHAR(100),
    receiver_phone VARCHAR(20),
    shipping_time TIMESTAMP NULL,
    completed_time TIMESTAMP NULL,
    remark TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
```

### 订单项表 (order_items)
```sql
CREATE TABLE order_items (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    product_name VARCHAR(200) NOT NULL,
    product_sku VARCHAR(100),
    product_image VARCHAR(255),
    quantity INT NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    total_amount DECIMAL(10, 2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id)
);
```

### 支付记录表 (payments)
```sql
CREATE TABLE payments (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_no VARCHAR(50) NOT NULL,
    payment_method TINYINT NOT NULL COMMENT '支付方式：1银联，2微信，3支付宝',
    amount DECIMAL(10, 2) NOT NULL,
    status TINYINT NOT NULL DEFAULT 0 COMMENT '支付状态：0待支付，1已支付，2已取消，3支付失败',
    transaction_no VARCHAR(100) COMMENT '第三方交易号',
    payment_time TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

## 4. 物流相关表

### 物流信息表 (shipping_info)
```sql
CREATE TABLE shipping_info (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id BIGINT NOT NULL,
    shipping_company VARCHAR(100),
    tracking_number VARCHAR(100),
    status TINYINT DEFAULT 1 COMMENT '物流状态：1待发货，2已发货，3运输中，4已送达',
    shipped_at TIMESTAMP NULL,
    delivered_at TIMESTAMP NULL,
    delivery_personnel_id BIGINT NULL,
    remark TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (delivery_personnel_id) REFERENCES users(id)
);
```

## 5. 购物车相关表

### 购物车表 (shopping_cart)
```sql
CREATE TABLE shopping_cart (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    UNIQUE KEY uk_user_product (user_id, product_id)
);
```

## 6. 购物卡相关表

### 购物卡表 (gift_cards)
```sql
CREATE TABLE gift_cards (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    card_no VARCHAR(50) UNIQUE NOT NULL,
    balance DECIMAL(10, 2) NOT NULL,
    status TINYINT DEFAULT 1 COMMENT '状态：1有效，0已使用，-1已过期',
    expired_at TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### 购物卡使用记录表 (gift_card_usage)
```sql
CREATE TABLE gift_card_usage (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    card_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    order_id BIGINT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    type TINYINT NOT NULL COMMENT '类型：1充值，2消费',
    remark TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (card_id) REFERENCES gift_cards(id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (order_id) REFERENCES orders(id)
);
```

## 7. 日志相关表

### 操作日志表 (operation_logs)
```sql
CREATE TABLE operation_logs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NULL,
    username VARCHAR(50),
    operation VARCHAR(100) NOT NULL,
    method VARCHAR(10) NOT NULL,
    url VARCHAR(255),
    ip VARCHAR(50),
    user_agent TEXT,
    request_params TEXT,
    response_result TEXT,
    execution_time BIGINT COMMENT '执行时间(ms)',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
```

## 8. 文件存储相关表

### 文件表 (files)
```sql
CREATE TABLE files (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    original_name VARCHAR(255) NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    file_path VARCHAR(500) NOT NULL,
    file_size BIGINT NOT NULL,
    file_type VARCHAR(50),
    module VARCHAR(50) COMMENT '所属模块',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT NULL,
    FOREIGN KEY (created_by) REFERENCES users(id)
);
```

## 9. Dashboard相关表

### 统计数据表 (statistics)
```sql
CREATE TABLE statistics (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    stat_date DATE NOT NULL,
    dau INT DEFAULT 0 COMMENT '日活跃用户数',
    order_count INT DEFAULT 0 COMMENT '订单数',
    order_amount DECIMAL(15, 2) DEFAULT 0 COMMENT '订单金额',
    paid_order_count INT DEFAULT 0 COMMENT '已支付订单数',
    paid_order_amount DECIMAL(15, 2) DEFAULT 0 COMMENT '已支付订单金额',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_stat_date (stat_date)
);
```

## 10. 初始化数据

```sql
-- 初始化角色数据
INSERT INTO roles (name, description) VALUES 
('ADMIN', '管理员'),
('DISTRIBUTOR', '分销商'),
('DELIVERY_PERSONNEL', '运输人员'),
('USER', '普通用户');

-- 初始化权限数据（示例）
INSERT INTO permissions (name, description) VALUES 
('USER_MANAGE', '用户管理'),
('PRODUCT_MANAGE', '商品管理'),
('ORDER_MANAGE', '订单管理'),
('SHIPPING_MANAGE', '物流管理'),
('DASHBOARD_VIEW', '仪表盘查看');

-- 管理员角色拥有所有权限
INSERT INTO role_permissions (role_id, permission_id) 
SELECT r.id, p.id 
FROM roles r, permissions p 
WHERE r.name = 'ADMIN';

-- 初始化分类数据（示例）
INSERT INTO categories (name, description, parent_id, sort_order) VALUES 
('电子产品', '各类电子产品', 0, 1),
('服装', '各类服装', 0, 2),
('家居', '家居用品', 0, 3);