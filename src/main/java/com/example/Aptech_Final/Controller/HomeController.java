package com.example.Aptech_Final.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.Aptech_Final.Service.HomeService;


//Đánh dấu lớp này là một Controller của Spring
@Controller
//Định nghĩa đường dẫn cơ sở cho tất cả các yêu cầu trong lớp này
@RequestMapping("/ComplexGym")
public class HomeController {
	// Tự động inject (tiêm) instace của HomeService vào Controller
	@Autowired
	private HomeService homeService;
	// Tạo method xử lý yêu cầu GET cho đường dẫn "/home"
	@GetMapping("/home")
	public String printString(Model model, Authentication authentication) {
		// Xác định role của người dùng
		String role = "Anonymous"; //Mặc định ban đầu là Anonymous trong spring security
		
		// Tạo điều kiện để kiểm tra xem có người dùng hiện tại đăng nhập vào không
		if (authentication != null //// Đảm bảo đối tượng Authentication không null
				&& authentication.isAuthenticated()) // Xác nhận rằng người dùng đã đăng nhập thành công
		{
			// Gọi phương thức xác định vai trò của user từ @Service
			role = homeService.getCurrentUserRole();
		}
		// Gắn các thông tin vào model để hiển thị trong html
		model.addAttribute("role", role);
		// Trả về tên file template Thymeleaf (home.html)
		return "home";
	}

	// Tạo method xử lý exception IllegalArgumentException
	@ExceptionHandler(IllegalArgumentException.class)
	public String handleIllegalArgumentException(IllegalArgumentException ex, Model model) {
		model.addAttribute("errMessage", ex.getMessage());
		//Trả về lại html "home" với thông báo lỗi
		return "redirect:/home";
	}
}
