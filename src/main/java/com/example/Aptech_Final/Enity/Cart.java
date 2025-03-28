package com.example.Aptech_Final.Enity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "CART")
public class Cart {
	// Id giỏ hàng
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CART_ID")
	private Long cartId;
	
	// Id của người dùng
	@Column(name = "USER_ID", nullable = false)
	private Long userId;
	
	// Id của sản phẩm
	@Column(name = "PRODUCT_ID", nullable = false)
	private Long productID;
	
	// Số lượng đơn hàng
	@Column(name = "AMOUNT", nullable = false)
	private Integer amount;
	
	// Thời gian cập nhập giỏ hàng
	@Column(name = "LAST_UPDATED", nullable = false)
	private LocalDateTime lastUpdated;

	// Getter và setter
	public Long getCartId() {
		return cartId;
	}

	public void setCartId(Long cartId) {
		this.cartId = cartId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getProductID() {
		return productID;
	}

	public void setProductID(Long productID) {
		this.productID = productID;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public LocalDateTime getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(LocalDateTime lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
	
	
}
