package com.example.Aptech_Final.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.Aptech_Final.Controller.DTO.OrdersManagementDTO;
import com.example.Aptech_Final.Enity.OrdersManagement;

@Repository
public interface OrdersManagementRepository extends JpaRepository<OrdersManagement, Long>{
	// Phương thức lấy tất cả đơn hàng
	@Query(value = "SELECT o.ORDER_ID AS orderId, o.USER_ID AS userId, u.USER_NAME AS name, o.FULL_NAME AS fullName, " +
            "o.USER_EMAIL AS email, o.USER_PHONE_NUMBER AS phoneNumber, " +
            "o.PROVINCE_ID AS provinceId, p.PROVINCE_NAME AS provinceName, " +
            "o.DISTRICT_ID AS districtId, d.DISTRICT_NAME AS districtName, " +
            "o.WARD_ID AS wardId, w.WARD_NAME AS wardName, " +
            "o.CUSTOMER_ADDRESS AS address, o.NOTE AS note, o.PAYMENT_METHOD AS paymentMethod, " +
            "o.ORDERS_STATUS AS orderStatus, o.TOTAL_AMOUNT AS totalAmount, " +
            "o.ORDER_START_TIME AS orderStartTime, o.ORDER_END_TIME AS orderEndTime " +
     "FROM dbo.ORDERS_MANAGEMENT o " +
         "LEFT JOIN dbo.USERS u ON o.USER_ID = u.USER_ID " +
         "LEFT JOIN dbo.PROVINCE p ON o.PROVINCE_ID = p.PROVINCE_ID " +
         "LEFT JOIN dbo.DISTRICT d ON o.DISTRICT_ID = d.DISTRICT_ID " +
         "LEFT JOIN dbo.WARD w ON o.WARD_ID = w.WARD_ID " +
     "WHERE (?1 IS NULL OR o.ORDER_ID = ?1) AND " +
           "(?2 IS NULL OR o.USER_ID = ?2) AND " +
           "(?3 IS NULL OR o.FULL_NAME LIKE CONCAT('%', ?3, '%')) AND " +
           "(?4 IS NULL OR o.USER_EMAIL LIKE CONCAT('%', ?4, '%')) AND " +
           "(?5 IS NULL OR o.USER_PHONE_NUMBER LIKE CONCAT('%', ?5, '%')) AND " +
           "(?6 IS NULL OR o.PROVINCE_ID = ?6) AND " +
           "(?7 IS NULL OR o.DISTRICT_ID = ?7) AND " +
           "(?8 IS NULL OR o.WARD_ID = ?8) AND " +
           "(?9 IS NULL OR o.CUSTOMER_ADDRESS LIKE CONCAT('%', ?9, '%')) AND " +
           "(?10 IS NULL OR o.ORDERS_STATUS LIKE CONCAT('%', ?10, '%'))",
    nativeQuery = true)
	List<OrdersManagementDTO> selectOrderInfo(Long orderId, Long userId, String fullName, String email, String phoneNumber,
                                       	Long provinceId, Long districtId, Long wardId, String address, String orderStatus);
    // Tìm tất cả đơn hàng của user
    List<OrdersManagement> findByUserId(Long userId);
    
    // Xóa chi tiết đơn hàng theo orderId
    void deleteByOrderId(Long orderId);

}
