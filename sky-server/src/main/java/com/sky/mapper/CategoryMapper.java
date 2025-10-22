package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotatiton.AutoFill;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.*;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Mapper
public interface CategoryMapper {
    /**
     * 分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    Page<Category> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * 修改
     * @param category
     */
    @AutoFill(value = OperationType.UPDATE)
    void update(Category category);

    /**
     * 添加分类
     * @param category
     */
    @AutoFill(value = OperationType.INSERT)
    @Insert("insert into category (type,name,sort,status,create_time,update_time,create_user,update_user)"
    +" values "
    +"(#{type},#{name},#{sort},#{status},#{createTime},#{updateTime},#{createUser},#{updateUser})")
    void addCategory(Category category);

    /**
     * 删除分类根据ID
     * @param id
     */
    @Delete("delete from category where id=#{id}")
    void deleteCategoryById(Long id);

    /**
     * 根据类型查询分类
     * @param type
     * @return
     */
    @Select("select * from category where type=#{type}")
    List<Category> selectByType(Integer type);
}
