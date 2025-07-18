-- 初始化数据库表结构
-- 由于使用Flyway，这里只需要创建基础数据

-- 创建角色
INSERT INTO roles (id, name, description, created_at, updated_at) VALUES
(1, 'ROLE_ADMIN', '管理员', NOW(), NOW()),
(2, 'ROLE_USER', '普通用户', NOW(), NOW());

-- 创建权限
INSERT INTO permissions (id, name, description, created_at, updated_at) VALUES
(1, 'user:read', '查看用户', NOW(), NOW()),
(2, 'user:write', '编辑用户', NOW(), NOW()),
(3, 'user:delete', '删除用户', NOW(), NOW()),
(4, 'product:read', '查看商品', NOW(), NOW()),
(5, 'product:write', '编辑商品', NOW(), NOW()),
(6, 'product:delete', '删除商品', NOW(), NOW()),
(7, 'order:read', '查看订单', NOW(), NOW()),
(8, 'order:write', '编辑订单', NOW(), NOW()),
(9, 'order:delete', '删除订单', NOW(), NOW()),
(10, 'logistics:read', '查看物流', NOW(), NOW()),
(11, 'logistics:write', '编辑物流', NOW(), NOW()),
(12, 'dashboard:read', '查看仪表盘', NOW(), NOW());

-- 角色权限关联
INSERT INTO role_permissions (role_id, permission_id) VALUES
-- 管理员拥有所有权限
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 6), (1, 7), (1, 8), (1, 9), (1, 10), (1, 11), (1, 12),
-- 普通用户只有查看权限
(2, 1), (2, 4), (2, 7), (2, 10), (2, 12);

-- 创建管理员用户 (密码: admin123)
INSERT INTO users (id, username, email, password, first_name, last_name, phone, status, created_at, updated_at) VALUES
(1, 'admin', 'admin@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBaUKk7h.T0mUO', '管理员', '用户', '13800138000', 'ACTIVE', NOW(), NOW());

-- 给用户分配管理员角色
INSERT INTO user_roles (user_id, role_id) VALUES
(1, 1);

-- 创建商品分类
INSERT INTO categories (id, name, description, sort_order, status, created_at, updated_at) VALUES
(1, '电子产品', '手机、电脑等电子设备', 1, 'ACTIVE', NOW(), NOW()),
(2, '服装', '男装、女装、童装', 2, 'ACTIVE', NOW(), NOW()),
(3, '家居用品', '家具、装饰、日用品', 3, 'ACTIVE', NOW(), NOW()),
(4, '图书', '小说、教材、工具书', 4, 'ACTIVE', NOW(), NOW()),
(5, '运动户外', '运动装备、户外用品', 5, 'ACTIVE', NOW(), NOW());

-- 创建商品
INSERT INTO products (id, name, description, sku, price, stock_quantity, category_id, status, created_at, updated_at) VALUES
(1, 'iPhone 15 Pro', '苹果最新旗舰手机，A17芯片，钛金属机身', 'IPHONE15PRO-128', 7999.00, 100, 1, 'ACTIVE', NOW(), NOW()),
(2, 'MacBook Air M3', '轻薄笔记本电脑，M3芯片，续航18小时', 'MBA-M3-256', 8999.00, 50, 1, 'ACTIVE', NOW(), NOW()),
(3, 'Nike Air Max 270', '经典运动鞋，舒适缓震', 'NIKE-AM270-BLK', 899.00, 200, 5, 'ACTIVE', NOW(), NOW()),
(4, '《Java编程思想》', 'Java编程经典教材，第4版', 'JAVA-THINKING-4', 108.00, 500, 4, 'ACTIVE', NOW(), NOW()),
(5, '宜家书桌', '简约现代书桌，实木材质', 'IKEA-DESK-120', 599.00, 30, 3, 'ACTIVE', NOW(), NOW());

-- 创建物流公司
INSERT INTO carriers (id, name, code, contact_person, phone, email, status, created_at, updated_at) VALUES
(1, '顺丰速运', 'SF', '张经理', '95338', 'service@sf-express.com', 'ACTIVE', NOW(), NOW()),
(2, '中通快递', 'ZTO', '李经理', '95311', 'service@zto.com', 'ACTIVE', NOW(), NOW()),
(3, '圆通速递', 'YTO', '王经理', '95554', 'service@yto.net.cn', 'ACTIVE', NOW(), NOW());

-- 创建测试订单
INSERT INTO orders (id, order_number, user_id, total_amount, status, payment_method, payment_status, shipping_address, created_at, updated_at) VALUES
('ORD-202407180001', 'ORD-202407180001', 1, 8097.00, 'PENDING', 'ALIPAY', 'PENDING', '北京市朝阳区xxx路xxx号', NOW(), NOW()),
('ORD-202407180002', 'ORD-202407180002', 1, 899.00, 'PAID', 'WECHAT', 'PAID', '上海市浦东新区xxx路xxx号', NOW(), NOW());

-- 创建订单商品
INSERT INTO order_items (id, order_id, product_id, quantity, price, created_at, updated_at) VALUES
(1, 'ORD-202407180001', 1, 1, 7999.00, NOW(), NOW()),
(2, 'ORD-202407180001', 4, 1, 108.00, NOW(), NOW()),
(3, 'ORD-202407180002', 3, 1, 899.00, NOW(), NOW());

-- 创建物流订单
INSERT INTO shipping_orders (id, order_id, carrier_id, tracking_number, status, estimated_delivery, created_at, updated_at) VALUES
(1, 'ORD-202407180002', 1, 'SF1234567890', 'IN_TRANSIT', NOW() + INTERVAL '3 days', NOW(), NOW());