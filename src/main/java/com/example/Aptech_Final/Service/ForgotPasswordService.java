package com.example.Aptech_Final.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.Aptech_Final.Enity.Users;
import com.example.Aptech_Final.Form.UserForm;
import com.example.Aptech_Final.Repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class ForgotPasswordService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private JavaMailSender javaMailSender;
	@Autowired
    private BCryptPasswordEncoder passwordEncoder;
	// Tạo 1 map để lưu trữ token tạm thời
	private final Map<String, String> tokenStorage = new HashMap<>();
	
    // Phương thức kiểm tra email có tồn tại không
    public boolean checkEmailExists(String email) {
        return userRepository.findByEmail(email) != null;
    }

	// Phương thức để gửi email đặt lại mật khẩu
	public void sendResetPasswordEmail (UserForm userForm) {
		// Tạo token ngẫu nhiên
        String token = UUID.randomUUID().toString();
		// Lưu token với email tương ứng (dùng Map ở trên) vào bộ nhớ tạm
        tokenStorage.put(userForm.getEmail(), token);
				
		// Tạo đường dẫn đặt lại mật khẩu, bao gồm token trong url
		String resetUrl = "http://localhost:8080/forgot-password/reset-form?token=" + token;

		// Tạo email chứa đường dẫn đặt lại mật khẩu
		SimpleMailMessage messageMailMessage = new SimpleMailMessage();
		// Gửi tới email của người dùng
		messageMailMessage.setTo(userForm.getEmail());
		// Đặt tiêu đề email
		messageMailMessage.setSubject("Reset your password");
		// Gửi nội dung email
		messageMailMessage.setText("Click here to reset your password: " + resetUrl);		
		// Gửi email
		javaMailSender.send(messageMailMessage);				
	}
	
	// Phương thức để xác thực token và lấy email
	public boolean validateToken(String token) {
		// Trả về email nếu token được validated, nếu không thì trả về null
		return tokenStorage.containsValue(token);
	}
	
	// Phương thức lấy email từ token
	public String getEmailByToken(String token) {
	    return tokenStorage.entrySet().stream()
	            .filter(entry -> entry.getValue().equals(token)) // Tìm token trong danh sách
	            .map(Map.Entry::getKey) // Lấy email tương ứng với token
	            .findFirst() // Lấy giá trị đầu tiên nếu có
	            .orElse(null); // Nếu không tìm thấy thì trả về null
	}
	
	
	// Phương thức để đặt lại mật khẩu cho người dùng
	@Transactional
	public String resetPassword(UserForm userForm) {
	    // Lấy username và email từ form
	    String username = userForm.getName();
	    String email = userForm.getEmail();
	    String newPassword = userForm.getPass();

	    // Kiểm tra xem email có tồn tại không
	    Users users = userRepository.findByEmail(email);
	    if (users == null) {
	        return "error: Email not found!";
	    }

	    // Kiểm tra username có khớp không
	    if (!users.getName().equals(username)) {
	        return "error: Username does not match!";
	    }

	    // Kiểm tra mật khẩu không được null
	    if (newPassword == null) {
	        return "error: Password cannot be empty.";
	    }

	    // Kiểm tra độ dài mật khẩu
	    if (newPassword.length() < 8 || newPassword.length() > 20) {
	        return "error: Password must be between 8 and 20 characters.";
	    }

	    // Kiểm tra ít nhất một chữ cái viết hoa
	    if (!Pattern.compile("[A-Z]").matcher(newPassword).find()) {
	        return "error: Password must contain at least one uppercase letter.";
	    }

	    // Kiểm tra ít nhất một chữ cái thường
	    if (!Pattern.compile("[a-z]").matcher(newPassword).find()) {
	        return "error: Password must contain at least one lowercase letter.";
	    }

	    // Kiểm tra ít nhất một số
	    if (!Pattern.compile("[0-9]").matcher(newPassword).find()) {
	        return "error: Password must contain at least one number.";
	    }

	    // Kiểm tra ít nhất một ký tự đặc biệt
	    if (!Pattern.compile("[@$!%*?&]").matcher(newPassword).find()) {
	        return "error: Password must contain at least one special character (@$!%*?&).";
	    }

	    // Kiểm tra không chứa ký tự full-size (Nhật Bản, Trung Quốc, Hàn Quốc)
	    if (Pattern.compile("[Ａ-Ｚａ-ｚ０-９]").matcher(newPassword).find()) {
	        return "error: Password must not contain full-size characters.";
	    }

	    // Cập nhật mật khẩu mới với mã hóa BCrypt
	    users.setPass(passwordEncoder.encode(newPassword));
	    userRepository.save(users);

	    // Xóa token sau khi đặt lại mật khẩu thành công
	    tokenStorage.remove(email);

	    return "success: Your password has been successfully reset. Please login.";
	}	
}
