package com.example.Aptech_Final.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Aptech_Final.Enity.District;

@Repository
public interface DistrictRepository extends JpaRepository<District, Long>{
	// Tạo phương thức tùy chỉnh để tìm danh sách quận theo id Tỉnh
	List<District> findQuanByIdTinh(Long provinceId);
}
