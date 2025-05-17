package com.example.Aptech_Final.Security.VNPay;

import java.util.concurrent.ConcurrentHashMap;

import com.example.Aptech_Final.Form.OrdersManagementForm;

public class OrderCache {
    // Lưu trữ tạm thời các đơn hàng theo UUID
    private static final ConcurrentHashMap<String, OrdersManagementForm> orderCache = new ConcurrentHashMap<>();

    // Lưu OrdersManagementForm vào cache
    public static void saveOrderToCache(String orderId, OrdersManagementForm orderForm) {
        orderCache.put(orderId, orderForm);
    }

    // Lấy OrdersManagementForm từ cache bằng UUID
    public static OrdersManagementForm getOrderFromCache(String orderId) {
        return orderCache.get(orderId);
    }

    // Xóa đơn hàng khỏi cache nếu cần
    public static void removeOrderFromCache(String orderId) {
        orderCache.remove(orderId);
    }
}
