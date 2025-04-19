package com.example.Aptech_Final.Controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.Aptech_Final.Enity.Users;
import com.example.Aptech_Final.Form.ScheduleForm;
import com.example.Aptech_Final.Repository.UserRepository;
import com.example.Aptech_Final.Service.HomeService;
import com.example.Aptech_Final.Service.ScheduleService;

@Controller
@RequestMapping("/ComplexGym/schedule")
public class ScheduleController {
    @Autowired
	private HomeController homeController;
	@Autowired
	private ScheduleService scheduleService;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private HomeService homeService;
	
	// Tạo khung giờ từ 5h đến 20h (15 slot)
	private List<Integer> generateTime() {
	    List<Integer> slots = new ArrayList<>();
	    for (int i = 5; i <= 20; i++) {
	        slots.add(i);
	    }
	    return slots;
	}

	// Tạo danh sách ngày từ hôm nay đến 6 ngày sau
	private List<LocalDate> generate7Days() {
	    List<LocalDate> dates = new ArrayList<>();
	    LocalDate today = LocalDate.now();
	    for (int i = 0; i < 7; i++) {
	        dates.add(today.plusDays(i));
	    }
	    return dates;
	}
}
