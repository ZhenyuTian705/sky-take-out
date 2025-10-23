package com.sky.controller.admin;


import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("adminSetmealController")
@Slf4j
@Api(tags = "套餐管理")
@RequestMapping("/admin/setmeal")
public class SetmealController {
    @Autowired
    SetmealService setmealService;

    /**
     * 套餐分页
     *
     * @param setmealPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation(value = "套餐分页")
    public Result<PageResult> page(SetmealPageQueryDTO setmealPageQueryDTO) {
        log.info("套餐分页:{}", setmealPageQueryDTO);
        PageResult pageResult = setmealService.page(setmealPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 添加套餐
     *
     * @param setmealDTO
     * @return
     */
    @PostMapping
    @ApiOperation(value = "添加套餐")
    public Result addSetmeel(@RequestBody SetmealDTO setmealDTO) {
        log.info("添加套餐:{}", setmealService);
        setmealService.insertSetmeal(setmealDTO);
        return Result.success();
    }

    /**
     * 根据 id查询套餐
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "根据id查询套餐")
    public Result<SetmealVO> getById(@PathVariable Long id){
        SetmealVO setmealVO = setmealService.getById(id);
        return Result.success(setmealVO);
    }

    /**
     * 修改套餐信息
     *
     * @param setmealDTO
     * @return
     */
    @PutMapping
    @ApiOperation(value = "修改套餐信息")
    public Result change(@RequestBody SetmealDTO setmealDTO) {
        log.info("修改套餐信息:{}", setmealDTO);
        setmealService.change(setmealDTO);
        return Result.success();

    }

    /**
     * 批量删除套餐
     * @param ids
     * @return
     */
    @DeleteMapping
    @ApiOperation(value = "批量删除套餐")
    public Result deleteBatch(@RequestParam List<Long> ids) {
        log.info("批量删除:{}", ids);
        setmealService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 修改菜单状态
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation(value = "修改菜单状态")
    public Result setStatus(@PathVariable Integer status, Long id) {
        log.info("修改套餐状态:{}", status);
        setmealService.setStatus(status, id);

        return Result.success();
    }


}
