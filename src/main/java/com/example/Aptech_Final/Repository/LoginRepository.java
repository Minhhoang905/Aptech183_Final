package com.example.Aptech_Final.Repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.Aptech_Final.Enity.Users;


// Đánh dấu interface này là một Spring Data Repository
@Repository
public interface LoginRepository extends JpaRepository<Users, Long>{
	
	// Tạo method để tìm kiếm tên của người dùng từ table USERS
	@Query(value = "SELECT * FROM dbo.USERS WHERE USER_NAME = ?1" , nativeQuery = true)
	Users findByName(String name);
	
}

