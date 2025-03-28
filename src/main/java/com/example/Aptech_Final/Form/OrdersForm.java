package com.example.Aptech_Final.Form;

import java.math.BigDecimal;

public class OrdersForm {
	// ID đơn hàng (tự động tăng)
	private Long orderId;
	// Tên người dùng
	private Long userId;
	// Username
	private String name;
	// Họ tên đầy đủ
	private String fullName;
	// Email
	private String email;
	// Số điện thoại
	private String phoneNumber;
	// Địa chỉ (Tỉnh/thành)
	private Long provinceId;
	// Địa chỉ (quận/huyện)
	private Long districtId;
	// Địa chỉ (xã/phường)
	private Long wardId;
	// Địa chỉ (số nhà cụ thể)
	private String address;
	// Note
	private String note;
	// Phương thức thanh toán
	private String paymentMethod;
	// Tình trạng đơn hàng 
	private String orderStatus;
	// Tổng số tiền đơn hàng
	private BigDecimal totalAmount;
	
	// Getter và setter
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public Long getProvinceId() {
		return provinceId;
	}
	public void setProvinceId(Long provinceId) {
		this.provinceId = provinceId;
	}
	public Long getDistrictId() {
		return districtId;
	}
	public void setDistrictId(Long districtId) {
		this.districtId = districtId;
	}
	public Long getWardId() {
		return wardId;
	}
	public void setWardId(Long wardId) {
		this.wardId = wardId;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
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
