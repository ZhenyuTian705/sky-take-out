package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.OrderService;
import com.sky.service.ReportService;
import com.sky.service.WorkSpaceService;
import com.sky.vo.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
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
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;


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
        List<Integer> newUserList = new ArrayList<>();
        List<Integer> totalList = new ArrayList<>();
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

    @Override
    public void exportBusinessExcel(HttpServletResponse response) {
        //查询数据库获取营业数据
        LocalDate begin = LocalDate.now().minusDays(30);
        LocalDate end = LocalDate.now().minusDays(1);
        BusinessDataVO businessDataVO = getBusinessData(LocalDateTime.of(begin, LocalTime.MIN), LocalDateTime.of(end, LocalTime.MAX));
        //写入表格
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("template/excel.xlsx");

        try {
            XSSFWorkbook workbook = new XSSFWorkbook(in);
            XSSFSheet sheet = workbook.getSheet("Sheet1");
            sheet.getRow(1).getCell(1).setCellValue("开始时间："+begin+"结束时间："+end+"               ");
            sheet.getRow(3).getCell(2).setCellValue(businessDataVO.getTurnover());
            sheet.getRow(3).getCell(4).setCellValue(businessDataVO.getOrderCompletionRate());
            sheet.getRow(3).getCell(6).setCellValue(businessDataVO.getNewUsers());
            sheet.getRow(4).getCell(2).setCellValue(businessDataVO.getValidOrderCount());
            sheet.getRow(4).getCell(4).setCellValue(businessDataVO.getUnitPrice());

            for (int i=0 ;i<30;i++){
                LocalDate date = begin.plusDays(i);
                BusinessDataVO vo = getBusinessData(LocalDateTime.of(date, LocalTime.MIN), LocalDateTime.of(date, LocalTime.MAX));
                sheet.getRow(7+i).getCell(1).setCellValue(date+"");
                sheet.getRow(7+i).getCell(2).setCellValue(vo.getOrderCompletionRate());
                sheet.getRow(7+i).getCell(3).setCellValue(vo.getValidOrderCount());
                sheet.getRow(7+i).getCell(4).setCellValue(vo.getOrderCompletionRate());
                sheet.getRow(7+i).getCell(5).setCellValue(vo.getUnitPrice());
                sheet.getRow(7+i).getCell(6).setCellValue(vo.getNewUsers());
            }
            ServletOutputStream out = response.getOutputStream();
            workbook.write(out);

            out.close();
            in.close();

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private BusinessDataVO getBusinessData(LocalDateTime begin, LocalDateTime end) {
        Map<String, Object> searchMap = new HashMap();
        searchMap.put("min", begin);
        searchMap.put("max", end);
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
}
