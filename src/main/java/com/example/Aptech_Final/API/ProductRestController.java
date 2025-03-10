package com.example.Aptech_Final.API;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Aptech_Final.Service.ProductsService;

//Đánh dấu lớp này là một REST Controller, tự động chuyển đổi kết quả trả về thành JSON
@RestController
//Định nghĩa URL gốc cho tất cả các API liên quan đến sản phẩm
@RequestMapping("/api/products")
public class ProductRestController {
    // Tự động tiêm (inject) ProductDetailService để xử lý logic cập nhật mô tả sản phẩm
    @Autowired
    private ProductsService productsService;

    @PostMapping("/updateDescription") // Endpoint cho phương thức POST cập nhật mô tả sản phẩm
    public ResponseEntity<?> updateProductDescription(@RequestBody Map<String, String> payload) {
        // Lấy productId từ payload và chuyển đổi về kiểu Long
        Long productId = Long.parseLong(payload.get("id"));
        // Lấy nội dung mô tả mới từ payload
        String newDescription = payload.get("description");

        // Gọi phương thức của ProductDetailService để lưu hoặc cập nhật mô tả sản phẩm
        productsService.saveProductDescription(productId, newDescription);

        // Trả về thông báo thành công dưới dạng JSON
        return ResponseEntity.ok(Collections.singletonMap("message", "Cập nhật thành công!"));
    }

}
