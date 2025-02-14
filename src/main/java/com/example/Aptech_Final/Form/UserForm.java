package com.example.Aptech_Final.Form;

import java.time.LocalDate;

// Tạo thuộc tính (và tên phải khớp với thuộc tính bên html)
public class UserForm {
	// id
	private Long id;
	// Username
	private String name;
	// Password hiện tại
	private String pass;
	// Password mới
	private String newPass;
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
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getNewPass() {
		return newPass;
	}
    public boolean isRememberMe() {
        return rememberMe;
    }

	public void setNewPass(String newPass) {
		this.newPass = newPass;
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
