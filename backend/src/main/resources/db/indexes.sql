-- 数据库索引优化脚本
-- 为提高查询性能，添加必要的索引

-- 用户表索引优化
-- 添加复合索引，支持多条件查询
CREATE INDEX IF NOT EXISTS idx_user_status_created_at ON users(status, created_at);
CREATE INDEX IF NOT EXISTS idx_user_role_created_at ON users(role, created_at);
CREATE INDEX IF NOT EXISTS idx_user_nickname ON users(nickname);
CREATE INDEX IF NOT EXISTS idx_user_email_phone ON users(email, phone);
CREATE INDEX IF NOT EXISTS idx_user_created_at ON users(created_at);
CREATE INDEX IF NOT EXISTS idx_user_updated_at ON users(updated_at);

-- 角色表索引优化
CREATE INDEX IF NOT EXISTS idx_role_name ON roles(name);
CREATE INDEX IF NOT EXISTS idx_role_created_at ON roles(created_at);

-- 用户角色关联表索引优化
CREATE INDEX IF NOT EXISTS idx_user_roles_user_id ON user_roles(user_id);
CREATE INDEX IF NOT EXISTS idx_user_roles_role_id ON user_roles(role_id);
CREATE INDEX IF NOT EXISTS idx_user_roles_created_at ON user_roles(created_at);

-- 商品分类表索引优化
-- 添加复合索引，支持多条件查询
CREATE INDEX IF NOT EXISTS idx_category_parent_status ON categories(parent_id, status);
CREATE INDEX IF NOT EXISTS idx_category_status_sort ON categories(status, sort_order);
CREATE INDEX IF NOT EXISTS idx_category_name ON categories(name);
CREATE INDEX IF NOT EXISTS idx_category_created_at ON categories(created_at);
CREATE INDEX IF NOT EXISTS idx_category_updated_at ON categories(updated_at);

-- 商品表索引优化
-- 添加复合索引，支持多条件查询
CREATE INDEX IF NOT EXISTS idx_product_category_status ON products(category_id, status);
CREATE INDEX IF NOT EXISTS idx_product_status_price ON products(status, price);
CREATE INDEX IF NOT EXISTS idx_product_category_price ON products(category_id, price);
CREATE INDEX IF NOT EXISTS idx_product_name ON products(name);
CREATE INDEX IF NOT EXISTS idx_product_sku ON products(sku);
CREATE INDEX IF NOT EXISTS idx_product_barcode ON products(barcode);
CREATE INDEX IF NOT EXISTS idx_product_created_at ON products(created_at);
CREATE INDEX IF NOT EXISTS idx_product_updated_at ON products(updated_at);

-- 商品库存表索引优化
CREATE INDEX IF NOT EXISTS idx_inventory_product_id ON product_inventory(product_id);
CREATE INDEX IF NOT EXISTS idx_inventory_quantity ON product_inventory(quantity);
CREATE INDEX IF NOT EXISTS idx_inventory_reserved_quantity ON product_inventory(reserved_quantity);
CREATE INDEX IF NOT EXISTS idx_inventory_created_at ON product_inventory(created_at);
CREATE INDEX IF NOT EXISTS idx_inventory_updated_at ON product_inventory(updated_at);

-- 订单表索引优化
CREATE TABLE IF NOT EXISTS orders (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    order_no VARCHAR(50) NOT NULL UNIQUE COMMENT '订单号',
    total_amount DECIMAL(12, 2) NOT NULL COMMENT '总金额',
    shipping_fee DECIMAL(10, 2) DEFAULT 0.00 COMMENT '运费',
    discount_amount DECIMAL(10, 2) DEFAULT 0.00 COMMENT '优惠金额',
    payment_method VARCHAR(20) COMMENT '支付方式',
    payment_status VARCHAR(20) DEFAULT 'PENDING' COMMENT '支付状态',
    order_status VARCHAR(20) DEFAULT 'PENDING' COMMENT '订单状态',
    shipping_address TEXT COMMENT '收货地址',
    shipping_name VARCHAR(100) COMMENT '收货人姓名',
    shipping_phone VARCHAR(20) COMMENT '收货人电话',
    shipping_region VARCHAR(100) COMMENT '收货地区',
    shipping_address_detail VARCHAR(255) COMMENT '收货地址详情',
    shipping_postal_code VARCHAR(10) COMMENT '邮政编码',
    note TEXT COMMENT '订单备注',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (user_id) REFERENCES users(id),
    INDEX idx_order_user_id (user_id),
    INDEX idx_order_status (order_status),
    INDEX idx_order_total_amount (total_amount),
    INDEX idx_order_created_at (created_at),
    INDEX idx_order_updated_at (updated_at),
    INDEX idx_order_user_status (user_id, order_status),
    INDEX idx_order_status_created_at (order_status, created_at)
) COMMENT '订单表';

-- 订单项表索引优化
CREATE TABLE IF NOT EXISTS order_items (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    order_id BIGINT NOT NULL COMMENT '订单ID',
    product_id BIGINT NOT NULL COMMENT '商品ID',
    product_name VARCHAR(200) NOT NULL COMMENT '商品名称',
    product_image VARCHAR(255) COMMENT '商品图片',
    quantity INT NOT NULL COMMENT '购买数量',
    unit_price DECIMAL(10, 2) NOT NULL COMMENT '单价',
    total_price DECIMAL(10, 2) NOT NULL COMMENT '总价',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    INDEX idx_order_item_order_id (order_id),
    INDEX idx_order_item_product_id (product_id),
    INDEX idx_order_item_order_product (order_id, product_id),
    INDEX idx_order_item_created_at (created_at)
) COMMENT '订单项表';

-- 购物车表索引优化
CREATE TABLE IF NOT EXISTS cart_items (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    product_id BIGINT NOT NULL COMMENT '商品ID',
    product_name VARCHAR(200) NOT NULL COMMENT '商品名称',
    product_image VARCHAR(255) COMMENT '商品图片',
    quantity INT NOT NULL DEFAULT 1 COMMENT '数量',
    unit_price DECIMAL(10, 2) NOT NULL COMMENT '单价',
    total_price DECIMAL(10, 2) NOT NULL COMMENT '总价',
    selected TINYINT(1) DEFAULT 1 COMMENT '是否选中',
    status TINYINT(1) DEFAULT 1 COMMENT '状态：1正常，0删除',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (product_id) REFERENCES products(id),
    INDEX idx_cart_user_id (user_id),
    INDEX idx_cart_product_id (product_id),
    INDEX idx_cart_user_product (user_id, product_id),
    INDEX idx_cart_status (status),
    INDEX idx_cart_selected (selected),
    INDEX idx_cart_user_status (user_id, status),
    INDEX idx_cart_user_selected (user_id, selected),
    INDEX idx_cart_created_at (created_at),
    INDEX idx_cart_updated_at (updated_at)
) COMMENT '购物车表';

-- 商品评价表索引优化
CREATE TABLE IF NOT EXISTS product_reviews (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    product_id BIGINT NOT NULL COMMENT '商品ID',
    order_id BIGINT NOT NULL COMMENT '订单ID',
    rating TINYINT NOT NULL COMMENT '评分：1-5',
    content TEXT COMMENT '评价内容',
    images TEXT COMMENT '图片JSON',
    status TINYINT(1) DEFAULT 1 COMMENT '状态：1显示，0隐藏',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (product_id) REFERENCES products(id),
    FOREIGN KEY (order_id) REFERENCES orders(id),
    INDEX idx_review_user_id (user_id),
    INDEX idx_review_product_id (product_id),
    INDEX idx_review_rating (rating),
    INDEX idx_review_status (status),
    INDEX idx_review_created_at (created_at),
    INDEX idx_review_product_status (product_id, status),
    INDEX idx_review_user_product (user_id, product_id)
) COMMENT '商品评价表';

-- 商品收藏表索引优化
CREATE TABLE IF NOT EXISTS product_favorites (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    product_id BIGINT NOT NULL COMMENT '商品ID',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (product_id) REFERENCES products(id),
    INDEX idx_favorite_user_id (user_id),
    INDEX idx_favorite_product_id (product_id),
    INDEX idx_favorite_user_product (user_id, product_id),
    INDEX idx_favorite_created_at (created_at)
) COMMENT '商品收藏表';

-- 商品浏览记录表索引优化
CREATE TABLE IF NOT EXISTS product_views (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    product_id BIGINT NOT NULL COMMENT '商品ID',
    view_count INT NOT NULL DEFAULT 1 COMMENT '浏览次数',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (product_id) REFERENCES products(id),
    INDEX idx_view_user_id (user_id),
    INDEX idx_view_product_id (product_id),
    INDEX idx_view_user_product (user_id, product_id),
    INDEX idx_view_created_at (created_at)
) COMMENT '商品浏览记录表';

-- 商品标签表索引优化
CREATE TABLE IF NOT EXISTS product_tags (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    name VARCHAR(50) NOT NULL UNIQUE COMMENT '标签名称',
    color VARCHAR(20) COMMENT '标签颜色',
    sort_order INT NOT NULL DEFAULT 0 COMMENT '排序',
    status TINYINT(1) DEFAULT 1 COMMENT '状态：1显示，0隐藏',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_tag_name (name),
    INDEX idx_tag_created_at (created_at)
) COMMENT '商品标签表';

-- 商品标签关联表索引优化
CREATE TABLE IF NOT EXISTS product_tag_relations (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    product_id BIGINT NOT NULL COMMENT '商品ID',
    tag_id BIGINT NOT NULL COMMENT '标签ID',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    FOREIGN KEY (tag_id) REFERENCES product_tags(id) ON DELETE CASCADE,
    INDEX idx_product_tag_product_id (product_id),
    INDEX idx_product_tag_tag_id (tag_id),
    INDEX idx_product_tag_product_tag (product_id, tag_id)
) COMMENT '商品标签关联表';

-- 商品规格表索引优化
CREATE TABLE IF NOT EXISTS product_specs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    product_id BIGINT NOT NULL COMMENT '商品ID',
    name VARCHAR(100) NOT NULL COMMENT '规格名称',
    sort_order INT NOT NULL DEFAULT 0 COMMENT '排序',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    INDEX idx_spec_product_id (product_id),
    INDEX idx_spec_name (name),
    INDEX idx_spec_created_at (created_at)
) COMMENT '商品规格表';

-- 商品规格值表索引优化
CREATE TABLE IF NOT EXISTS spec_values (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    spec_id BIGINT NOT NULL COMMENT '规格ID',
    value VARCHAR(100) NOT NULL COMMENT '规格值',
    sort_order INT NOT NULL DEFAULT 0 COMMENT '排序',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (spec_id) REFERENCES product_specs(id) ON DELETE CASCADE,
    INDEX idx_spec_value_spec_id (spec_id),
    INDEX idx_spec_value_value (value),
    INDEX idx_spec_value_spec_value (spec_id, value)
) COMMENT '商品规格值表';

-- 商品规格组合表索引优化
CREATE TABLE IF NOT EXISTS product_combinations (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    product_id BIGINT NOT NULL COMMENT '商品ID',
    specs JSON NOT NULL COMMENT '规格组合JSON',
    sku VARCHAR(100) NOT NULL UNIQUE COMMENT 'SKU',
    barcode VARCHAR(100) COMMENT '条形码',
    price DECIMAL(10, 2) NOT NULL COMMENT '价格',
    market_price DECIMAL(10, 2) COMMENT '市场价',
    cost_price DECIMAL(10, 2) COMMENT '成本价',
    stock INT NOT NULL DEFAULT 0 COMMENT '库存',
    sales INT NOT NULL DEFAULT 0 COMMENT '销量',
    image VARCHAR(255) COMMENT '商品图片',
    status TINYINT(1) DEFAULT 1 COMMENT '状态：1上架，0下架',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    INDEX idx_combination_product_id (product_id),
    INDEX idx_combination_specs (specs),
    INDEX idx_combination_sku (sku),
    INDEX idx_combination_created_at (created_at)
) COMMENT '商品规格组合表';

-- 商品图片表索引优化
CREATE TABLE IF NOT EXISTS product_images (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    product_id BIGINT NOT NULL COMMENT '商品ID',
    image_url VARCHAR(255) NOT NULL COMMENT '图片URL',
    image_type VARCHAR(20) DEFAULT 'DETAIL' COMMENT '图片类型：MAIN-主图，DETAIL-详情图',
    sort_order INT NOT NULL DEFAULT 0 COMMENT '排序',
    is_cover TINYINT(1) DEFAULT 0 COMMENT '是否封面图',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    INDEX idx_image_product_id (product_id),
    INDEX idx_image_type (image_type),
    INDEX idx_image_product_type (product_id, image_type),
    INDEX idx_image_created_at (created_at)
) COMMENT '商品图片表';

-- 商品属性表索引优化
CREATE TABLE IF NOT EXISTS product_attributes (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    product_id BIGINT NOT NULL COMMENT '商品ID',
    name VARCHAR(100) NOT NULL COMMENT '属性名称',
    value TEXT COMMENT '属性值',
    sort_order INT NOT NULL DEFAULT 0 COMMENT '排序',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    INDEX idx_attr_product_id (product_id),
    INDEX idx_attr_name (name),
    INDEX idx_attr_product_name (product_id, name)
) COMMENT '商品属性表';

-- 商品属性值表索引优化
CREATE TABLE IF NOT EXISTS attribute_values (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    attribute_id BIGINT NOT NULL COMMENT '属性ID',
    value VARCHAR(255) NOT NULL COMMENT '属性值',
    sort_order INT NOT NULL DEFAULT 0 COMMENT '排序',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (attribute_id) REFERENCES product_attributes(id) ON DELETE CASCADE,
    INDEX idx_attr_value_attr_id (attribute_id),
    INDEX idx_attr_value_value (value),
    INDEX idx_attr_value_attr_value (attribute_id, value)
) COMMENT '商品属性值表';

-- 商品属性组合表索引优化
CREATE TABLE IF NOT EXISTS product_attribute_combinations (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    product_id BIGINT NOT NULL COMMENT '商品ID',
    attributes JSON NOT NULL COMMENT '属性组合JSON',
    sku VARCHAR(100) NOT NULL UNIQUE COMMENT 'SKU',
    price DECIMAL(10, 2) NOT NULL COMMENT '价格',
    stock INT NOT NULL DEFAULT 0 COMMENT '库存',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    INDEX idx_attr_combination_product_id (product_id),
    INDEX idx_attr_combination_attributes (attributes),
    INDEX idx_attr_combination_created_at (created_at)
) COMMENT '商品属性组合表';

-- 商品促销表索引优化
CREATE TABLE IF NOT EXISTS product_promotions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    product_id BIGINT NOT NULL COMMENT '商品ID',
    promotion_name VARCHAR(200) NOT NULL COMMENT '促销名称',
    promotion_type VARCHAR(50) NOT NULL COMMENT '促销类型',
    start_date TIMESTAMP NOT NULL COMMENT '开始时间',
    end_date TIMESTAMP NOT NULL COMMENT '结束时间',
    discount_type VARCHAR(20) NOT NULL COMMENT '折扣类型：PERCENT-百分比，FIXED-固定金额',
    discount_value DECIMAL(10, 2) NOT NULL COMMENT '折扣值',
    min_quantity INT DEFAULT 1 COMMENT '最小购买数量',
    max_quantity INT COMMENT '最大购买数量',
    status TINYINT(1) DEFAULT 1 COMMENT '状态：1启用，0禁用',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    INDEX idx_promotion_product_id (product_id),
    INDEX idx_promotion_type (promotion_type),
    INDEX idx_promotion_status (status),
    INDEX idx_promotion_start_date (start_date),
    INDEX idx_promotion_end_date (end_date),
    INDEX idx_promotion_product_status (product_id, status),
    INDEX idx_promotion_type_status (promotion_type, status)
) COMMENT '商品促销表';

-- 商品优惠券表索引优化
CREATE TABLE IF NOT EXISTS product_coupons (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    product_id BIGINT NOT NULL COMMENT '商品ID',
    coupon_name VARCHAR(200) NOT NULL COMMENT '优惠券名称',
    coupon_code VARCHAR(50) NOT NULL UNIQUE COMMENT '优惠券代码',
    coupon_type VARCHAR(50) NOT NULL COMMENT '优惠券类型',
    start_date TIMESTAMP NOT NULL COMMENT '开始时间',
    end_date TIMESTAMP NOT NULL COMMENT '结束时间',
    discount_type VARCHAR(20) NOT NULL COMMENT '折扣类型：PERCENT-百分比，FIXED-固定金额',
    discount_value DECIMAL(10, 2) NOT NULL COMMENT '折扣值',
    min_amount DECIMAL(10, 2) DEFAULT 0.00 COMMENT '最小使用金额',
    max_amount DECIMAL(10, 2) COMMENT '最大使用金额',
    usage_limit INT DEFAULT 0 COMMENT '使用次数限制',
    used_count INT DEFAULT 0 COMMENT '已使用次数',
    status TINYINT(1) DEFAULT 1 COMMENT '状态：1启用，0禁用',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    INDEX idx_coupon_product_id (product_id),
    INDEX idx_coupon_type (coupon_type),
    INDEX idx_coupon_status (status),
    INDEX idx_coupon_start_date (start_date),
    INDEX idx_coupon_end_date (end_date),
    INDEX idx_coupon_product_status (product_id, status),
    INDEX idx_coupon_type_status (coupon_type, status)
) COMMENT '商品优惠券表';

-- 商品优惠券使用记录表索引优化
CREATE TABLE IF NOT EXISTS coupon_usage_records (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    coupon_id BIGINT NOT NULL COMMENT '优惠券ID',
    order_id BIGINT NOT NULL COMMENT '订单ID',
    used_amount DECIMAL(10, 2) NOT NULL COMMENT '使用金额',
    used_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '使用时间',
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (coupon_id) REFERENCES product_coupons(id),
    FOREIGN KEY (order_id) REFERENCES orders(id),
    INDEX idx_coupon_usage_user_id (user_id),
    INDEX idx_coupon_usage_coupon_id (coupon_id),
    INDEX idx_coupon_usage_user_coupon (user_id, coupon_id),
    INDEX idx_coupon_usage_used_at (used_at)
) COMMENT '商品优惠券使用记录表';

-- 商品配送表索引优化
CREATE TABLE IF NOT EXISTS product_shipping (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    order_id BIGINT NOT NULL COMMENT '订单ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    shipping_company VARCHAR(100) COMMENT '物流公司',
    tracking_number VARCHAR(100) COMMENT '物流单号',
    shipping_status VARCHAR(50) DEFAULT 'PENDING' COMMENT '配送状态',
    shipping_address TEXT COMMENT '配送地址',
    shipping_name VARCHAR(100) COMMENT '收货人姓名',
    shipping_phone VARCHAR(20) COMMENT '收货人电话',
    shipping_region VARCHAR(100) COMMENT '收货地区',
    shipping_address_detail VARCHAR(255) COMMENT '收货地址详情',
    shipping_postal_code VARCHAR(10) COMMENT '邮政编码',
    estimated_delivery_time TIMESTAMP COMMENT '预计送达时间',
    actual_delivery_time TIMESTAMP COMMENT '实际送达时间',
    shipping_fee DECIMAL(10, 2) NOT NULL COMMENT '运费',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (order_id) REFERENCES orders(id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    INDEX idx_shipping_order_id (order_id),
    INDEX idx_shipping_user_id (user_id),
    INDEX idx_shipping_status (shipping_status),
    INDEX idx_shipping_created_at (created_at),
    INDEX idx_shipping_user_status (user_id, shipping_status)
) COMMENT '商品配送表';

-- 商品配送地址表索引优化
CREATE TABLE IF NOT EXISTS shipping_addresses (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    recipient_name VARCHAR(100) NOT NULL COMMENT '收货人姓名',
    recipient_phone VARCHAR(20) NOT NULL COMMENT '收货人电话',
    province VARCHAR(50) NOT NULL COMMENT '省份',
    city VARCHAR(50) NOT NULL COMMENT '城市',
    district VARCHAR(50) NOT NULL COMMENT '区县',
    address_detail VARCHAR(255) NOT NULL COMMENT '详细地址',
    postal_code VARCHAR(10) COMMENT '邮政编码',
    is_default TINYINT(1) DEFAULT 0 COMMENT '是否默认地址',
    status TINYINT(1) DEFAULT 1 COMMENT '状态：1正常，0禁用',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (user_id) REFERENCES users(id),
    INDEX idx_shipping_address_user_id (user_id),
    INDEX idx_shipping_address_default (is_default),
    INDEX idx_shipping_address_user_default (user_id, is_default)
) COMMENT '商品配送地址表';

-- 商品配送方式表索引优化
CREATE TABLE IF NOT EXISTS shipping_methods (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    name VARCHAR(100) NOT NULL COMMENT '配送方式名称',
    description TEXT COMMENT '配送方式描述',
    shipping_type VARCHAR(50) NOT NULL COMMENT '配送类型：STANDARD-标准快递，EXPRESS-特快专递，SAME_DAY-当日达',
    base_fee DECIMAL(10, 2) NOT NULL COMMENT '基础运费',
    additional_fee DECIMAL(10, 2) DEFAULT 0.00 COMMENT '续重运费',
    free_amount DECIMAL(10, 2) DEFAULT 0.00 COMMENT '免运费金额',
    estimated_days INT COMMENT '预计天数',
    sort_order INT NOT NULL DEFAULT 0 COMMENT '排序',
    status TINYINT(1) DEFAULT 1 COMMENT '状态：1启用，0禁用',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_shipping_method_name (name),
    INDEX idx_shipping_method_status (status),
    INDEX idx_shipping_method_created_at (created_at)
) COMMENT '商品配送方式表';

-- 商品配送费用表索引优化
CREATE TABLE IF NOT EXISTS shipping_fees (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    method_id BIGINT NOT NULL COMMENT '配送方式ID',
    region_id BIGINT NOT NULL COMMENT '地区ID',
    weight_from DECIMAL(10, 2) NOT NULL DEFAULT 0.00 COMMENT '重量起始值',
    weight_to DECIMAL(10, 2) NOT NULL COMMENT '重量结束值',
)