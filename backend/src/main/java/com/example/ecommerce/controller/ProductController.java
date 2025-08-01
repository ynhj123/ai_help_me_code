package com.example.ecommerce.controller;

import com.example.ecommerce.dto.ProductDTO;
import com.example.ecommerce.dto.ProductQueryRequest;
import com.example.ecommerce.entity.ProductInventory;
import com.example.ecommerce.service.ProductService;
import com.example.ecommerce.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * 商品控制器
 */
@RestController
@RequestMapping("/api/products")
@Validated
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * 获取商品列表
     *
     * @param queryRequest 查询请求
     * @return 商品列表
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResultVO<List<ProductDTO>> getProductList(@Valid ProductQueryRequest queryRequest) {
        List<ProductDTO> productList = productService.getProductList(queryRequest);
        return ResultVO.success(productList);
    }

    /**
     * 获取商品总数
     *
     * @param queryRequest 查询请求
     * @return 总数
     */
    @GetMapping("/count")
    @PreAuthorize("hasRole('ADMIN')")
    public ResultVO<Integer> getProductCount(@Valid ProductQueryRequest queryRequest) {
        int count = productService.getProductCount(queryRequest);
        return ResultVO.success(count);
    }

    /**
     * 获取商品详情
     *
     * @param id 商品ID
     * @return 商品详情
     */
    @GetMapping("/{id}")
    public ResultVO<ProductDTO> getProduct(@PathVariable Long id) {
        ProductDTO productDTO = productService.getProductById(id);
        return ResultVO.success(productDTO);
    }

    /**
     * 根据SKU获取商品
     *
     * @param sku SKU
     * @return 商品详情
     */
    @GetMapping("/sku/{sku}")
    public ResultVO<ProductDTO> getProductBySku(@PathVariable String sku) {
        ProductDTO productDTO = productService.getProductBySku(sku);
        return ResultVO.success(productDTO);
    }

    /**
     * 创建商品
     *
     * @param productDTO 商品信息
     * @return 创建的商品
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResultVO<ProductDTO> createProduct(@Valid @RequestBody ProductDTO productDTO) {
        ProductDTO createdProduct = productService.createProduct(productDTO);
        return ResultVO.success("商品创建成功", createdProduct);
    }

    /**
     * 更新商品
     *
     * @param id        商品ID
     * @param productDTO 商品信息
     * @return 更新后的商品
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResultVO<ProductDTO> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductDTO productDTO) {
        ProductDTO updatedProduct = productService.updateProduct(id, productDTO);
        return ResultVO.success("商品更新成功", updatedProduct);
    }

    /**
     * 删除商品
     *
     * @param id 商品ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResultVO<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResultVO.success("商品删除成功", null);
    }

    /**
     * 上架商品
     *
     * @param id 商品ID
     * @return 更新后的商品
     */
    @PutMapping("/{id}/enable")
    @PreAuthorize("hasRole('ADMIN')")
    public ResultVO<ProductDTO> enableProduct(@PathVariable Long id) {
        ProductDTO productDTO = productService.enableProduct(id);
        return ResultVO.success("商品上架成功", productDTO);
    }

    /**
     * 下架商品
     *
     * @param id 商品ID
     * @return 更新后的商品
     */
    @PutMapping("/{id}/disable")
    @PreAuthorize("hasRole('ADMIN')")
    public ResultVO<ProductDTO> disableProduct(@PathVariable Long id) {
        ProductDTO productDTO = productService.disableProduct(id);
        return ResultVO.success("商品下架成功", productDTO);
    }

    /**
     * 获取商品库存信息
     *
     * @param productId 商品ID
     * @return 库存信息
     */
    @GetMapping("/{id}/inventory")
    @PreAuthorize("hasRole('ADMIN')")
    public ResultVO<ProductInventory> getProductInventory(@PathVariable Long id) {
        ProductInventory inventory = productService.getProductInventory(id);
        return ResultVO.success(inventory);
    }

    /**
     * 更新商品库存
     *
     * @param id       商品ID
     * @param quantity 库存数量
     * @return 更新后的库存信息
     */
    @PutMapping("/{id}/inventory")
    @PreAuthorize("hasRole('ADMIN')")
    public ResultVO<ProductInventory> updateProductInventory(
            @PathVariable Long id, 
            @RequestParam Integer quantity) {
        ProductInventory inventory = productService.updateProductInventory(id, quantity);
        return ResultVO.success("库存更新成功", inventory);
    }

    /**
     * 增加商品库存
     *
     * @param id       商品ID
     * @param quantity 增加数量
     * @return 更新后的库存信息
     */
    @PutMapping("/{id}/inventory/increase")
    @PreAuthorize("hasRole('ADMIN')")
    public ResultVO<ProductInventory> increaseProductInventory(
            @PathVariable Long id, 
            @RequestParam Integer quantity) {
        ProductInventory inventory = productService.increaseProductInventory(id, quantity);
        return ResultVO.success("库存增加成功", inventory);
    }

    /**
     * 减少商品库存
     *
     * @param id       商品ID
     * @param quantity 减少数量
     * @return 更新后的库存信息
     */
    @PutMapping("/{id}/inventory/decrease")
    @PreAuthorize("hasRole('ADMIN')")
    public ResultVO<ProductInventory> decreaseProductInventory(
            @PathVariable Long id, 
            @RequestParam Integer quantity) {
        ProductInventory inventory = productService.decreaseProductInventory(id, quantity);
        return ResultVO.success("库存减少成功", inventory);
    }

    /**
     * 检查商品库存是否充足
     *
     * @param productId 商品ID
     * @param quantity  需要的数量
     * @return 是否充足
     */
    @GetMapping("/{id}/inventory/check")
    @PreAuthorize("hasRole('ADMIN')")
    public ResultVO<Boolean> checkInventoryStock(
            @PathVariable Long id, 
            @RequestParam Integer quantity) {
        boolean isStockAvailable = productService.checkInventoryStock(id, quantity);
        return ResultVO.success(isStockAvailable);
    }

    /**
     * 预留库存
     *
     * @param productId 商品ID
     * @param quantity  预留数量
     * @return 是否预留成功
     */
    @PostMapping("/{id}/inventory/reserve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResultVO<Boolean> reserveInventory(
            @PathVariable Long id, 
            @RequestParam Integer quantity) {
        boolean success = productService.reserveInventory(id, quantity);
        if (success) {
            return ResultVO.success("库存预留成功", true);
        } else {
            return ResultVO.error(400, "库存不足", false);
        }
    }

    /**
     * 释放预留库存
     *
     * @param productId 商品ID
     * @param quantity  释放数量
     * @return 释放结果
     */
    @PostMapping("/{id}/inventory/release")
    @PreAuthorize("hasRole('ADMIN')")
    public ResultVO<Void> releaseInventory(
            @PathVariable Long id, 
            @RequestParam Integer quantity) {
        productService.releaseInventory(id, quantity);
        return ResultVO.success("库存释放成功", null);
    }
}