package com.example.Aptech_Final.Enity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "ORDERS_DETAIL")
public class OrdersDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORDERS_DETAIL_ID")
    private Long ordersDetailId; // ID chi tiết đơn hàng (tự động tăng)

    @Column(name = "ORDER_ID", nullable = false)
    private Long orderId; // ID đơn hàng

    @Column(name = "PRODUCT_ID", nullable = false)
    private Long productId; // ID sản phẩm

    @Column(name = "AMOUNT", nullable = false)
    private Integer amount; // Số lượng sản phẩm trong đơn hàng
    
    // Getter và Setter
	public Long getOrdersDetailId() {
		return ordersDetailId;
	}

	public void setOrdersDetailId(Long ordersDetailId) {
		this.ordersDetailId = ordersDetailId;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}
    
}
