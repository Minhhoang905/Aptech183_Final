package com.example.Aptech_Final.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.Aptech_Final.Controller.DTO.DropdownDTO;
import com.example.Aptech_Final.Enity.District;
import com.example.Aptech_Final.Enity.Province;
import com.example.Aptech_Final.Enity.Ward;
import com.example.Aptech_Final.Form.UserForm;
import com.example.Aptech_Final.Service.UserService;



// Đánh dấu class này là 1 controller của spring
@Controller
public class UserController {
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
				return "redirect:/ComplexGym/home";
			}
		}
		//Kiểm tra nếu có tham số "error" trong URL (được gửi từ Spring Security)
		if (error != null) {
			model.addAttribute("errMes", "Wrong name or password");
		}		
		// Nếu chưa đăng nhập thì đưa về lại login
		return "login";
	}
    
	// Phương thức để hiển thị page register.html
	@GetMapping("/register")
	public String register(ModelMap model) {
		// Tạo đối tượng ở lớp DropDownDTP
		DropdownDTO dropdownDTO = userService.getDropDown();
		// Thêm danh sách các tỉnh (lấy từ DropdownDTO) vào model
		model.addAttribute("provinceList", dropdownDTO.getProvinceList());
		// Thêm đối tượng "Tỉnh" rỗng để binding với thymleaf
		model.addAttribute("province", new Province());
		// Thêm đối tượng "Quận" rỗng để binding với thymleaf
		model.addAttribute("district", new District());
		// Thêm đối tượng "Xã" rỗng để binding với thymleaf
		model.addAttribute("ward", new Ward());
		// Đối tượng rỗng để binding với th:object ở form
		model.addAttribute("userForm", new UserForm());
		return "register";
	} 
	// Phương thức để thực hiện submit ở register.html
	@PostMapping("/doRegister")
	public String registerUser(@ModelAttribute("userForm") UserForm userForm, Model model, RedirectAttributes redirectAttributes) {
		// Tạo đối tượng ở lớp DropDownDTP
		DropdownDTO dropdownDTO = userService.getDropDown();
		// Thêm danh sách các tỉnh (lấy từ DropdownDTO) vào model
		model.addAttribute("provinceList", dropdownDTO.getProvinceList());
		// Thêm đối tượng "Tỉnh" rỗng để binding với thymleaf
		model.addAttribute("province", new Province());
		// Thêm đối tượng "Quận" rỗng để binding với thymleaf
		model.addAttribute("district", new District());
		// Thêm đối tượng "Xã" rỗng để binding với thymleaf
		model.addAttribute("ward", new Ward());
		// Đối tượng rỗng để binding với th:object ở form
		model.addAttribute("userForm", new UserForm());
				
		// Tạo boolean và gọi phương thức từ service
        boolean success = userService.isValid(userForm, model);
        // Tạo if-else để chuyển về controller
        if (success) {
        	// Thêm thông báo thành công vào Flash Attribute để hiển thị sau khi chuyển hướng
        	redirectAttributes.addFlashAttribute("successMessage", "Đăng ký thành công!");
        	// Chuyển hướng về lại register sau khi đăng ký thành công (và js ở FE sẽ thực hiện chuyển về login)
            return "redirect:/register";
        } else {
        	// Thêm thông báo thất bại vào Flash Attribute để hiển thị sau khi chuyển hướng
        	redirectAttributes.addFlashAttribute("errorMessage", "Đăng ký thất bại. Vui lòng thử lại.");
        	// Trở về trang đăng ký nếu có lỗi
            return "redirect:/register";
        }
	}

}
