-- 数据库索引优化脚本
-- 用于添加复合索引和优化查询性能

-- 用户表复合索引优化
ALTER TABLE users ADD INDEX idx_status_role (status, role);
ALTER TABLE users ADD INDEX idx_email_status (email, status);
ALTER TABLE users ADD INDEX idx_phone_status (phone, status);
ALTER TABLE users ADD INDEX idx_created_status (created_at, status);

-- 商品分类表复合索引优化
ALTER TABLE categories ADD INDEX idx_parent_sort (parent_id, sort_order);
ALTER TABLE categories ADD INDEX idx_parent_status_sort (parent_id, status, sort_order);
ALTER TABLE categories ADD INDEX idx_name_parent (name, parent_id);

-- 商品表复合索引优化
ALTER TABLE products ADD INDEX idx_category_price (category_id, price);
ALTER TABLE products ADD INDEX idx_category_status_price (category_id, status, price);
ALTER TABLE products ADD INDEX idx_brand_status (brand, status);
ALTER TABLE products ADD INDEX idx_price_status (price, status);
ALTER TABLE products ADD INDEX idx_sku_status (sku, status);
ALTER TABLE products ADD INDEX idx_name_brand (name, brand);
ALTER TABLE products ADD INDEX idx_created_status (created_at, status);
ALTER TABLE products ADD INDEX idx_updated_status (updated_at, status);

-- 商品库存表复合索引优化
ALTER TABLE product_inventory ADD INDEX idx_product_quantity (product_id, quantity);
ALTER TABLE product_inventory ADD INDEX idx_product_reserved (product_id, reserved_quantity);
ALTER TABLE product_inventory ADD INDEX idx_quantity_reserved (quantity, reserved_quantity);
ALTER TABLE product_inventory ADD INDEX idx_low_stock (quantity, reserved_quantity) WHERE quantity < 10;
ALTER TABLE product_inventory ADD INDEX idx_out_of_stock (quantity) WHERE quantity = 0;

-- 购物车表复合索引优化
ALTER TABLE cart_items ADD INDEX idx_user_product_status (user_id, product_id, status);
ALTER TABLE cart_items ADD INDEX idx_user_category_status (user_id, category_id, status);
ALTER TABLE cart_items ADD INDEX idx_user_selected_status (user_id, selected, status);
ALTER TABLE cart_items ADD INDEX idx_product_status (product_id, status);
ALTER TABLE cart_items ADD INDEX idx_category_status (category_id, status);
ALTER TABLE cart_items ADD INDEX idx_price_quantity (product_price, quantity);
ALTER TABLE cart_items ADD INDEX idx_created_user (created_at, user_id);
ALTER TABLE cart_items ADD INDEX idx_updated_user (updated_at, user_id);

-- 订单表复合索引优化
ALTER TABLE orders ADD INDEX idx_user_order_no (user_id, order_no);
ALTER TABLE orders ADD INDEX idx_user_status_created (user_id, status, created_at);
ALTER TABLE orders ADD INDEX idx_status_user_created (status, user_id, created_at);
ALTER TABLE orders ADD INDEX idx_payment_status (payment_method, status);
ALTER TABLE orders ADD INDEX idx_paid_status_created (paid_at, status, created_at);
ALTER TABLE orders ADD INDEX idx_total_amount (total_amount);
ALTER TABLE orders ADD INDEX idx_paid_amount (paid_amount);
ALTER TABLE orders ADD INDEX idx_logistics_tracking (logistics_company, tracking_number);
ALTER TABLE orders ADD INDEX idx_cancelled (cancelled_at) WHERE cancelled_at IS NOT NULL;
ALTER TABLE orders ADD INDEX idx_completed (completed_at) WHERE completed_at IS NOT NULL;
ALTER TABLE orders ADD INDEX idx_shipped (shipped_at) WHERE shipped_at IS NOT NULL;

-- 订单商品表复合索引优化
ALTER TABLE order_items ADD INDEX idx_order_product_category (order_id, product_id, category_id);
ALTER TABLE order_items ADD INDEX idx_product_order (product_id, order_id);
ALTER TABLE order_items ADD INDEX idx_category_order (category_id, order_id);
ALTER TABLE order_items ADD INDEX idx_price_quantity_order (product_price, quantity, order_id);
ALTER TABLE order_items ADD INDEX idx_order_created_product (order_id, created_at, product_id);
ALTER TABLE order_items ADD INDEX idx_product_created_order (product_id, created_at, order_id);

-- 系统日志表复合索引优化
ALTER TABLE system_logs ADD INDEX idx_user_action_created (user_id, action, created_at);
ALTER TABLE system_logs ADD INDEX idx_action_resource (action, resource_type, resource_id);
ALTER TABLE system_logs ADD INDEX idx_resource_action (resource_type, resource_id, action);
ALTER TABLE system_logs ADD INDEX idx_ip_created (ip_address, created_at);
ALTER TABLE system_logs ADD INDEX idx_type_created (resource_type, created_at);

-- 文件上传表复合索引优化
ALTER TABLE file_uploads ADD INDEX idx_type_created (file_type, created_at);
ALTER TABLE file_uploads ADD INDEX idx_mime_created (mime_type, created_at);
ALTER TABLE file_uploads ADD INDEX idx_size_created (file_size, created_at);
ALTER TABLE file_uploads ADD INDEX idx_user_type (created_by, file_type);
ALTER TABLE file_uploads ADD INDEX idx_name_type (original_name, file_type);

-- 用户角色关联表复合索引优化
ALTER TABLE user_roles ADD INDEX idx_role_user (role_id, user_id);
ALTER TABLE user_roles ADD INDEX idx_created_role (created_at, role_id);

-- 分页查询优化索引
-- 为常见的分页查询场景添加索引

-- 用户列表分页查询
ALTER TABLE users ADD INDEX idx_pagination_status_created (status, created_at DESC);

-- 商品列表分页查询
ALTER TABLE products ADD INDEX idx_pagination_category_status_created (category_id, status, created_at DESC);
ALTER TABLE products ADD INDEX idx_pagination_status_created (status, created_at DESC);

-- 分类列表分页查询
ALTER TABLE categories ADD INDEX idx_pagination_parent_status_created (parent_id, status, created_at DESC);
ALTER TABLE categories ADD INDEX idx_pagination_status_created (status, created_at DESC);

-- 订单列表分页查询
ALTER TABLE orders ADD INDEX idx_pagination_user_status_created (user_id, status, created_at DESC);
ALTER TABLE orders ADD INDEX idx_pagination_status_created (status, created_at DESC);

-- 购物车列表分页查询
ALTER TABLE cart_items ADD INDEX idx_pagination_user_status_created (user_id, status, created_at DESC);
ALTER TABLE cart_items ADD INDEX idx_pagination_status_created (status, created_at DESC);

-- 库存列表分页查询
ALTER TABLE product_inventory ADD INDEX idx_pagination_product_created (product_id, created_at DESC);
ALTER TABLE product_inventory ADD INDEX idx_pagination_quantity_created (quantity, created_at DESC);

-- 日志列表分页查询
ALTER TABLE system_logs ADD INDEX idx_pagination_user_created (user_id, created_at DESC);
ALTER TABLE system_logs ADD INDEX idx_pagination_action_created (action, created_at DESC);
ALTER TABLE system_logs ADD INDEX idx_pagination_type_created (resource_type, created_at DESC);

-- 文件列表分页查询
ALTER TABLE file_uploads ADD INDEX idx_pagination_type_created (file_type, created_at DESC);
ALTER TABLE file_uploads ADD INDEX idx_pagination_user_created (created_by, created_at DESC);

-- 搜索查询优化索引
-- 为常见的搜索查询场景添加索引

-- 用户搜索
ALTER TABLE users ADD INDEX idx_search_username_email (username, email);
ALTER TABLE users ADD INDEX idx_search_nickname (nickname);

-- 商品搜索
ALTER TABLE products ADD INDEX idx_search_name_sku (name, sku);
ALTER TABLE products ADD INDEX idx_search_name_brand (name, brand);
ALTER TABLE products ADD INDEX idx_search_sku_brand (sku, brand);

-- 分类搜索
ALTER TABLE categories ADD INDEX idx_search_name_parent (name, parent_id);

-- 订单搜索
ALTER TABLE orders ADD INDEX idx_search_order_no (order_no);
ALTER TABLE orders ADD INDEX idx_search_receiver (receiver_name, receiver_phone);

-- 统计查询优化索引
-- 为常见的统计查询场景添加索引

-- 用户统计
ALTER TABLE users ADD INDEX idx_stats_status_created (status, DATE(created_at));

-- 商品统计
ALTER TABLE products ADD INDEX idx_stats_category_status_created (category_id, status, DATE(created_at));
ALTER TABLE products ADD INDEX idx_stats_brand_status_created (brand, status, DATE(created_at));

-- 订单统计
ALTER TABLE orders ADD INDEX idx_stats_user_status_created (user_id, status, DATE(created_at));
ALTER TABLE orders ADD INDEX idx_stats_status_paid_created (status, paid_at, DATE(created_at));

-- 购物车统计
ALTER TABLE cart_items ADD INDEX idx_stats_user_status_created (user_id, status, DATE(created_at));

-- 库存统计
ALTER TABLE product_inventory ADD INDEX idx_stats_product_created (product_id, DATE(created_at));

-- 时间范围查询优化索引
-- 为常见的时间范围查询场景添加索引

-- 用户时间范围查询
ALTER TABLE users ADD INDEX idx_time_range_created (created_at);
ALTER TABLE users ADD INDEX idx_time_range_updated (updated_at);

-- 商品时间范围查询
ALTER TABLE products ADD INDEX idx_time_range_created (created_at);
ALTER TABLE products ADD INDEX idx_time_range_updated (updated_at);

-- 订单时间范围查询
ALTER TABLE orders ADD INDEX idx_time_range_created (created_at);
ALTER TABLE orders ADD INDEX idx_time_range_updated (updated_at);
ALTER TABLE orders ADD INDEX idx_time_range_paid (paid_at);
ALTER TABLE orders ADD INDEX idx_time_range_shipped (shipped_at);
ALTER TABLE orders ADD INDEX idx_time_range_completed (completed_at);
ALTER TABLE orders ADD INDEX idx_time_range_cancelled (cancelled_at);

-- 购物车时间范围查询
ALTER TABLE cart_items ADD INDEX idx_time_range_created (created_at);
ALTER TABLE cart_items ADD INDEX idx_time_range_updated (updated_at);

-- 库存时间范围查询
ALTER TABLE product_inventory ADD INDEX idx_time_range_created (created_at);
ALTER TABLE product_inventory ADD INDEX idx_time_range_updated (updated_at);

-- 日志时间范围查询
ALTER TABLE system_logs ADD INDEX idx_time_range_created (created_at);

-- 文件时间范围查询
ALTER TABLE file_uploads ADD INDEX idx_time_range_created (created_at);
ALTER TABLE file_uploads ADD INDEX idx_time_range_updated (updated_at);

-- 数据分区优化（如果数据量很大）
-- 这里提供分区方案，实际使用时需要根据数据量来决定是否启用

-- 按时间范围分区的示例（MySQL 8.0+）
-- ALTER TABLE orders PARTITION BY RANGE (TO_DAYS(created_at)) (
--     PARTITION p2023 VALUES LESS THAN (TO_DAYS('2024-01-01')),
--     PARTITION p2024 VALUES LESS THAN (TO_DAYS('2025-01-01')),
--     PARTITION p2025 VALUES LESS THAN (TO_DAYS('2026-01-01')),
--     PARTITION pmax VALUES LESS THAN MAXVALUE
-- );

-- 按用户ID分区的示例（适用于用户表）
-- ALTER TABLE users PARTITION BY HASH(id) PARTITIONS 16;

-- 按商品ID分区的示例（适用于商品表）
-- ALTER TABLE products PARTITION BY HASH(id) PARTITIONS 16;

-- 按订单ID分区的示例（适用于订单表）
-- ALTER TABLE orders PARTITION BY HASH(id) PARTITIONS 16;

-- 索引维护脚本
-- 定期执行以保持索引性能

-- 分析表统计信息
ANALYZE TABLE users, roles, user_roles, categories, products, product_inventory, cart_items, orders, order_items, system_logs, file_uploads;

-- 优化表（如果需要）
-- OPTIMIZE TABLE users, roles, user_roles, categories, products, product_inventory, cart_items, orders, order_items, system_logs, file_uploads;

-- 清理碎片（InnoDB表）
-- ALTER TABLE users ENGINE=InnoDB;
-- ALTER TABLE roles ENGINE=InnoDB;
-- ALTER TABLE user_roles ENGINE=InnoDB;
-- ALTER TABLE categories ENGINE=InnoDB;
-- ALTER TABLE products ENGINE=InnoDB;
-- ALTER TABLE product_inventory ENGINE=InnoDB;
-- ALTER TABLE cart_items ENGINE=InnoDB;
-- ALTER TABLE orders ENGINE=InnoDB;
-- ALTER TABLE order_items ENGINE=InnoDB;
-- ALTER TABLE system_logs ENGINE=InnoDB;
-- ALTER TABLE file_uploads ENGINE=InnoDB;

-- 索引使用情况监控
-- 可以定期执行以下查询来监控索引使用情况

-- 查看索引使用情况
-- SELECT * FROM sys.schema_index_statistics WHERE table_schema = 'your_database_name';

-- 查看未使用的索引
-- SELECT * FROM sys.schema_unused_indexes WHERE table_schema = 'your_database_name';

-- 查看重复的索引
-- SELECT * FROM sys.schema_redundant_indexes WHERE table_schema = 'your_database_name';

-- 查看索引碎片情况
-- SELECT table_name, index_name, round(data_free/1024/1024, 2) as fragmentation_mb 
-- FROM information_schema.tables 
-- WHERE table_schema = 'your_database_name' 
-- AND data_free > 0;

-- 索引维护建议
-- 1. 定期分析表统计信息
-- 2. 监控索引使用情况，删除未使用的索引
-- 3. 定期优化表以减少碎片
-- 4. 根据查询模式调整索引策略
-- 5. 对于大表，考虑使用分区
-- 6. 定期备份索引维护脚本
-- 7. 在低峰期执行索引维护操作
-- 8. 监控查询性能，根据实际情况调整索引

-- 注意事项
-- 1. 在生产环境执行索引优化前，请先在测试环境验证
-- 2. 索引不是越多越好，过多的索引会影响写入性能
-- 3. 定期监控索引使用情况，及时调整索引策略
-- 4. 对于大表，索引维护操作可能会影响性能，建议在低峰期执行
-- 5. 根据实际业务需求调整索引策略