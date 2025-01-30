package com.example.Aptech_Final.Form;

// Tạo thuộc tính (và tên phải khớp với thuộc tính bên html)
public class LoginForm {
	// Username
	private String username;
	// Password hiện tại
	private String password;
	// Password mới
	private String newPassword;
	
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


}
