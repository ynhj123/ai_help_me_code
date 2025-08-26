-- 创建ecommerce数据库
CREATE DATABASE IF NOT EXISTS ecommerce 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

-- 切换到ecommerce数据库
USE ecommerce;

-- 显示数据库信息
SHOW DATABASES;
SELECT DATABASE();