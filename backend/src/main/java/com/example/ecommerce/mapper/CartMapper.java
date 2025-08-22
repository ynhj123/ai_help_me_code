package com.example.ecommerce.mapper;

import com.example.ecommerce.entity.Cart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
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
    int insert(Cart cart);

    /**
     * 批量插入购物车项
     *
     * @param cartItems 购物车项列表
     * @return 影响行数
     */
    int batchInsert(@Param("cartItems") List<Cart> cartItems);

    /**
     * 根据ID查询购物车项
     *
     * @param id 购物车项ID
     * @return 购物车项
     */
    Cart selectById(Long id);

    /**
     * 根据用户ID和商品ID查询购物车项
     *
     * @param userId   用户ID
     * @param productId 商品ID
     * @return 购物车项
     */
    Cart selectByUserIdAndProductId(@Param("userId") Long userId, @Param("productId") Long productId);

    /**
     * 根据用户ID查询购物车列表
     *
     * @param userId 用户ID
     * @return 购物车列表
     */
    List<Cart> selectByUserId(Long userId);

    /**
     * 查询购物车列表（分页）
     *
     * @param offset 偏移量
     * @param limit  限制数量
     * @return 购物车列表
     */
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
    int update(Cart cart);

    /**
     * 更新购物车项数量
     *
     * @param id       购物车项ID
     * @param quantity 数量
     * @return 影响行数
     */
    int updateQuantity(@Param("id") Long id, @Param("quantity") Integer quantity);

    /**
     * 更新选中状态
     *
     * @param id      购物车项ID
     * @param selected 选中状态
     * @return 影响行数
     */
    int updateSelected(@Param("id") Long id, @Param("selected") Boolean selected);

    /**
     * 批量更新选中状态
     *
     * @param userId   用户ID
     * @param selected 选中状态
     * @return 影响行数
     */
    int batchUpdateSelected(@Param("userId") Long userId, @Param("selected") Boolean selected);

    /**
     * 根据ID删除购物车项
     *
     * @param id 购物车项ID
     * @return 影响行数
     */
    int deleteById(Long id);

    /**
     * 根据用户ID和商品ID删除购物车项
     *
     * @param userId   用户ID
     * @param productId 商品ID
     * @return 影响行数
     */
    int deleteByUserIdAndProductId(@Param("userId") Long userId, @Param("productId") Long productId);

    /**
     * 根据用户ID删除购物车项
     *
     * @param userId 用户ID
     * @return 影响行数
     */
    int deleteByUserId(Long userId);

    /**
     * 软删除购物车项
     *
     * @param id 购物车项ID
     * @return 影响行数
     */
    int softDelete(Long id);

    /**
     * 软删除用户的所有购物车项
     *
     * @param userId 用户ID
     * @return 影响行数
     */
    int softDeleteByUserId(Long userId);

    /**
     * 清空用户购物车
     *
     * @param userId 用户ID
     * @return 影响行数
     */
    int clearCart(Long userId);

    /**
     * 计算用户购物车总金额
     *
     * @param userId 用户ID
     * @return 总金额
     */
    BigDecimal calculateTotalAmount(@Param("userId") Long userId);

    /**
     * 计算用户购物车商品总数
     *
     * @param userId 用户ID
     * @return 总数
     */
    int calculateTotalQuantity(@Param("userId") Long userId);

    /**
     * 检查购物车项是否存在
     *
     * @param id 购物车项ID
     * @return 是否存在
     */
    boolean existsById(Long id);

    /**
     * 检查购物车项是否存在
     *
     * @param userId   用户ID
     * @param productId 商品ID
     * @return 是否存在
     */
    boolean existsByUserIdAndProductId(@Param("userId") Long userId, @Param("productId") Long productId);

    /**
     * 检查购物车项是否属于指定用户
     *
     * @param id     购物车项ID
     * @param userId 用户ID
     * @return 是否属于
     */
    boolean belongsToUser(@Param("id") Long id, @Param("userId") Long userId);
}