package com.example.Aptech_Final.Enity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "ORDERS_DETAIL")
public class OrdersDetail {
	// ID chi tiết đơn hàng (tự động tăng)
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ORDERS_DETAIL_ID")
	private Long ordersDetailId; 
	
	// ID đơn hàng
	@Column(name = "ORDER_ID", nullable = false)
	private Long orderId; 
	
	// ID sản phẩm
	@Column(name = "PRODUCT_ID", nullable = false)
	private Long productId; 
	
	// Số lượng sản phẩm trong đơn hàng
	@Column(name = "AMOUNT", nullable = false)
	private int  amount; 
	
	// Thời gian đặt hàng
    @Column(name = "LAST_UPDATED", nullable = false)
    private LocalDateTime lastUpdated;  

    // Giá sản phẩm tại thời điểm đặt hàng
    @Column(name = "UNIT_PRICE", nullable = false, precision = 18, scale = 2)
    private BigDecimal unitPrice;
    
    // Phương thức tính tổng tiền
    public BigDecimal  getTotalPrice() {
    	return (unitPrice != null) ? unitPrice.multiply(BigDecimal.valueOf(amount)) : BigDecimal.ZERO;
    }
    
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

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public LocalDateTime getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(LocalDateTime lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public BigDecimal getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}
             
}
