package com.example.Aptech_Final.API;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.Aptech_Final.Enity.Users;
import com.example.Aptech_Final.Form.CartForm;
import com.example.Aptech_Final.Repository.CartRepository;
import com.example.Aptech_Final.Repository.OrdersManagementRepository;
import com.example.Aptech_Final.Repository.UserRepository;
import com.example.Aptech_Final.Service.CartService;
import com.example.Aptech_Final.Service.HomeService;

@Controller
@RequestMapping("/api/ComplexGym/cart")
public class CartControllerAPI {
	@Autowired
	private OrdersManagementRepository ordersManagementRepository;
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

	//API lấy giỏ hàng từ local storage vào database
    @GetMapping("/cart")
    public ResponseEntity<List<CartForm>> getCartItems(Model model, Authentication authentication) {
		// Gọi phương thức để lấy role và tên của người dùng vào form
        addCommonAttributes(model, authentication);

        String username = authentication.getName();
        // Lấy user từ database
        Users user = userRepository.findByUsername(username);

        List<CartForm> cartItems = cartService.getCartItems(user.getId());
        return ResponseEntity.ok(cartItems);
    }

}
