package com.example.Aptech_Final.Repository;



import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.Aptech_Final.Controller.DTO.UserManagementDTO;
import com.example.Aptech_Final.Enity.Cart;
import com.example.Aptech_Final.Enity.Users;


// Đánh dấu interface này là một Spring Data Repository
@Repository
public interface UserRepository extends JpaRepository<Users, Long>{
	
	// Tạo phương thức để tìm kiếm tên của người dùng từ table USERS
	@Query(value = "SELECT * FROM dbo.USERS WHERE USER_NAME = ?1" , nativeQuery = true)
	Users findByName(String name);
	
	// Tạo phương thức kiểm tra xem có email của người dùng trong DB không ?
	boolean existsByEmail(String email);
	
    // Tìm User theo username (USER_NAME trong database)
    @Query("SELECT u FROM Users u WHERE u.name = :username")
    Users findByUsername(@Param("username") String username);
    
	// Tạo phương thức để tìm kiếm theo email
	Users findByEmail(String email);
	
	// Tạo phương thức mới, dùng interface `UserManagement` để hiển thị kết quả theo custom query
	@Query(value = "SELECT u.USER_ID AS id, u.USER_NAME AS name, u.FULL_NAME AS fullName, " +
            			"u.USER_DOB AS dateOfBirth, u.USER_EMAIL AS email, " +
            			"u.USER_PHONE_NUMBER AS phoneNumber, p.PROVINCE_NAME AS provinceName, " +
            			"d.DISTRICT_NAME AS districtName, w.WARD_NAME AS wardName, " +
            			"u.CUSTOMER_ADDRESS AS address, u.USER_ROLE AS role " +
            		"FROM dbo.USERS u " +
            			"LEFT JOIN dbo.PROVINCE p ON u.PROVINCE_ID = p.PROVINCE_ID " +
            			"LEFT JOIN dbo.DISTRICT d ON u.DISTRICT_ID = d.DISTRICT_ID " +
            			"LEFT JOIN dbo.WARD w ON u.WARD_ID = w.WARD_ID " +
            		"WHERE (?1 IS NULL OR u.USER_ID = ?1) AND " +
            				"(?2 IS NULL OR u.USER_NAME LIKE CONCAT('%', ?2, '%')) AND " +
            				"(?3 IS NULL OR u.FULL_NAME LIKE CONCAT('%', ?3, '%')) AND " +
            				"(?4 IS NULL OR u.USER_DOB = ?4) AND " +
            				"(?5 IS NULL OR u.USER_EMAIL LIKE CONCAT('%', ?5, '%')) AND " +
            				"(?6 IS NULL OR u.USER_PHONE_NUMBER LIKE CONCAT('%', ?6, '%')) AND " +
            				"(?7 IS NULL OR u.PROVINCE_ID = ?7) AND " +
            				"(?8 IS NULL OR u.DISTRICT_ID = ?8) AND " +
            				"(?9 IS NULL OR u.WARD_ID = ?9) AND " +
            				"(?10 IS NULL OR u.CUSTOMER_ADDRESS LIKE CONCAT('%', ?10, '%')) AND " +
            				"(?11 IS NULL OR u.USER_ROLE LIKE CONCAT('%', ?11, '%')) AND " +
            				"u.USER_ROLE <> 'admin'", 
    nativeQuery = true)	List<UserManagementDTO> selectUsersInfo(Long id, String name, String fullName, LocalDate dateOfBirth, String email,
         String phoneNumber, Long provinceId, Long districtId, Long wardId, String address, String role);
	
}



