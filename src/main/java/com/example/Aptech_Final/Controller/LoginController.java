package com.example.Aptech_Final.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.Aptech_Final.Service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;




// Đánh dấu class này là 1 controller của spring
@Controller
public class LoginController {
	@Autowired
	private UserService userService; //Service chứa các phương thức

	
    @SuppressWarnings("deprecation") // Loại bỏ cảnh báo deprecation, liên quan đến các phương thức hoặc lớp cũ
	@GetMapping("/login")  //Dùng getmapping với authentication từ spring security để đăng nhập 
	public String login(Authentication authentication, ModelMap model, @RequestParam (value ="error", required = false) String error) {
		// Kiểm tra xem người dùng đã đăng nhập hay chưa thông qua đối tượng
		// Athentication
		if (null != authentication) {
			// Tạo biến String rồi gán tên đăng nhập của người dùng thông qua Authentication
			String username = authentication.getName();
			// Tạo if để kiểm tra xem tên đăng nhập của người dùng có bị rỗng không
			if (!StringUtils.isEmpty(username)) {
				// Nếu đã đăng nhập, điều hướng về link sau
				return "redirect:/home/search";
			}
		}
		//Kiểm tra nếu có tham số "error" trong URL (được gửi từ Spring Security)
		if (error != null) {
			model.addAttribute("errMes", "Wrong name or password");
		}		
		// Nếu chưa đăng nhập thì đưa về lại login
		return "login";
	}
    

}
