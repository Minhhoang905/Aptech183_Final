package com.example.Aptech_Final.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.Aptech_Final.Enity.OrdersManagement;

@Repository
public interface OrdersManagementRepository extends JpaRepository<OrdersManagement, Long>{
    // Tìm tất cả đơn hàng của user
    List<OrdersManagement> findByUserId(Long userId);

    @Query("SELECT o FROM OrdersManagement o WHERE o.userId = :userId AND o.orderStatus = :status")
    List<OrdersManagement> findByUserIdAndStatus(@Param("userId") Long userId, @Param("status") String status);

}
