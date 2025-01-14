package com.example.Aptech_Final.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller

public class HomeController {
	
	// Phương thức lấy cequestMapping(path)ác thông tin ở màn hình home
	@RequestMapping(path = "/home", method = RequestMethod.GET)
	public String getHome () {
		  
		
		return "Demo";
	}
}
