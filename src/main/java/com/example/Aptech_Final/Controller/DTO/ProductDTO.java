package com.example.Aptech_Final.Controller.DTO;

import java.util.List;


public class ProductDTO {
	// Danh sách hiển thị thông tin sản phẩm
	List<ProductManagementDTO> productManagementDTOs;

	// Getter và setter
	public List<ProductManagementDTO> getProductManagementDTOs() {
		return productManagementDTOs;
	}

	public void setProductManagementDTOs(List<ProductManagementDTO> productManagementDTOs) {
		this.productManagementDTOs = productManagementDTOs;
	}
	
	
}
