package com.example.ecommerce.service;

import com.example.ecommerce.dto.ProductDTO;
import com.example.ecommerce.dto.ProductQueryRequest;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.entity.ProductInventory;

import java.util.List;

/**
 * 商品服务接口
 */
public interface ProductService {

    /**
     * 创建商品
     *
     * @param productDTO 商品信息
     * @return 创建的商品
     */
    ProductDTO createProduct(ProductDTO productDTO);

    /**
     * 更新商品
     *
     * @param id        商品ID
     * @param productDTO 商品信息
     * @return 更新后的商品
     */
    ProductDTO updateProduct(Long id, ProductDTO productDTO);

    /**
     * 根据ID获取商品
     *
     * @param id 商品ID
     * @return 商品信息
     */
    ProductDTO getProductById(Long id);

    /**
     * 根据SKU获取商品
     *
     * @param sku SKU
     * @return 商品信息
     */
    ProductDTO getProductBySku(String sku);

    /**
     * 获取商品列表
     *
     * @param queryRequest 查询请求
     * @return 商品列表
     */
    List<ProductDTO> getProductList(ProductQueryRequest queryRequest);

    /**
     * 获取商品总数
     *
     * @param queryRequest 查询请求
     * @return 总数
     */
    int getProductCount(ProductQueryRequest queryRequest);

    /**
     * 删除商品
     *
     * @param id 商品ID
     */
    void deleteProduct(Long id);

    /**
     * 上架商品
     *
     * @param id 商品ID
     * @return 更新后的商品
     */
    ProductDTO enableProduct(Long id);

    /**
     * 下架商品
     *
     * @param id 商品ID
     * @return 更新后的商品
     */
    ProductDTO disableProduct(Long id);

    /**
     * 获取商品库存信息
     *
     * @param productId 商品ID
     * @return 库存信息
     */
    ProductInventory getProductInventory(Long productId);

    /**
     * 更新商品库存
     *
     * @param productId 商品ID
     * @param quantity  库存数量
     * @return 更新后的库存信息
     */
    ProductInventory updateProductInventory(Long productId, Integer quantity);

    /**
     * 增加商品库存
     *
     * @param productId 商品ID
     * @param quantity  增加数量
     * @return 更新后的库存信息
     */
    ProductInventory increaseProductInventory(Long productId, Integer quantity);

    /**
     * 减少商品库存
     *
     * @param productId 商品ID
     * @param quantity  减少数量
     * @return 更新后的库存信息
     */
    ProductInventory decreaseProductInventory(Long productId, Integer quantity);

    /**
     * 检查商品库存是否充足
     *
     * @param productId 商品ID
     * @param quantity  需要的数量
     * @return 是否充足
     */
    boolean checkInventoryStock(Long productId, Integer quantity);

    /**
     * 预留库存
     *
     * @param productId 商品ID
     * @param quantity  预留数量
     * @return 是否预留成功
     */
    boolean reserveInventory(Long productId, Integer quantity);

    /**
     * 释放预留库存
     *
     * @param productId 商品ID
     * @param quantity  释放数量
     */
    void releaseInventory(Long productId, Integer quantity);
}