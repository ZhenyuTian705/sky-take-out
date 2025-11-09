package com.sky.mapper;

import com.sky.entity.SetmealDish;
import com.sky.vo.DishItemVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetMealDishMapper {
    /**
     * 根据菜品ID查询套餐ID
     * @param dishIds
     * @return
     */
    List<Long> getSetMealIdsBydishIds(List<Long> dishIds);

    /**
     * 插入关系
     * @param setmealDishes
     */
    void insertBatch(List<SetmealDish> setmealDishes);

    /**
     * 根据套餐id 获得对应菜品信息
     * @param setmealId
     * @return
     */
    @Select("select  * from setmeal_dish where setmeal_id=#{setmealId}")
    List<SetmealDish> getSetmealDishById(Long setmealId);

    /**
     * 删除关系
     * @param setmealDishes
     */
    void deleteBatch(List<SetmealDish> setmealDishes);

    void deleteBatch2(List<Long> ids);

    @Select("select ds.name,sd.copies,ds.image,ds.description from dish ds inner join setmeal_dish sd on ds.id=sd.dish_id where sd.setmeal_id=#{id}")
    List<DishItemVO> GetDishesBySetmeal(Long id);
}
