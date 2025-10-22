package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.exception.SetmealEnableFailedException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetMealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    SetmealMapper setmealMapper;

    @Autowired
    SetMealDishMapper setMealDishMapper;

    @Autowired
    DishMapper dishMapper;
    /**
     * 套餐分页
     * @param setmealPageQueryDTO
     * @return
     */
    @Override
    public PageResult page(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
        Page<Setmeal> page = setmealMapper.page(setmealPageQueryDTO);
        PageResult pageResult = new PageResult();
        Long total = page.getTotal();
        List<Setmeal> list = page.getResult();
        pageResult.setTotal(total);
        pageResult.setRecords(list);

        return pageResult;
    }

    /**
     * 添加套餐
     * @param setmealDTO
     */
    @Override
    @Transactional
    public void insertSetmeal(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmealMapper.insert(setmeal);
        Long id = setmeal.getId();
        setmealDTO.setId(id);
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishes.forEach(d -> {
            d.setSetmealId(id);
        });
        setMealDishMapper.insertBatch(setmealDishes);

    }

    /**
     * 根据 id查询套餐
     * @param id
     * @return
     */
    @Override
    public SetmealVO getById(Long id) {
        SetmealVO setmealVO = new SetmealVO();
        Setmeal setmeal = setmealMapper.getById(id);
        BeanUtils.copyProperties(setmeal, setmealVO);
        List<SetmealDish> setmealDishes = setMealDishMapper.getSetmealDishById(id);
        setmealVO.setSetmealDishes(setmealDishes);
        return setmealVO;
    }

    /**
     * 修改套餐
     * @param setmealDTO
     */
    @Override
    public void change(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmealMapper.update(setmeal);
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishes.forEach(d -> {
            d.setSetmealId(setmeal.getId());
        });
        setMealDishMapper.deleteBatch(setmealDishes);
        setMealDishMapper.insertBatch(setmealDishes);
    }

    /**
     * 批量删除套餐
     * @param ids
     */
    @Override
    @Transactional
    public void deleteBatch(List<Long> ids) {
        //套餐不能处于销售状态
        List<Setmeal> setmeals = setmealMapper.getByIds(ids);
        for (Setmeal setmeal : setmeals) {
            if(setmeal.getStatus()==1){
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        }
        //删除
        setmealMapper.deleteBatch(ids);
        setMealDishMapper.deleteBatch2(ids);
    }

    /**
     * 修改套餐状态
     * @param status
     * @param id
     */
    @Override
    public void setStatus(Integer status, Long id) {
        //如果套餐关联的菜品有停售则不可以起售
        List<Dish> dishes = dishMapper.getBySetmealId(id);
        for (Dish d : dishes) {
            if(d.getStatus()==0&&status==1){
                throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
            }
        }
        Setmeal setmeal = new Setmeal();
        setmeal.setStatus(status);
        setmeal.setId(id);
        setmealMapper.update(setmeal);
    }
}

