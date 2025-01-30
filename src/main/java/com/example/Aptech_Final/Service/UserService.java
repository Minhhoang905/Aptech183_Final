package com.example.Aptech_Final.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Aptech_Final.Enity.Users;
import com.example.Aptech_Final.Repository.LoginRepository;


// Đánh dấu class này là 1 service của spring
@Service
public class UserService{
	// Tự động inject instance của LoginRepository vào class này
	@Autowired
	private LoginRepository loginRepository;
    // Phương thức để kiểm tra thông tin user ở database
	public boolean IsValidateUser(String name, String pass) {
		// Tìm kiếm user theo name
		Users user = loginRepository.findByName(name);
		//Trả về user và check mật khẩu
		return user!= null && user.getPass().equals(pass);		
	}
	
}

