package com.example.Aptech_Final.Service;
// Tạo interface cung cấp phương thức loadUserByUsername cho việc xác thực người dùng trong hệ thống bảo mật.

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserDetailsService {
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
