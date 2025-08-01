package com.example.ecommerce.service;

import com.example.ecommerce.dto.CartDTO;

import java.util.List;

/**
 * 购物车服务接口
 */
public interface CartService {

    /**
     * 添加商品到购物车
     *
     * @param cartDTO 购物车项
     * @return 添加的购物车项
     */
    CartDTO addToCart(CartDTO cartDTO);

    /**
     * 更新购物车项数量
     *
     * @param id       购物车项ID
     * @param quantity 数量
     * @return 更新后的购物车项
     */
    CartDTO updateQuantity(Long id, Integer quantity);

    /**
     * 更新购物车项选中状态
     *
     * @param id      购物车项ID
     * @param selected 选中状态
     * @return 更新后的购物车项
     */
    CartDTO updateSelected(Long id, Boolean selected);

    /**
     * 批量更新选中状态
     *
     * @param userId   用户ID
     * @param selected 选中状态
     * @return 更新结果
     */
    boolean batchUpdateSelected(Long userId, Boolean selected);

    /**
     * 获取购物车项
     *
     * @param id 购物车项ID
     * @return 购物车项
     */
    CartDTO getCartById(Long id);

    /**
     * 根据用户ID和商品ID获取购物车项
     *
     * @param userId   用户ID
     * @param productId 商品ID
     * @return 购物车项
     */
    CartDTO getCartByUserIdAndProductId(Long userId, Long productId);

    /**
     * 获取用户购物车列表
     *
     * @param userId 用户ID
     * @return 购物车列表
     */
    List<CartDTO> getCartByUserId(Long userId);

    /**
     * 获取购物车列表（分页）
     *
     * @param page 页码
     * @param size 每页大小
     * @return 购物车列表
     */
    List<CartDTO> getCartList(Integer page, Integer size);

    /**
     * 获取购物车列表（带条件）
     *
     * @param userId     用户ID
     * @param productId  商品ID
     * @param categoryId 分类ID
     * @param status     状态
     * @param selected   选中状态
     * @param page       页码
     * @param size       每页大小
     * @return 购物车列表
     */
    List<CartDTO> getCartList(Long userId, Long productId, Long categoryId, Integer status, 
                             Boolean selected, Integer page, Integer size);

    /**
     * 获取购物车总数
     *
     * @param userId     用户ID
     * @param productId  商品ID
     * @param categoryId 分类ID
     * @param status     状态
     * @param selected   选中状态
     * @return 总数
     */
    int getCartCount(Long userId, Long productId, Long categoryId, Integer status, Boolean selected);

    /**
     * 获取用户购物车总金额
     *
     * @param userId 用户ID
     * @return 总金额
     */
    java.math.BigDecimal getTotalAmount(Long userId);

    /**
     * 获取用户购物车商品总数
     *
     * @param userId 用户ID
     * @return 总数
     */
    int getTotalQuantity(Long userId);

    /**
     * 获取用户选中的购物车项
     *
     * @param userId 用户ID
     * @return 选中的购物车项列表
     */
    List<CartDTO> getSelectedItems(Long userId);

    /**
     * 更新购物车项
     *
     * @param id       购物车项ID
     * @param cartDTO  购物车项信息
     * @return 更新后的购物车项
     */
    CartDTO updateCart(Long id, CartDTO cartDTO);

    /**
     * 删除购物车项
     *
     * @param id 购物车项ID
     */
    void deleteCart(Long id);

    /**
     * 根据用户ID和商品ID删除购物车项
     *
     * @param userId   用户ID
     * @param productId 商品ID
     */
    void deleteCartByUserIdAndProductId(Long userId, Long productId);

    /**
     * 清空用户购物车
     *
     * @param userId 用户ID
     */
    void clearCart(Long userId);

    /**
     * 软删除购物车项
     *
     * @param id 购物车项ID
     */
    void softDelete(Long id);

    /**
     * 软删除用户的所有购物车项
     *
     * @param userId 用户ID
     */
    void softDeleteByUserId(Long userId);

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
    boolean existsByUserIdAndProductId(Long userId, Long productId);

    /**
     * 检查购物车项是否属于指定用户
     *
     * @param id     购物车项ID
     * @param userId 用户ID
     * @return 是否属于
     */
    boolean belongsToUser(Long id, Long userId);

    /**
     * 检查商品是否已在购物车中
     *
     * @param userId   用户ID
     * @param productId 商品ID
     * @return 是否已存在
     */
    boolean existsInCart(Long userId, Long productId);

    /**
     * 从购物车中移除商品
     *
     * @param userId   用户ID
     * @param productId 商品ID
     * @return 移除结果
     */
    boolean removeFromCart(Long userId, Long productId);

    /**
     * 计算购物车项总金额
     *
     * @param productPrice 商品单价
     * @param quantity     数量
     * @return 总金额
     */
    java.math.BigDecimal calculateSubtotal(java.math.BigDecimal productPrice, Integer quantity);

    /**
     * 验证购物车项数量
     *
     * @param quantity 数量
     * @return 是否有效
     */
    boolean validateQuantity(Integer quantity);

    /**
     * 验证购物车项价格
     *
     * @param price 价格
     * @return 是否有效
     */
    boolean validatePrice(java.math.BigDecimal price);
}