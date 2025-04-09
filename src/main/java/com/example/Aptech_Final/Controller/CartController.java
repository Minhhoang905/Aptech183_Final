package com.example.Aptech_Final.Controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.Aptech_Final.Enity.Users;
import com.example.Aptech_Final.Form.CartForm;
import com.example.Aptech_Final.Repository.CartRepository;
import com.example.Aptech_Final.Repository.UserRepository;
import com.example.Aptech_Final.Service.CartService;
import com.example.Aptech_Final.Service.HomeService;

@Controller
@RequestMapping("/ComplexGym/cart")
public class CartController {
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
            	// Thêm các thông tin sau vào model
                model.addAttribute("userId", user.getId());  
                model.addAttribute("username", user.getName());
                model.addAttribute("role", role);
            }
        }
    }

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
        List<CartForm> cartItems = cartService.getCartItems(userId);
        // Đưa dữ liệu vào Model để hiển thị trên giao diện
        model.addAttribute("cartItems", cartItems);
        
        // Gọi getTotalAmount() để lấy tổng tiền giỏ hàng sau khi cập nhật
        BigDecimal totalAmount = cartRepository.getTotalAmount(userId);
        totalAmount = totalAmount != null ? totalAmount : BigDecimal.ZERO;
        // Đưa dữ liệu vào Model để hiển thị trên giao diện
        model.addAttribute("totalAmount", totalAmount);
        
		return "html_resources/cart";
	}
	
    
     // Phương thức để thêm sản phẩm vào giỏ hàng ở productDetail  
    @PostMapping("/add")
    public String addToCart(Model model, @RequestParam("productId") Long productId,
            @RequestParam("quantity") Integer quantity,
            Authentication authentication, RedirectAttributes redirectAttributes) {
    	// Gọi phương thức để lấy role và username vào Form
        addCommonAttributes(model, authentication);
        
        // Gọi phương thức từ service để thêm giỏ hàng
        String result = cartService.addToCart(authentication.getName(), productId, quantity);
        // Nếu có lỗi, redirect về lại trang chi tiết sản phẩm với thông báo lỗi
        if (result.startsWith("error: ")) {
            redirectAttributes.addFlashAttribute("errorMessage", result.substring(7)); // Bỏ "error: "
            return "redirect:/ComplexGym/products/detailProduct?id=" + productId;
        }

        // Nếu thành công, redirect về giỏ hàng với thông báo thành công
        redirectAttributes.addFlashAttribute("successMessage", result.substring(9)); // Bỏ "success: "
        return "redirect:/ComplexGym/cart";
    }


	// Phương thức để cập nhập sản phẩm trong giỏ hàng
    @PostMapping("/update")
    @ResponseBody
    public String updateCartItems(
            @RequestParam("cartId") Long cartId,
            @RequestParam("amount") Integer amount,
            Authentication authentication, 
            Model model) {
    	
		// Gọi phương thức để lấy role và tên của người dùng vào form
        addCommonAttributes(model, authentication);
        
        // Lấy userId từ model
        Long userId = (Long) model.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login"; // Nếu userId null, điều hướng về trang login
        }

        String responseMessage = cartService.updateCartAmount(cartId, amount, userId);

        // Kiểm tra lỗi từ service
        if (responseMessage.startsWith("error")) {
            return responseMessage;
        }
        // Gọi getTotalAmount() để lấy tổng tiền giỏ hàng sau khi cập nhật
        BigDecimal totalAmount = cartRepository.getTotalAmount(userId);
        totalAmount = totalAmount != null ? totalAmount : BigDecimal.ZERO;
        
        return amount + "|" + totalAmount.toString();
    }

	// Phương thức để xóa sản phẩm trong giỏ hàng
	@PostMapping("/delete")
	public String deleteCartItem(Model model, @RequestParam("cartId") Long cartId, 
            Authentication authentication) {
    	// Gọi phương thức xác định vai trò của user từ @Service
        addCommonAttributes(model, authentication);
        // Gọi phương thức từ Service để xóa đơn hàng
        cartService.deleteCartItem(cartId);
		return "redirect:/ComplexGym/cart";
	}
	
    // Xóa toàn bộ giỏ hàng của user (sử dụng khi cần reset giỏ hàng)
    @PostMapping("/clear")
    public String clearCart(Authentication authentication) {
        Long userId = cartService.getUserIdByUsername(authentication.getName());
        if (userId != null) {
            cartService.clearCart(userId);
        }
        return "redirect:/ComplexGym/cart";
    }

}
