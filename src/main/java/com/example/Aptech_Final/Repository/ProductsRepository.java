package com.example.Aptech_Final.Repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.Aptech_Final.Controller.DTO.ProductManagementDTO;
import com.example.Aptech_Final.Enity.Products;

import jakarta.transaction.Transactional;

@Repository
public interface ProductsRepository extends JpaRepository<Products, Long> {
	
	@Query(value = "SELECT PRODUCT_ID AS id, PRODUCT_NAME AS productName, PRICE, TYPE, QUANTITY, IMAGE_PATH FROM Product", nativeQuery = true)
	List<ProductManagementDTO> findAllProducts(Long id, String productName, BigDecimal price, String type, int quantity, String imagePath);
	
	// Phương thức tìm kiếm theo thể loại
	List<Products> findByType(String type);
	
	// Tìm kiếm đối tượng theo id
	Products findById(long id);
		
    // Lấy số lượng tồn kho hiện tại bằng native query
    @Query(value = "SELECT QUANTITY FROM PRODUCT WHERE PRODUCT_ID = :productId", nativeQuery = true)
    int getCurrentQuantity(@Param("productId") Long productId);

    // Cập nhật số lượng sản phẩm
    @Modifying
    @Transactional
    @Query(value = "UPDATE PRODUCT SET QUANTITY = :newQuantity WHERE PRODUCT_ID = :productId", nativeQuery = true)
    void updateQuantity(@Param("productId") Long productId, @Param("newQuantity") int newQuantity);
    
}
