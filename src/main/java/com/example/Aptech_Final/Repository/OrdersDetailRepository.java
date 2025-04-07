package com.example.Aptech_Final.Repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.Aptech_Final.Controller.DTO.OrderDetailDTO;
import com.example.Aptech_Final.Enity.OrdersDetail;
import com.example.Aptech_Final.Form.CartForm;

@Repository
public interface OrdersDetailRepository extends JpaRepository<OrdersDetail, Long>{
	// Gọi list dựa theo điều kiện của orderId
    List<OrdersDetail> findByOrderId(Long orderId);

	// Phương thức tìm kiếm theo order id và product id
    @Query("SELECT o FROM OrdersDetail o WHERE o.orderId = :orderId AND o.productId = :productId")
    List<OrdersDetail> findByOrderIdAndProductId(@Param("orderId") Long orderId, @Param("productId") Long productId);
    
    // Phương thức để hiển thị danh sách chi tiết đơn hàng (dùng custom query)
    @Query(value = "SELECT " +
            "od.ORDERS_DETAIL_ID AS ordersDetailId, " +
            "od.ORDER_ID AS orderId, " +
            "od.PRODUCT_ID AS productId, " +
            "p.PRODUCT_NAME AS productName, " +
            "od.AMOUNT AS amount, " +
            "od.UNIT_PRICE AS unitPrice, " +
            "CAST(od.AMOUNT * od.UNIT_PRICE AS DECIMAL(18, 2)) AS totalPrice " +  // Ép kiểu totalPrice
        "FROM dbo.ORDERS_DETAIL od " +
        "JOIN dbo.PRODUCT p ON od.PRODUCT_ID = p.PRODUCT_ID " +
        "WHERE od.ORDER_ID = ?1", 
        nativeQuery = true)
    List<OrderDetailDTO> findOrderDetailsByOrderId(Long orderId);
    
    // Lấy danh sách sản phẩm và số lượng theo orderId (chuyển từ orderDetail => CartForm)
    @Query(value = "SELECT od.ORDERS_DETAIL_ID AS id, od.PRODUCT_ID AS productId, od.AMOUNT AS amount, " +
            "       p.IMAGE_PATH AS imagePath, p.PRODUCT_NAME AS productName, " +
            "       p.PRICE AS price, p.QUANTITY AS quantity " +
            "FROM ORDERS_DETAIL od " +
            "JOIN PRODUCT p ON od.PRODUCT_ID = p.PRODUCT_ID " +
            "WHERE od.ORDER_ID = :orderId", nativeQuery = true)
    List<CartForm> getCartItemsByOrderId(@Param("orderId") Long orderId);

    // Xóa chi tiết đơn hàng theo orderId
    void deleteByOrderId(Long orderId);

}
