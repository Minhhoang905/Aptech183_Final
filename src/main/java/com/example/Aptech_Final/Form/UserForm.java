package com.example.Aptech_Final.Form;

import java.time.LocalDate;

// Tạo thuộc tính (và tên phải khớp với thuộc tính bên html)
public class UserForm {
	// Username
	private String username;
	// Password hiện tại
	private String password;
	// Password mới
	private String newPassword;
	// Thuộc tính cho checkbox Remember Me
	private boolean rememberMe;
	// Họ tên đầy đủ
	private String fullName;
	// Ngày tháng năm sinh
	private LocalDate dateOfBirth;
	// Email
	private String email;
	// Số điện thoại
	private String phoneNumber;
	// Vai trò
	private String role;
	// Địa chỉ (Tỉnh/thành)
	private Long provinceId;
	// Địa chỉ (quận/huyện)
	private Long districtId;
	// Địa chỉ (xã/phường)
	private Long wardId;
	// Địa chỉ (số nhà cụ thể)
	private String address;
	
	//Tạo getter và setter
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
    public boolean isRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
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
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
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
}
