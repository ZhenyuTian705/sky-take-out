package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotatiton.AutoFill;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealMapper {
    /**
     * 套餐分页
     * @param setmealPageQueryDTO
     * @return
     */

    Page<Setmeal> page(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 插入套餐
     * @param setmeal
     */
    @AutoFill(OperationType.INSERT)
    void insert(Setmeal setmeal);

    /**
     * 根据id查询套餐
     * @param id
     * @return
     */
    @Select("select * from setmeal where id=#{id}")
    Setmeal getById(Long id);

    /**
     * 更新套餐
     * @param setmeal
     */
    @AutoFill(OperationType.UPDATE)
    void update(Setmeal setmeal);

    /**
     * 批量删除
     * @param ids
     */
    void deleteBatch(List<Long> ids);

    /**
     * 批量获取
     * @param ids
     * @return
     */
    List<Setmeal> getByIds(List<Long> ids);


    @Select("select * from setmeal where category_id=#{categoryId}")
    List<Setmeal> getByCategoryId(Long categoryId);

    @Select("SELECT status from setmeal")
    List<Integer> getAllStatus();
}
