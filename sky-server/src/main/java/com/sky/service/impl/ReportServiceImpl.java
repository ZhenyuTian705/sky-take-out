package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.OrderService;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import org.apache.commons.lang3.StringUtils;
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
public class ReportServiceImpl implements ReportService {

    @Autowired
    OrderMapper orderMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    OrderDetailMapper orderDetailMapper;

    @Override
    public TurnoverReportVO turnoverStatistics(LocalDate startDate, LocalDate endDate) {
        List<LocalDate> dates = new ArrayList<>();
        dates.add(startDate);
        while (!startDate.equals(endDate)) {
            startDate = startDate.plusDays(1);
            dates.add(startDate);
        }
        List<Double> turnoverList = new ArrayList<>();
        for (LocalDate date : dates) {
            LocalDateTime min = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime max = LocalDateTime.of(date, LocalTime.MAX);
            Map<String, Object> map = new HashMap();
            map.put("min", min);
            map.put("max", max);
            map.put("status", Orders.COMPLETED);
            Double turnover = orderMapper.sumByMap(map);
            if (turnover == null) {
                turnover = 0.0;
            }
            turnoverList.add(turnover);
        }


        return TurnoverReportVO.builder().dateList(StringUtils.join(dates, ","))
                .turnoverList(StringUtils.join(turnoverList, ","))
                .build();
    }

    @Override
    public UserReportVO userStatistics(LocalDate startDate, LocalDate endDate) {
        List<LocalDate> dates = new ArrayList<>();
        dates.add(startDate);
        while (!startDate.equals(endDate)) {
            startDate = startDate.plusDays(1);
            dates.add(startDate);
        }
        List<Double> newUserList = new ArrayList<>();
        List<Double> totalList = new ArrayList<>();
        for (LocalDate date : dates) {
            LocalDateTime min = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime max = LocalDateTime.of(date, LocalTime.MAX);
            Map<String, Object> map = new HashMap();
            map.put("max", max);
            totalList.add(userMapper.sumByMap(map));
            map.put("min", min);
            newUserList.add(userMapper.sumByMap(map));
        }
        return UserReportVO.builder()
                .dateList(StringUtils.join(dates, ","))
                .newUserList(StringUtils.join(newUserList, ","))
                .totalUserList(StringUtils.join(totalList, ","))
                .build();
    }

    @Override
    public OrderReportVO ordersStatistics(LocalDate startDate, LocalDate endDate) {
        List<LocalDate> dates = new ArrayList<>();
        dates.add(startDate);
        while (!startDate.equals(endDate)) {
            startDate = startDate.plusDays(1);
            dates.add(startDate);
        }
        List<Integer> totalList = new ArrayList<>();
        List<Integer> effectiveList = new ArrayList<>();

        int totalOrderCount = 0;
        int effectiveOrderCount = 0;
        for (LocalDate date : dates) {
            LocalDateTime min = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime max = LocalDateTime.of(date, LocalTime.MAX);
            Map<String, Object> map = new HashMap();
            map.put("min", min);
            map.put("max", max);
            Integer number_all = orderMapper.sumNumberByMap(map);
            totalOrderCount += number_all;
            totalList.add(number_all);
            map.put("status", Orders.COMPLETED);
            Integer number_effective = orderMapper.sumNumberByMap(map);
            effectiveList.add(number_effective);
            effectiveOrderCount += number_effective;
        }
        Double orderCompletionRate = ((double) effectiveOrderCount) / (double) totalOrderCount;
        return OrderReportVO.builder()
                .dateList(StringUtils.join(dates, ","))
                .orderCompletionRate(orderCompletionRate)
                .totalOrderCount(totalOrderCount)
                .validOrderCount(effectiveOrderCount)
                .orderCountList(StringUtils.join(totalList, ","))
                .validOrderCountList(StringUtils.join(effectiveList, ","))
                .build();
    }

    @Override
    public SalesTop10ReportVO ordersTop10(LocalDate startDate, LocalDate endDate) {
        LocalDateTime min = LocalDateTime.of(startDate, LocalTime.MIN);
        LocalDateTime max = LocalDateTime.of(endDate, LocalTime.MAX);
        if (min==null || max==null) {
            throw new RuntimeException("时间不完整");
        }
        Map<String, Object> map = new HashMap();
        map.put("min", min);
        map.put("max", max);
        List<GoodsSalesDTO> goodsSalesDTOList = orderDetailMapper.getTop10(map);
        List<String> nameList = new ArrayList<>();
        List<Integer> numberList = new ArrayList<>();
        for (GoodsSalesDTO goodsSalesDTO : goodsSalesDTOList) {
            nameList.add(goodsSalesDTO.getName());
            numberList.add(goodsSalesDTO.getNumber());
        }

        return SalesTop10ReportVO.builder()
                .nameList(StringUtils.join(nameList, ","))
                .numberList(StringUtils.join(numberList, ","))
                .build();
    }
}
