package com.example.Aptech_Final.Controller.DTO;

import java.math.BigDecimal;

// DTO giúp truyền dữ liệu từ Controller sang View (Thymeleaf)
public class CartDTO {
	// ID của giỏ hàng
    private Long id; 
    // Số lượng sản phẩm
    private int amount; 
    // Đường dẫn ảnh sản phẩm
    private String imagePath;
    // Tên sản phẩm
    private String productName;
    // Giá sản phẩm
    private BigDecimal price; 
    // Số lượng tồn kho của sản phẩm
    private int quantity; 
    
    // Getter & Setter
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
    
}
