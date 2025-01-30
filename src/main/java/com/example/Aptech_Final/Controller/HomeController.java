package com.example.Aptech_Final.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


//Đánh dấu lớp này là một Controller của Spring
@Controller
//Định nghĩa đường dẫn cơ sở cho tất cả các yêu cầu trong lớp này
@RequestMapping("/home")
public class HomeController {

	// Tạo method xử lý yêu cầu GET cho đường dẫn "/main"
	@GetMapping("/main")
	public String printString(Model model) {
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
