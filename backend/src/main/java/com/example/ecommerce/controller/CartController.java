package com.example.ecommerce.controller;

import com.example.ecommerce.dto.CartDTO;
import com.example.ecommerce.service.CartService;
import com.example.ecommerce.vo.ResultVO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

import java.math.BigDecimal;
import java.util.List;

/**
 * 购物车控制器
 * 使用构造器注入依赖，避免@Autowired字段注入
 */
@RestController
@RequestMapping("/api/cart")
@Validated
public class CartController {

    private final CartService cartService;

    /**
     * 构造器注入
     *
     * @param cartService 购物车服务
     */
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    /**
     * 添加商品到购物车
     *
     * @param cartDTO     购物车项
     * @param userDetails 当前用户
     * @return 添加的购物车项
     */
    @PostMapping
    public ResultVO<CartDTO> addToCart(@Valid @RequestBody CartDTO cartDTO,
                                       @AuthenticationPrincipal UserDetails userDetails) {
        // 设置用户ID
        cartDTO.setUserId(Long.parseLong(userDetails.getUsername()));

        CartDTO addedCart = cartService.addToCart(cartDTO);
        return ResultVO.success("商品已添加到购物车", addedCart);
    }

    /**
     * 更新购物车项数量
     *
     * @param id          购物车项ID
     * @param quantity    数量
     * @param userDetails 当前用户
     * @return 更新后的购物车项
     */
    @PutMapping("/{id}/quantity")
    public ResultVO<CartDTO> updateQuantity(@PathVariable Long id, @RequestParam Integer quantity,
                                            @AuthenticationPrincipal UserDetails userDetails) {
        // 检查购物车项是否属于当前用户
        if (!cartService.belongsToUser(id, Long.parseLong(userDetails.getUsername()))) {
            throw new org.springframework.security.access.AccessDeniedException("无权操作该购物车项");
        }

        CartDTO cartDTO = cartService.updateQuantity(id, quantity);
        return ResultVO.success("购物车数量更新成功", cartDTO);
    }

    /**
     * 更新购物车项选中状态
     *
     * @param id          购物车项ID
     * @param selected    选中状态
     * @param userDetails 当前用户
     * @return 更新后的购物车项
     */
    @PutMapping("/{id}/selected")
    public ResultVO<CartDTO> updateSelected(@PathVariable Long id, @RequestParam Boolean selected,
                                            @AuthenticationPrincipal UserDetails userDetails) {
        // 检查购物车项是否属于当前用户
        if (!cartService.belongsToUser(id, Long.parseLong(userDetails.getUsername()))) {
            throw new org.springframework.security.access.AccessDeniedException("无权操作该购物车项");
        }

        CartDTO cartDTO = cartService.updateSelected(id, selected);
        return ResultVO.success("选中状态更新成功", cartDTO);
    }

    /**
     * 批量更新选中状态
     *
     * @param selected    选中状态
     * @param userDetails 当前用户
     * @return 更新结果
     */
    @PutMapping("/selected/batch")
    public ResultVO<Boolean> batchUpdateSelected(@RequestParam Boolean selected,
                                                 @AuthenticationPrincipal UserDetails userDetails) {
        boolean result = cartService.batchUpdateSelected(Long.parseLong(userDetails.getUsername()), selected);
        return ResultVO.success("批量更新选中状态成功", result);
    }

    /**
     * 获取购物车项详情
     *
     * @param id          购物车项ID
     * @param userDetails 当前用户
     * @return 购物车项
     */
    @GetMapping("/{id}")
    public ResultVO<CartDTO> getCart(@PathVariable Long id,
                                     @AuthenticationPrincipal UserDetails userDetails) {
        // 检查购物车项是否属于当前用户
        if (!cartService.belongsToUser(id, Long.parseLong(userDetails.getUsername()))) {
            throw new org.springframework.security.access.AccessDeniedException("无权访问该购物车项");
        }

        CartDTO cartDTO = cartService.getCartById(id);
        return ResultVO.success(cartDTO);
    }

    /**
     * 获取用户购物车列表
     *
     * @param userDetails 当前用户
     * @return 购物车列表
     */
    @GetMapping("/my")
    public ResultVO<List<CartDTO>> getMyCart(@AuthenticationPrincipal UserDetails userDetails) {
        List<CartDTO> cartList = cartService.getCartByUserId(Long.parseLong(userDetails.getUsername()));
        return ResultVO.success(cartList);
    }

    /**
     * 获取购物车列表（分页）
     *
     * @param page 页码
     * @param size 每页大小
     * @return 购物车列表
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResultVO<List<CartDTO>> getCartList(
            @RequestParam(defaultValue = "1") @Min(1) Integer page,
            @RequestParam(defaultValue = "10") @Min(1) Integer size) {
        List<CartDTO> cartList = cartService.getCartList(page, size);
        return ResultVO.success(cartList);
    }

    /**
     * 条件查询购物车列表
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
    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    public ResultVO<List<CartDTO>> searchCarts(@RequestParam(required = false) Long userId,
                                               @RequestParam(required = false) Long productId,
                                               @RequestParam(required = false) Long categoryId,
                                               @RequestParam(required = false) Integer status,
                                               @RequestParam(required = false) Boolean selected,
                                               @RequestParam(defaultValue = "1") @Min(1) Integer page,
                                               @RequestParam(defaultValue = "10") @Min(1) Integer size) {
        List<CartDTO> cartList = cartService.getCartList(userId, productId, categoryId, status, selected, page, size);
        return ResultVO.success(cartList);
    }

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
    @GetMapping("/count")
    @PreAuthorize("hasRole('ADMIN')")
    public ResultVO<Integer> getCartCount(@RequestParam(required = false) Long userId,
                                          @RequestParam(required = false) Long productId,
                                          @RequestParam(required = false) Long categoryId,
                                          @RequestParam(required = false) Integer status,
                                          @RequestParam(required = false) Boolean selected) {
        int count = cartService.getCartCount(userId, productId, categoryId, status, selected);
        return ResultVO.success(count);
    }

    /**
     * 获取用户购物车总金额
     *
     * @param userDetails 当前用户
     * @return 总金额
     */
    @GetMapping("/total-amount")
    public ResultVO<java.math.BigDecimal> getTotalAmount(@AuthenticationPrincipal UserDetails userDetails) {
        BigDecimal totalAmount = cartService.getTotalAmount(Long.parseLong(userDetails.getUsername()));
        return ResultVO.success(totalAmount);
    }

    /**
     * 获取用户购物车商品总数
     *
     * @param userDetails 当前用户
     * @return 总数
     */
    @GetMapping("/total-quantity")
    public ResultVO<Integer> getTotalQuantity(@AuthenticationPrincipal UserDetails userDetails) {
        int totalQuantity = cartService.getTotalQuantity(Long.parseLong(userDetails.getUsername()));
        return ResultVO.success(totalQuantity);
    }

    /**
     * 获取用户选中的购物车项
     *
     * @param userDetails 当前用户
     * @return 选中的购物车项列表
     */
    @GetMapping("/selected")
    public ResultVO<List<CartDTO>> getSelectedItems(@AuthenticationPrincipal UserDetails userDetails) {
        List<CartDTO> selectedItems = cartService.getSelectedItems(Long.parseLong(userDetails.getUsername()));
        return ResultVO.success(selectedItems);
    }

    /**
     * 更新购物车项
     *
     * @param id          购物车项ID
     * @param cartDTO     购物车项信息
     * @param userDetails 当前用户
     * @return 更新后的购物车项
     */
    @PutMapping("/{id}")
    public ResultVO<CartDTO> updateCart(@PathVariable Long id, @Valid @RequestBody CartDTO cartDTO,
                                        @AuthenticationPrincipal UserDetails userDetails) {
        // 检查购物车项是否属于当前用户
        if (!cartService.belongsToUser(id, Long.parseLong(userDetails.getUsername()))) {
            throw new org.springframework.security.access.AccessDeniedException("无权操作该购物车项");
        }

        CartDTO updatedCart = cartService.updateCart(id, cartDTO);
        return ResultVO.success("购物车更新成功", updatedCart);
    }

    /**
     * 删除购物车项
     *
     * @param id          购物车项ID
     * @param userDetails 当前用户
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public ResultVO<Void> deleteCart(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        // 检查购物车项是否属于当前用户
        if (!cartService.belongsToUser(id, Long.parseLong(userDetails.getUsername()))) {
            throw new org.springframework.security.access.AccessDeniedException("无权操作该购物车项");
        }

        cartService.deleteCart(id);
        return ResultVO.success("购物车项删除成功", null);
    }

    /**
     * 根据商品ID删除购物车项
     *
     * @param productId   商品ID
     * @param userDetails 当前用户
     * @return 删除结果
     */
    @DeleteMapping("/product/{productId}")
    public ResultVO<Boolean> deleteCartByProduct(@PathVariable Long productId, @AuthenticationPrincipal UserDetails userDetails) {
        boolean result = cartService.removeFromCart(Long.parseLong(userDetails.getUsername()), productId);
        return ResultVO.success("购物车项删除成功", result);
    }

    /**
     * 清空购物车
     *
     * @param userDetails 当前用户
     * @return 清空结果
     */
    @DeleteMapping("/clear")
    public ResultVO<Void> clearCart(@AuthenticationPrincipal UserDetails userDetails) {
        cartService.clearCart(Long.parseLong(userDetails.getUsername()));
        return ResultVO.success("购物车已清空", null);
    }

    /**
     * 软删除购物车项
     *
     * @param id          购物车项ID
     * @param userDetails 当前用户
     * @return 删除结果
     */
    @PutMapping("/{id}/soft-delete")
    public ResultVO<Void> softDelete(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        // 检查购物车项是否属于当前用户
        if (!cartService.belongsToUser(id, Long.parseLong(userDetails.getUsername()))) {
            throw new org.springframework.security.access.AccessDeniedException("无权操作该购物车项");
        }

        cartService.softDelete(id);
        return ResultVO.success("购物车项已移除", null);
    }

    /**
     * 检查购物车项是否存在
     *
     * @param id 购物车项ID
     * @return 是否存在
     */
    @GetMapping("/{id}/exists")
    public ResultVO<Boolean> existsById(@PathVariable Long id) {
        boolean exists = cartService.existsById(id);
        return ResultVO.success(exists);
    }

    /**
     * 检查购物车项是否存在
     *
     * @param userId    用户ID
     * @param productId 商品ID
     * @return 是否存在
     */
    @GetMapping("/exists")
    public ResultVO<Boolean> existsByUserIdAndProductId(@RequestParam Long userId, @RequestParam Long productId) {
        boolean exists = cartService.existsByUserIdAndProductId(userId, productId);
        return ResultVO.success(exists);
    }

    /**
     * 检查购物车项是否属于指定用户
     *
     * @param id     购物车项ID
     * @param userId 用户ID
     * @return 是否属于
     */
    @GetMapping("/{id}/belongs-to-user/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResultVO<Boolean> belongsToUser(@PathVariable Long id, @PathVariable Long userId) {
        boolean belongs = cartService.belongsToUser(id, userId);
        return ResultVO.success(belongs);
    }

    /**
     * 检查商品是否已在购物车中
     *
     * @param userId    用户ID
     * @param productId 商品ID
     * @return 是否已存在
     */
    @GetMapping("/exists-in-cart")
    public ResultVO<Boolean> existsInCart(@RequestParam Long userId, @RequestParam Long productId) {
        boolean exists = cartService.existsInCart(userId, productId);
        return ResultVO.success(exists);
    }

    /**
     * 从购物车中移除商品
     *
     * @param userId    用户ID
     * @param productId 商品ID
     * @return 移除结果
     */
    @DeleteMapping("/remove")
    public ResultVO<Boolean> removeFromCart(@RequestParam Long userId, @RequestParam Long productId) {
        boolean result = cartService.removeFromCart(userId, productId);
        return ResultVO.success("商品已从购物车移除", result);
    }

    /**
     * 计算购物车项总金额
     *
     * @param productPrice 商品单价
     * @param quantity     数量
     * @return 总金额
     */
    @GetMapping("/calculate-subtotal")
    public ResultVO<java.math.BigDecimal> calculateSubtotal(@RequestParam java.math.BigDecimal productPrice,
                                                            @RequestParam Integer quantity) {
        BigDecimal subtotal = cartService.calculateSubtotal(productPrice, quantity);
        return ResultVO.success(subtotal);
    }

    /**
     * 验证购物车项数量
     *
     * @param quantity 数量
     * @return 是否有效
     */
    @GetMapping("/validate-quantity")
    public ResultVO<Boolean> validateQuantity(@RequestParam Integer quantity) {
        boolean valid = cartService.validateQuantity(quantity);
        return ResultVO.success(valid);
    }

    /**
     * 验证购物车项价格
     *
     * @param price 价格
     * @return 是否有效
     */
    @GetMapping("/validate-price")
    public ResultVO<Boolean> validatePrice(@RequestParam java.math.BigDecimal price) {
        boolean valid = cartService.validatePrice(price);
        return ResultVO.success(valid);
    }
}