package com.example.Aptech_Final.Controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.Aptech_Final.Form.UserForm;
import com.example.Aptech_Final.Service.ForgotPasswordService;
@Controller
public class ForgotPasswordController {
	// Tự động inject instance của ForgotPasswordService vào class này
	@Autowired
	private ForgotPasswordService forgotPasswordService;
	// Bước 1: Hiển thị form nhập email
	@GetMapping(path ="/forgot-password")
    public String showForgotPasswordForm(Model model) {
		model.addAttribute("userForm", new UserForm());
        return "html_resources/forgot-password";
    }
	
	// Bước 2 & 3: Nhận email, xử lý và gửi link
    // Gửi yêu cầu đặt lại mật khẩu (sau khi nhập email)
    @PostMapping("/forgot-password/request")
    public String requestResetPassword(@ModelAttribute("userForm") UserForm userForm, RedirectAttributes redirectAttributes) {
    	// Kiểm tra email có phải null hoặc rỗng không
        if (userForm.getEmail() == null || userForm.getEmail().trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Email cannot be empty.");
        }else {
            // Gửi email có chứa token
            forgotPasswordService.sendResetPasswordEmail(userForm);      
            // Trả về trang pop-up thông báo
            redirectAttributes.addFlashAttribute("successMessage", "A reset password link has been sent to your email. Please check your inbox.");
        }
        // Trả về thông báo đã gửi email
       return "redirect:/forgot-password";

    }
    
    // Bước 4: Hiển thị form đặt lại mật khẩu

	// Hiển thị form đặt lại mật khẩu với token
	@GetMapping("/forgot-password/reset-form")
	public String showResetForm(@ModelAttribute("changeNewPass") UserForm userForm, @RequestParam("token") String token, Model model, RedirectAttributes redirectAttributes) {
		
		// Lấy email từ token
        String email = forgotPasswordService.getEmailByToken(token);
        
		// Throw exception nếu trả về kết quả null
        if (email == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid or expired token.");
            return "redirect:/forgot-password";
        }
		
        model.addAttribute("email", email);
        model.addAttribute("token", token);
        model.addAttribute("changeNewPass", new UserForm());

		return "html_resources/reset-password";
	}
	
    // Bước 5: Cập nhật mật khẩu mới
	// Xử lý đặt lại mật khẩu
	@PostMapping("/forgot-password/reset")
	public String resetPassword(
	        @ModelAttribute("changeNewPass") UserForm userForm,
	        @RequestParam("token") String token,
	        RedirectAttributes redirectAttributes) {

	    // Lấy email từ token
	    String email = forgotPasswordService.getEmailByToken(token);

	    if (email == null) {
	        redirectAttributes.addFlashAttribute("errorMessage", "Invalid or expired token.");
	        return "redirect:/forgot-password";
	    }
	    
	    // Gán email vào UserForm để tránh null
	    userForm.setEmail(email);

	    // Gọi service để xử lý đặt lại mật khẩu
	    String result = forgotPasswordService.resetPassword(userForm);

	    if (result.startsWith("success: ")) {
	        // Đặt lại mật khẩu thành công
	    	redirectAttributes.addFlashAttribute("successMessage", result.substring(8));
	        return "redirect:/forgot-password";
	    } else {
	        // Nếu có lỗi, hiển thị pop-up lỗi
	        redirectAttributes.addFlashAttribute("errorMessage", result.substring(6));

	        // Encode token để tránh lỗi URL
	        String encodedToken = URLEncoder.encode(token, StandardCharsets.UTF_8);
	        return "redirect:/forgot-password/reset-form?token=" + encodedToken;
	    }
	}
}
