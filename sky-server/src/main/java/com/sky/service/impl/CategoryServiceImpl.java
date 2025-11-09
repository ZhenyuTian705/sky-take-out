package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryMapper categoryMapper;
    @Autowired
    DishMapper dishMapper;
    @Autowired
    SetmealMapper setmealMapper;
    /**
     * 分页查询业务实现
     * @param categoryPageQueryDTO
     * @return
     */
    public PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO) {
        //使用分页处理器
        PageHelper.startPage(categoryPageQueryDTO.getPage(), categoryPageQueryDTO.getPageSize());
        Integer type = categoryPageQueryDTO.getType();
        //查询数据库
        Page<Category> page = categoryMapper.pageQuery(categoryPageQueryDTO);
        //封装结果
        long total = page.getTotal();
        List<Category> list = page.getResult();
        PageResult pageResult = new PageResult(total, list);

        return pageResult;
    }

    /**
     * 修改分类业务实现
     * @param categoryDTO
     */
    @Override
    public void changeCategory(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);
        String name_last = categoryDTO.getName().substring(categoryDTO.getName().length() - 2 ,categoryDTO.getName().length());
        //确保类别和名称保持一致
        if(name_last.equals("套餐")){
            category.setType(2);
        } else{
            category.setType(1);
        }




        categoryMapper.update(category);
    }

    /**
     * 启用禁用分类
     * @param status
     * @param id
     */
    @Override
    public void changeStatus(Integer status, Long id) {
        Category category = new Category();
        category.setId(id);
        category.setStatus(status);
        categoryMapper.update(category);
    }

    /**
     * 添加分类
     * @param categoryDTO
     */
    @Override
    public void addCategory(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);

        category.setStatus(0);
        categoryMapper.addCategory(category);
    }

    /**
     * 根据id删除分类
     * @param id
     */
    @Override
    @Transactional
    public void deleteCategoryById(Long id) {
        List<Dish> list = dishMapper.getByCategoryId(id.intValue());
        if (list != null && list.size() > 0) {
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_DISH);
        }
        List<Setmeal> list2 = setmealMapper.getByCategoryId(id);
        if (list2 != null && list2.size() > 0) {
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_SETMEAL);
        }
        categoryMapper.deleteCategoryById(id);
    }

    /**
     * 根据类型查询分类
     * @param type
     * @return
     */
    @Override
    public List<Category> selectByType(Integer type) {

        List<Category> list= categoryMapper.selectByType(type);
        return list;
    }


}
