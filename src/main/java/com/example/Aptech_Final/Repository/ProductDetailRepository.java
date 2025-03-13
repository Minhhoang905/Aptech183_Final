package com.example.Aptech_Final.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Aptech_Final.Enity.ProductDetail;

@Repository
public interface ProductDetailRepository extends JpaRepository<ProductDetail, Long>{
	// Phương thức tìm mô tả theo id
	ProductDetail findByProductId(Long id);
}
