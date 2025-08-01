package com.example.ecommerce.mapper;

import com.example.ecommerce.entity.Product;
import org.apache.ibatis.annotations.*;

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
    @Insert({
        "<script>",
        "INSERT INTO products (name, description, category_id, price, market_price, cost_price, sku, barcode, image, gallery, detail, status, created_at, updated_at)",
        "VALUES (#{name}, #{description}, #{categoryId}, #{price}, #{marketPrice}, #{costPrice}, #{sku}, #{barcode}, #{image}, #{gallery}, #{detail}, #{status}, #{createdAt}, #{updatedAt})",
        "</script>"
    })
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Product product);

    /**
     * 根据ID查询商品
     *
     * @param id 商品ID
     * @return 商品
     */
    @Select("SELECT * FROM products WHERE id = #{id}")
    Product selectById(Long id);

    /**
     * 根据SKU查询商品
     *
     * @param sku SKU
     * @return 商品
     */
    @Select("SELECT * FROM products WHERE sku = #{sku}")
    Product selectBySku(String sku);

    /**
     * 查询商品列表
     *
     * @param offset 偏移量
     * @param limit  限制数量
     * @return 商品列表
     */
    @Select("<script>" +
            "SELECT * FROM products WHERE 1=1 " +
            "<if test='name != null and name != \"\"'>AND name LIKE CONCAT('%', #{name}, '%')</if>" +
            "<if test='categoryId != null'>AND category_id = #{categoryId}</if>" +
            "<if test='sku != null and sku != \"\"'>AND sku = #{sku}</if>" +
            "<if test='status != null'>AND status = #{status}</if>" +
            "ORDER BY ${sortBy} ${sortOrder} LIMIT #{offset}, #{limit}" +
            "</script>")
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
    @Select("<script>" +
            "SELECT COUNT(*) FROM products WHERE 1=1 " +
            "<if test='name != null and name != \"\"'>AND name LIKE CONCAT('%', #{name}, '%')</if>" +
            "<if test='categoryId != null'>AND category_id = #{categoryId}</if>" +
            "<if test='sku != null and sku != \"\"'>AND sku = #{sku}</if>" +
            "<if test='status != null'>AND status = #{status}</if>" +
            "</script>")
    int count(@Param("name") String name, @Param("categoryId") Long categoryId,
              @Param("sku") String sku, @Param("status") Integer status);

    /**
     * 更新商品
     *
     * @param product 商品
     * @return 影响行数
     */
    @Update({
        "<script>",
        "UPDATE products",
        "<set>",
        "<if test='name != null'>name = #{name},</if>",
        "<if test='description != null'>description = #{description},</if>",
        "<if test='categoryId != null'>category_id = #{categoryId},</if>",
        "<if test='price != null'>price = #{price},</if>",
        "<if test='marketPrice != null'>market_price = #{marketPrice},</if>",
        "<if test='costPrice != null'>cost_price = #{costPrice},</if>",
        "<if test='sku != null'>sku = #{sku},</if>",
        "<if test='barcode != null'>barcode = #{barcode},</if>",
        "<if test='image != null'>image = #{image},</if>",
        "<if test='gallery != null'>gallery = #{gallery},</if>",
        "<if test='detail != null'>detail = #{detail},</if>",
        "<if test='status != null'>status = #{status},</if>",
        "updated_at = #{updatedAt}",
        "</set>",
        "WHERE id = #{id}",
        "</script>"
    })
    int update(Product product);

    /**
     * 根据ID删除商品
     *
     * @param id 商品ID
     * @return 影响行数
     */
    @Delete("DELETE FROM products WHERE id = #{id}")
    int deleteById(Long id);

    /**
     * 根据分类ID查询商品数量
     *
     * @param categoryId 分类ID
     * @return 商品数量
     */
    @Select("SELECT COUNT(*) FROM products WHERE category_id = #{categoryId}")
    int countByCategoryId(Long categoryId);
}