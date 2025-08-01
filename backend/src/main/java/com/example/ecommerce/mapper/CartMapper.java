package com.example.ecommerce.mapper;

import com.example.ecommerce.entity.Cart;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 购物车Mapper接口
 */
@Mapper
public interface CartMapper {

    /**
     * 插入购物车项
     *
     * @param cart 购物车项
     * @return 影响行数
     */
    @Insert({
        "<script>",
        "INSERT INTO cart_items (user_id, product_id, product_name, product_image, product_sku, " +
        "product_price, quantity, subtotal, category_id, category_name, status, selected, created_at, updated_at)",
        "VALUES (#{userId}, #{productId}, #{productName}, #{productImage}, #{productSku}, " +
        "#{productPrice}, #{quantity}, #{subtotal}, #{categoryId}, #{categoryName}, " +
        "#{status}, #{selected}, #{createdAt}, #{updatedAt})",
        "</script>"
    })
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Cart cart);

    /**
     * 批量插入购物车项
     *
     * @param cartItems 购物车项列表
     * @return 影响行数
     */
    @Insert({
        "<script>",
        "INSERT INTO cart_items (user_id, product_id, product_name, product_image, product_sku, " +
        "product_price, quantity, subtotal, category_id, category_name, status, selected, created_at, updated_at)",
        "VALUES ",
        "<foreach collection='cartItems' item='item' separator=','>",
        "(#{item.userId}, #{item.productId}, #{item.productName}, #{item.productImage}, #{item.productSku}, " +
        "#{item.productPrice}, #{item.quantity}, #{item.subtotal}, #{item.categoryId}, #{item.categoryName}, " +
        "#{item.status}, #{item.selected}, #{item.createdAt}, #{item.updatedAt})",
        "</foreach>",
        "</script>"
    })
    int batchInsert(@Param("cartItems") List<Cart> cartItems);

    /**
     * 根据ID查询购物车项
     *
     * @param id 购物车项ID
     * @return 购物车项
     */
    @Select("SELECT * FROM cart_items WHERE id = #{id}")
    Cart selectById(Long id);

    /**
     * 根据用户ID和商品ID查询购物车项
     *
     * @param userId   用户ID
     * @param productId 商品ID
     * @return 购物车项
     */
    @Select("SELECT * FROM cart_items WHERE user_id = #{userId} AND product_id = #{productId} AND status = 1")
    Cart selectByUserIdAndProductId(@Param("userId") Long userId, @Param("productId") Long productId);

    /**
     * 根据用户ID查询购物车列表
     *
     * @param userId 用户ID
     * @return 购物车列表
     */
    @Select("SELECT * FROM cart_items WHERE user_id = #{userId} AND status = 1 ORDER BY id DESC")
    List<Cart> selectByUserId(Long userId);

    /**
     * 查询购物车列表（分页）
     *
     * @param offset 偏移量
     * @param limit  限制数量
     * @return 购物车列表
     */
    @Select("SELECT * FROM cart_items WHERE status = 1 ORDER BY id DESC LIMIT #{offset}, #{limit}")
    List<Cart> selectPage(@Param("offset") int offset, @Param("limit") int limit);

    /**
     * 查询购物车列表（带条件）
     *
     * @param offset     偏移量
     * @param limit      限制数量
     * @param userId     用户ID
     * @param productId  商品ID
     * @param categoryId 分类ID
     * @param status     状态
     * @param selected   选中状态
     * @return 购物车列表
     */
    @Select("<script>" +
            "SELECT * FROM cart_items WHERE 1=1" +
            "<if test='userId != null'> AND user_id = #{userId}</if>" +
            "<if test='productId != null'> AND product_id = #{productId}</if>" +
            "<if test='categoryId != null'> AND category_id = #{categoryId}</if>" +
            "<if test='status != null'> AND status = #{status}</if>" +
            "<if test='selected != null'> AND selected = #{selected}</if>" +
            "ORDER BY id DESC LIMIT #{offset}, #{limit}" +
            "</script>")
    List<Cart> selectAll(
            @Param("offset") int offset,
            @Param("limit") int limit,
            @Param("userId") Long userId,
            @Param("productId") Long productId,
            @Param("categoryId") Long categoryId,
            @Param("status") Integer status,
            @Param("selected") Boolean selected
    );

    /**
     * 查询购物车总数
     *
     * @param userId     用户ID
     * @param productId  商品ID
     * @param categoryId 分类ID
     * @param status     状态
     * @param selected   选中状态
     * @return 总数
     */
    @Select("<script>" +
            "SELECT COUNT(*) FROM cart_items WHERE 1=1" +
            "<if test='userId != null'> AND user_id = #{userId}</if>" +
            "<if test='productId != null'> AND product_id = #{productId}</if>" +
            "<if test='categoryId != null'> AND category_id = #{categoryId}</if>" +
            "<if test='status != null'> AND status = #{status}</if>" +
            "<if test='selected != null'> AND selected = #{selected}</if>" +
            "</script>")
    int count(
            @Param("userId") Long userId,
            @Param("productId") Long productId,
            @Param("categoryId") Long categoryId,
            @Param("status") Integer status,
            @Param("selected") Boolean selected
    );

    /**
     * 更新购物车项
     *
     * @param cart 购物车项
     * @return 影响行数
     */
    @Update({
        "<script>",
        "UPDATE cart_items",
        "<set>",
        "<if test='userId != null'>user_id = #{userId},</if>",
        "<if test='productId != null'>product_id = #{productId},</if>",
        "<if test='productName != null'>product_name = #{productName},</if>",
        "<if test='productImage != null'>product_image = #{productImage},</if>",
        "<if test='productSku != null'>product_sku = #{productSku},</if>",
        "<if test='productPrice != null'>product_price = #{productPrice},</if>",
        "<if test='quantity != null'>quantity = #{quantity},</if>",
        "<if test='subtotal != null'>subtotal = #{subtotal},</if>",
        "<if test='categoryId != null'>category_id = #{categoryId},</if>",
        "<if test='categoryName != null'>category_name = #{categoryName},</if>",
        "<if test='status != null'>status = #{status},</if>",
        "<if test='selected != null'>selected = #{selected},</if>",
        "updated_at = #{updatedAt}",
        "</set>",
        "WHERE id = #{id}",
        "</script>"
    })
    int update(Cart cart);

    /**
     * 更新购物车项数量
     *
     * @param id       购物车项ID
     * @param quantity 数量
     * @return 影响行数
     */
    @Update("UPDATE cart_items SET quantity = #{quantity}, subtotal = product_price * #{quantity}, updated_at = NOW() WHERE id = #{id}")
    int updateQuantity(@Param("id") Long id, @Param("quantity") Integer quantity);

    /**
     * 更新选中状态
     *
     * @param id      购物车项ID
     * @param selected 选中状态
     * @return 影响行数
     */
    @Update("UPDATE cart_items SET selected = #{selected}, updated_at = NOW() WHERE id = #{id}")
    int updateSelected(@Param("id") Long id, @Param("selected") Boolean selected);

    /**
     * 批量更新选中状态
     *
     * @param userId   用户ID
     * @param selected 选中状态
     * @return 影响行数
     */
    @Update("UPDATE cart_items SET selected = #{selected}, updated_at = NOW() WHERE user_id = #{userId} AND status = 1")
    int batchUpdateSelected(@Param("userId") Long userId, @Param("selected") Boolean selected);

    /**
     * 根据ID删除购物车项
     *
     * @param id 购物车项ID
     * @return 影响行数
     */
    @Delete("DELETE FROM cart_items WHERE id = #{id}")
    int deleteById(Long id);

    /**
     * 根据用户ID和商品ID删除购物车项
     *
     * @param userId   用户ID
     * @param productId 商品ID
     * @return 影响行数
     */
    @Delete("DELETE FROM cart_items WHERE user_id = #{userId} AND product_id = #{productId}")
    int deleteByUserIdAndProductId(@Param("userId") Long userId, @Param("productId") Long productId);

    /**
     * 根据用户ID删除购物车项
     *
     * @param userId 用户ID
     * @return 影响行数
     */
    @Delete("DELETE FROM cart_items WHERE user_id = #{userId}")
    int deleteByUserId(Long userId);

    /**
     * 软删除购物车项
     *
     * @param id 购物车项ID
     * @return 影响行数
     */
    @Update("UPDATE cart_items SET status = 0, updated_at = NOW() WHERE id = #{id}")
    int softDelete(Long id);

    /**
     * 软删除用户的所有购物车项
     *
     * @param userId 用户ID
     * @return 影响行数
     */
    @Update("UPDATE cart_items SET status = 0, updated_at = NOW() WHERE user_id = #{userId}")
    int softDeleteByUserId(Long userId);

    /**
     * 清空用户购物车
     *
     * @param userId 用户ID
     * @return 影响行数
     */
    @Delete("DELETE FROM cart_items WHERE user_id = #{userId}")
    int clearCart(Long userId);

    /**
     * 计算用户购物车总金额
     *
     * @param userId 用户ID
     * @return 总金额
     */
    @Select("SELECT COALESCE(SUM(subtotal), 0) FROM cart_items WHERE user_id = #{userId} AND status = 1 AND selected = 1")
    java.math.BigDecimal calculateTotalAmount(@Param("userId") Long userId);

    /**
     * 计算用户购物车商品总数
     *
     * @param userId 用户ID
     * @return 总数
     */
    @Select("SELECT COALESCE(SUM(quantity), 0) FROM cart_items WHERE user_id = #{userId} AND status = 1 AND selected = 1")
    int calculateTotalQuantity(@Param("userId") Long userId);

    /**
     * 检查购物车项是否存在
     *
     * @param id 购物车项ID
     * @return 是否存在
     */
    @Select("SELECT COUNT(*) > 0 FROM cart_items WHERE id = #{id} AND status = 1")
    boolean existsById(Long id);

    /**
     * 检查购物车项是否存在
     *
     * @param userId   用户ID
     * @param productId 商品ID
     * @return 是否存在
     */
    @Select("SELECT COUNT(*) > 0 FROM cart_items WHERE user_id = #{userId} AND product_id = #{productId} AND status = 1")
    boolean existsByUserIdAndProductId(@Param("userId") Long userId, @Param("productId") Long productId);

    /**
     * 检查购物车项是否属于指定用户
     *
     * @param id     购物车项ID
     * @param userId 用户ID
     * @return 是否属于
     */
    @Select("SELECT COUNT(*) > 0 FROM cart_items WHERE id = #{id} AND user_id = #{userId} AND status = 1")
    boolean belongsToUser(@Param("id") Long id, @Param("userId") Long userId);
}