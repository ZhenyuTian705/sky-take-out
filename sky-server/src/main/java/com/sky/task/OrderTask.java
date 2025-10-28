package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class OrderTask {

    @Autowired
    OrderMapper orderMapper;

    @Scheduled(cron = "0 * * * * *")
    public void processTimeOut() {
        log.info("定时处理超时订单：{}", LocalDateTime.now());
        List<Orders> list = orderMapper.selectByPayStatus(Orders.PENDING_PAYMENT,LocalDateTime.now().plusMinutes(-15));
        if ( list!=null && list.size() > 0) {
            for (Orders order : list) {
                order.setStatus(Orders.CANCELLED);
                order.setCancelReason("订单超时");
                order.setCancelTime(LocalDateTime.now());
                orderMapper.update(order);
            }
        }
    }

    @Scheduled(cron = "0 0 1 * * *")
    public  void processDelivery(){
        log.info("处理一直在派送中的订单：{}", LocalDateTime.now());
        List<Orders> list = orderMapper.selectByPayStatus(Orders.DELIVERY_IN_PROGRESS,LocalDateTime.now().plusMinutes(-60));
        if ( list!=null && list.size() > 0) {
            for (Orders order : list) {
                order.setStatus(Orders.COMPLETED);
                orderMapper.update(order);
            }
        }
    }

}
