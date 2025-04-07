package com.example.Aptech_Final.Controller.DTO;

import java.math.BigDecimal;

public interface OrderDetailDTO {
    Long getOrdersDetailId();
    Long getOrderId();
    Long getProductId();
    String getProductName();
    Integer getAmount();
    BigDecimal getUnitPrice();
    BigDecimal getTotalPrice();

}
