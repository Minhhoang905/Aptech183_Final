package com.example.Aptech_Final.Controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.Aptech_Final.Controller.DTO.OrderDetailDTO;
import com.example.Aptech_Final.Controller.DTO.OrdersManagementDTO;
import com.example.Aptech_Final.Controller.DTO.UsersDTO;
import com.example.Aptech_Final.Enity.District;
import com.example.Aptech_Final.Enity.OrdersDetail;
import com.example.Aptech_Final.Enity.Province;
import com.example.Aptech_Final.Enity.Users;
import com.example.Aptech_Final.Enity.Ward;
import com.example.Aptech_Final.Form.CartForm;
import com.example.Aptech_Final.Form.OrdersManagementForm;
import com.example.Aptech_Final.Repository.CartRepository;
import com.example.Aptech_Final.Repository.UserRepository;
import com.example.Aptech_Final.Security.VNPay.Config;
import com.example.Aptech_Final.Service.*;

import jakarta.servlet.http.HttpServletRequest;

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
	@Autowired
	private UserService userService;
	@Autowired
	private PaymentService paymentService;
    @Autowired
    private VNPAYService vnPayService;

    // Tạo phương thức để thêm role và username vào model
    private void addCommonAttributes(Model model, Authentication authentication) {
        if (authentication != null) {
            String role = homeService.getCurrentUserRole();
            String username = authentication.getName();           
            // Lấy user từ database
            Users user = userRepository.findByUsername(username);
            
            if (user != null) {
                model.addAttribute("userId", user.getId());
                model.addAttribute("username", user.getName());
                model.addAttribute("role", role);
                model.addAttribute("email", user.getEmail());
                model.addAttribute("phoneNumber", user.getPhoneNumber());
                model.addAttribute("address", user.getAddress());
            }
        }
    }
    
    // Phương thức để xử lý lấy sẵn thông tin
    private void prepareData(OrdersManagementForm ordersManagementForm, Long id, Long userId,  Model model) {
		// Tạo đối tượng ở lớp DropDownDTO
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
		
	    // Lấy userId từ request, nếu có
	    Long userToFind = (id != null) ? id : userId;
		// Lấy đối tượng Users để cập nhật (gọi từ service)
		Users checkout = userService.findUserById(userToFind).orElse(new Users());
		
        // Gọi service để lấy danh sách sản phẩm trong giỏ hàng
        List<CartForm> cartItems = cartService.getCartItems(userId);
        // Gán list thành 1 arraylist nếu bị lỗi null
        if (cartItems == null) {
            cartItems = new ArrayList<>();
        }

	    // Gán dữ liệu từ `checkout` vào `OrdersManagementDTO`
		ordersManagementForm.setUserId(checkout.getId());
		ordersManagementForm.setName(checkout.getName());
		ordersManagementForm.setFullName(checkout.getFullName());
		ordersManagementForm.setEmail(checkout.getEmail());
		ordersManagementForm.setPhoneNumber(checkout.getPhoneNumber());
		ordersManagementForm.setProvinceId(checkout.getProvinceId());
		ordersManagementForm.setDistrictId(checkout.getDistrictId());
		ordersManagementForm.setWardId(checkout.getWardId());
		ordersManagementForm.setAddress(checkout.getAddress());
		// Textarea rỗng ban đầu
		ordersManagementForm.setNote(""); 
        // Gán cartItems vào OrdersManagementForm (nếu nó chưa có)
        ordersManagementForm.setItems(cartItems);

	    // Thêm đối tượng userUpdate để binding với th:object ở form update
	    model.addAttribute("checkout", ordersManagementForm);

        // Đưa dữ liệu vào Model để hiển thị trên giao diện
        model.addAttribute("cartItems", cartItems);
        
        // Gọi getTotalAmount() để lấy tổng tiền giỏ hàng sau khi cập nhật
        BigDecimal totalAmount = cartRepository.getTotalAmount(userToFind);
        totalAmount = totalAmount != null ? totalAmount : BigDecimal.ZERO;       
        // Đưa dữ liệu vào Model để hiển thị trên giao diện
        model.addAttribute("totalAmount", totalAmount);
        
        // Xử lý các sản phẩm trong giỏ hàng
        List<OrdersDetail> ordersDetails = new ArrayList<>();

        // Chuyển các sản phẩm từ giỏ hàng vào OrdersDetail
        for (CartForm item : cartItems) {
            OrdersDetail orderDetail = new OrdersDetail();
            orderDetail.setProductId(item.getProductId());
            orderDetail.setUnitPrice(item.getPrice());
            orderDetail.setAmount(item.getAmount());
            orderDetail.setTotalPrice(item.getPrice().multiply(BigDecimal.valueOf(item.getAmount())));
            
            // Thêm vào danh sách ordersDetails
            ordersDetails.add(orderDetail);
        }


    }
    
    // Phương thức hiển thị giỏ hàng
	@GetMapping("")
	public String getCart(@ModelAttribute("checkout") OrdersManagementForm ordersManagementForm, Model model, Authentication authentication, @RequestParam(value = "userId", required = false) Long id) {
	    
		// Gọi phương thức để lấy role và tên của người dùng vào form
        addCommonAttributes(model, authentication);
        
        // Lấy userId từ model
        Long userId = (Long) model.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login"; // Nếu userId null, điều hướng về trang login
        }
        
        // Gọi phương thức prepareData() để chuẩn bị dữ liệu
        prepareData(ordersManagementForm, id, userId, model);
        
		return "html_resources/payment";
	}
	
	@PostMapping("/confirmPayment")
	public String buyNow(
						@ModelAttribute("checkout") OrdersManagementForm ordersManagementForm,
						@RequestParam(name = "userId", required = false) Long id,
	                     Model model,
	                     Authentication authentication, 
	                     RedirectAttributes redirectAttributes,
	                     HttpServletRequest request) {
	    
		// Gọi phương thức để lấy role và tên của người dùng vào form
        addCommonAttributes(model, authentication);
        
        // Lấy userId từ model
        Long userId = (Long) model.getAttribute("userId");
        if (userId == null) {
		    // Điều hướng sang trang login
            return "redirect:/login";
        }
        
        // Gọi phương thức prepareData() để chuẩn bị dữ liệu
        prepareData(ordersManagementForm, id, userId, model);
        
        // Xử lý phương thức thanh toán VNPay (Dùng UUID + Cache)
        if ("VNPay".equalsIgnoreCase(ordersManagementForm.getPaymentMethod())) {
            // B1: Tạo UUID
            String uuid = java.util.UUID.randomUUID().toString();

            // B2: Lưu OrdersManagementForm vào cache tạm bằng UUID
            com.example.Aptech_Final.Security.VNPay.OrderCache.saveOrderToCache(uuid, ordersManagementForm);

            // B3: Tạo URL sang đường dẫn của VNPay (từ lớp Config)
            // Note: Vì đang dùng ngrok nên phải điều chỉnh url của ngrok
            String urlReturn = Config.vnp_ReturnUrl; 
            // Gọi phương thức createOrder ở vnPayService với các tham số yêu cầu
            String vnpayUrl = vnPayService.createOrder(request, ordersManagementForm.getTotalAmount().intValue(), uuid, urlReturn);

            // B4: Redirect sang VNPay
            return "redirect:" + vnpayUrl;
        }

		// Tạo và gọi phương thức từ service
        String confirmPayment = paymentService.getOrder(ordersManagementForm, model);
        
        // Tạo if-else để chuyển về controller
        if (confirmPayment.startsWith("success: ")) {
        	// Thêm thông báo thành công vào Flash Attribute để hiển thị sau khi chuyển hướng
        	redirectAttributes.addFlashAttribute("successMessage", confirmPayment.substring(8));
        	// Gọi cập nhật tồn kho
        	paymentService.updateProductQuantities(ordersManagementForm.getItems());
        	
        	return "redirect:/ComplexGym/payment/ordersManagement";
		}else {
    	    // Kiểm tra nếu người dùng đã chọn Tỉnh thì load Quận/Huyện tương ứng
    	    if (ordersManagementForm.getProvinceId() != null) {
    	        List<District> districtList = userService.getDistrictByProvinceId(ordersManagementForm.getProvinceId());
    	        model.addAttribute("districtList", districtList);
    	    }

    	    if (ordersManagementForm.getDistrictId() != null) {
    	        List<Ward> wardList = userService.getWardByDistrictId(ordersManagementForm.getDistrictId());
    	        model.addAttribute("wardList", wardList);
    	    }

        	// Thêm thông báo thất bại vào Flash Attribute để hiển thị sau khi chuyển hướng
        	redirectAttributes.addFlashAttribute("errorMessage", confirmPayment.substring(6));
        	        	
		    // Điều hướng sang trang thanh toán
		    return "redirect:/ComplexGym/payment";

		}
	}
	
	// Phương thức để hiển thị danh sách quản lý đơn hàng
    @GetMapping("/ordersManagement")
    public String showOrderManagement(Model model, Authentication authentication) {
        // Gọi phương thức để lấy role và tên của người dùng vào form
        addCommonAttributes(model, authentication);
        
        // Lấy userId từ model
        Long userId = (Long) model.getAttribute("userId");
        if (userId == null) {
        	// Điều hướng về trang login nếu là null
            return "redirect:/login";
        }

        // Lấy role đăng nhập
        String role = (String) model.getAttribute("role");
        
        // Tạo if-else cho để tách xử lý của admin và của user/PT
        if ("ROLE_ADMIN".equals(role)) {
            // Gọi service để lấy danh sách đơn hàng của toàn bộ người dùng (admin)
            List<OrdersManagementDTO> orders = paymentService.getOrders(null, null, null, null, null, 
                    null, null, null, null, null);
        	
            
            // Gọi service để lấy chi tiết đơn hàng theo từng OrderId và nhóm theo OrderId
            Map<Long, List<OrderDetailDTO>> orderDetailsGrouped = paymentService.getOrderDetailsGroupedByOrderId(orders);
            
        	// Gọi service để lấy thông tin chi tiết đơn hàng với sản phẩm kèm theo (tạo 1 arraylist)
            List<OrderDetailDTO> allOrderDetail = new ArrayList<>();

        	for (Long orderId : orderDetailsGrouped.keySet()) {
        		// Gọi phương thức từ service, trả về danh sách OrderDetailDTO
        		List<OrderDetailDTO> orderDetailsWithProductInfo = paymentService.getOrderDetailsWithProductInfo(orderId);
        		
        		// Thêm tất cả vào arraylist của OrderDetailDTO
        		allOrderDetail.addAll(orderDetailsWithProductInfo);
        	}
        	
            // Đưa dữ liệu vào model để hiển thị trên giao diện
            model.addAttribute("orders", orders);
            model.addAttribute("orderDetailsGrouped", orderDetailsGrouped);
            model.addAttribute("allOrderDetail", allOrderDetail);
		}else {			
	        // Gọi service để lấy danh sách đơn hàng cho người dùng
		    List<OrdersManagementDTO> orders = paymentService.getAllOrdersByUserId(userId);
	        
	        // Gọi service để lấy danh sách chi tiết đơn hàng theo từng OrderId
		    Map<Long, List<OrderDetailDTO>> orderDetailsGrouped = paymentService.getOrderDetailsGroupedByOrderId(orders);

	        // Gọi service để lấy thông tin chi tiết đơn hàng với sản phẩm kèm theo
	        List<OrderDetailDTO> allOrderDetail = new ArrayList<>();
	        for (Long orderId : orderDetailsGrouped.keySet()) {
	            // Gọi phương thức từ service, trả về danh sách OrderDetailDTO
	            List<OrderDetailDTO> orderDetailsWithProductInfo = paymentService.getOrderDetailsWithProductInfo(orderId);
	            
	            // Thêm vào danh sách tổng
	            allOrderDetail.addAll(orderDetailsWithProductInfo);
	        }
	        // Đưa dữ liệu vào model để hiển thị trên giao diện
	        model.addAttribute("orders", orders);
	        model.addAttribute("orderDetailsGrouped", orderDetailsGrouped);
	        model.addAttribute("allOrderDetail", allOrderDetail);
		}   
        
        // Trả về trang orderManagement.html
        return "html_resources/ordersManagement";
    }
    
    // Phương thức cập nhập trạng thái đơn hàng
    @PostMapping("/updateOrderStatus")
    public String updateOrderStatus(Model model, 
    								Authentication authentication, 
    								RedirectAttributes redirectAttributes, 
    								@RequestParam("orderId") Long orderId, 
    								@RequestParam("orderStatus") String orderStatus) {
    	// Gọi phương thức để lấy role và tên người dùng vào form
    	addCommonAttributes(model, authentication);
    	
    	// Lấy userId từ model
    	Long userId = (Long) model.getAttribute("userId");
    	if (userId == null) {
			// Điều hướng về trang login nếu userId là null
    		return "redirect:/login";
		}
    	
    	// Gọi phương thức từ service để cập nhập trạng thái mua hàng
    	String orderResult = paymentService.updateOrderStatus(orderId, orderStatus);
    	
    	// Trả về kết quả dựa theo phương thức
    	if (orderResult.startsWith("error: ")) {
			redirectAttributes.addFlashAttribute("errorMessage", orderResult.substring(6).trim());
		} else {
			redirectAttributes.addFlashAttribute("successMessage", orderResult.substring(8).trim());
		}
    	
    	// Điều hướng về lại trang orderManagement
    	return "redirect:/ComplexGym/payment/ordersManagement";
    }
    
    // Phương thức để xóa đơn hàng (ở chỉ tiết đơn hàng và quản lý đơn hàng)
    @PostMapping("/deleteOrder")
    public String deleteOrder(Model model, 
    								Authentication authentication, 
    								RedirectAttributes redirectAttributes, 
    								@RequestParam("orderId") Long orderId, 
    								@RequestParam("orderStatus") String orderStatus) {
    	// Gọi phương thức để lấy role và tên người dùng vào form
    	addCommonAttributes(model, authentication);
    	
    	// Lấy userId từ model
    	Long userId = (Long) model.getAttribute("userId");
    	if (userId == null) {
			// Điều hướng về trang login nếu userId là null
    		return "redirect:/login";
		}

    	// Tạo if-else để check xem trạng thái có phải là 'CANCEL' không
    	if ("CANCEL".equals(orderStatus)) {
    		
        	// Gọi phương thức từ service để xóa đơn hàng và cập nhập lại số lượng tồn kho khi trạng thái là 'CANCEL'
			String orderResult = paymentService.deleteOrder(orderId);	
			
			// Trả về kết quả dựa theo phương thức
			if (orderResult.startsWith("error: ")) {
				redirectAttributes.addFlashAttribute("errorMessage", orderResult.substring(6).trim());
			} else {
				redirectAttributes.addFlashAttribute("successMessage", orderResult.substring(8).trim());
			}
		}else {
			redirectAttributes.addFlashAttribute("errorMessage", "This order cannot be deleted because it's in the following status: " + orderStatus);
		}
    	// Điều hướng về lại trang orderManagement
    	return "redirect:/ComplexGym/payment/ordersManagement";
    }
}
