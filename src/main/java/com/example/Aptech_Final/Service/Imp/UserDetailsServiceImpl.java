package com.example.Aptech_Final.Service.Imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.Aptech_Final.Enity.Users;
import com.example.Aptech_Final.Repository.LoginRepository;

public class UserDetailsServiceImpl implements UserDetailsService{
	// Inject LoginRepository để lấy truy cập DB
	@Autowired
	private LoginRepository loginRepository;
	
    // Gọi phương thức (ghi đè phương thức có sẵn trong UserDetails) khi người dùng đăng nhập để xác thực thông tin người dùng
    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
    	// Tìm kiếm tên của người dùng từ DB (gọi phương thức từ Repo)
        Users user = loginRepository.findByName(name);
        // Ném ra 1 exception nếu không thấy người dùng
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + name);
        }
        // Trả về 1 đối tượng UserDetails với 3 thông tin người dùng bao gồm
        return org.springframework.security.core.userdetails.User
            .withUsername(user.getName()) // Tên
            .password(user.getPass()) // Mật khẩu
            .roles(user.getRole()) // Vai trò
            .build(); // Thực hiện build dối tượng Userdetails
    }
}
