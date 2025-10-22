package com.sky.mapper;

import com.sky.annotatiton.AutoFill;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishFlavorMapper {

    /**
     *  批量插入口味
     * @param flavors
     */
    void insertBatch(List<DishFlavor> flavors);

    /**
     * 删除菜品关联的口味
     * @param ids
     */
    void delete(List<Long> ids);

    /**
     * 根据id获得口味
     * @param id
     * @return
     */
    @Select("select * from dish_flavor where dish_id=#{id}")
    List<DishFlavor> getFlavorsById(Long id);


}
