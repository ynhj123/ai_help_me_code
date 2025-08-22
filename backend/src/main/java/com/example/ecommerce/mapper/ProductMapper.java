package com.example.ecommerce.mapper;

import com.example.ecommerce.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品Mapper接口
 */
@Mapper
public interface ProductMapper {

    /**
     * 插入商品
     *
     * @param product 商品
     * @return 影响行数
     */
    int insert(Product product);

    /**
     * 根据ID查询商品
     *
     * @param id 商品ID
     * @return 商品
     */
    Product selectById(Long id);

    /**
     * 根据SKU查询商品
     *
     * @param sku SKU
     * @return 商品
     */
    Product selectBySku(String sku);

    /**
     * 查询商品列表
     *
     * @param offset 偏移量
     * @param limit  限制数量
     * @param name 商品名称
     * @param categoryId 分类ID
     * @param sku SKU
     * @param status 状态
     * @param sortBy 排序字段
     * @param sortOrder 排序方式
     * @return 商品列表
     */
    List<Product> selectAll(@Param("offset") int offset, @Param("limit") int limit, 
                           @Param("name") String name, @Param("categoryId") Long categoryId,
                           @Param("sku") String sku, @Param("status") Integer status,
                           @Param("sortBy") String sortBy, @Param("sortOrder") String sortOrder);

    /**
     * 查询商品总数
     *
     * @param name 商品名称
     * @param categoryId 分类ID
     * @param sku SKU
     * @param status 状态
     * @return 总数
     */
    int count(@Param("name") String name, @Param("categoryId") Long categoryId,
              @Param("sku") String sku, @Param("status") Integer status);

    /**
     * 更新商品
     *
     * @param product 商品
     * @return 影响行数
     */
    int update(Product product);

    /**
     * 根据ID删除商品
     *
     * @param id 商品ID
     * @return 影响行数
     */
    int deleteById(Long id);

    /**
     * 根据分类ID查询商品数量
     *
     * @param categoryId 分类ID
     * @return 商品数量
     */
    int countByCategoryId(Long categoryId);
}