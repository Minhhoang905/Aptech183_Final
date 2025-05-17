package com.example.Aptech_Final.Controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.Aptech_Final.Enity.OrdersManagement;
import com.example.Aptech_Final.Form.OrdersManagementForm;
import com.example.Aptech_Final.Security.VNPay.Config;
import com.example.Aptech_Final.Security.VNPay.OrderCache;
import com.example.Aptech_Final.Service.PaymentService;
import com.example.Aptech_Final.Service.VNPAYService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/ComplexGym/payment/VNPay")
public class VNPayController {

    @Autowired
    private VNPAYService vnPayService;
	@Autowired
	private PaymentService paymentService;

	
    // Chuyển hướng người dùng đến cổng thanh toán VNPAY
    @PostMapping("/submitOrder")
    public String submitOrder(@ModelAttribute("checkout") OrdersManagementForm ordersManagementForm,
                              @RequestParam("amount") BigDecimal orderTotal,
                              HttpServletRequest request) {

        // Chuyển từ VND sang xu (x100)
        int amountInVND = orderTotal.intValue();

        // Tạo UUID cho đơn hàng, dùng làm khóa cache và vnp_OrderInfo
        String uuid = UUID.randomUUID().toString();

        // Lưu OrdersManagementForm vào cache với UUID
        OrderCache.saveOrderToCache(uuid, ordersManagementForm);

        // Lấy URL trả về (callback URL của VNPay)
        String vnpReturnUrl = Config.vnp_ReturnUrl;

        // Tạo URL thanh toán VNPay với UUID truyền làm orderInfo
        String vnpayUrl = vnPayService.createOrder(request, amountInVND, uuid, vnpReturnUrl);

        System.out.println("Redirecting to VNPay URL: " + vnpayUrl);
        return "redirect:" + vnpayUrl;
    }
    
    // Xử lý callback sau khi thanh toán xong
    @GetMapping("/vnpay-payment-return")
    public String paymentCompleted(HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
    	
        // Xác minh tính hợp lệ của kết quả thanh toán từ VNPay (chữ ký, trạng thái, ...)
        int paymentStatus = vnPayService.orderReturn(request);
        
        // Lấy UUID từ tham số trả về để truy cập đơn hàng đã lưu trong cache
        String uuid = request.getParameter("vnp_OrderInfo");

        // Lấy OrdersManagementForm từ cache dựa theo UUID
        OrdersManagementForm ordersManagementForm = OrderCache.getOrderFromCache(uuid);
        // Trả về 1 web chứa 1 Alert cảnh báo nếu bị null
        if (ordersManagementForm == null) {
            model.addAttribute("errorCode", "Order information does not exist or the session has expired!");
            return "html_resources/orderFail";
        }
        
        // Lấy số tiền từ tham số trả về và chuyển đổi từ xu về VND
        String amountStr = request.getParameter("vnp_Amount");
        BigDecimal amount = new BigDecimal(amountStr).divide(BigDecimal.valueOf(100));
        ordersManagementForm.setTotalAmount(amount);
        
        // If-else để trả về trạng thái thanh toán (nếu là 1 thì thành công)
        if (paymentStatus == 1) {
        	// Gọi phương thức từ paymentService để lưu vào database
            String result = paymentService.getOrder(ordersManagementForm, model);
            
            // If-else để trả về kết quả
            if (result.startsWith("success")) {
            	// Gọi phương thức từ paymentService để cập nhập số lượng sản phẩm.
                paymentService.updateProductQuantities(ordersManagementForm.getItems());

                // Xóa cache sau khi xử lý xong
                OrderCache.removeOrderFromCache(uuid);
                // Truyền vào model khi có thanh toán thành công
                redirectAttributes.addFlashAttribute("successMessage", "Payment successful for order: " + uuid);
                
                // Redirect đến đường dẫn mong muốn
                return "redirect:/ComplexGym/payment/ordersManagement";
            } else {
                // Truyền vào model ở orderFail.html khi có thanh toán thất bại
                model.addAttribute("errorCode", "Error saving order after VNPay payment.");
                
                // Redirect đến đường dẫn mong muốn
                return "html_resources/orderFail";
            }
        } else {        	
        	// Gọi responseCode từ VNPAY
            String errorCode = request.getParameter("vnp_ResponseCode");
            // Truyền vào model ở orderFail.html khi có thanh toán thất bại
            model.addAttribute("errorCode", errorCode);
            
            // Redirect đến đường dẫn mong muốn          
            return "html_resources/orderFail";
        }
    }

    
}
