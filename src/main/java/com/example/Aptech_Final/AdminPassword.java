package com.example.Aptech_Final;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class AdminPassword {
    public static void main(String[] args) {
		// Tạo account admin với passwords đã mã hóa ở đây
		String password = "admin";
		// Tạo một instance của BCryptPasswordEncoder để thực hiện mã hóa mật khẩu
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		// Mã hóa mật khẩu bằng BCrypt
		String hashedPassword = encoder.encode(password);
		// In ra mật khẩu đã mã hóa, rồi copy mật khẩu đó đưa vào SQL
		System.out.println("Mật khẩu đã mã hóa: " + hashedPassword);
    }
}
