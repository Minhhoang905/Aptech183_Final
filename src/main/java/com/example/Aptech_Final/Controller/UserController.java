package com.example.Aptech_Final.Controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.Aptech_Final.Controller.DTO.UsersDTO;
import com.example.Aptech_Final.Enity.District;
import com.example.Aptech_Final.Enity.Province;
import com.example.Aptech_Final.Enity.Users;
import com.example.Aptech_Final.Enity.Ward;
import com.example.Aptech_Final.Form.UserForm;
import com.example.Aptech_Final.Service.HomeService;
import com.example.Aptech_Final.Service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;



// Đánh dấu class này là 1 controller của spring
@Controller
public class UserController {
	@Autowired
	private UserService userService; //Service chứa các phương thức
	@Autowired
	private HomeService homeService;
	
    @SuppressWarnings("deprecation") // Loại bỏ cảnh báo deprecation, liên quan đến các phương thức hoặc lớp cũ
	@GetMapping("/login")  //Dùng getmapping với authentication từ spring security để đăng nhập 
	public String login(Authentication authentication, ModelMap model,
						@RequestParam (value ="error", required = false) String error, 
						@RequestParam(value = "rememberMe", required = false) String rememberMe) {
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
		// Kiểm tra nếu có tham số "error" trong URL (được gửi từ Spring Security)
		if (error != null) {
			model.addAttribute("errMes", "Wrong name or password");
		}
		
		// Kiểm tra xem có tham số rememberMe từ form thì thêm vào model 
		if (rememberMe != null) {
			model.addAttribute("remember", true);
		}
		// Nếu chưa đăng nhập thì đưa về lại login
		return "login";
	}
    
	// Phương thức để hiển thị page register.html
	@GetMapping("/register")
	public String register(ModelMap model) {
		// Tạo đối tượng ở lớp DropDownDTP
		UsersDTO dropdownDTO = userService.getDropDown();
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
		// Tạo đối tượng ở lớp DropDownDTO
		UsersDTO dropdownDTO = userService.getDropDown();
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
        String response = userService.isValid(userForm, model);
        // Tạo if-else để chuyển về controller
        if (response.startsWith("success:")) {
        	 // Bỏ "success:"
        	redirectAttributes.addFlashAttribute("successMessage", response.substring(8));
        	// Chuyển hướng về lại register sau khi đăng ký thành công (và js ở FE sẽ thực hiện chuyển về login)
            return "redirect:/register";
        } else {
    	    // Kiểm tra nếu người dùng đã chọn Tỉnh thì load Quận/Huyện tương ứng
    	    if (userForm.getProvinceId() != null) {
    	        List<District> districtList = userService.getDistrictByProvinceId(userForm.getProvinceId());
    	        model.addAttribute("districtList", districtList);
    	    }

    	    if (userForm.getDistrictId() != null) {
    	        List<Ward> wardList = userService.getWardByDistrictId(userForm.getDistrictId());
    	        model.addAttribute("wardList", wardList);
    	    }
        	//Giữ nguyên userForm để Thymeleaf có thể hiển thị lại giá trị đã nhập
            model.addAttribute("userForm", userForm);
        	// Bỏ "error:"
        	model.addAttribute("errorMessage", response.substring(6));
        	// Trở về trang đăng ký nếu có lỗi
            return "register";
        }
	}
	
	// Phương thức để hiển thị changePass.html
	@GetMapping("/changePass")
	public String changePass(ModelMap model) {
		return "changePass";
	}
	
	// Phương thức để cập nhập mật khẩu ở changePass.html
	@PostMapping("/doChangePass")
	public String doChangePass(@ModelAttribute("changePasswordForm") UserForm userForm, Model model, RedirectAttributes redirectAttributes) {
		// Đối tượng rỗng để binding với th:object ở form
        //model.addAttribute("changePasswordForm", new UserForm());

		// Gọi phương thức `changePass` từ UserService để kiểm tra và cập nhập mật khẩu
		String isPasswordChanged = userService.changePass(	userForm.getName(),
															userForm.getPass(),
															userForm.getNewPass(), model);
		// Tạo if-else để điều hướng trang web
		if (isPasswordChanged.startsWith("success:")) {
			// Nếu thành công => Thêm thuộc tính để chuyển hướng tới trang login với thông báo
			redirectAttributes.addFlashAttribute("successMessage", isPasswordChanged.substring(8));
			// Đi đến trang login.html
			return "redirect:/login";
		}else {
	    	//Giữ nguyên userForm để Thymeleaf có thể hiển thị lại giá trị đã nhập
	        model.addAttribute("changePasswordForm", userForm);
			// Nếu thất bại => Thêm thuộc tính "mes" ở trang đổi mật khẩu với thông báo
			model.addAttribute("errorMessage", isPasswordChanged.substring(6));
			// Quay trở lại trang ChangePass.html
			return "changePass";
		}
	}	
	
	// Phương thức để log out và đưa về page login.html
	@GetMapping(path = "/logout")
	public String logout(HttpServletRequest request, HttpServletResponse reponse) {
		//Tạo đối tượng SecurityContextLogoutHandler để xử lý việc đăng xuất
		SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
		//Gọi phương thức logout() có sẵn của SecurityContextServerLogoutHandle để thực hiện đăng xuất
		logoutHandler.logout(request, reponse, null);
		return "redirect:/ComplexGym/home";
	}
	
	// Phương thức để xử lý yêu cầu GET cho đường dẫn `userManagement`
	@GetMapping(path = "/userManagement")
	public String getUserManagement(Model model, @ModelAttribute("userManagementObject") Users users) {
		// Gọi phương thức `getUsersDTO` ở UserService để lấy dữ liệu cần thiết cho @controller
		UsersDTO usersDTO = userService.getUsersDTO(users);
		// Gọi phương thức xác định vai trò của user (là admin) từ @Service
		String role = homeService.getCurrentUserRole();
		// Thêm thông tin về role vào form ở html
		model.addAttribute("role", role);
		// Thêm đối tượng rỗng để binding với th:object ở form
		model.addAttribute("userManagementObject", new Users());
		// Thêm  danh sách kết quả tìm kiếm địa điểm du lịch ở `UsersDTO` vào model
		model.addAttribute("usersResults", usersDTO.getUserManagementDTO());
		
		// Trả về tên file template Thymeleaf trong back-end (userManagement.html)		
		return "userManagement";
	}
	
	// Phương thức để tìm kiếm thông tin ở đường dẫn `userManagement`
	@PostMapping(path = "/searchUserInformation")
	public String postUserManagement (@ModelAttribute("userManagementObject") Users users, @RequestParam(value = "keyword", required = false) String keyword, Model model) {
		
        // Gọi service để lấy UsersDTO dựa trên keyword
        UsersDTO usersDTO = userService.getUsersDTOByKeyword(keyword);
		// Gọi phương thức xác định vai trò của user (là admin) từ @Service
		String role = homeService.getCurrentUserRole();
		// Thêm thông tin về role vào form ở html
		model.addAttribute("role", role);
		// Thêm đối tượng rỗng để binding với th:object ở form
		model.addAttribute("userManagementObject", new Users());
        // Đưa danh sách kết quả (List<UserManagementDTO>) vào model
        model.addAttribute("usersResults", usersDTO.getUserManagementDTO());
		
	    return "userManagement"; 
	}
	
	// Phương thức xử lý yêu cầu GET cho đường dẫn "/userManagement/update"
	@GetMapping(path ="/userManagement/updateUser")
	public String getUpdateUser(Model model, @RequestParam("id") Long id) {
		// Tạo đối tượng ở lớp DropDownDTP
		UsersDTO dropdownDTO = userService.getDropDown();
		// Thêm danh sách các tỉnh (lấy từ DropdownDTO) vào model
		model.addAttribute("provinceList", dropdownDTO.getProvinceList());
	    model.addAttribute("districtList", dropdownDTO.getDistrictList());
	    model.addAttribute("wardList", dropdownDTO.getWardList()); 
		// Thêm đối tượng "Tỉnh" rỗng để binding với thymleaf
		model.addAttribute("province", new Province());
		// Thêm đối tượng "Quận" rỗng để binding với thymleaf
		model.addAttribute("district", new District());
		// Thêm đối tượng "Xã" rỗng để binding với thymleaf
		model.addAttribute("ward", new Ward());
		
		// Lấy đối tượng Users để cập nhật (gọi từ service)
		Users userUpdate = userService.findUserById(id).orElse(new Users());
	    // Thêm đối tượng userUpdate để binding với th:object ở form update
		model.addAttribute("userUpdate", userUpdate);
		// Trả về html "updateInfo"
		return "updateUserInfo";
	}
	
	// Phương thức thực hiện cập nhập thông tin người dùng theo id
	@PostMapping(path = "/userManagement/doUpdateUserInfo")
	public String doUpdateUser(@ModelAttribute("userUpdate") UserForm userForm, Model model, RedirectAttributes redirectAttributes) {
		// Tạo đối tượng ở lớp DropDownDTO
		UsersDTO dropdownDTO = userService.getDropDown();
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
				
		// Tạo và gọi phương thức từ service
        String doUpdate = userService.isUserInfoUpdateValid(userForm, model);
        // Tạo if-else để chuyển về controller
        if (doUpdate.startsWith("success: ")) {
        	// Thêm thông báo thành công vào Flash Attribute để hiển thị sau khi chuyển hướng
        	redirectAttributes.addFlashAttribute("successMessage", doUpdate.substring(8));
        	// Chuyển hướng về lại userManagement sau khi cập nhập thành công (và js ở FE sẽ thực hiện chuyển về login)
            return "redirect:/userManagement/updateUser?id=" + userForm.getId();
        } else {
    	    // Kiểm tra nếu người dùng đã chọn Tỉnh thì load Quận/Huyện tương ứng
    	    if (userForm.getProvinceId() != null) {
    	        List<District> districtList = userService.getDistrictByProvinceId(userForm.getProvinceId());
    	        model.addAttribute("districtList", districtList);
    	    }

    	    if (userForm.getDistrictId() != null) {
    	        List<Ward> wardList = userService.getWardByDistrictId(userForm.getDistrictId());
    	        model.addAttribute("wardList", wardList);
    	    }
        	// Thêm thông báo thất bại vào Flash Attribute để hiển thị sau khi chuyển hướng
        	redirectAttributes.addFlashAttribute("errorMessage", doUpdate.substring(6));
        	//Giữ nguyên userForm để Thymeleaf có thể hiển thị lại giá trị đã nhập
            model.addAttribute("userForm", userForm);
        	// Trở về trang đăng ký nếu có lỗi
            return "redirect:/userManagement/updateUser?id=" + userForm.getId();
        }
	}
	
	// Phương thức xóa thông tin người dùng bằng id
	@GetMapping(path = "/userManagement/doDeleteUser")
	public String deleteInfoById(@RequestParam(value = "id") Long id, Model model) {
		// Gọi phương thức xác định vai trò của user (là admin) từ @Service
		String role = homeService.getCurrentUserRole();
		// Thêm thông tin về role vào form ở html
		model.addAttribute("role", role);		
		// Xóa thông tin cần xóa bằng method ở service 
		userService.deleteInfoById(id);
		
		// Trả về html `userManagement`
		return "redirect:/userManagement";
	}
}
