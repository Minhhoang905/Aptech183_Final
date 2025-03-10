package com.example.Aptech_Final.Enity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
// Gọi tên của table
@Table(name = "PRODUCT_DETAIL")
public class ProductDetail {
	// Tạo entity có liên kết với Product
    @Id
    @Column(name = "PRODUCT_ID") 
    private Long productId;
    // Mô tả nội dung sản phẩm
    @Column(name = "DESCRIPTION", columnDefinition = "NVARCHAR(MAX)")
    private String description;
    
    // Getter và Setter
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
        
}
