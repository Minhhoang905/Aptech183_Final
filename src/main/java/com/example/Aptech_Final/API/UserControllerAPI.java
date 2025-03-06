package com.example.Aptech_Final.API;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.Aptech_Final.Controller.DTO.UsersDTO;
import com.example.Aptech_Final.Enity.District;
import com.example.Aptech_Final.Enity.Province;
import com.example.Aptech_Final.Enity.Ward;
import com.example.Aptech_Final.Form.UserForm;
import com.example.Aptech_Final.Repository.DistrictRepository;
import com.example.Aptech_Final.Repository.WardRepository;
import com.example.Aptech_Final.Service.UserService;


@RestController
@RequestMapping("/loginAPI")
public class UserControllerAPI {
	// Tự động inject instance của UserService vào class này
	@Autowired	
	private	UserService userService;
	// Tự động inject instance của DistrictRepository vào class này
	@Autowired
	private DistrictRepository districtRepository;
	// Tự động inject instance của WardRepository vào class này
	@Autowired
	private WardRepository wardRepository;
	
	// Tạo method để get all tên quận
	@GetMapping(path = "/getDistrictDropdown")
	public List<District> getAllDistrict(@RequestParam("provinceId")Long provinceId) {
		System.out.println("Id Tỉnh được nhận từ front-end: " + provinceId);
		//Long tinhId = Long.parseLong(idTinh);
		List<District> districtList = districtRepository.findDistrictByProvinceId(provinceId);
		System.out.println("Danh sách quận: " + districtList);
		return districtList;
	}
	// Tạo method để get all Xã
	@GetMapping(path = "/getWardDropdown")
	public List<Ward> getAllXa(@RequestParam("districtId")Long districtId) {
		System.out.println("Id Tỉnh được nhận từ front-end: " + districtId);
		//Long quanId = Long.parseLong(idQuan); // Chuyển chuỗi thành Long
		List<Ward> wardList = wardRepository.findWardByDistrictId(districtId);
		System.out.println("Danh sách xã: " + wardList);
		return wardList;
	}
	@PostMapping("/register")
	public String registerUser(@ModelAttribute("user") UserForm userForm, Model model, RedirectAttributes redirectAttributes) {
		// Tạo đối tượng ở lớp DropDownDTP
		UsersDTO dropdownDTO = new UsersDTO();
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
        // Tạo if-else 
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

}
