package com.example.ecommerce.service.impl;

import com.example.ecommerce.dto.CartDTO;
import com.example.ecommerce.entity.Cart;
import com.example.ecommerce.enums.CartStatus;
import com.example.ecommerce.exception.BusinessException;
import com.example.ecommerce.mapper.CartMapper;
import com.example.ecommerce.service.CartService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 购物车服务实现类
 */
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartMapper cartMapper;

    @Override
    @Transactional
    public CartDTO addToCart(CartDTO cartDTO) {
        // 检查商品是否已在购物车中
        if (existsInCart(cartDTO.getUserId(), cartDTO.getProductId())) {
            // 如果已存在，更新数量
            Cart existingCart = cartMapper.selectByUserIdAndProductId(cartDTO.getUserId(), cartDTO.getProductId());
            Integer newQuantity = existingCart.getQuantity() + cartDTO.getQuantity();
            return updateQuantity(existingCart.getId(), newQuantity);
        }

        // 计算商品总金额
        BigDecimal subtotal = calculateSubtotal(cartDTO.getProductPrice(), cartDTO.getQuantity());
        cartDTO.setSubtotal(subtotal);

        // 设置购物车项状态
        cartDTO.setStatus(CartStatus.ACTIVE);
        cartDTO.setCreatedAt(LocalDateTime.now());
        cartDTO.setUpdatedAt(LocalDateTime.now());

        // 插入购物车项
        Cart cart = new Cart();
        BeanUtils.copyProperties(cartDTO, cart);
        cartMapper.insert(cart);

        // 转换为DTO
        CartDTO createdCartDTO = new CartDTO();
        BeanUtils.copyProperties(cart, createdCartDTO);
        return createdCartDTO;
    }

    @Override
    @Transactional
    public CartDTO updateQuantity(Long id, Integer quantity) {
        // 检查购物车项是否存在
        Cart cart = cartMapper.selectById(id);
        if (cart == null) {
            throw new BusinessException(404, "购物车项不存在");
        }

        // 验证数量
        if (!validateQuantity(quantity)) {
            throw new BusinessException(400, "商品数量必须大于0");
        }

        // 更新数量和总金额
        BigDecimal subtotal = calculateSubtotal(cart.getProductPrice(), quantity);
        cart.setQuantity(quantity);
        cart.setSubtotal(subtotal);
        cart.setUpdatedAt(LocalDateTime.now());
        cartMapper.updateQuantity(id, quantity);

        // 转换为DTO
        CartDTO cartDTO = new CartDTO();
        BeanUtils.copyProperties(cart, cartDTO);
        return cartDTO;
    }

    @Override
    @Transactional
    public CartDTO updateSelected(Long id, Boolean selected) {
        // 检查购物车项是否存在
        Cart cart = cartMapper.selectById(id);
        if (cart == null) {
            throw new BusinessException(404, "购物车项不存在");
        }

        // 更新选中状态
        cart.setSelected(selected);
        cart.setUpdatedAt(LocalDateTime.now());
        cartMapper.updateSelected(id, selected);

        // 转换为DTO
        CartDTO cartDTO = new CartDTO();
        BeanUtils.copyProperties(cart, cartDTO);
        return cartDTO;
    }

    @Override
    @Transactional
    public boolean batchUpdateSelected(Long userId, Boolean selected) {
        return cartMapper.batchUpdateSelected(userId, selected) > 0;
    }

    @Override
    public CartDTO getCartById(Long id) {
        // 查询购物车项
        Cart cart = cartMapper.selectById(id);
        if (cart == null) {
            throw new BusinessException(404, "购物车项不存在");
        }

        // 转换为DTO
        CartDTO cartDTO = new CartDTO();
        BeanUtils.copyProperties(cart, cartDTO);
        return cartDTO;
    }

    @Override
    public CartDTO getCartByUserIdAndProductId(Long userId, Long productId) {
        // 查询购物车项
        Cart cart = cartMapper.selectByUserIdAndProductId(userId, productId);
        if (cart == null) {
            throw new BusinessException(404, "购物车项不存在");
        }

        // 转换为DTO
        CartDTO cartDTO = new CartDTO();
        BeanUtils.copyProperties(cart, cartDTO);
        return cartDTO;
    }

    @Override
    public List<CartDTO> getCartByUserId(Long userId) {
        // 查询购物车列表
        List<Cart> cartList = cartMapper.selectByUserId(userId);

        // 转换为DTO列表
        return cartList.stream().map(cart -> {
            CartDTO cartDTO = new CartDTO();
            BeanUtils.copyProperties(cart, cartDTO);
            return cartDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public List<CartDTO> getCartList(Integer page, Integer size) {
        // 计算偏移量
        int offset = (page - 1) * size;

        // 查询购物车列表
        List<Cart> cartList = cartMapper.selectPage(offset, size);

        // 转换为DTO列表
        return cartList.stream().map(cart -> {
            CartDTO cartDTO = new CartDTO();
            BeanUtils.copyProperties(cart, cartDTO);
            return cartDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public List<CartDTO> getCartList(Long userId, Long productId, Long categoryId, Integer status, 
                                   Boolean selected, Integer page, Integer size) {
        // 计算偏移量
        int offset = (page - 1) * size;

        // 查询购物车列表
        List<Cart> cartList = cartMapper.selectAll(
                offset, size, userId, productId, categoryId, status, selected
        );

        // 转换为DTO列表
        return cartList.stream().map(cart -> {
            CartDTO cartDTO = new CartDTO();
            BeanUtils.copyProperties(cart, cartDTO);
            return cartDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public int getCartCount(Long userId, Long productId, Long categoryId, Integer status, Boolean selected) {
        return cartMapper.count(userId, productId, categoryId, status, selected);
    }

    @Override
    public BigDecimal getTotalAmount(Long userId) {
        return cartMapper.calculateTotalAmount(userId);
    }

    @Override
    public int getTotalQuantity(Long userId) {
        return cartMapper.calculateTotalQuantity(userId);
    }

    @Override
    public List<CartDTO> getSelectedItems(Long userId) {
        // 查询用户购物车列表
        List<Cart> cartList = cartMapper.selectByUserId(userId);

        // 过滤出选中的项
        List<Cart> selectedItems = cartList.stream()
                .filter(cart -> Boolean.TRUE.equals(cart.getSelected()))
                .collect(Collectors.toList());

        // 转换为DTO列表
        return selectedItems.stream().map(cart -> {
            CartDTO cartDTO = new CartDTO();
            BeanUtils.copyProperties(cart, cartDTO);
            return cartDTO;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CartDTO updateCart(Long id, CartDTO cartDTO) {
        // 检查购物车项是否存在
        Cart cart = cartMapper.selectById(id);
        if (cart == null) {
            throw new BusinessException(404, "购物车项不存在");
        }

        // 更新购物车项
        BeanUtils.copyProperties(cartDTO, cart, "id", "userId", "productId", "status", "createdAt");
        cart.setUpdatedAt(LocalDateTime.now());
        cartMapper.update(cart);

        // 转换为DTO
        CartDTO updatedCartDTO = new CartDTO();
        BeanUtils.copyProperties(cart, updatedCartDTO);
        return updatedCartDTO;
    }

    @Override
    @Transactional
    public void deleteCart(Long id) {
        // 检查购物车项是否存在
        if (!existsById(id)) {
            throw new BusinessException(404, "购物车项不存在");
        }

        // 删除购物车项
        cartMapper.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteCartByUserIdAndProductId(Long userId, Long productId) {
        // 删除购物车项
        cartMapper.deleteByUserIdAndProductId(userId, productId);
    }

    @Override
    @Transactional
    public void clearCart(Long userId) {
        // 清空购物车
        cartMapper.clearCart(userId);
    }

    @Override
    @Transactional
    public void softDelete(Long id) {
        // 检查购物车项是否存在
        if (!existsById(id)) {
            throw new BusinessException(404, "购物车项不存在");
        }

        // 软删除购物车项
        cartMapper.softDelete(id);
    }

    @Override
    @Transactional
    public void softDeleteByUserId(Long userId) {
        // 软删除用户的所有购物车项
        cartMapper.softDeleteByUserId(userId);
    }

    @Override
    public boolean existsById(Long id) {
        return cartMapper.existsById(id);
    }

    @Override
    public boolean existsByUserIdAndProductId(Long userId, Long productId) {
        return cartMapper.existsByUserIdAndProductId(userId, productId);
    }

    @Override
    public boolean belongsToUser(Long id, Long userId) {
        return cartMapper.belongsToUser(id, userId);
    }

    @Override
    public boolean existsInCart(Long userId, Long productId) {
        return cartMapper.existsByUserIdAndProductId(userId, productId);
    }

    @Override
    @Transactional
    public boolean removeFromCart(Long userId, Long productId) {
        // 检查购物车项是否存在
        if (!existsInCart(userId, productId)) {
            return false;
        }

        // 删除购物车项
        cartMapper.deleteByUserIdAndProductId(userId, productId);
        return true;
    }

    @Override
    public BigDecimal calculateSubtotal(BigDecimal productPrice, Integer quantity) {
        if (productPrice == null || quantity == null) {
            return BigDecimal.ZERO;
        }
        return productPrice.multiply(new BigDecimal(quantity));
    }

    @Override
    public boolean validateQuantity(Integer quantity) {
        return quantity != null && quantity > 0;
    }

    @Override
    public boolean validatePrice(BigDecimal price) {
        return price != null && price.compareTo(BigDecimal.ZERO) > 0;
    }
}