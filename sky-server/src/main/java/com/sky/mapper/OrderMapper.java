package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import com.sky.vo.OrderVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {
    void insert(Orders orders);
    /**
     * 根据订单号查询订单
     * @param orderNumber
     */
    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /**
     * 修改订单信息
     * @param orders
     */
    void update(Orders orders);

    Page<OrderVO> page(OrdersPageQueryDTO ordersPageQueryDTO);
    @Select("select * from orders where id=#{id}")
    Orders getById(Long id);

    @Select("select * from orders")
    List<Orders> getAll();

    @Select("select * from orders where status=#{Status} and order_time<#{orderTime}")
    List<Orders> selectByPayStatus(Integer Status, LocalDateTime orderTime);


    Double sumByMap(Map<String, Object> map);

    Integer sumNumberByMap(Map<String, Object> map);
}
