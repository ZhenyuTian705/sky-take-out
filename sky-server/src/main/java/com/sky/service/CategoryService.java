package com.sky.service;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.result.PageResult;

public interface CategoryService {

    /**
     * 分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     *  修改分类
     * @param categoryDTO
     */
    void changeCategory(CategoryDTO categoryDTO);

    /**
     * 启用禁用分类
     * @param status
     * @param id
     */
    void changeStatus(Integer status, Long id);

    /**
     * 添加分类
     * @param categoryDTO
     */
    void addCategory(CategoryDTO categoryDTO);

    /**
     * 根据id删除分类
     * @param id
     */
    void deleteCategoryById(Long id);
}
