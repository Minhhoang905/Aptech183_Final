package com.example.Aptech_Final.Repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.Aptech_Final.Enity.OrdersDetail;

@Repository
public interface OrdersDetailRepository extends JpaRepository<OrdersDetail, Long>{
	// Gọi list dựa theo điều kiện của orderId
    List<OrdersDetail> findByOrderId(Long orderId);

	// Phương thức tìm kiếm theo order id và product id
    @Query("SELECT o FROM OrdersDetail o WHERE o.orderId = :orderId AND o.productId = :productId")
    OrdersDetail findByOrderIdAndProductId(@Param("orderId") Long orderId, @Param("productId") Long productId);
    
    
}
