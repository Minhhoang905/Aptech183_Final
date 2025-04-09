package com.example.Aptech_Final.Form;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrdersManagementForm {
	private Long orderId;
    private Long userId;
    // Tên user (user_name từ bảng USERS)
    private String name;
    // Tên người nhận ở OrdersManagementForm
    private String fullName;
    private String email;
    private String phoneNumber;
    
    private Long provinceId;
    private Long districtId;
    private Long wardId;
    
    private String address;
    private String note;
    private String paymentMethod;
	private String orderStatus;
	private BigDecimal totalAmount;
	
    private LocalDateTime orderStartTime;
    private LocalDateTime orderEndTime;

    // Danh sách sản phẩm trong từ giỏ hàng
    private List<CartForm> items;
    
    // Getters và Setters
    
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
	public List<CartForm> getItems() {
		return items;
	}
	public void setItems(List<CartForm> items) {
		this.items = items;
	}
	public LocalDateTime getOrderStartTime() {
		return orderStartTime;
	}
	public void setOrderStartTime(LocalDateTime orderStartTime) {
		this.orderStartTime = orderStartTime;
	}
	public LocalDateTime getOrderEndTime() {
		return orderEndTime;
	}
	public void setOrderEndTime(LocalDateTime orderEndTime) {
		this.orderEndTime = orderEndTime;
	}
    
}
