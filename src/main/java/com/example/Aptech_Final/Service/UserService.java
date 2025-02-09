package com.example.Aptech_Final.Service;


import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;

import com.example.Aptech_Final.Controller.DTO.DropdownDTO;
import com.example.Aptech_Final.Enity.District;
import com.example.Aptech_Final.Enity.Users;
import com.example.Aptech_Final.Enity.Ward;
import com.example.Aptech_Final.Form.UserForm;
import com.example.Aptech_Final.Repository.DistrictRepository;
import com.example.Aptech_Final.Repository.UserRepository;
import com.example.Aptech_Final.Repository.ProvinceRepository;
import com.example.Aptech_Final.Repository.WardRepository;


// Đánh dấu class này là 1 service của spring
@Service
public class UserService{
	// Tự động inject instance của LoginRepository vào class này
	@Autowired
	private UserRepository userRepository;
	// Tự động inject instance của PasswordEncoder vào class này	
	@Autowired
	private PasswordEncoder passwordEncoder;
	// Tự động inject instance của ProvinceRepository vào class này	
	@Autowired
	private ProvinceRepository provinceRepository;
	// Tự động inject instance của DistrictRepository vào class này
	@Autowired
	private DistrictRepository districtRepository;
	// Tự động inject instance của WardRepository vào class này
	@Autowired
	private WardRepository wardRepository;

	// Lấy danh sách quận theo id Tỉnh (bằng phương thức tùy chỉnh trong repo)
	public List<District> getDistrictByProvinceId(Long provinceId) {
		return districtRepository.findDistrictByProvinceId(provinceId);
	}
	
	// Lấy danh sách xã theo id Quận (bằng phương thức tùy chỉnh trong repo)
	public List<Ward> getWardByDistrictId(Long districtId) {
		return wardRepository.findWardByDistrictId(districtId);
	}
	//Method hiển thị các dropdown
	public DropdownDTO getDropDown() {
		// Tạo DTO chứa các dữ liệu cần thiết cho trang home
		DropdownDTO dropdownDTO = new DropdownDTO();
		
		// Lấy danh sách Tỉnh từ phương thức ở repository
		dropdownDTO.setProvinceList(provinceRepository.findAll());
		return dropdownDTO;
	}

    // Phương thức để kiểm tra thông tin user ở database
	public boolean isValidateUser(String name, String pass) {
		// Tìm kiếm user theo name
		Users user = userRepository.findByName(name);
		//Trả về user và check mật khẩu
		return user!= null && passwordEncoder.matches(pass, user.getPass());		
	}
	
	// Phương thức boolean để check điều kiện đăng ký
	public boolean isValid(UserForm userForm, Model model) {
		// Check xem tên tài khoản đã có chưa, nếu có thì trả về false
		if (userRepository.findByName(userForm.getUsername()) != null) {
			model.addAttribute("mes","Tên người dùng đã tồn tại.");
			return false; // <= Invalid
		}
		// Tạo ifelse check các điều kiện
		if (userForm.getUsername().length() < 6) {
			// Kiểm tra nếu độ dài phải nằm trong khoảng 8 đến 16
			model.addAttribute("mes", "Username less than 5 characters");
			return false; // <= Invalid
		}else if (userForm.getPassword().length() < 9 || userForm.getPassword().length() > 21) {
			// Kiểm tra độ dài phải nằm trong khoảng 8 đến 20
			model.addAttribute("mes", "Password must be between 8 and 20 characters");
			return false; // <= Invalid
		}else if (!Pattern.compile("[A-Z]").matcher(userForm.getPassword()).find()) {
			// Kiểm tra kí tự in hoa của password
			model.addAttribute("mes", "Password must contain at least one uppercase letter");
			return false; // <= Invalid
		}else if (!Pattern.compile("[^a-zA-Z0-9]").matcher(userForm.getPassword()).find()) {
			// Kiểm tra kí tự đặc biệt của password
            model.addAttribute("mes", "Password must contain at least one special character");
            return false; // <= Invalid
		}else { 
			// Gọi đối tượng User
			Users obj = new Users();
			// Set các thông tin từ userForm vào đối tượng user
			// Tên tài khoản
			obj.setName(userForm.getUsername());
			// Mật khẩu (dùng encoder từ SecurityConfig để mã hóa trong database)
			obj.setPass(passwordEncoder.encode(userForm.getPassword()));
			// Vai trò
			obj.setRole(userForm.getRole());
			// Họ tên đầy đủ
			obj.setFullName(userForm.getFullName());
			// Ngày tháng năm sinh
			obj.setDateOfBirth(userForm.getDateOfBirth());
			// Email
			obj.setEmail(userForm.getEmail());
			// Số điện thoại
			obj.setPhoneNumber(userForm.getPhoneNumber());
			// Id của tỉnh
			obj.setProvinceId(userForm.getProvinceId());
			// Id của quận
			obj.setDistrictId(userForm.getDistrictId());
			// Id của xã
			obj.setWardId(userForm.getWardId());
			// Số nhà cụ thể
			obj.setAddress(userForm.getAddress());
			// Gọi phương thức có sẵn trong spring để lưu thông tin vào database
			userRepository.save(obj);
			// Gửi thông tin vào thuộc tính ở front-end (login)
			model.addAttribute("okMess", "Register succesfull");
			return true; // <= Valid
		}				
	}
	
	// Phương thức boolean để cập nhập mật khẩu
	public boolean changePass(String username,String oldPassword , String newPassword, ModelMap model) {
		// Kiểm tra tính hợp lệ của user 
		Users users = userRepository.findByName(username);
		// Tạo if-else để kiểm tra thông tin và check xem mật khẩu đã nhập có đúng không
		if (users != null && passwordEncoder.matches(oldPassword, users.getPass())) {	
			if (newPassword.length() < 9 || newPassword.length() > 21) {
				// Kiểm tra độ dài phải nằm trong khoảng 8 đến 20
				model.addAttribute("mes", "Password must be between 8 and 20 characters");
				return false; // <= Invalid
			}else if (!Pattern.compile("[A-Z]").matcher(newPassword).find()) {
				// Kiểm tra kí tự in hoa của password
				model.addAttribute("mes", "Password must contain at least one uppercase letter");
				return false; // <= Invalid
			}else if (!Pattern.compile("[^a-zA-Z0-9]").matcher(newPassword).find()) {
				// Kiểm tra kí tự đặc biệt của password
	            model.addAttribute("mes", "Password must contain at least one special character");
	            return false; // <= Invalid
			}else {
				// Cập nhập mật khẩu mới với passwordEncoder
				users.setPass(passwordEncoder.encode(newPassword));
				// Cập nhập vào database
				userRepository.save(users);
				// Trả về true
				return true;
			}
		}else {
			model.addAttribute("mes", "Old password is incorrect");
			// Trả về false nếu không hợp lệ
			return false;
		}
	}
	
}

