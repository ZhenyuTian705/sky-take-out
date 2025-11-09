package com.sky.service.impl;

import com.sky.entity.Orders;
import com.sky.entity.Setmeal;
import com.sky.mapper.DishMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.WorkSpaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WorkSpaceServiceImpl implements WorkSpaceService {
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    SetmealMapper setmealMapper;
    @Autowired
    DishMapper dishMapper;

    @Override
    public BusinessDataVO getTodayBusinessData() {
        Map<String, Object> searchMap = new HashMap();
        LocalDate today = LocalDate.now();
        LocalDateTime minDate = LocalDateTime.of(today, LocalTime.MIN);
        LocalDateTime maxDate = LocalDateTime.of(today, LocalTime.MAX);
        searchMap.put("min", minDate);
        searchMap.put("max", maxDate);
        Integer totalOrderCount = orderMapper.sumNumberByMap(searchMap);
        searchMap.put("status", Orders.COMPLETED);
        Integer validOrderCount = orderMapper.sumNumberByMap(searchMap);
        Double turnover = orderMapper.sumByMap(searchMap);
        if (turnover == null) {
            turnover = 0.0;
        }
        Double orderCompletionRate = (double) validOrderCount / (double) totalOrderCount;
        if(totalOrderCount==0){
            orderCompletionRate = 0.0;
        }
        Double unitPrice = (double) turnover / (double) validOrderCount;
        if (validOrderCount == 0) {
            unitPrice = 0.0;
        }
        Integer newUsers = userMapper.sumByMap(searchMap);

        return BusinessDataVO.builder()
                .newUsers(newUsers)
                .orderCompletionRate(orderCompletionRate)
                .unitPrice(unitPrice)
                .turnover(turnover)
                .validOrderCount(validOrderCount)
                .build();
    }

    @Override
    public SetmealOverViewVO getOverviewSetmeals() {

        List<Integer> list = setmealMapper.getAllStatus();
        SetmealOverViewVO vo = new SetmealOverViewVO();
        int soldCount = 0;
        int discount = 0;
        for (int i = 0; i < list.size(); i++) {
            int status = list.get(i);
            if (status == 1) {
                soldCount++;
            }else if (status == 0) {
                discount++;
            }
        }
        vo.setSold(soldCount);
        vo.setDiscontinued(discount);
        return vo;
    }

    @Override
    public DishOverViewVO getOverviewDishes() {
        List<Integer> list = dishMapper.getAllStatus();
        DishOverViewVO vo = new DishOverViewVO();
        int soldCount = 0;
        int discount = 0;
        for (int i = 0; i < list.size(); i++) {
            int status = list.get(i);
            if (status == 1) {
                soldCount++;
            }else if (status == 0) {
                discount++;
            }
        }
        vo.setSold(soldCount);
        vo.setDiscontinued(discount);
        return vo;
    }

    @Override
    public OrderOverViewVO getOverviewOrders() {
        List<Orders> list = orderMapper.getAll();
        int allOrderCount = list.size();
        int canceledOrderCount = 0;
        int completedOrderCount = 0;
        int deliveringOrderCount = 0;
        int waitingOrderCount = 0;
        for (int i = 0; i < allOrderCount; i++) {
            Orders order = list.get(i);
            if (order.getStatus() == Orders.CANCELLED) {
                canceledOrderCount++;
            }else if (order.getStatus() == Orders.COMPLETED) {
                completedOrderCount++;
            }else if(order.getStatus() == Orders.CONFIRMED){
                deliveringOrderCount++;
            }else if(order.getStatus() == Orders.TO_BE_CONFIRMED){
                waitingOrderCount++;
            }

        }
        return OrderOverViewVO.builder()
                .allOrders(allOrderCount)
                .cancelledOrders(canceledOrderCount)
                .completedOrders(completedOrderCount)
                .deliveredOrders(deliveringOrderCount)
                .waitingOrders(waitingOrderCount)
                .build();
    }
}
