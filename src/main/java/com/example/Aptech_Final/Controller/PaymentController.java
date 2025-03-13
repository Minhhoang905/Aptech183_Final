package com.example.Aptech_Final.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.Aptech_Final.Service.HomeService;

@Controller
public class PaymentController {
    @Autowired
    private HomeService homeService;

	@GetMapping("/payment")
	public String getCart(Model model) {
    	// Gọi phương thức xác định vai trò của user từ @Service
    	String role = homeService.getCurrentUserRole();
		// Thêm thông tin về role vào form ở html
		model.addAttribute("role", role);

		return "payment";
	}

}
