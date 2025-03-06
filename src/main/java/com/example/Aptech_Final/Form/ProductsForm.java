package com.example.Aptech_Final.Form;

import org.springframework.web.multipart.MultipartFile;

// Tạo thuộc tính (và tên phải khớp với thuộc tính bên html)
public class ProductsForm {
	// id sản phẩm
	private Long id;
	// Tên sản phẩm
	private String productName;
	// Giá tiền
    private String price; 
	// Loại sản phẩm
	private String type;
	// Số lượng
	private int quantity;	
	// Hình ảnh
	private MultipartFile imagePath;
	
	// Getter và setter
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public MultipartFile getImagePath() {
		return imagePath;
	}
	public void setImagePath(MultipartFile imagePath) {
		this.imagePath = imagePath;
	}
	
	
}
