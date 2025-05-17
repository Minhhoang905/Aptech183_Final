package com.example.Aptech_Final.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.example.Aptech_Final.Controller.DTO.OrderDetailDTO;
import com.example.Aptech_Final.Controller.DTO.OrdersManagementDTO;
import com.example.Aptech_Final.Enity.OrdersDetail;
import com.example.Aptech_Final.Enity.OrdersManagement;
import com.example.Aptech_Final.Form.CartForm;
import com.example.Aptech_Final.Form.OrdersDetailForm;
import com.example.Aptech_Final.Form.OrdersManagementForm;
import com.example.Aptech_Final.Repository.OrdersDetailRepository;
import com.example.Aptech_Final.Repository.OrdersManagementRepository;
import com.example.Aptech_Final.Repository.ProductsRepository;
import com.example.Aptech_Final.Security.VNPay.OrderCache;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;


@Service
public class PaymentService {
    @Autowired
    private CartService cartService;
    @Autowired
    private OrdersManagementRepository managementRepository;
    @Autowired
    private OrdersDetailRepository ordersDetailRepository;
    @Autowired
    private ProductsRepository productsRepository;
    
    // Phương thức để cập nhập số lượng tồn kho khi đã mua hàng
    public void updateProductQuantities(List<CartForm> cartItems) {
        // Dùng for để duyệt qua từng sản phẩm trong giỏ hàng
        for (CartForm item : cartItems) {
        	// Lấy ID của sản phẩm
            Long productId = item.getProductId();
            // Lấy số lượng sản phẩm đã mua
            int amountPurchased = item.getAmount();

            // Lấy số lượng tồn kho hiện tại (lấy từ repo)
            int currentStock = productsRepository.getCurrentQuantity(productId);

            // Tính toán số lượng tồn kho mới sau khi trừ số lượng đã mua (Đảm bảo số lượng không âm)
            int newStock = Math.max(currentStock - amountPurchased, 0);

            // Lưu vào database
            productsRepository.updateQuantity(productId, newStock);
        }
    }
    
    // Phương thức trả lại số lượng tồn khi khi đã về trạng thái 'CANCEL'
    public void restoreProductQuantities(List<CartForm> cartItems) {
        // Dùng for để duyệt qua từng sản phẩm trong giỏ hàng
        for (CartForm item : cartItems) {
        	// Lấy ID của sản phẩm
            Long productId = item.getProductId();
            // Lấy số lượng sản phẩm đã mua
            int amountCanceled = item.getAmount();

            // Lấy số lượng tồn kho hiện tại (lấy từ repo)
            int currentStock = productsRepository.getCurrentQuantity(productId);

            // Cộng số lượng bị huỷ lại vào kho
            int newStock = currentStock + amountCanceled;
            
            // Lưu vào database
            productsRepository.updateQuantity(productId, newStock);
        }
    }

    // Phương thức để lưu thông tin vào table `orderDetail`
    public void saveOrderDetail(Long orderId, List<CartForm> cartItems) {
    	
        // Kiểm tra nếu giỏ hàng rỗng
        if (cartItems == null || cartItems.isEmpty()) {
            throw new IllegalArgumentException("Cart is empty. Cannot save order details.");
        }

    	// Tạo 1 arraylist mới từ OrdersDetail
    	List<OrdersDetail> ordersDetail = new ArrayList<>();
    	
    	// Dùng for-each để gán các thông tin vào entity `OrdersDetail`
    	for (CartForm item : cartItems) {
    		
            // Kiểm tra nếu productId không hợp lệ
            if (item.getProductId() == null) {
                throw new IllegalArgumentException("Product ID is null for item: " + item);
            }

            // Kiểm tra sự tồn tại của sản phẩm trong cơ sở dữ liệu
            if (!productsRepository.existsById(item.getProductId())) {
                throw new IllegalArgumentException("Product ID is invalid for item: " + item);
            }

            // Gọi đối tượng OrderDetail rồi gán 
            OrdersDetail dto = new OrdersDetail();
            // Gán id đơn hàng
            dto.setOrderId(orderId);
            // Gán id sản phẩm từ CartDTO
            dto.setProductId(item.getProductId());
            // Gán số lượng sản phẩm
            dto.setAmount(item.getAmount());
            // Gán ngày cập nhập cuối cùng
            dto.setLastUpdated(LocalDateTime.now());
            // Gán giá tiền đơn vị
            dto.setUnitPrice(item.getPrice());
            // Tính tổng tiền (unitPrice * amount) bằng cách gọi phương thức tính tổng ở entity
            dto.setTotalPrice(item.getPrice().multiply(BigDecimal.valueOf(item.getAmount())));

            // Thêm vào arraylist của OrderDetail
            ordersDetail.add(dto);
        }
    	
        // Lưu vào database
        ordersDetailRepository.saveAll(ordersDetail);
    }
    
    // Phương thức để lưu sản phẩm vào order management
    public String getOrder(OrdersManagementForm managementForm, Model model) {
    	
    	// Kiểm tra nếu không có sản phẩm trong giỏ hàng
    	if(managementForm.getItems() == null || managementForm.getItems().size() == 0) {
    		return "error: Your Cart is empty!";
    	}
    	
        // Kiểm tra mỗi sản phẩm trong giỏ hàng có productId hợp lệ không
        for (CartForm item : managementForm.getItems()) {
            if (item.getProductId() == null) {
                return "error: Product ID is missing for one or more items!";
            }
        }

        // Tính toán tổng tiền đơn hàng từ các sản phẩm trong giỏ hàng (khởi tạo với giá tiền là 0 để tránh null)
    	BigDecimal totalAmount = BigDecimal.ZERO;

    	for (CartForm item : managementForm.getItems()) {
    	    BigDecimal itemTotal = item.getPrice().multiply(BigDecimal.valueOf(item.getAmount()));
    	    totalAmount = totalAmount.add(itemTotal);
    	}
    	     	
    	// Gọi đối tượng từ DTO để gán thông tin vào database
    	OrdersManagement orders = new OrdersManagement();
    	
    	// Gán thông tin từ Form vào entity `OrdersManagement`
    	// Id của người dùng
    	orders.setUserId(managementForm.getUserId());
    	// Họ tên đầy đủ
        orders.setFullName(managementForm.getFullName());
        // Mail
        orders.setEmail(managementForm.getEmail());
        // Số điện thoại
        orders.setPhoneNumber(managementForm.getPhoneNumber());
        // Id Tỉnh
        orders.setProvinceId(managementForm.getProvinceId());
        // Id Quận
        orders.setDistrictId(managementForm.getDistrictId());
        // Id Xã
        orders.setWardId(managementForm.getWardId());
        // Địa chỉ cụ thể
        orders.setAddress(managementForm.getAddress());
        // Ghi chú
    	orders.setNote(managementForm.getNote());
    	// Phương thức thanh toán
    	orders.setPaymentMethod(managementForm.getPaymentMethod());
    	// Tình trạng đơn hàng
    	orders.setOrderStatus("PROCESSING");
    	// Tổng số tiền đơn hàng
    	orders.setTotalAmount(totalAmount);
    	// Thời gian đặt hàng
    	orders.setOrderStartTime(LocalDateTime.now());
    	
    	// Gọi đối tượng của `OrdersManagement` rồi lưu vào database 
    	orders = managementRepository.save(orders);
    	
    	// Gọi phương thức saveOrderDetail() để lưu thông tin vào table ordersDetail
    	saveOrderDetail(orders.getOrderId(), managementForm.getItems());
    	
    	// Xóa giỏ hàng của user sau khi đã xác nhận thanh toán
    	cartService.clearCart(managementForm.getUserId());
    	
    	return "success: Order placed successfully!";
    }
    
    // Phương thức lấy toàn bộ đơn hàng của tất cả user (dành cho admin)
    public List<OrdersManagementDTO> getOrders(Long orderId, Long userId, String fullName, String email, String phoneNumber,
            Long provinceId, Long districtId, Long wardId, String address, String orderStatus) {
        return managementRepository.selectOrderInfo(orderId, userId, fullName, email, phoneNumber, 
            provinceId, districtId, wardId, address, orderStatus);
    }
    
    // Phương thức lấy tất cả đơn hàng của người dùng
    public List<OrdersManagementDTO> getAllOrdersByUserId(Long userId) {
        return managementRepository.selectOrderInfo(null, userId, null, null, null, null, null, null, null, null);
    }
    
    // Phương thức lấy chi tiết sản phẩm của đơn hàng
    public List<OrderDetailDTO> getOrderDetailsWithProductInfo(Long orderId) {
        return ordersDetailRepository.findOrderDetailsByOrderId(orderId);
    }
    
    // Phương thức để lấy thông tin đơn hàng và chi tiết đơn hàng từ cả 2 bảng
    public OrdersDetailForm getOrderDetails(Long orderId) {
        OrdersManagement order = managementRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("error: Order not found"));

        List<OrderDetailDTO> orderDetails = ordersDetailRepository.findOrderDetailsByOrderId(orderId);

        OrdersDetailForm dto = new OrdersDetailForm();
        dto.setOrder(order);
        dto.setOrderDetails(orderDetails);

        return dto;
    }
    
    // Phương thức lấy chi tiết đơn hàng theo từng OrderId và nhóm theo OrderId
    public Map<Long, List<OrderDetailDTO>> getOrderDetailsGroupedByOrderId(List<OrdersManagementDTO> orders) {
        Map<Long, List<OrderDetailDTO>> orderDetailsGrouped = new HashMap<>();
        
        // Duyệt qua tất cả đơn hàng và lấy chi tiết đơn hàng tương ứng với custom query
        for (OrdersManagementDTO orderDTO : orders) {
            // Lấy chi tiết đơn hàng từ custom query, sử dụng OrderDetailDTO
            List<OrderDetailDTO> orderDetails = ordersDetailRepository.findOrderDetailsByOrderId(orderDTO.getOrderId());
            orderDetailsGrouped.put(orderDTO.getOrderId(), orderDetails);
        }
        
        return orderDetailsGrouped;
    }

    // Phương thức cập nhập trạng thái đơn hàng
    public String updateOrderStatus (Long orderId, String orderStatus) {
    	// Dùng repo để gọi id đơn hàng
    	OrdersManagement order = managementRepository.findById(orderId).orElse(null);
    	// Trả về lỗi nếu là null
    	if (order == null) {
			return "error: Order not found!";
		}
    	
    	// Trả về lỗi nếu đơn hàng có trạng thái là `CANCEL`
    	if ("CANCEL".equals(order.getOrderStatus())) {
			return "error: Sorry, you have canceled this order. Please create a new order.";
		}
    	
    	// Gán trạng thái đơn hàng hiện tại vào trạng thái trong OrdersManagement
    	order.setOrderStatus(orderStatus);
    	// Cập nhật thời gian kết thúc mỗi lần đổi trạng thái
    	order.setOrderEndTime(LocalDateTime.now());
    	
    	// Lưu vào database
    	managementRepository.save(order);
    	
    	// Trả về câu lệnh thành công
    	return "succes: Order status updated completed!";
    }
    
    // Phương thức xóa dữ liệu trong database (chỉ dùng khi trạng thái là 'CANCEL'
    @Transactional // Dùng @Transactional (bắt buộc) để đảm bảo tính toàn vẹn dữ liệu trong giao dịch
    public String deleteOrder (Long orderId) {
    	try {
            // Lấy chi tiết giỏ hàng từ ORDER_DETAIL qua orderId
            List<CartForm> cartItems = ordersDetailRepository.getCartItemsByOrderId(orderId);
            
            // Nếu đơn hàng là 'CANCEL', gọi phương thức restoreProductQuantities() để cập nhập lại số lượng tồn kho
            restoreProductQuantities(cartItems);
            
        	// Xóa chi tiết đơn hàng trước tiên
        	ordersDetailRepository.deleteByOrderId(orderId);
        	
        	// Xóa đơn hàng sau khi xóa chi tiết đơn hàng
        	managementRepository.deleteByOrderId(orderId);
        	
        	// Trả về lỗi
        	return "success: Order deleted successful. You can't backup this order anymore!";

		} catch (Exception e) {
			System.out.println("Error cause: "+ e.getMessage());
			return "error: Order deleted unsuccessfully. Please try again or contact to Hoangdnm2";
		}
    }
}
