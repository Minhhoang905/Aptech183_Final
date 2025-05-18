package com.example.Aptech_Final.Controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.Aptech_Final.Controller.DTO.SlotInfo;
import com.example.Aptech_Final.Enity.Users;
import com.example.Aptech_Final.Form.ScheduleBookingForm;
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
	
	@GetMapping("")
	public String getScheduleView(Model model, Authentication authentication) {
	    // Thêm các thuộc tính chung vào model (ví dụ: userId, role,...)
	    homeController.addCommonAttributes(model, authentication);

	    // Lấy userId và role từ model
	    Long userId = (Long) model.getAttribute("userId");
	    String role = (String) model.getAttribute("role");

	    // Kiểm tra nếu userId và role không tồn tại, chuyển hướng về trang login hoặc trang khác nếu cần thiết
	    if (userId == null || role == null) {
	        return "redirect:/login";
	    }

	    // Lấy lịch theo vai trò người dùng từ service
	    Map<LocalDate, List<ScheduleForm>> scheduleMap = scheduleService.getScheduleByUserRole(userId, role);
	    System.out.println(scheduleMap.toString());
	    // Danh sách ngày từ hôm nay đến 6 ngày tiếp theo
	    List<LocalDate> dates = new ArrayList<>();
	    LocalDate today = LocalDate.now();

	    // Tạo vòng lặp với số ngày hiện tại + 6 ngày tiếp theo
	    for (int i = 0; i <= 6; i++) {
	    	// Tạo biến LocalDate và gán ngày hiện tại vào
	        LocalDate currentDate = today.plusDays(i);
	        // Add này hiện tại vào trong List<LocalDate> ở trên
	        dates.add(currentDate);

	        // Đảm bảo mỗi ngày đều có entry trong scheduleMap, tránh null trong view
	        // Note: Dùng putIfAbsent để tránh ghi đè key - value
	        scheduleMap.putIfAbsent(currentDate, new ArrayList<>());
	    }

	    // Thêm lịch vào model
	    // Map<LocalDate, List<ScheduleForm>> scheduleMap
	    model.addAttribute("scheduleMap", scheduleMap);  
	    // List<LocalDate> dates
	    model.addAttribute("dates", dates);
	    // Tạo danh sách khung giờ (IntStream.rangeClosed) 5h -> 20h, rồi chuyển từ int => Integer, rồi chuyển thành List
	    model.addAttribute("timeSlots", IntStream.rangeClosed(5, 20).boxed().toList());
	    
	    // Trả về view
	    return "html_resources/schedule";
	}
	
	// Phương thức để thực hiện đăng ký lịch tập vào database
	@PostMapping("/book")
	public String bookScheduleSlot(Model model,@ModelAttribute("scheduleBooking") ScheduleBookingForm bookingForm, Authentication authentication, RedirectAttributes redirectAttributes) {
	    // Thêm các thuộc tính chung vào model (ví dụ: userId, role,...)
	    homeController.addCommonAttributes(model, authentication);
	    model.addAttribute("scheduleBooking", new ScheduleBookingForm());  

	    // Lấy userId và role từ model
	    Long userId = (Long) model.getAttribute("userId");
	    String role = (String) model.getAttribute("role");
	    // Kiểm tra nếu userId và role không tồn tại, chuyển hướng về trang login hoặc trang khác nếu cần thiết
	    if (userId == null || role == null) {
	        return "redirect:/login";
	    }

		// Tạo và gọi phương thức từ service
	    String result = scheduleService.bookSchedule(bookingForm, userId);
	    
        // Tạo if-else để chuyển về controller
	    if (result.startsWith("success: ")) {
			redirectAttributes.addFlashAttribute("successMessage", result.substring(8));
			
		} else {
			redirectAttributes.addFlashAttribute("errorMessage", result.substring(6));

		}
		return "redirect:/ComplexGym/schedule";
	}
	
	// Phương thức để hủy lịch tập trong database
	@PostMapping("/cancel")
	public String cancelScheduleSlot(Model model, Authentication authentication, RedirectAttributes redirectAttributes,
					@ModelAttribute("cancelBooking") ScheduleBookingForm bookingForm) {
	    // Thêm các thuộc tính chung vào model (ví dụ: userId, role,...)
	    homeController.addCommonAttributes(model, authentication);
	    model.addAttribute("cancelBooking", new ScheduleBookingForm());  
	    
	    // Lấy userId và role từ model
	    Long userId = (Long) model.getAttribute("userId");
	    
		// Gọi phương thức từ service 
	    String result = scheduleService.cancelSchedule(bookingForm, userId);
	    
        // Tạo if-else để chuyển về controller
	    if (result.startsWith("success: ")) {
			redirectAttributes.addFlashAttribute("successMessage", result.substring(8));
		} else {
			redirectAttributes.addFlashAttribute("errorMessage", "Oops, something happens. Try again or contact to admin!");
		}
	    
	    return "redirect:/ComplexGym/schedule";
	}

}

