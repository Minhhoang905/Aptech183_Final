package com.example.Aptech_Final.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//Đánh dấu class này 1 lớp cấu hình (configuration)của spring
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import com.example.Aptech_Final.Service.Imp.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

//	@Bean này thay thế cho WebSecurityConfigureAdapter
	@SuppressWarnings("removal")
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf().disable()
		.authorizeHttpRequests(auth -> auth
                	.requestMatchers("/images/**", "/css/**", "/js/**").permitAll() // Cho phép truy cập ảnh, CSS, JS
					.requestMatchers("/ComplexGym/home", "/loginAPI", "/loginAPI/getDistrictDropdown", "/loginAPI/getWardDropdown").permitAll() //Không đăng nhập: Chỉ được phép xem home
					.requestMatchers("/cart/**","/products/**", "/payment").permitAll()
					.requestMatchers("/forgot-password/**","/reset-password/**").permitAll() 
					.requestMatchers("/doLogin",  "/doChangePass", "/changePass", "/logout").permitAll() 
					.requestMatchers("/ComplexGym/home").hasAnyRole("ADMIN", "USER") // Cả user và admin thực hiện search
					.requestMatchers("/register","/doRegister", "/userManagement", "/searchUserInformation", "/userManagement/updateUser", "/userManagement/doUpdateUserInfo", "/userManagement/doDeleteUser").hasRole("ADMIN") // admin: có toàn quyền
					.requestMatchers("/products/productManagement").hasRole("ADMIN") // admin: có toàn quyền
	                .requestMatchers("/api/products/updateDescription").hasRole("ADMIN") // Chỉ cho phép admin truy cập endpoint updateDescription
					.anyRequest().authenticated() // Yêu cầu khác phải đăng nhập
			)
			.formLogin(login -> login
					.loginPage("/login") //Trang login
					.defaultSuccessUrl("/ComplexGym/home", true) // Chuyển hướng sau khi đăng nhập thành công
					.permitAll() //Ai cũng có thể truy cập
			)			
			.logout(logout -> logout
					.logoutUrl("/logout") //Đường dẫn logout
					.logoutSuccessUrl("/ComplexGym/home") // Chuyển hướng sau khi đăng xuất thành công
					.invalidateHttpSession(true) //Xóa session
					.clearAuthentication(true) // Xóa authen
					.permitAll()
			)
	        .rememberMe(rememberMe -> rememberMe // Kích hoạt Remember-Me
	                .key("uniqueAndSecret") // Tên khóa bí mật
	                .tokenValiditySeconds(7 * 24 * 60 * 60) // Thời hạn 7 ngày
	            )
	        .anonymous(); //Bật chế độ khách (anonymous) 
		
		//Hoàn tất cấu hình bảo mật và trả về SecurityFilterChain
		return http.build();
	}
		
    @Autowired
    protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    // Xử lý lỗi đăng nhập
    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
    	// Thêm param `error=true` vào url khi đăng nhập thất bại
    	return new SimpleUrlAuthenticationFailureHandler("/login?error=true");
    }
}
