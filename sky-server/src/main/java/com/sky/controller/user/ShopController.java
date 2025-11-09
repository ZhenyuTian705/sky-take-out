package com.sky.controller.user;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController("userShopController")
@RequestMapping("/user/shop")
@Slf4j
@Api(tags = "店铺相关接口")
public class ShopController {
    @Autowired
    private RedisTemplate redisTemplate;

    public static final String SHOP_STATUS = "SHOP_STATUS";
    /**
     * 获取店铺的营业状态
     * @return
     */
    @ApiOperation(value = "获取营业状态")
    @GetMapping("/status")
    public Result<Integer> getStatus(){

        Integer status =(Integer) redisTemplate.opsForValue().get(SHOP_STATUS);
        log.info("获取店铺的营业状态为：{}",status==1?"营业中":"打样中");
        return Result.success(status);
    }
}
