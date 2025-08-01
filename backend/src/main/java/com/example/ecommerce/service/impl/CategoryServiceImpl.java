package com.example.ecommerce.service.impl;

import com.example.ecommerce.dto.CategoryDTO;
import com.example.ecommerce.dto.CategoryTreeNodeDTO;
import com.example.ecommerce.entity.Category;
import com.example.ecommerce.enums.CategoryStatus;
import com.example.ecommerce.exception.BusinessException;
import com.example.ecommerce.mapper.CategoryMapper;
import com.example.ecommerce.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 分类服务实现类
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    @Transactional
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        // 检查父级分类是否存在
        if (categoryDTO.getParentId() != null && categoryDTO.getParentId() != 0) {
            Category parentCategory = categoryMapper.selectById(categoryDTO.getParentId());
            if (parentCategory == null) {
                throw new BusinessException(400, "父级分类不存在");
            }
        }

        // 创建分类
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);
        category.setStatus(CategoryStatus.ACTIVE);
        category.setCreatedAt(LocalDateTime.now());
        category.setUpdatedAt(LocalDateTime.now());

        // 保存分类
        categoryMapper.insert(category);

        // 转换为DTO
        CategoryDTO createdCategoryDTO = new CategoryDTO();
        BeanUtils.copyProperties(category, createdCategoryDTO);
        return createdCategoryDTO;
    }

    @Override
    @Transactional
    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
        // 查询分类
        Category category = categoryMapper.selectById(id);
        if (category == null) {
            throw new BusinessException(404, "分类不存在");
        }

        // 检查父级分类是否存在
        if (categoryDTO.getParentId() != null && categoryDTO.getParentId() != 0) {
            // 不能将分类移动到自己的子分类下
            if (categoryDTO.getParentId().equals(id)) {
                throw new BusinessException(400, "不能将分类移动到自己的子分类下");
            }

            // 检查父级分类是否存在
            Category parentCategory = categoryMapper.selectById(categoryDTO.getParentId());
            if (parentCategory == null) {
                throw new BusinessException(400, "父级分类不存在");
            }
        }

        // 更新分类
        BeanUtils.copyProperties(categoryDTO, category, "id", "createdAt", "status");
        category.setUpdatedAt(LocalDateTime.now());

        // 保存分类
        categoryMapper.update(category);

        // 转换为DTO
        CategoryDTO updatedCategoryDTO = new CategoryDTO();
        BeanUtils.copyProperties(category, updatedCategoryDTO);
        return updatedCategoryDTO;
    }

    @Override
    public CategoryDTO getCategoryById(Long id) {
        // 查询分类
        Category category = categoryMapper.selectById(id);
        if (category == null) {
            throw new BusinessException(404, "分类不存在");
        }

        // 转换为DTO
        CategoryDTO categoryDTO = new CategoryDTO();
        BeanUtils.copyProperties(category, categoryDTO);
        return categoryDTO;
    }

    @Override
    public List<CategoryDTO> getCategoriesByParentId(Long parentId) {
        // 查询子分类
        List<Category> categoryList = categoryMapper.selectByParentId(parentId);

        // 转换为DTO列表
        return categoryList.stream().map(category -> {
            CategoryDTO categoryDTO = new CategoryDTO();
            BeanUtils.copyProperties(category, categoryDTO);
            return categoryDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        // 查询所有分类
        List<Category> categoryList = categoryMapper.selectAll();

        // 转换为DTO列表
        return categoryList.stream().map(category -> {
            CategoryDTO categoryDTO = new CategoryDTO();
            BeanUtils.copyProperties(category, categoryDTO);
            return categoryDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public List<CategoryDTO> getAllEnabledCategories() {
        // 查询所有启用的分类
        List<Category> categoryList = categoryMapper.selectAllEnabled();

        // 转换为DTO列表
        return categoryList.stream().map(category -> {
            CategoryDTO categoryDTO = new CategoryDTO();
            BeanUtils.copyProperties(category, categoryDTO);
            return categoryDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public List<CategoryDTO> getCategoryList(Integer page, Integer size) {
        // 计算偏移量
        int offset = (page - 1) * size;

        // 查询分类列表
        List<Category> categoryList = categoryMapper.selectPage(offset, size);

        // 转换为DTO列表
        return categoryList.stream().map(category -> {
            CategoryDTO categoryDTO = new CategoryDTO();
            BeanUtils.copyProperties(category, categoryDTO);
            return categoryDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public int getCategoryCount() {
        return categoryMapper.count();
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        // 查询分类
        Category category = categoryMapper.selectById(id);
        if (category == null) {
            throw new BusinessException(404, "分类不存在");
        }

        // 检查是否有子分类
        if (categoryMapper.hasChildren(id)) {
            throw new BusinessException(400, "该分类下存在子分类，不能删除");
        }

        // 检查是否有商品
        if (categoryMapper.hasProducts(id)) {
            throw new BusinessException(400, "该分类下存在商品，不能删除");
        }

        // 删除分类
        categoryMapper.deleteById(id);
    }

    @Override
    @Transactional
    public CategoryDTO enableCategory(Long id) {
        // 查询分类
        Category category = categoryMapper.selectById(id);
        if (category == null) {
            throw new BusinessException(404, "分类不存在");
        }

        // 启用分类
        category.setStatus(CategoryStatus.ACTIVE);
        category.setUpdatedAt(LocalDateTime.now());
        categoryMapper.update(category);

        // 转换为DTO
        CategoryDTO categoryDTO = new CategoryDTO();
        BeanUtils.copyProperties(category, categoryDTO);
        return categoryDTO;
    }

    @Override
    @Transactional
    public CategoryDTO disableCategory(Long id) {
        // 查询分类
        Category category = categoryMapper.selectById(id);
        if (category == null) {
            throw new BusinessException(404, "分类不存在");
        }

        // 禁用分类
        category.setStatus(CategoryStatus.INACTIVE);
        category.setUpdatedAt(LocalDateTime.now());
        categoryMapper.update(category);

        // 转换为DTO
        CategoryDTO categoryDTO = new CategoryDTO();
        BeanUtils.copyProperties(category, categoryDTO);
        return categoryDTO;
    }

    @Override
    public List<CategoryTreeNodeDTO> getCategoryTree() {
        // 获取所有分类
        List<CategoryDTO> allCategories = getAllCategories();

        // 转换为树节点
        List<CategoryTreeNodeDTO> treeNodes = allCategories.stream()
                .map(this::convertToTreeNode)
                .collect(Collectors.toList());

        // 构建树结构
        return buildCategoryTree(treeNodes, 0L);
    }

    @Override
    public List<CategoryDTO> getCategoryPath(Long id) {
        // 查询分类路径
        List<Category> categoryPath = categoryMapper.getCategoryPath(id);

        // 转换为DTO列表
        return categoryPath.stream().map(category -> {
            CategoryDTO categoryDTO = new CategoryDTO();
            BeanUtils.copyProperties(category, categoryDTO);
            return categoryDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public boolean hasChildren(Long id) {
        return categoryMapper.hasChildren(id);
    }

    @Override
    public boolean hasProducts(Long id) {
        return categoryMapper.hasProducts(id);
    }

    @Override
    @Transactional
    public CategoryDTO moveCategory(Long id, Long newParentId) {
        // 查询分类
        Category category = categoryMapper.selectById(id);
        if (category == null) {
            throw new BusinessException(404, "分类不存在");
        }

        // 检查新的父级分类是否存在
        if (newParentId != null && newParentId != 0) {
            Category newParentCategory = categoryMapper.selectById(newParentId);
            if (newParentCategory == null) {
                throw new BusinessException(400, "新的父级分类不存在");
            }
        }

        // 移动分类
        category.setParentId(newParentId);
        category.setUpdatedAt(LocalDateTime.now());
        categoryMapper.update(category);

        // 转换为DTO
        CategoryDTO categoryDTO = new CategoryDTO();
        BeanUtils.copyProperties(category, categoryDTO);
        return categoryDTO;
    }

    @Override
    @Transactional
    public CategoryDTO updateCategorySortOrder(Long id, Integer sortOrder) {
        // 查询分类
        Category category = categoryMapper.selectById(id);
        if (category == null) {
            throw new BusinessException(404, "分类不存在");
        }

        // 更新排序
        category.setSortOrder(sortOrder);
        category.setUpdatedAt(LocalDateTime.now());
        categoryMapper.update(category);

        // 转换为DTO
        CategoryDTO categoryDTO = new CategoryDTO();
        BeanUtils.copyProperties(category, categoryDTO);
        return categoryDTO;
    }

    /**
     * 转换为树节点
     *
     * @param categoryDTO 分类DTO
     * @return 树节点
     */
    private CategoryTreeNodeDTO convertToTreeNode(CategoryDTO categoryDTO) {
        CategoryTreeNodeDTO treeNode = new CategoryTreeNodeDTO();
        BeanUtils.copyProperties(categoryDTO, treeNode);
        return treeNode;
    }

    /**
     * 构建分类树
     *
     * @param nodes  所有节点
     * @param parentId 父级ID
     * @return 树结构
     */
    private List<CategoryTreeNodeDTO> buildCategoryTree(List<CategoryTreeNodeDTO> nodes, Long parentId) {
        List<CategoryTreeNodeDTO> tree = new ArrayList<>();

        for (CategoryTreeNodeDTO node : nodes) {
            if (parentId.equals(node.getParentId())) {
                List<CategoryTreeNodeDTO> children = buildCategoryTree(nodes, node.getId());
                node.setChildren(children);
                node.setHasChildren(!children.isEmpty());
                tree.add(node);
            }
        }

        return tree;
    }
}