package com.example.ecommerce.service;

import com.example.ecommerce.dto.CartItemDTO;
import com.example.ecommerce.entity.CartItem;
import com.example.ecommerce.exception.BusinessException;
import com.example.ecommerce.mapper.CartItemMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * CartService的测试类
 */
class CartServiceTest {

    @Mock
    private CartItemMapper cartItemMapper;

    @Mock
    private ProductService productService;

    @InjectMocks
    private CartService cartService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddToCart_Success() {
        // 准备测试数据
        Long userId = 1L;
        Long productId = 1L;
        Integer quantity = 2;

        CartItem existingItem = null;
        CartItem cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setUserId(userId);
        cartItem.setProductId(productId);
        cartItem.setQuantity(quantity);
        cartItem.setCreatedAt(LocalDateTime.now());
        cartItem.setUpdatedAt(LocalDateTime.now());

        // 配置mock行为
        when(cartItemMapper.selectByUserIdAndProductId(userId, productId)).thenReturn(existingItem);
        when(cartItemMapper.insert(any(CartItem.class))).thenReturn(1);

        // 执行被测方法
        CartItemDTO cartItemDTO = cartService.addToCart(userId, productId, quantity);

        // 验证结果
        assertNotNull(cartItemDTO);
        assertEquals(userId, cartItemDTO.getUserId());
        assertEquals(productId, cartItemDTO.getProductId());
        assertEquals(quantity, cartItemDTO.getQuantity());
        verify(cartItemMapper).selectByUserIdAndProductId(userId, productId);
        verify(cartItemMapper).insert(any(CartItem.class));
    }

    @Test
    void testAddToCart_UpdateExistingItem() {
        // 准备测试数据
        Long userId = 1L;
        Long productId = 1L;
        Integer quantity = 3;

        CartItem existingItem = new CartItem();
        existingItem.setId(1L);
        existingItem.setUserId(userId);
        existingItem.setProductId(productId);
        existingItem.setQuantity(2);

        // 配置mock行为
        when(cartItemMapper.selectByUserIdAndProductId(userId, productId)).thenReturn(existingItem);
        when(cartItemMapper.updateQuantity(anyLong(), anyInt())).thenReturn(1);

        // 执行被测方法
        CartItemDTO cartItemDTO = cartService.addToCart(userId, productId, quantity);

        // 验证结果
        assertNotNull(cartItemDTO);
        assertEquals(userId, cartItemDTO.getUserId());
        assertEquals(productId, cartItemDTO.getProductId());
        assertEquals(5, cartItemDTO.getQuantity()); // 2 + 3 = 5
        verify(cartItemMapper).selectByUserIdAndProductId(userId, productId);
        verify(cartItemMapper).updateQuantity(1L, 5);
    }

    @Test
    void testUpdateQuantity_Success() {
        // 准备测试数据
        Long cartItemId = 1L;
        Integer quantity = 5;

        CartItem cartItem = new CartItem();
        cartItem.setId(cartItemId);
        cartItem.setUserId(1L);
        cartItem.setProductId(1L);
        cartItem.setQuantity(3);

        // 配置mock行为
        when(cartItemMapper.selectById(cartItemId)).thenReturn(cartItem);
        when(cartItemMapper.updateQuantity(cartItemId, quantity)).thenReturn(1);

        // 执行被测方法
        CartItemDTO cartItemDTO = cartService.updateQuantity(cartItemId, quantity);

        // 验证结果
        assertNotNull(cartItemDTO);
        assertEquals(cartItemId, cartItemDTO.getId());
        assertEquals(quantity, cartItemDTO.getQuantity());
        verify(cartItemMapper).selectById(cartItemId);
        verify(cartItemMapper).updateQuantity(cartItemId, quantity);
    }

    @Test
    void testUpdateQuantity_CartItemNotFound() {
        // 准备测试数据
        Long cartItemId = 1L;
        Integer quantity = 5;

        // 配置mock行为
        when(cartItemMapper.selectById(cartItemId)).thenReturn(null);

        // 执行被测方法并验证异常
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            cartService.updateQuantity(cartItemId, quantity);
        });

        // 验证结果
        assertEquals(404, exception.getCode());
        assertEquals("购物车商品不存在", exception.getMessage());
        verify(cartItemMapper).selectById(cartItemId);
        verify(cartItemMapper, never()).updateQuantity(anyLong(), anyInt());
    }

    @Test
    void testRemoveFromCart_Success() {
        // 准备测试数据
        Long cartItemId = 1L;

        CartItem cartItem = new CartItem();
        cartItem.setId(cartItemId);
        cartItem.setUserId(1L);
        cartItem.setProductId(1L);

        // 配置mock行为
        when(cartItemMapper.selectById(cartItemId)).thenReturn(cartItem);
        when(cartItemMapper.deleteById(cartItemId)).thenReturn(1);

        // 执行被测方法
        cartService.removeFromCart(cartItemId);

        // 验证结果
        verify(cartItemMapper).selectById(cartItemId);
        verify(cartItemMapper).deleteById(cartItemId);
    }

    @Test
    void testRemoveFromCart_CartItemNotFound() {
        // 准备测试数据
        Long cartItemId = 1L;

        // 配置mock行为
        when(cartItemMapper.selectById(cartItemId)).thenReturn(null);

        // 执行被测方法并验证异常
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            cartService.removeFromCart(cartItemId);
        });

        // 验证结果
        assertEquals(404, exception.getCode());
        assertEquals("购物车商品不存在", exception.getMessage());
        verify(cartItemMapper).selectById(cartItemId);
        verify(cartItemMapper, never()).deleteById(anyLong());
    }

    @Test
    void testGetCartItems_Success() {
        // 准备测试数据
        Long userId = 1L;

        List<CartItem> cartItems = new ArrayList<>();
        CartItem item1 = new CartItem();
        item1.setId(1L);
        item1.setUserId(userId);
        item1.setProductId(1L);
        item1.setQuantity(2);
        CartItem item2 = new CartItem();
        item2.setId(2L);
        item2.setUserId(userId);
        item2.setProductId(2L);
        item2.setQuantity(1);
        cartItems.add(item1);
        cartItems.add(item2);

        // 配置mock行为
        when(cartItemMapper.selectByUserId(userId)).thenReturn(cartItems);

        // 执行被测方法
        List<CartItemDTO> cartItemDTOs = cartService.getCartItems(userId);

        // 验证结果
        assertNotNull(cartItemDTOs);
        assertEquals(2, cartItemDTOs.size());
        verify(cartItemMapper).selectByUserId(userId);
    }

    @Test
    void testClearCart_Success() {
        // 准备测试数据
        Long userId = 1L;

        // 配置mock行为
        when(cartItemMapper.deleteByUserId(userId)).thenReturn(2);

        // 执行被测方法
        int count = cartService.clearCart(userId);

        // 验证结果
        assertEquals(2, count);
        verify(cartItemMapper).deleteByUserId(userId);
    }

    @Test
    void testGetCartItemCount_Success() {
        // 准备测试数据
        Long userId = 1L;

        // 配置mock行为
        when(cartItemMapper.countByUserId(userId)).thenReturn(5);

        // 执行被测方法
        int count = cartService.getCartItemCount(userId);

        // 验证结果
        assertEquals(5, count);
        verify(cartItemMapper).countByUserId(userId);
    }

    @Test
    void testSelectCartItems_Success() {
        // 准备测试数据
        List<Long> cartItemIds = new ArrayList<>();
        cartItemIds.add(1L);
        cartItemIds.add(2L);
        cartItemIds.add(3L);

        // 配置mock行为
        when(cartItemMapper.updateSelectedStatus(cartItemIds, true)).thenReturn(3);

        // 执行被测方法
        int count = cartService.selectCartItems(cartItemIds);

        // 验证结果
        assertEquals(3, count);
        verify(cartItemMapper).updateSelectedStatus(cartItemIds, true);
    }

    @Test
    void testDeselectCartItems_Success() {
        // 准备测试数据
        List<Long> cartItemIds = new ArrayList<>();
        cartItemIds.add(1L);
        cartItemIds.add(2L);

        // 配置mock行为
        when(cartItemMapper.updateSelectedStatus(cartItemIds, false)).thenReturn(2);

        // 执行被测方法
        int count = cartService.deselectCartItems(cartItemIds);

        // 验证结果
        assertEquals(2, count);
        verify(cartItemMapper).updateSelectedStatus(cartItemIds, false);
    }

    @Test
    void testSelectAllCartItems_Success() {
        // 准备测试数据
        Long userId = 1L;

        // 配置mock行为
        when(cartItemMapper.updateSelectedStatusByUserId(userId, true)).thenReturn(5);

        // 执行被测方法
        int count = cartService.selectAllCartItems(userId);

        // 验证结果
        assertEquals(5, count);
        verify(cartItemMapper).updateSelectedStatusByUserId(userId, true);
    }

    @Test
    void testDeselectAllCartItems_Success() {
        // 准备测试数据
        Long userId = 1L;

        // 配置mock行为
        when(cartItemMapper.updateSelectedStatusByUserId(userId, false)).thenReturn(5);

        // 执行被测方法
        int count = cartService.deselectAllCartItems(userId);

        // 验证结果
        assertEquals(5, count);
        verify(cartItemMapper).updateSelectedStatusByUserId(userId, false);
    }

    @Test
    void testGetSelectedCartItems_Success() {
        // 准备测试数据
        Long userId = 1L;

        List<CartItem> cartItems = new ArrayList<>();
        CartItem item1 = new CartItem();
        item1.setId(1L);
        item1.setUserId(userId);
        item1.setProductId(1L);
        item1.setQuantity(2);
        item1.setSelected(true);
        cartItems.add(item1);

        // 配置mock行为
        when(cartItemMapper.selectSelectedByUserId(userId)).thenReturn(cartItems);

        // 执行被测方法
        List<CartItemDTO> cartItemDTOs = cartService.getSelectedCartItems(userId);

        // 验证结果
        assertNotNull(cartItemDTOs);
        assertEquals(1, cartItemDTOs.size());
        assertTrue(cartItemDTOs.get(0).isSelected());
        verify(cartItemMapper).selectSelectedByUserId(userId);
    }

    @Test
    void testIsCartItemOwner_Success() {
        // 准备测试数据
        Long cartItemId = 1L;
        Long userId = 1L;

        CartItem cartItem = new CartItem();
        cartItem.setId(cartItemId);
        cartItem.setUserId(userId);

        // 配置mock行为
        when(cartItemMapper.selectById(cartItemId)).thenReturn(cartItem);

        // 执行被测方法
        boolean isOwner = cartService.isCartItemOwner(cartItemId, userId);

        // 验证结果
        assertTrue(isOwner);
        verify(cartItemMapper).selectById(cartItemId);
    }

    @Test
    void testIsCartItemOwner_NotOwner() {
        // 准备测试数据
        Long cartItemId = 1L;
        Long userId = 1L;
        Long wrongUserId = 2L;

        CartItem cartItem = new CartItem();
        cartItem.setId(cartItemId);
        cartItem.setUserId(userId);

        // 配置mock行为
        when(cartItemMapper.selectById(cartItemId)).thenReturn(cartItem);

        // 执行被测方法
        boolean isOwner = cartService.isCartItemOwner(cartItemId, wrongUserId);

        // 验证结果
        assertFalse(isOwner);
        verify(cartItemMapper).selectById(cartItemId);
    }

    @Test
    void testIsCartItemOwner_CartItemNotFound() {
        // 准备测试数据
        Long cartItemId = 1L;
        Long userId = 1L;

        // 配置mock行为
        when(cartItemMapper.selectById(cartItemId)).thenReturn(null);

        // 执行被测方法
        boolean isOwner = cartService.isCartItemOwner(cartItemId, userId);

        // 验证结果
        assertFalse(isOwner);
        verify(cartItemMapper).selectById(cartItemId);
    }

    @Test
    void testRemoveSelectedCartItems_Success() {
        // 准备测试数据
        Long userId = 1L;

        // 配置mock行为
        when(cartItemMapper.deleteSelectedByUserId(userId)).thenReturn(3);

        // 执行被测方法
        int count = cartService.removeSelectedCartItems(userId);

        // 验证结果
        assertEquals(3, count);
        verify(cartItemMapper).deleteSelectedByUserId(userId);
    }
}