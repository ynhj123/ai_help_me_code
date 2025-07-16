package com.admin.modules.dashboard.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Data
public class DashboardStatsDto {
    // 用户统计
    private Long totalUsers;
    private Long activeUsersToday;
    private Long newUsersToday;
    private Long newUsersThisWeek;
    private Long newUsersThisMonth;
    
    // 商品统计
    private Long totalProducts;
    private Long activeProducts;
    private Long lowStockProducts;
    private Long outOfStockProducts;
    
    // 订单统计
    private Long totalOrders;
    private Long ordersToday;
    private Long ordersThisWeek;
    private Long ordersThisMonth;
    private BigDecimal revenueToday;
    private BigDecimal revenueThisWeek;
    private BigDecimal revenueThisMonth;
    
    // 转化率
    private BigDecimal productConversionRate;
    private BigDecimal orderConversionRate;
    private BigDecimal userRetentionRate;
    
    // 物流统计
    private Long totalShipments;
    private Long pendingShipments;
    private Long deliveredShipments;
    private Long returnedShipments;
    
    // 图表数据
    private Map<String, Long> userGrowthTrend;
    private Map<String, BigDecimal> revenueTrend;
    private Map<String, Long> orderTrend;
    private Map<String, Long> productSalesRanking;
    
    // 实时数据
    private LocalDateTime lastUpdated;
}