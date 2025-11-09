package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotatiton.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface DishMapper {

    /**
     * 新增菜品
     * @param dish
     */
    @AutoFill(value = OperationType.INSERT)
    void insert(Dish dish);

    /**
     * 菜品分页
     * @return
     */
    Page<DishVO> page(DishPageQueryDTO queryDTO);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    @Select("select * from dish where id=#{id}")
    Dish getById(Long id);

    /**
     * 批量删除菜品
     * @param ids
     */
    void delete(List<Long> ids);

    /**
     * 更新菜品
     * @param dish
     */
    @AutoFill(value = OperationType.UPDATE)
    void update(Dish dish);


    @Select("select * from dish where category_id=#{categoryId}")
    List<Dish> getByCategoryId(Integer categoryId);

    /**
     * 根据套餐id查找其关联的菜品
     * @param id
     * @return
     */
    List<Dish> getBySetmealId(Long id);
    @Select("SELECT status from dish")
    List<Integer> getAllStatus();
}
