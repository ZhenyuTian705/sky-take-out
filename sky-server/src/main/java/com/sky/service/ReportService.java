package com.sky.service;

import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;

public interface ReportService {
    TurnoverReportVO turnoverStatistics(LocalDate startDate, LocalDate endDate);

    UserReportVO userStatistics(LocalDate startDate, LocalDate endDate);

    OrderReportVO ordersStatistics(LocalDate startDate, LocalDate endDate);

    SalesTop10ReportVO ordersTop10(LocalDate startDate, LocalDate endDate);

    void exportBusinessExcel(HttpServletResponse response);
}
