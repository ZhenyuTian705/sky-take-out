package com.sky.controller.user;

import com.sky.entity.Dish;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController("userDishController")
@Slf4j
@Api(tags = "用户端菜品接口")
@RequestMapping("/user/dish")
public class DishController {
    @Autowired
    DishService dishService;

    @Autowired
    RedisTemplate redisTemplate;

    @GetMapping("/list")
    @ApiOperation("用户端根据分类获得菜品")
    public Result<List<DishVO>> getDishesByCategoryId(int categoryId) {
        //构造redis的key
        String key="dish_"+categoryId;
        List<DishVO> dishVOList =(List<DishVO>)redisTemplate.opsForValue().get(key);
        if(dishVOList!=null&&dishVOList.size()>0){
            return Result.success(dishVOList);
        }

        log.info("根据分类获得菜品：{}", categoryId);
        dishVOList = dishService.getDishesByCategoryId(categoryId);
        dishVOList.removeIf(dishVO -> dishVO.getStatus() == 0);
        redisTemplate.opsForValue().set(key,dishVOList);

        return Result.success(dishVOList);
    }

}
