package com.example.Aptech_Final.Enity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
//Gọi tên của table
@Table(name = "PRODUCT") //Sẽ lấy theo tên của Table này (nếu đặt tên class đúng với tên table thì k cần cũng được)

public class Products {
	// Tạo các thuộc tính = tên trường trong database (k cần đúng tên, nhưng nên đặt giống cho khỏe):
    // Đánh dấu trường này là khóa chính (primary key) của bảng LOGIN
	@Id
    // Tự động sinh giá trị cho khóa chính (ID) khi một bản ghi mới được tạo
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PRODUCT_ID")
	private Long id;
	// Tên sản phẩm
	@Column(name = "PRODUCT_NAME")
	private String productName;
	// Giá tiền
	@Column(name = "PRICE", precision = 18, scale = 2)
	private BigDecimal price;
	// Loại sản phẩm
	@Column(name = "TYPE")
	private String type;
	// Số lượng
	@Column(name = "QUANTITY", nullable = false)
	private int quantity;  // Kiểu int để lưu số lượng sản phẩm
	// Đường dẫn của ảnh
	@Column(name = "IMAGE_PATH")
	private String imagePath;

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
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	
}
