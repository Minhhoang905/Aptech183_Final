package com.example.Aptech_Final.Controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.Aptech_Final.Controller.DTO.CartDTO;
import com.example.Aptech_Final.Enity.Users;
import com.example.Aptech_Final.Repository.CartRepository;
import com.example.Aptech_Final.Repository.UserRepository;
import com.example.Aptech_Final.Service.CartService;
import com.example.Aptech_Final.Service.HomeService;

@Controller
@RequestMapping("/ComplexGym/payment")
public class PaymentController {

	@Autowired
	private UserRepository userRepository;
    @Autowired
    private HomeService homeService;   
    @Autowired
    private CartService cartService;
    @Autowired
    private CartRepository cartRepository;

    
    // Tạo phương thức để thêm role và username vào model
    private void addCommonAttributes(Model model, Authentication authentication) {
        if (authentication != null) {
            String role = homeService.getCurrentUserRole();
            String username = authentication.getName();           
            // Lấy user từ database
            Users user = userRepository.findByUsername(username);
            
            if (user != null) {
                model.addAttribute("userId", user.getId());  // Thêm USER_ID vào model
                model.addAttribute("username", user.getName());
                model.addAttribute("role", role);
            }
        }
    }
    
    // Phương thức hiển thị giỏ hàng
	@GetMapping("")
	public String getCart(Model model, Authentication authentication) {
	    
		// Gọi phương thức để lấy role và tên của người dùng vào form
        addCommonAttributes(model, authentication);
        
        // Lấy userId từ model
        Long userId = (Long) model.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login"; // Nếu userId null, điều hướng về trang login
        }

        // Gọi service để lấy danh sách sản phẩm trong giỏ hàng
        List<CartDTO> cartItems = cartService.getCartItems(userId);
        // Đưa dữ liệu vào Model để hiển thị trên giao diện
        model.addAttribute("cartItems", cartItems);
        
        // Gọi getTotalAmount() để lấy tổng tiền giỏ hàng sau khi cập nhật
        BigDecimal totalAmount = cartRepository.getTotalAmount();
        totalAmount = totalAmount != null ? totalAmount : BigDecimal.ZERO;
        // Đưa dữ liệu vào Model để hiển thị trên giao diện
        model.addAttribute("totalAmount", totalAmount);
        
		return "payment";
	}
	
	@PostMapping("")
	public String buyNow(@RequestParam("productId") Long productId, 
	                     @RequestParam("quantity") int quantity,
	                     Model model,
	                     Authentication authentication, 
	                     RedirectAttributes redirectAttributes) {
	    
		// Gọi phương thức để lấy role và tên của người dùng vào form
        addCommonAttributes(model, authentication);

	    if (authentication == null) {
	        return "redirect:/login"; // Nếu chưa đăng nhập, chuyển hướng về trang login
	    }

        // Lấy userId từ model
        Long userId = (Long) model.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login"; // Nếu userId null, điều hướng về trang login
        }

        // Gọi service để lấy danh sách sản phẩm trong giỏ hàng
        List<CartDTO> cartItems = cartService.getCartItems(userId);
        // Đưa dữ liệu vào Model để hiển thị trên giao diện
        model.addAttribute("cartItems", cartItems);

	    // 🛒 Thêm sản phẩm vào giỏ hàng
	    cartService.addToCart(authentication.getName(), productId, quantity);

	    // ✅ Điều hướng sang trang thanh toán
	    return "redirect:/ComplexGym/payment";
	}

//	@PostMapping("/checkout")
//	public String confirmPayment(Authentication authentication, RedirectAttributes redirectAttributes) {
//	    if (authentication == null) {
//	        return "redirect:/login";
//	    }
//
//	    String username = authentication.getName();
//	    Users user = userRepository.findByUsername(username);
//
//	    if (user == null) {
//	        return "redirect:/login";
//	    }
//
//	    boolean success = cartService.processCheckout(user.getId());
//
//	    if (success) {
//	        redirectAttributes.addFlashAttribute("message", "✅ Thanh toán thành công!");
//	        return "redirect:/ComplexGym/orders";
//	    } else {
//	        redirectAttributes.addFlashAttribute("error", "⚠️ Thanh toán thất bại!");
//	        return "redirect:/ComplexGym/payment";
//	    }
//	}

}
