package com.example.ecommerce.controller;

import com.example.ecommerce.dto.CartItemDTO;
import com.example.ecommerce.service.CartService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * CartController的测试类
 */
@WebMvcTest(CartController.class)
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartService cartService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testAddToCart() throws Exception {
        // 准备测试数据
        CartItemDTO cartItemDTO = new CartItemDTO();
        cartItemDTO.setUserId(1L);
        cartItemDTO.setProductId(1L);
        cartItemDTO.setQuantity(2);

        CartItemDTO createdCartItemDTO = new CartItemDTO();
        createdCartItemDTO.setId(1L);
        createdCartItemDTO.setUserId(1L);
        createdCartItemDTO.setProductId(1L);
        createdCartItemDTO.setQuantity(2);

        // 配置mock行为
        when(cartService.addToCart(any(CartItemDTO.class))).thenReturn(createdCartItemDTO);

        // 执行被测方法
        mockMvc.perform(post("/api/cart/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cartItemDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.productId").value(1))
                .andExpect(jsonPath("$.quantity").value(2))
                .andExpect(header().string("Location", "/api/cart/items/1"));

        // 验证结果
        verify(cartService).addToCart(any(CartItemDTO.class));
    }

    @Test
    void testAddToCart_Unauthenticated() throws Exception {
        // 准备测试数据
        CartItemDTO cartItemDTO = new CartItemDTO();

        // 执行被测方法并验证认证
        mockMvc.perform(post("/api/cart/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cartItemDTO)))
                .andExpect(status().isUnauthorized());

        // 验证结果
        verify(cartService, never()).addToCart(any(CartItemDTO.class));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testUpdateQuantity() throws Exception {
        // 准备测试数据
        Long cartItemId = 1L;
        Integer quantity = 5;

        CartItemDTO updatedCartItemDTO = new CartItemDTO();
        updatedCartItemDTO.setId(cartItemId);
        updatedCartItemDTO.setQuantity(quantity);

        // 配置mock行为
        when(cartService.updateQuantity(eq(cartItemId), eq(quantity))).thenReturn(updatedCartItemDTO);

        // 执行被测方法
        mockMvc.perform(put("/api/cart/items/{id}/quantity", cartItemId)
                .param("quantity", String.valueOf(quantity))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(cartItemId))
                .andExpect(jsonPath("$.quantity").value(quantity));

        // 验证结果
        verify(cartService).updateQuantity(eq(cartItemId), eq(quantity));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testRemoveFromCart() throws Exception {
        // 准备测试数据
        Long cartItemId = 1L;

        // 执行被测方法
        mockMvc.perform(delete("/api/cart/items/{id}", cartItemId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // 验证结果
        verify(cartService).removeFromCart(cartItemId);
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testGetCartItems() throws Exception {
        // 准备测试数据
        Long userId = 1L;

        List<CartItemDTO> cartItemList = new ArrayList<>();
        CartItemDTO cartItem1 = new CartItemDTO();
        cartItem1.setId(1L);
        cartItem1.setUserId(userId);
        cartItem1.setProductId(1L);
        cartItem1.setProductName("Product 1");
        cartItem1.setQuantity(2);
        cartItem1.setPrice(new BigDecimal(100.0));
        CartItemDTO cartItem2 = new CartItemDTO();
        cartItem2.setId(2L);
        cartItem2.setUserId(userId);
        cartItem2.setProductId(2L);
        cartItem2.setProductName("Product 2");
        cartItem2.setQuantity(1);
        cartItem2.setPrice(new BigDecimal(150.0));
        cartItemList.add(cartItem1);
        cartItemList.add(cartItem2);

        // 配置mock行为
        when(cartService.getCartItems(userId)).thenReturn(cartItemList);

        // 执行被测方法
        mockMvc.perform(get("/api/cart/items")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].productName").value("Product 1"))
                .andExpect(jsonPath("$[0].quantity").value(2))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].productName").value("Product 2"))
                .andExpect(jsonPath("$[1].quantity").value(1));

        // 验证结果
        verify(cartService).getCartItems(userId);
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testClearCart() throws Exception {
        // 准备测试数据
        Long userId = 1L;

        // 执行被测方法
        mockMvc.perform(delete("/api/cart/items")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // 验证结果
        verify(cartService).clearCart(userId);
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testGetCartItemCount() throws Exception {
        // 准备测试数据
        Long userId = 1L;

        // 配置mock行为
        when(cartService.getCartItemCount(userId)).thenReturn(5);

        // 执行被测方法
        mockMvc.perform(get("/api/cart/items/count")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));

        // 验证结果
        verify(cartService).getCartItemCount(userId);
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testSelectCartItems() throws Exception {
        // 准备测试数据
        Long userId = 1L;
        Long[] cartItemIds = {1L, 2L};

        // 执行被测方法
        mockMvc.perform(put("/api/cart/items/select")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cartItemIds)))
                .andExpect(status().isOk());

        // 验证结果
        verify(cartService).selectCartItems(userId, cartItemIds);
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testDeselectCartItems() throws Exception {
        // 准备测试数据
        Long userId = 1L;
        Long[] cartItemIds = {1L, 2L};

        // 执行被测方法
        mockMvc.perform(put("/api/cart/items/deselect")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cartItemIds)))
                .andExpect(status().isOk());

        // 验证结果
        verify(cartService).deselectCartItems(userId, cartItemIds);
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testSelectAllCartItems() throws Exception {
        // 准备测试数据
        Long userId = 1L;

        // 执行被测方法
        mockMvc.perform(put("/api/cart/items/selectAll")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // 验证结果
        verify(cartService).selectAllCartItems(userId);
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testDeselectAllCartItems() throws Exception {
        // 准备测试数据
        Long userId = 1L;

        // 执行被测方法
        mockMvc.perform(put("/api/cart/items/deselectAll")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // 验证结果
        verify(cartService).deselectAllCartItems(userId);
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testGetSelectedCartItems() throws Exception {
        // 准备测试数据
        Long userId = 1L;

        List<CartItemDTO> selectedCartItems = new ArrayList<>();
        CartItemDTO cartItem1 = new CartItemDTO();
        cartItem1.setId(1L);
        cartItem1.setUserId(userId);
        cartItem1.setSelected(true);
        selectedCartItems.add(cartItem1);

        // 配置mock行为
        when(cartService.getSelectedCartItems(userId)).thenReturn(selectedCartItems);

        // 执行被测方法
        mockMvc.perform(get("/api/cart/items/selected")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].selected").value(true));

        // 验证结果
        verify(cartService).getSelectedCartItems(userId);
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testIsCartItemOwner() throws Exception {
        // 准备测试数据
        Long cartItemId = 1L;

        // 配置mock行为
        when(cartService.isCartItemOwner(cartItemId)).thenReturn(true);

        // 执行被测方法
        mockMvc.perform(get("/api/cart/items/{id}/isOwner", cartItemId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        // 验证结果
        verify(cartService).isCartItemOwner(cartItemId);
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testRemoveSelectedCartItems() throws Exception {
        // 准备测试数据
        Long userId = 1L;

        // 执行被测方法
        mockMvc.perform(delete("/api/cart/items/selected")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // 验证结果
        verify(cartService).removeSelectedCartItems(userId);
    }
}