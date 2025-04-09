package com.example.Aptech_Final.Form;

import java.math.BigDecimal;

// DTO giúp truyền dữ liệu từ Controller sang View (Thymeleaf)
public class CartForm {
	// ID của giỏ hàng
    private Long id; 
    // ID của sản phẩm
    private Long productId;
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
    
    
    //Constructor
    public CartForm() {}

	public CartForm(Long id, Long productId, int amount, String imagePath, String productName, BigDecimal price,
			int quantity) {
		this.id = id;
		this.productId = productId;
		this.amount = amount;
		this.imagePath = imagePath;
		this.productName = productName;
		this.price = price;
		this.quantity = quantity;
	}
    
	// Getter & Setter
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
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
