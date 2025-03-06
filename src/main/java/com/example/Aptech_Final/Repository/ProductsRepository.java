package com.example.Aptech_Final.Repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.Aptech_Final.Controller.DTO.ProductManagementDTO;
import com.example.Aptech_Final.Enity.Products;

@Repository
public interface ProductsRepository extends JpaRepository<Products, Long> {
	
	@Query(value = "SELECT PRODUCT_ID AS id, PRODUCT_NAME AS productName, PRICE, TYPE, QUANTITY, IMAGE_PATH FROM Product", nativeQuery = true)
	List<ProductManagementDTO> findAllProducts(Long id, String productName, BigDecimal price, String type, int quantity, String imagePath);
	// Phương thức tìm kiếm theo thể loại
	List<Products> findByType(String type);
	
	// Phương thức tìm kiếm sản phẩm
	@Query(value = """
	        SELECT PRODUCT_ID AS id, PRODUCT_NAME AS productName, PRICE, TYPE, QUANTITY, IMAGE_PATH FROM PRODUCT 
	        WHERE PRODUCT_ID LIKE %:keyword% 
	           OR PRODUCT_NAME LIKE %:keyword% 
	           OR TYPE LIKE %:keyword% 
	           OR CAST(PRICE AS NVARCHAR) LIKE %:keyword% 
	           OR CAST(QUANTITY AS NVARCHAR) LIKE %:keyword% 
	           OR IMAGE_PATH LIKE %:keyword%
	        """, nativeQuery = true)
	List<ProductManagementDTO> searchProductsByKeyword(@Param("keyword") String keyword);

}
