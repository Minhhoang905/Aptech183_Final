package com.example.Aptech_Final.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Aptech_Final.Enity.Ward;

@Repository
public interface WardRepository extends JpaRepository<Ward, Long>{
	// Tạo phương thức tùy chỉnh để tìm danh sách xã theo id Quận
	List<Ward> findXaByIdQuan(Long districtId);
}
