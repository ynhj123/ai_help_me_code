package com.admin.modules.dashboard.service;

import com.admin.modules.auth.entity.User;
import com.admin.modules.auth.repository.UserRepository;
import com.admin.modules.dashboard.dto.DashboardStatsDto;
import com.admin.modules.logistics.entity.ShippingOrder;
import com.admin.modules.logistics.enums.ShippingStatus;
import com.admin.modules.logistics.repository.ShippingOrderRepository;
import com.admin.modules.order.entity.Order;
import com.admin.modules.order.enums.OrderStatus;
import com.admin.modules.order.repository.OrderRepository;
import com.admin.modules.product.entity.Product;
import com.admin.modules.product.entity.Sku;
import com.admin.modules.product.enums.ProductStatus;
import com.admin.modules.product.repository.ProductRepository;
import com.admin.modules.product.repository.SkuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {
    
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final SkuRepository skuRepository;
    private final OrderRepository orderRepository;
    private final ShippingOrderRepository shippingOrderRepository;
    
    public DashboardStatsDto getDashboardStats() {
        DashboardStatsDto stats = new DashboardStatsDto();
        
        // 用户统计
        calculateUserStats(stats);
        
        // 商品统计
        calculateProductStats(stats);
        
        // 订单统计
        calculateOrderStats(stats);
        
        // 转化率计算
        calculateConversionRates(stats);
        
        // 物流统计
        calculateLogisticsStats(stats);
        
        // 趋势数据
        calculateTrendData(stats);
        
        stats.setLastUpdated(LocalDateTime.now());
        
        return stats;
    }
    
    private void calculateUserStats(DashboardStatsDto stats) {
        LocalDateTime todayStart = LocalDate.now().atStartOfDay();
        LocalDateTime weekStart = LocalDate.now().minusDays(7).atStartOfDay();
        LocalDateTime monthStart = LocalDate.now().minusDays(30).atStartOfDay();
        
        // 总用户数
        stats.setTotalUsers(userRepository.count());
        
        // 今日活跃用户（假设有最后登录时间字段）
        stats.setActiveUsersToday(userRepository.countByLastLoginTimeAfter(todayStart));
        
        // 今日新用户
        stats.setNewUsersToday(userRepository.countByCreatedAtAfter(todayStart));
        
        // 本周新用户
        stats.setNewUsersThisWeek(userRepository.countByCreatedAtAfter(weekStart));
        
        // 本月新用户
        stats.setNewUsersThisMonth(userRepository.countByCreatedAtAfter(monthStart));
    }
    
    private void calculateProductStats(DashboardStatsDto stats) {
        List<Product> allProducts = productRepository.findAll();
        List<Sku> allSkus = skuRepository.findAll();
        
        // 总商品数
        stats.setTotalProducts((long) allProducts.size());
        
        // 活跃商品数（上架状态）
        stats.setActiveProducts(allProducts.stream()
                .filter(p -> p.getStatus() == ProductStatus.ACTIVE)
                .count());
        
        // 低库存商品（库存<10）
        stats.setLowStockProducts(allSkus.stream()
                .filter(s -> s.getStock() != null && s.getStock() < 10 && s.getStock() > 0)
                .count());
        
        // 缺货商品
        stats.setOutOfStockProducts(allSkus.stream()
                .filter(s -> s.getStock() != null && s.getStock() <= 0)
                .count());
    }
    
    private void calculateOrderStats(DashboardStatsDto stats) {
        LocalDateTime todayStart = LocalDate.now().atStartOfDay();
        LocalDateTime weekStart = LocalDate.now().minusDays(7).atStartOfDay();
        LocalDateTime monthStart = LocalDate.now().minusDays(30).atStartOfDay();
        
        List<Order> allOrders = orderRepository.findAll();
        
        // 总订单数
        stats.setTotalOrders((long) allOrders.size());
        
        // 今日订单
        stats.setOrdersToday(orderRepository.countByCreatedAtAfter(todayStart));
        
        // 本周订单
        stats.setOrdersThisWeek(orderRepository.countByCreatedAtAfter(weekStart));
        
        // 本月订单
        stats.setOrdersThisMonth(orderRepository.countByCreatedAtAfter(monthStart));
        
        // 今日收入
        stats.setRevenueToday(calculateRevenue(orderRepository.findByCreatedAtAfter(todayStart)));
        
        // 本周收入
        stats.setRevenueThisWeek(calculateRevenue(orderRepository.findByCreatedAtAfter(weekStart)));
        
        // 本月收入
        stats.setRevenueThisMonth(calculateRevenue(orderRepository.findByCreatedAtAfter(monthStart)));
    }
    
    private void calculateConversionRates(DashboardStatsDto stats) {
        long totalUsers = stats.getTotalUsers();
        long totalOrders = stats.getTotalOrders();
        long totalProducts = stats.getTotalProducts();
        
        // 订单转化率 = 下单用户数 / 总用户数
        if (totalUsers > 0) {
            long orderedUsers = orderRepository.findDistinctUserIds().size();
            BigDecimal orderConversionRate = BigDecimal.valueOf(orderedUsers)
                    .divide(BigDecimal.valueOf(totalUsers), 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
            stats.setOrderConversionRate(orderConversionRate);
        } else {
            stats.setOrderConversionRate(BigDecimal.ZERO);
        }
        
        // 商品转化率 = 有销量的商品数 / 总商品数
        if (totalProducts > 0) {
            List<Long> soldProductIds = orderRepository.findSoldProductIds();
            long soldProducts = soldProductIds.stream().distinct().count();
            BigDecimal productConversionRate = BigDecimal.valueOf(soldProducts)
                    .divide(BigDecimal.valueOf(totalProducts), 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
            stats.setProductConversionRate(productConversionRate);
        } else {
            stats.setProductConversionRate(BigDecimal.ZERO);
        }
        
        // 用户留存率（简化计算：30天内再次登录的用户比例）
        if (totalUsers > 0) {
            LocalDateTime thirtyDaysAgo = LocalDate.now().minusDays(30).atStartOfDay();
            long retainedUsers = userRepository.countByLastLoginTimeAfter(thirtyDaysAgo);
            BigDecimal retentionRate = BigDecimal.valueOf(retainedUsers)
                    .divide(BigDecimal.valueOf(totalUsers), 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
            stats.setUserRetentionRate(retentionRate);
        } else {
            stats.setUserRetentionRate(BigDecimal.ZERO);
        }
    }
    
    private void calculateLogisticsStats(DashboardStatsDto stats) {
        List<ShippingOrder> allShipments = shippingOrderRepository.findAll();
        
        // 总运单数
        stats.setTotalShipments((long) allShipments.size());
        
        // 待发货
        stats.setPendingShipments(allShipments.stream()
                .filter(s -> s.getStatus() == ShippingStatus.PENDING || s.getStatus() == ShippingStatus.PROCESSING)
                .count());
        
        // 已送达
        stats.setDeliveredShipments(allShipments.stream()
                .filter(s -> s.getStatus() == ShippingStatus.DELIVERED)
                .count());
        
        // 已退回
        stats.setReturnedShipments(allShipments.stream()
                .filter(s -> s.getStatus() == ShippingStatus.RETURNED)
                .count());
    }
    
    private void calculateTrendData(DashboardStatsDto stats) {
        // 用户增长趋势（最近7天）
        Map<String, Long> userGrowthTrend = new LinkedHashMap<>();
        for (int i = 6; i >= 0; i--) {
            LocalDate date = LocalDate.now().minusDays(i);
            LocalDateTime startOfDay = date.atStartOfDay();
            LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
            long count = userRepository.countByCreatedAtBetween(startOfDay, endOfDay);
            userGrowthTrend.put(date.toString(), count);
        }
        stats.setUserGrowthTrend(userGrowthTrend);
        
        // 收入趋势（最近7天）
        Map<String, BigDecimal> revenueTrend = new LinkedHashMap<>();
        for (int i = 6; i >= 0; i--) {
            LocalDate date = LocalDate.now().minusDays(i);
            LocalDateTime startOfDay = date.atStartOfDay();
            LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
            List<Order> dailyOrders = orderRepository.findByCreatedAtBetween(startOfDay, endOfDay);
            BigDecimal dailyRevenue = calculateRevenue(dailyOrders);
            revenueTrend.put(date.toString(), dailyRevenue);
        }
        stats.setRevenueTrend(revenueTrend);
        
        // 订单趋势（最近7天）
        Map<String, Long> orderTrend = new LinkedHashMap<>();
        for (int i = 6; i >= 0; i--) {
            LocalDate date = LocalDate.now().minusDays(i);
            LocalDateTime startOfDay = date.atStartOfDay();
            LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
            long count = orderRepository.countByCreatedAtBetween(startOfDay, endOfDay);
            orderTrend.put(date.toString(), count);
        }
        stats.setOrderTrend(orderTrend);
        
        // 商品销量排行（前10）
        Map<String, Long> productSalesRanking = orderRepository.findProductSalesRanking()
                .stream()
                .limit(10)
                .collect(Collectors.toMap(
                        entry -> entry.get("productName").toString(),
                        entry -> Long.valueOf(entry.get("salesCount").toString()),
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
        stats.setProductSalesRanking(productSalesRanking);
    }
    
    private BigDecimal calculateRevenue(List<Order> orders) {
        return orders.stream()
                .filter(order -> order.getStatus() == OrderStatus.COMPLETED || order.getStatus() == OrderStatus.DELIVERED)
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}