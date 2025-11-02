package com.sky.controller.admin;


import com.sky.result.Result;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;

@RestController
@Slf4j
@Api(tags = "数据统计相关")
@RequestMapping("/admin/report")
public class ReportController {

    @Autowired
    ReportService reportService;

    @GetMapping("/turnoverStatistics")
    @ApiOperation(value = "营业额统计")
    public Result<TurnoverReportVO> turnoverStatistics(
            @RequestParam("begin") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate
            , @RequestParam("end") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {

        TurnoverReportVO reportVO = reportService.turnoverStatistics(startDate, endDate);
        return Result.success(reportVO);
    }

    @GetMapping("/userStatistics")
    public Result<UserReportVO> userStatistics(
            @RequestParam("begin") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate
            , @RequestParam("end") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        UserReportVO userReportVO = reportService.userStatistics(startDate, endDate);
        return Result.success(userReportVO);
    }

    @GetMapping("/ordersStatistics")
    public Result<OrderReportVO> ordersStatistics(
            @RequestParam("begin") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate
            , @RequestParam("end") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        OrderReportVO orderReportVO = reportService.ordersStatistics(startDate, endDate);
        return Result.success(orderReportVO);


    }

    @GetMapping("/top10")
    public Result<SalesTop10ReportVO> top10(
            @RequestParam("begin") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate
            , @RequestParam("end") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        SalesTop10ReportVO salesTop10ReportVO = reportService.ordersTop10(startDate, endDate);
        return Result.success(salesTop10ReportVO);
    }

    @GetMapping("/export")
    public void export(HttpServletResponse response) {
        reportService.exportBusinessExcel(response);
    }
}