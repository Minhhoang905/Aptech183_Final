package com.example.Aptech_Final.Service;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.example.Aptech_Final.Controller.DTO.UserManagementDTO;
import com.example.Aptech_Final.Controller.DTO.UsersDTO;
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
	public UsersDTO getDropDown() {
		// Tạo DTO chứa các dữ liệu cần thiết cho trang home
		UsersDTO dropdownDTO = new UsersDTO();
		
	    // Lấy danh sách Tỉnh
	    dropdownDTO.setProvinceList(provinceRepository.findAll());
	    // Lấy danh sách Quận (District)
	    dropdownDTO.setDistrictList(districtRepository.findAll());
	    // Lấy danh sách Xã (Ward)
	    dropdownDTO.setWardList(wardRepository.findAll());
	    
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
	public String isValid(UserForm userForm, Model model) {
		// Check xem tên tài khoản đã có chưa, nếu có thì trả về false
		if (userRepository.findByName(userForm.getName()) != null) {
			return "error: Username already exists."; 
		}
		// Check xem có trùng lặp email trong DB hay không
		if (userRepository.existsByEmail(userForm.getEmail())) {
			return "error: Email already exists.";
		}
		// Tạo ifelse check các điều kiện
		if (userForm.getName().length() < 6) {
			// Kiểm tra nếu độ dài phải nằm trong khoảng 8 đến 16
			return "error: Username less than 5 characters"; // <= Invalid
		}else if (userForm.getPass().length() < 9 || userForm.getPass().length() > 21) {
			// Kiểm tra độ dài phải nằm trong khoảng 8 đến 20
			return "error: Password must be between 8 and 20 characters";
		}else if (!Pattern.compile("[A-Z]").matcher(userForm.getPass()).find()) {
			// Kiểm tra kí tự in hoa của password
			return "error: Password must contain at least one uppercase letter"; // <= Invalid
		}else if (!Pattern.compile("[^a-zA-Z0-9]").matcher(userForm.getPass()).find()) {
			// Kiểm tra kí tự đặc biệt của password
            return "error: Password must contain at least one special character"; // <= Invalid
		}else { 
			// Gọi đối tượng User
			Users obj = new Users();
			// Set các thông tin từ userForm vào đối tượng user
			// Tên tài khoản
			obj.setName(userForm.getName());
			// Mật khẩu (dùng encoder từ SecurityConfig để mã hóa trong database)
			obj.setPass(passwordEncoder.encode(userForm.getPass()));
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
			
			return "success: Register succesfull!";
		}				
	}
	
	// Phương thức để cập nhập mật khẩu
	public String changePass(String username,String oldPassword , String newPassword, Model model) {
		// Kiểm tra tính hợp lệ của user 
		Users users = userRepository.findByName(username);
		// Tạo if-else để kiểm tra thông tin và check xem mật khẩu đã nhập có đúng không
		if (users != null && passwordEncoder.matches(oldPassword, users.getPass())) {	
			if (newPassword.length() < 9 || newPassword.length() > 21) {
				// Kiểm tra độ dài phải nằm trong khoảng 8 đến 20
				//model.addAttribute("mes", "Password must be between 8 and 20 characters");
				return "error: Password must be between 8 and 20 characters"; 
			}else if (!Pattern.compile("[A-Z]").matcher(newPassword).find()) {
				// Kiểm tra kí tự in hoa của password
				//model.addAttribute("mes", "Password must contain at least one uppercase letter");
				return "error: Password must contain at least one uppercase letter"; // <= Invalid
			}else if (!Pattern.compile("[^a-zA-Z0-9]").matcher(newPassword).find()) {
				// Kiểm tra kí tự đặc biệt của password
	           // model.addAttribute("mes", "Password must contain at least one special character");
	            return "error: Password must contain at least one special character"; // <= Invalid
			}else {
				// Cập nhập mật khẩu mới với passwordEncoder
				users.setPass(passwordEncoder.encode(newPassword));
				// Cập nhập vào database
				userRepository.save(users);
				// Trả về true
				return "success: Password changed successfully";
			}
		}else {
			//model.addAttribute("mes", "Old password is incorrect");
			// Trả về false nếu không hợp lệ
			return "error: Invalid username or password";
		}
	}

	// Phương thức để tìm kiếm thông tin user dựa trên các tham số (parameter) truyền vào
	public List<UserManagementDTO> findUser(Long id, 
											String username, 
											String fullName, 
											LocalDate dateOfBirth, 
											String email, 
											String phoneNumber, 
											Long provinceId, 
											Long districtId, 
											Long wardId, 
											String address, 
											String role) {
		List<UserManagementDTO> user = userRepository.selectUsersInfo(id, username, fullName, dateOfBirth, email, phoneNumber, provinceId, districtId, wardId, address, role);
		return user;
	}
	
	// Phương thức để chuẩn bị dữ liệu, hiển thị ban đầu cho trang userManagement
	public UsersDTO getUsersDTO(Users users) {

		// Lấy danh sách của UsersDTO theo từ entity của Users
		// id
		Long userId = users.getId();
		// Tên tài khoản
		String userName = users.getName();
		// Họ tên đầy đủ
		String userFullName = users.getFullName();
		// Ngày tháng năm sinh
		LocalDate userDOB = users.getDateOfBirth();
		// Mail
		String userEmail = users.getEmail();
		// Số điện thoại
		String phoneNumber = users.getPhoneNumber();
		// if-else rút gọn của id tỉnh (Nếu (điều kiện ?) thì lấy giá trị trước (:) ,
		// còn không thì lấy sau (:)
		Long provinceId = (users.getProvinceId() != null) ? users.getProvinceId() : null;
		// if-else rút gọn của id quận (Nếu (điều kiện ?) thì lấy giá trị trước (:) ,
		// còn không thì lấy sau (:)
		Long districtId = (users.getDistrictId() != null) ? users.getDistrictId() : null;
		// if-else rút gọn của id quận (Nếu (điều kiện ?) thì lấy giá trị trước (:) ,
		// còn không thì lấy sau (:)
		Long wardId = (users.getWardId() != null) ? users.getWardId() : null;
		// Số nhà
		String userAddress = users.getAddress();
		// Vai trò
		String userRole = users.getRole();
		
		// Gọi phương thức findUser trong `@Service` để hiển thị thông tin theo yêu cầu từ list
		List<UserManagementDTO> userManagementDTOs = findUser(userId, userName, userFullName, userDOB, userEmail, phoneNumber, provinceId, districtId, wardId, userAddress, userRole);
		
		// Tạo đối tượng của UsersDTO
		UsersDTO usersDTO = new UsersDTO();
		// Gán danh sách userManagementDTOs vào danh sách UserManagementDTO ở lớp UsersDTO
		usersDTO.setUserManagementDTO(userManagementDTOs);
		
		// Trả về đối tượng của lớp usersDTO
		return usersDTO;
	}
	
    // Phương thức sử dụng hàm escape ký tự đặc biệt trong chuỗi tìm kiếm
    private String escapeSpecialChars(String input) {
        if (input == null) return null;
        // Thứ tự quan trọng: escape "\" trước, sau đó "%" và "_"
        return input.replace("\\", "\\\\")
                    .replace("%", "\\%")
                    .replace("_", "\\_");
    }

    // Phương thức tìm kiếm user theo keyword (xử lý cả trường hợp ký tự đặc biệt và chuỗi rỗng)
    public List<UserManagementDTO> searchUsers(String keyword) {
    	// Trả về trường hợp là null và chuỗi rồng
        if (keyword == null || keyword.trim().isEmpty()) {
        	// Nếu không nhập gì, có thể gọi repository để trả về toàn bộ user
            return userRepository.searchUsers(keyword);
        }
        // Escape ký tự đặc biệt để đảm bảo tìm kiếm chính xác
        String escapedKeyword = escapeSpecialChars(keyword.trim());
        return userRepository.searchUsers(escapedKeyword);
    }

	// Phương thức trả về khi nhấn button `search` ở trang userManagement
    // (sử dụng kết quả từ phương thức searchUsers đã được xử lý)
	public UsersDTO getUsersDTOByKeyword(String keyword) {
		
	    // Gọi phương thức `searchUsers` để tìm kiếm theo ký tự đặc biệt
	    List<UserManagementDTO> userManagementDTOs = searchUsers(keyword);
	    
	    // Tạo đối tượng UsersDTO
	    UsersDTO usersDTO = new UsersDTO();
		// Gán danh sách userManagementDTOs vào danh sách UserManagementDTO ở lớp UsersDTO
	    usersDTO.setUserManagementDTO(userManagementDTOs);
	    
	    return usersDTO;
	}
	
	// Phương thức tìm kiếm thông tin bằng id
	public Optional<Users> findUserById(Long id) {
		// Trả về kết quả bằng phương thức có sẵn của spring boot
		return userRepository.findById(id);
	}
	
	// Phương thức để cập nhập thông tin của user bằng id
	public String isUserInfoUpdateValid (UserForm userForm, Model model) {
	    // Tìm user theo ID
	    Users obj = userRepository.findById(userForm.getId()).orElse(null);
	    if (obj == null) {
	        return "error: User not found!";
	    }

	    // Kiểm tra độ dài username
	    if (userForm.getName().length() < 5) {
	        return "error: Username must be at least 5 characters long!";
	    }

	    // Kiểm tra username đã tồn tại chưa (ngoại trừ chính user đang sửa)
	    Users existingUser = userRepository.findByName(userForm.getName());
	    if (existingUser != null && !existingUser.getId().equals(userForm.getId())) {
	        return "error: Username already exists!";
	    }

	    // Kiểm tra email đã tồn tại chưa (ngoại trừ chính user đang sửa)
	    Users existingEmail = userRepository.findByEmail(userForm.getEmail());
	    if (existingEmail != null && !existingEmail.getId().equals(userForm.getId())) {
	        return "error: Email already exists!";
	    }

		// Set các thông tin từ userForm vào đối tượng user
		// id
		obj.setId(userForm.getId());
		// Tên tài khoản
		obj.setName(userForm.getName());
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
			
		return "success: User information updated successfully!";
		}				
	
	
	// Phương thức xóa thông tin với id
	public void deleteInfoById (Long id) {
		userRepository.deleteById(id);
	}

}

