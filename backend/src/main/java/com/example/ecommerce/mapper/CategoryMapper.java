package com.example.ecommerce.mapper;

import com.example.ecommerce.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 分类Mapper接口
 * 已迁移至XML风格，所有SQL在CategoryMapper.xml中定义
 */
@Mapper
public interface CategoryMapper {

    /**
     * 插入分类
     *
     * @param category 分类
     * @return 影响行数
     */
    int insert(Category category);

    /**
     * 根据ID查询分类
     *
     * @param id 分类ID
     * @return 分类
     */
    Category selectById(Long id);

    /**
     * 根据父级分类ID查询子分类
     *
     * @param parentId 父级分类ID
     * @return 子分类列表
     */
    List<Category> selectByParentId(Long parentId);

    /**
     * 查询所有分类
     *
     * @return 分类列表
     */
    List<Category> selectAll();

    /**
     * 查询所有启用的分类
     *
     * @return 分类列表
     */
    List<Category> selectAllEnabled();

    /**
     * 查询分类列表（分页）
     *
     * @param offset 偏移量
     * @param limit  限制数量
     * @return 分类列表
     */
    List<Category> selectPage(@Param("offset") int offset, @Param("limit") int limit);

    /**
     * 查询分类总数
     *
     * @return 总数
     */
    int count();

    /**
     * 更新分类
     *
     * @param category 分类
     * @return 影响行数
     */
    int update(Category category);

    /**
     * 根据ID删除分类
     *
     * @param id 分类ID
     * @return 影响行数
     */
    int deleteById(Long id);

    /**
     * 根据父级分类ID删除子分类
     *
     * @param parentId 父级分类ID
     * @return 影响行数
     */
    int deleteByParentId(Long parentId);

    /**
     * 检查分类是否存在子分类
     *
     * @param id 分类ID
     * @return 是否存在子分类
     */
    boolean hasChildren(Long id);

    /**
     * 检查分类下是否有商品
     *
     * @param id 分类ID
     * @return 是否有商品
     */
    boolean hasProducts(Long id);

    /**
     * 获取分类路径
     *
     * @param id 分类ID
     * @return 分类路径（ID列表）
     */
    List<Category> getCategoryPath(Long id);
}