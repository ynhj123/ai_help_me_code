-- 创建用户表
CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码',
    email VARCHAR(100) COMMENT '邮箱',
    phone VARCHAR(20) COMMENT '手机号',
    nickname VARCHAR(100) COMMENT '昵称',
    avatar VARCHAR(255) COMMENT '头像',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1正常，0禁用',
    role VARCHAR(20) NOT NULL DEFAULT 'USER' COMMENT '角色',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_username (username),
    INDEX idx_email (email),
    INDEX idx_phone (phone),
    INDEX idx_status (status),
    INDEX idx_role (role),
    INDEX idx_created_at (created_at)
) COMMENT '用户表';

-- 创建角色表
CREATE TABLE IF NOT EXISTS roles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    name VARCHAR(50) NOT NULL UNIQUE COMMENT '角色名称',
    description VARCHAR(255) COMMENT '角色描述',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_name (name)
) COMMENT '角色表';

-- 创建用户角色关联表
CREATE TABLE IF NOT EXISTS user_roles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE,
    UNIQUE KEY uk_user_role (user_id, role_id),
    INDEX idx_user_id (user_id),
    INDEX idx_role_id (role_id)
) COMMENT '用户角色关联表';

-- 创建商品分类表
CREATE TABLE IF NOT EXISTS categories (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    name VARCHAR(100) NOT NULL COMMENT '分类名称',
    description TEXT COMMENT '分类描述',
    parent_id BIGINT NOT NULL DEFAULT 0 COMMENT '父级分类ID',
    sort_order INT NOT NULL DEFAULT 0 COMMENT '排序',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1显示，0隐藏',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_parent_id (parent_id),
    INDEX idx_status (status),
    INDEX idx_sort_order (sort_order),
    INDEX idx_parent_status (parent_id, status),
    INDEX idx_created_at (created_at)
) COMMENT '商品分类表';

-- 创建商品表
CREATE TABLE IF NOT EXISTS products (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    name VARCHAR(200) NOT NULL COMMENT '商品名称',
    description TEXT COMMENT '商品描述',
    category_id BIGINT NOT NULL COMMENT '分类ID',
    price DECIMAL(10, 2) NOT NULL COMMENT '价格',
    market_price DECIMAL(10, 2) COMMENT '市场价',
    cost_price DECIMAL(10, 2) COMMENT '成本价',
    sku VARCHAR(100) NOT NULL UNIQUE COMMENT 'SKU',
    barcode VARCHAR(100) COMMENT '条形码',
    brand VARCHAR(100) COMMENT '品牌',
    image VARCHAR(255) COMMENT '商品图片',
    gallery TEXT COMMENT '商品图集',
    detail TEXT COMMENT '商品详情',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1上架，0下架',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (category_id) REFERENCES categories(id),
    INDEX idx_category_id (category_id),
    INDEX idx_sku (sku),
    INDEX idx_status (status),
    INDEX idx_price (price),
    INDEX idx_brand (brand),
    INDEX idx_category_status (category_id, status),
    INDEX idx_created_at (created_at),
    INDEX idx_updated_at (updated_at)
) COMMENT '商品表';

-- 创建商品库存表
CREATE TABLE IF NOT EXISTS product_inventory (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    product_id BIGINT NOT NULL COMMENT '商品ID',
    quantity INT NOT NULL DEFAULT 0 COMMENT '库存数量',
    reserved_quantity INT NOT NULL DEFAULT 0 COMMENT '预留库存数量',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    UNIQUE KEY uk_product_id (product_id),
    INDEX idx_quantity (quantity),
    INDEX idx_reserved_quantity (reserved_quantity),
    INDEX idx_available_quantity ((quantity - reserved_quantity)),
    INDEX idx_product_status (product_id, (SELECT status FROM products WHERE id = product_id)),
    INDEX idx_created_at (created_at),
    INDEX idx_updated_at (updated_at)
) COMMENT '商品库存表';

-- 创建购物车表
CREATE TABLE IF NOT EXISTS cart_items (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    product_id BIGINT NOT NULL COMMENT '商品ID',
    product_name VARCHAR(200) NOT NULL COMMENT '商品名称',
    product_image VARCHAR(255) COMMENT '商品图片',
    product_sku VARCHAR(100) NOT NULL COMMENT '商品SKU',
    product_price DECIMAL(10, 2) NOT NULL COMMENT '商品价格',
    quantity INT NOT NULL DEFAULT 1 COMMENT '购买数量',
    subtotal DECIMAL(10, 2) NOT NULL COMMENT '小计',
    category_id BIGINT NOT NULL COMMENT '分类ID',
    category_name VARCHAR(100) NOT NULL COMMENT '分类名称',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1正常，0删除',
    selected TINYINT NOT NULL DEFAULT 1 COMMENT '是否选中：1选中，0未选中',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_product_id (product_id),
    INDEX idx_category_id (category_id),
    INDEX idx_status (status),
    INDEX idx_selected (selected),
    INDEX idx_user_status (user_id, status),
    INDEX idx_user_selected (user_id, selected),
    INDEX idx_user_product (user_id, product_id),
    INDEX idx_created_at (created_at),
    INDEX idx_updated_at (updated_at),
    UNIQUE KEY uk_user_product (user_id, product_id)
) COMMENT '购物车表';

-- 创建订单表
CREATE TABLE IF NOT EXISTS orders (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    order_no VARCHAR(50) NOT NULL UNIQUE COMMENT '订单编号',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    receiver_name VARCHAR(100) NOT NULL COMMENT '收货人姓名',
    receiver_phone VARCHAR(20) NOT NULL COMMENT '收货人电话',
    receiver_address TEXT NOT NULL COMMENT '收货地址',
    total_amount DECIMAL(10, 2) NOT NULL COMMENT '订单总金额',
    product_amount DECIMAL(10, 2) NOT NULL COMMENT '商品金额',
    shipping_fee DECIMAL(10, 2) NOT NULL DEFAULT 0 COMMENT '运费',
    discount_amount DECIMAL(10, 2) NOT NULL DEFAULT 0 COMMENT '优惠金额',
    paid_amount DECIMAL(10, 2) NOT NULL COMMENT '实付金额',
    payment_method VARCHAR(20) COMMENT '支付方式',
    paid_at TIMESTAMP NULL COMMENT '支付时间',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1待付款，2已付款，3已发货，4已完成，5已取消',
    remark TEXT COMMENT '订单备注',
    logistics_company VARCHAR(100) COMMENT '物流公司',
    tracking_number VARCHAR(100) COMMENT '物流单号',
    shipped_at TIMESTAMP NULL COMMENT '发货时间',
    completed_at TIMESTAMP NULL COMMENT '完成时间',
    cancelled_at TIMESTAMP NULL COMMENT '取消时间',
    cancel_reason TEXT COMMENT '取消原因',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_order_no (order_no),
    INDEX idx_user_id (user_id),
    INDEX idx_status (status),
    INDEX idx_payment_method (payment_method),
    INDEX idx_paid_at (paid_at),
    INDEX idx_created_at (created_at),
    INDEX idx_updated_at (updated_at),
    INDEX idx_user_status (user_id, status),
    INDEX idx_status_created (status, created_at),
    INDEX idx_paid_status (paid_at, status)
) COMMENT '订单表';

-- 创建订单商品表
CREATE TABLE IF NOT EXISTS order_items (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    order_id BIGINT NOT NULL COMMENT '订单ID',
    product_id BIGINT NOT NULL COMMENT '商品ID',
    product_name VARCHAR(200) NOT NULL COMMENT '商品名称',
    product_image VARCHAR(255) COMMENT '商品图片',
    product_sku VARCHAR(100) NOT NULL COMMENT '商品SKU',
    product_price DECIMAL(10, 2) NOT NULL COMMENT '商品价格',
    quantity INT NOT NULL COMMENT '购买数量',
    subtotal DECIMAL(10, 2) NOT NULL COMMENT '小计',
    category_id BIGINT NOT NULL COMMENT '分类ID',
    category_name VARCHAR(100) NOT NULL COMMENT '分类名称',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    INDEX idx_order_id (order_id),
    INDEX idx_product_id (product_id),
    INDEX idx_category_id (category_id),
    INDEX idx_order_product (order_id, product_id),
    INDEX idx_created_at (created_at),
    INDEX idx_updated_at (updated_at),
    INDEX idx_order_created (order_id, created_at)
) COMMENT '订单商品表';

-- 创建日志表
CREATE TABLE IF NOT EXISTS system_logs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    user_id BIGINT COMMENT '用户ID',
    action VARCHAR(100) NOT NULL COMMENT '操作动作',
    resource_type VARCHAR(50) NOT NULL COMMENT '资源类型',
    resource_id BIGINT COMMENT '资源ID',
    description TEXT COMMENT '操作描述',
    ip_address VARCHAR(45) COMMENT 'IP地址',
    user_agent TEXT COMMENT '用户代理',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL,
    INDEX idx_user_id (user_id),
    INDEX idx_action (action),
    INDEX idx_resource_type (resource_type),
    INDEX idx_resource_id (resource_id),
    INDEX idx_created_at (created_at),
    INDEX idx_user_action (user_id, action)
) COMMENT '系统日志表';

-- 创建文件上传表
CREATE TABLE IF NOT EXISTS file_uploads (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    original_name VARCHAR(255) NOT NULL COMMENT '原始文件名',
    stored_name VARCHAR(255) NOT NULL COMMENT '存储文件名',
    file_path VARCHAR(500) NOT NULL COMMENT '文件路径',
    file_size BIGINT NOT NULL COMMENT '文件大小',
    mime_type VARCHAR(100) NOT NULL COMMENT '文件类型',
    file_type VARCHAR(50) NOT NULL COMMENT '文件分类',
    created_by BIGINT COMMENT '创建人',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE SET NULL,
    INDEX idx_file_type (file_type),
    INDEX idx_created_by (created_by),
    INDEX idx_created_at (created_at),
    INDEX idx_mime_type (mime_type)
) COMMENT '文件上传表';

-- 初始化默认角色
INSERT IGNORE INTO roles (name, description) VALUES
('ADMIN', '管理员'),
('DISTRIBUTOR', '分销商'),
('DELIVERY_PERSONNEL', '运输人员'),
('USER', '普通用户');

-- 初始化默认管理员用户
INSERT IGNORE INTO users (username, password, email, phone, nickname, status, role) VALUES
('admin', '$2a$10$w9ziyLKe3EvIfkSsPzT9jO6.W7DZvQzC41Z9B52rAyTz4dCq8uK6u', 'admin@example.com', '13800138000', '管理员', 1, 'ADMIN');