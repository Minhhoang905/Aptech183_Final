package com.example.Aptech_Final.Controller.DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface OrdersManagementDTO {
    Long getUserId();  // ID người dùng
    String getName();  // Tên người dùng
    String getFullName();  // Tên đầy đủ của khách hàng
    String getEmail();  // Email khách hàng
    String getPhoneNumber();  // Số điện thoại khách hàng
    Long getProvinceId();  // ID tỉnh
    Long getDistrictId();  // ID quận
    Long getWardId();  // ID xã/phường
    String getProvinceName();  // Tên tỉnh
    String getDistrictName();  // Tên quận
    String getWardName();  // Tên xã/phường
    String getAddress();  // Địa chỉ khách hàng
    String getNote();  // Ghi chú
    String getPaymentMethod();  // Phương thức thanh toán
    String getOrderStatus();  // Trạng thái đơn hàng
    BigDecimal getTotalAmount();  // Tổng số tiền
    LocalDateTime getOrderStartTime();  // Thời gian bắt đầu đơn hàng
    LocalDateTime getOrderEndTime();  // Thời gian kết thúc đơn hàng

    Long getOrderId();  // Trả về Order ID
    String getProductName();  // Trả về tên sản phẩm (nếu có trong DTO)
    Integer getAmount();  // Trả về số lượng sản phẩm
    BigDecimal getUnitPrice();  // Trả về đơn giá sản phẩm
    BigDecimal getTotalPrice();  // Trả về tổng giá trị đơn hàng

}
