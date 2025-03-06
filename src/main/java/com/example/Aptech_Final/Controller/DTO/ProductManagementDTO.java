package com.example.Aptech_Final.Controller.DTO;

import java.math.BigDecimal;

/**
 * Interface ProductManagementDTO là một Data Transfer Object (DTO) dùng để truyền tải
 * các data cần thiết của đối tượng Product mà không cần toàn bộ entity
 * Interface này định nghĩa các phương thức getter cần có, giúp việc lấy data dễ dàng hơn.
 */

public interface ProductManagementDTO {
	
	// Lấy các getter từ các table dựa theo câu lệnh query ở Repo
	// id
	Long getId();
	// Tên sản phẩm
	String getProductName();
	// Giá tiền
	BigDecimal getPrice();
	// Loại sản phẩm
	String getType();
	// Số lượng
	int getQuantity();
	// Đường dẫn hình ảnh
	String getImagePath();
	
}
