package com.example.Aptech_Final.Service;

import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;

@Service
public class HomeService {
	
	//Tạo method để xác định role của người đăng nhập
	public String getCurrentUserRole() {
	    // Lấy đối tượng Authentication hiện tại từ SecurityContextHolder
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    
	    // Kiểm tra nếu không có người dùng nào đang đăng nhập
	    if (authentication == null || authentication.getPrincipal() == null) {
	        throw new IllegalStateException("No authenticated user found");
	    }

	    // Lấy thông tin về principal (đối tượng đại diện cho người dùng hiện tại)
	    Object principal = authentication.getPrincipal();

	    // Kiểm tra nếu principal là một đối tượng UserDetails (Spring Security User)
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
	        // Lấy danh sách quyền (roles) của người dùng, nối thành chuỗi phân tách bằng dấu phẩy

            return userDetails.getAuthorities().stream()
                    .map(authority -> authority.getAuthority())
                    .collect(Collectors.joining(","));
        }

        return "Anonymous"; // Trả về Anonymous nếu có lỗi
	    
	}

}
