package com.example.Aptech_Final.Repository;

import java.math.BigDecimal;
import java.util.List;

import org.apache.el.lang.ELArithmetic.BigDecimalDelegate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.Aptech_Final.Enity.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
	// Lấy danh sách sản phẩm trong giỏ hàng của user
	List<Cart> findByUserId(Long userId);

	// Tìm sản phẩm trong giỏ hàng theo user_id và productID
	@Query("SELECT c FROM Cart c WHERE c.userId = :userId AND c.productID = :productId")
	Cart findByUserIdAndProductId(@Param("userId") Long userId, @Param("productId") Long productId);

	// Xóa toàn bộ giỏ hàng của user sau khi thanh toán
	void deleteByUserId(Long userId);

	// Query để lấy giỏ hàng kèm thông tin sản phẩm từ bảng PRODUCT
	@Query(value = """
			SELECT c.CART_ID, c.AMOUNT, p.IMAGE_PATH, p.PRODUCT_NAME, p.PRICE, p.QUANTITY
			FROM CART c
			JOIN PRODUCT p ON c.PRODUCT_ID = p.PRODUCT_ID
			WHERE c.USER_ID = :userId
			""", nativeQuery = true)
	List<Object[]> findCartWithProductInfo(@Param("userId") Long userId);
	
	// Tính tổng tiền sản phẩm
	@Query("SELECT SUM(c.amount * p.price) FROM Cart c JOIN Products p ON c.productID = p.id")
	BigDecimal getTotalAmount();

}
