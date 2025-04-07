package com.example.Aptech_Final.Form;

import java.util.List;

import com.example.Aptech_Final.Controller.DTO.OrderDetailDTO;
import com.example.Aptech_Final.Enity.OrdersManagement;

public class OrdersDetailForm {
	
    // Danh sách sản phẩm trong từ giỏ hàng
    private List<CartForm> items;
    // Thông tin đơn hàng
    private OrdersManagement order; 
    // Chi tiết đơn hàng (danh sách sản phẩm)
    private List<OrderDetailDTO> orderDetails; 

    // Getters và Setters

	public List<CartForm> getItems() {
		return items;
	}

	public void setItems(List<CartForm> items) {
		this.items = items;
	}

	public OrdersManagement getOrder() {
		return order;
	}

	public void setOrder(OrdersManagement order) {
		this.order = order;
	}

    public List<OrderDetailDTO> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetailDTO> orderDetails) {
        this.orderDetails = orderDetails;
    }
    
}
