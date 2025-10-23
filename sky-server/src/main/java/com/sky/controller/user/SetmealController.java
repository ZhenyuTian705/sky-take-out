package com.sky.controller.user;

import com.sky.entity.Setmeal;
import com.sky.mapper.SetMealDishMapper;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("userSetmealController")
@Slf4j
@Api(tags = "用户端套餐管理")
@RequestMapping("/user/setmeal")
public class SetmealController {

    @Autowired
    private  SetmealService setmealService;


    @GetMapping("/list")
    public Result<List<Setmeal>> getSetmealByCategory(Integer categoryId) {
        List<Setmeal> list = setmealService.getByCategory(categoryId);
        return Result.success(list);
    }

    @GetMapping("/dish/{id}")
    public Result<List<DishItemVO>> getDishBySetMeal(@PathVariable Long id) {
        List<DishItemVO> list = setmealService.GetDishesBySetmeal(id);
        return Result.success(list);
    }
}
