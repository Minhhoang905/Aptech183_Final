package com.example.Aptech_Final.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Aptech_Final.Enity.Cart;
import com.example.Aptech_Final.Enity.OrdersDetail;
import com.example.Aptech_Final.Enity.OrdersManagement;
import com.example.Aptech_Final.Enity.Products;
import com.example.Aptech_Final.Enity.Users;
import com.example.Aptech_Final.Form.CartForm;
import com.example.Aptech_Final.Repository.*;
import jakarta.transaction.Transactional;

@Service
public class CartService {
	
	@Autowired
	private CartRepository cartRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ProductsRepository productsRepository;


    // Lấy danh sách sản phẩm trong giỏ hàng theo User ID
    public List<Cart> getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId);
    }

	// Phương thức lấy ID của user dựa vào username
    public Long getUserIdByUsername(String username) {
        Users user = userRepository.findByName(username);
        return user != null ? user.getId() : null;
    }

	// Phương thức lấy Product từ database
	public Products getProductById(Long productId) {
		return productsRepository.findById(productId).orElse(null);
	}

	// Phương thức lấy danh sách sản phẩm trong giỏ hàng của người dùng từ database
	public List<CartForm> getCartItems(Long userId) {
	    // Gọi phương thức trong repository để lấy dữ liệu giỏ hàng của người dùng (dưới dạng danh sách Object[])
	    List<Object[]> results = cartRepository.findCartWithProductInfo(userId);
	    
	    // Tạo danh sách để lưu các sản phẩm trong giỏ hàng sau khi chuyển đổi từ Object[] sang CartDTO
	    List<CartForm> cartItems = new ArrayList<>();

	    // Duyệt qua danh sách kết quả trả về từ database
	    for (Object[] row : results) {
	        // Tạo một đối tượng CartDTO để lưu thông tin của từng sản phẩm trong giỏ hàng
	        CartForm dto = new CartForm();

			// Gán giá trị từ mảng Object[] vào các thuộc tính của DTO
			// Lấy Cart ID từ cột đầu tiên
			dto.setId((Long) row[0]);
			// Lấy số lượng sản phẩm trong giỏ hàng
			dto.setAmount((Integer) row[1]);
			// Lấy đường dẫn ảnh của sản phẩm
			dto.setImagePath((String) row[2]);
			// Lấy tên sản phẩm
			dto.setProductName((String) row[3]);
			// Lấy giá sản phẩm
			dto.setPrice((BigDecimal) row[4]);
			// Lấy số lượng tồn kho của sản phẩm
			dto.setQuantity(((Number) row[5]).intValue());;
			// Lấy product Id
			dto.setProductId((Long) row[6]);
	        // Thêm sản phẩm đã được chuyển đổi vào danh sách cartItems
	        cartItems.add(dto);
	    }

	    // Trả về danh sách sản phẩm trong giỏ hàng dưới dạng List<CartDTO>
	    return cartItems;
	}

	// Phương thức thêm sản phẩm vào giỏ hàng
	public String addToCart(String username, Long productId, int quantity) {
	    // Lấy thông tin sản phẩm từ database
		Products products = productsRepository.findById(productId).orElse(null);
		if(products == null) return "error: Product not found!";
		
	    // Tìm sản phẩm trong giỏ hàng của người dùng
        Long userId = getUserIdByUsername(username);
        if (userId == null) return "error: User not found!";
        
        // Lấy giỏ hàng của người dùng với sản phẩm này
        Cart cartItem = cartRepository.findByUserIdAndProductId(userId, productId);
        
        if (cartItem != null) {
            // Nếu sản phẩm đã có trong giỏ hàng, kiểm tra số lượng tồn kho
        	int newAmount = cartItem.getAmount() + quantity;
        	// Giới hạn số lượng trong giỏ hàng không được vượt quá số lượng tồn kho
        	if(newAmount > products.getQuantity()) {
        		return "error: Not enough stock available!";
        	};
        	// Set số lượng mới vào cart
        	cartItem.setAmount(newAmount);
        	// Lưu vào database
        	cartRepository.save(cartItem);

        } else {
        	// Nếu sản phẩm chưa có trong giỏ hàng thì giới hạn số lượng tối đa
        	if (quantity > products.getQuantity()) {
                return "error: Not enough stock available!";
			}
        	// Gán giỏ hàng đang có là 1 giỏ hàng mới và lưu thông tin
            cartItem = new Cart();
            cartItem.setUserId(userId);
            cartItem.setProductID(productId);
            cartItem.setAmount(quantity);

        	// Lưu vào database
        	cartRepository.save(cartItem);
        }
        return "success: Added to cart!";
    }
	
    // Cập nhật số lượng sản phẩm trong giỏ hàng (có kiểm tra tồn kho)
    @Transactional
    public String updateCartAmount(Long cartId, Integer amount, Long userId) {
        Cart cartItem = cartRepository.findById(cartId).orElse(null);
        if (cartItem == null) {
            return "error: Cart item not found!";
        }

        // Lấy sản phẩm để kiểm tra tồn kho
        Products product = productsRepository.findById(cartItem.getProductID()).orElse(null);
        if (product == null) {
            return "error: Product not found!";
        }

        // Kiểm tra số lượng có vượt quá tồn kho không
        if (amount > product.getQuantity()) {
            return "error: Quantity exceeds available stock!";
        }

        if (amount > 0) {
            cartItem.setAmount(amount);
            cartRepository.save(cartItem);
        } else {
            cartRepository.delete(cartItem);
        }
        
        // Tính tổng tiền mới sau khi cập nhật giỏ hàng
        BigDecimal totalAmount = cartRepository.getTotalAmount(userId);
        // Dùng if-else rút gọn để tính tổng tiền
        totalAmount = totalAmount != null ? totalAmount : BigDecimal.ZERO;

        return "success: Update successful! Total: " + totalAmount + " VND";
    }
    

	// Xóa 1 sản phẩm khỏi giỏ hàng
	public void deleteCartItem(Long cartId) {
	    cartRepository.deleteById(cartId);
	}
	
	// Xóa toàn bộ giỏ hàng khi đã thanh toán
    @Transactional
	public void clearCart(Long userId) {
	    cartRepository.deleteByUserId(userId);
	}

}
