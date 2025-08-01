package com.example.ecommerce.mapper;

import com.example.ecommerce.entity.Category;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 分类Mapper接口
 */
@Mapper
public interface CategoryMapper {

    /**
     * 插入分类
     *
     * @param category 分类
     * @return 影响行数
     */
    @Insert({
        "<script>",
        "INSERT INTO categories (name, description, parent_id, sort_order, status, created_at, updated_at)",
        "VALUES (#{name}, #{description}, #{parentId}, #{sortOrder}, #{status}, #{createdAt}, #{updatedAt})",
        "</script>"
    })
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Category category);

    /**
     * 根据ID查询分类
     *
     * @param id 分类ID
     * @return 分类
     */
    @Select("SELECT * FROM categories WHERE id = #{id}")
    Category selectById(Long id);

    /**
     * 根据父级分类ID查询子分类
     *
     * @param parentId 父级分类ID
     * @return 子分类列表
     */
    @Select("SELECT * FROM categories WHERE parent_id = #{parentId} ORDER BY sort_order ASC, id ASC")
    List<Category> selectByParentId(Long parentId);

    /**
     * 查询所有分类
     *
     * @return 分类列表
     */
    @Select("SELECT * FROM categories ORDER BY sort_order ASC, id ASC")
    List<Category> selectAll();

    /**
     * 查询所有启用的分类
     *
     * @return 分类列表
     */
    @Select("SELECT * FROM categories WHERE status = 1 ORDER BY sort_order ASC, id ASC")
    List<Category> selectAllEnabled();

    /**
     * 查询分类列表（分页）
     *
     * @param offset 偏移量
     * @param limit  限制数量
     * @return 分类列表
     */
    @Select("SELECT * FROM categories ORDER BY sort_order ASC, id ASC LIMIT #{offset}, #{limit}")
    List<Category> selectPage(@Param("offset") int offset, @Param("limit") int limit);

    /**
     * 查询分类总数
     *
     * @return 总数
     */
    @Select("SELECT COUNT(*) FROM categories")
    int count();

    /**
     * 更新分类
     *
     * @param category 分类
     * @return 影响行数
     */
    @Update({
        "<script>",
        "UPDATE categories",
        "<set>",
        "<if test='name != null'>name = #{name},</if>",
        "<if test='description != null'>description = #{description},</if>",
        "<if test='parentId != null'>parent_id = #{parentId},</if>",
        "<if test='sortOrder != null'>sort_order = #{sortOrder},</if>",
        "<if test='status != null'>status = #{status},</if>",
        "updated_at = #{updatedAt}",
        "</set>",
        "WHERE id = #{id}",
        "</script>"
    })
    int update(Category category);

    /**
     * 根据ID删除分类
     *
     * @param id 分类ID
     * @return 影响行数
     */
    @Delete("DELETE FROM categories WHERE id = #{id}")
    int deleteById(Long id);

    /**
     * 根据父级分类ID删除子分类
     *
     * @param parentId 父级分类ID
     * @return 影响行数
     */
    @Delete("DELETE FROM categories WHERE parent_id = #{parentId}")
    int deleteByParentId(Long parentId);

    /**
     * 检查分类是否存在子分类
     *
     * @param id 分类ID
     * @return 是否存在子分类
     */
    @Select("SELECT COUNT(*) > 0 FROM categories WHERE parent_id = #{id}")
    boolean hasChildren(Long id);

    /**
     * 检查分类下是否有商品
     *
     * @param id 分类ID
     * @return 是否有商品
     */
    @Select("SELECT COUNT(*) > 0 FROM products WHERE category_id = #{id}")
    boolean hasProducts(Long id);

    /**
     * 获取分类路径
     *
     * @param id 分类ID
     * @return 分类路径（ID列表）
     */
    @Select("<script>" +
            "WITH RECURSIVE category_tree AS (" +
            "    SELECT id, parent_id, name FROM categories WHERE id = #{id}" +
            "    UNION ALL" +
            "    SELECT c.id, c.parent_id, c.name FROM categories c" +
            "    INNER JOIN category_tree ct ON c.id = ct.parent_id" +
            ")" +
            "SELECT id, parent_id, name FROM category_tree ORDER BY parent_id DESC" +
            "</script>")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "parentId", column = "parent_id"),
        @Result(property = "name", column = "name")
    })
    List<Category> getCategoryPath(Long id);
}