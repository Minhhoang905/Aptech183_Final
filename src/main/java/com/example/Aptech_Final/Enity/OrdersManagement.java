package com.example.Aptech_Final.Enity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "ODDERS_MANAGEMENT")
public class OrdersManagement {
	// ID đơn hàng (tự động tăng)
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ORDER_ID")
	private Long orderId;
	
	// Tên người dùng
	@Column(name = "USER_ID")
	private Long userId;

	// Phương thức thanh toán
	@Column(name = "PAYMENT_METHOD", nullable = false)
	private String paymentMethod;

	// Tình trạng đơn hàng (VD: Đang xử lý, Hoàn thành, Hủy...)
	@Column(name = "ORDERS_STATUS", nullable = false)
	private String orderStatus;

	// Tổng số tiền đơn hàng
	@Column(name = "TOTAL_AMOUNT", nullable = false, precision = 18, scale = 2)
	private BigDecimal totalAmount;

	// Getter và Setter
	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}


	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}
	
		
}
