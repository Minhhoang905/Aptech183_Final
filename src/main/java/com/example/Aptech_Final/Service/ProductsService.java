package com.example.Aptech_Final.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.Aptech_Final.Controller.DTO.ProductDTO;
import com.example.Aptech_Final.Controller.DTO.ProductManagementDTO;
import com.example.Aptech_Final.Enity.ProductDetail;
import com.example.Aptech_Final.Enity.Products;
import com.example.Aptech_Final.Form.ProductsForm;
import com.example.Aptech_Final.Repository.ProductDetailRepository;
import com.example.Aptech_Final.Repository.ProductsRepository;

@Service
public class ProductsService {
	@Autowired
	private ProductsRepository productsRepository;
	@Autowired
	private ImageService imageService;
	@Autowired
	private ProductDetailRepository productDetailRepository;
	
	// Phương thức để thêm sản phẩm vào database
	public String saveProduct(ProductsForm productsForm, MultipartFile multipartFile) {
		try {
			// Kiểm tra tên sản phẩm không được để trống
			if (productsForm.getProductName() == null || productsForm.getProductName().trim().isEmpty()) {
				return "error: Product name cannot be Emty!";
			}
			// Kiểm tra xem giá tiền có phải null không
			if (productsForm.getPrice() == null || productsForm.getPrice().trim().isEmpty()) {
			    return "error: Price cannot be empty!";
			}
			// Kiểm tra số lượng có hợp lệ không
			if (productsForm.getQuantity() < 1) {
				return "error: Quantity must be at least 1.";
			}
			
			// Kiểm tra giá tiền có hợp lệ không
			String priceString = productsForm.getPrice();
			// Kiểm tra xem có ký tự full-size không
			if (Pattern.compile("[\\uFF00-\\uFFFF]").matcher(priceString).find()) {
				return "error: Price must use half-size characters only.";
			}
			// Kiểm tra có ký tự đặc biệt không
			if (!priceString.matches("^[0-9]+(\\.[0-9]{1,2})?$")) {
				return "error: Price must contain only numeric characters.";
			}
			// Chuyển đổi từ String sang BigDecimal
			BigDecimal priceBigDecimal = new BigDecimal(priceString);
			// Kiểm tra giá trị tối thiểu
			if (priceBigDecimal.compareTo(BigDecimal.valueOf(1000)) < 0) {
				return "error: Price must be at least 1000.";
			}
			// Tạo biến để lưu ảnh vào thư mục
			String imagePath = imageService.saveFile(multipartFile);

			// Tạo đối tượng product, gán từ form vào entity rồi lưu vào database
			Products products = new Products();
			products.setProductName(productsForm.getProductName());
			products.setPrice(new BigDecimal(productsForm.getPrice()));
			products.setType(productsForm.getType());
			products.setQuantity(productsForm.getQuantity());
			products.setImagePath(imagePath);

			productsRepository.save(products);
			// Trả về thông báo thành công với tên sản phẩm và loại sản phẩm
			return "success: " + products.getProductName() + " (" + products.getType()
					+ ") has been created successfully!";

		} catch (NumberFormatException e) {
			return "error: Price must be a valid number.";
		} catch (IOException e) {
			// Lỗi không upload được hình ảnh
			return "error: Failed to upload image.";
		}
	}
	
	// Phương thức để chuẩn bị dữ liệu, hiển thị ban đầu cho trang userManagement
	public ProductDTO getProductDTO(Products products) {
		
		// Lấy danh sách của ProductDTO theo từ entity của Product
		// id
		Long productId = products.getId();
		// Tên sản phẩm
		String productName = products.getProductName();
		// Giá tiền
		BigDecimal productPrice = products.getPrice();
		// Loại sản phẩm
		String productType = products.getType();
		// Số lượng
		int productQuantity = products.getQuantity();
		// Đường dẫn hình ảnh
		String productImagePath = products.getImagePath();
		
		// Gọi phương thức findUser trong `@Service` để hiển thị thông tin theo yêu cầu từ list
		List<ProductManagementDTO> productManagementDTOs = productsRepository.findAllProducts(productId, productName, productPrice, productType, productQuantity, productImagePath);
		
		// Tạo đối tượng của UsersDTO
		ProductDTO productDTO = new ProductDTO();
		// Gán danh sách vào danh sách productManagementDTOs ở lớp ProductDTO
		productDTO.setProductManagementDTOs(productManagementDTOs);
		
		return productDTO;
	}
	
    // Phương thức sử dụng hàm escape ký tự đặc biệt trong chuỗi tìm kiếm
    private String escapeSpecialChars(String input) {
        if (input == null) return null;
        // Thứ tự quan trọng: escape "\" trước, sau đó "%" và "_"
        return input.replace("\\", "\\\\")
                    .replace("%", "\\%")
                    .replace("_", "\\_");
    }

    // Phương thức tìm kiếm user theo keyword (xử lý cả trường hợp ký tự đặc biệt và chuỗi rỗng)
    public List<ProductManagementDTO> searchProducts(String keyword) {
    	// Trả về trường hợp là null và chuỗi rồng
        if (keyword == null || keyword.trim().isEmpty()) {
        	// Nếu không nhập gì, có thể gọi repository để trả về toàn bộ user
            return productsRepository.searchProductsByKeyword(keyword);
        }
        // Escape ký tự đặc biệt để đảm bảo tìm kiếm chính xác
        String escapedKeyword = escapeSpecialChars(keyword.trim());
        return productsRepository.searchProductsByKeyword(escapedKeyword);
    }

	// Phương thức trả về khi nhấn button `search` ở trang productManagement
    // (sử dụng kết quả từ phương thức searchUsers đã được xử lý)
	public ProductDTO getProductsDTOByKeyword(String keyword) {
		
	    // Gọi phương thức `searchProducts` để tìm kiếm theo ký tự đặc biệt
	    List<ProductManagementDTO> productManagementDTOs = searchProducts(keyword);
	    
	    // Tạo đối tượng ProductDTO
	    ProductDTO productDTO = new ProductDTO();
		// Gán danh sách productManagementDTOs vào danh sách productManagementDTOs ở lớp ProductDTO
	    productDTO.setProductManagementDTOs(productManagementDTOs);
	    
	    return productDTO;
	}
	
	// Phương thức tìm kiếm thông tin bằng id
	public Optional<Products> findProductByID(Long id){
		// Trả về kết quả bằng phương thức có sẵn của spring boot
		return productsRepository.findById(id);
	}
	
	// Phương thức thực hiện việc cập nhập sản phẩm
	public String doUpdateProducts(ProductsForm productsForm, MultipartFile multipartFile) {
		try {
			// Tìm sản phẩm bằng ID
			Products products = productsRepository.findById(productsForm.getId()).orElse(null);
			// Trả về nếu là null
			if (products == null) {
			    return "error: Product not found!";
			}
			// Kiểm tra tên sản phẩm không được để trống
			if (productsForm.getProductName() == null || productsForm.getProductName().trim().isEmpty()) {
				return "error: Product name cannot be Emty!";
			}
			// Kiểm tra xem giá tiền có phải null không
			if (productsForm.getPrice() == null || productsForm.getPrice().trim().isEmpty()) {
			    return "error: Price cannot be empty!";
			}
			// Kiểm tra số lượng có hợp lệ không
			if (productsForm.getQuantity() < 1) {
				return "error: Quantity must be at least 1.";
			}
			
			// Kiểm tra giá tiền có hợp lệ không
			String priceString = productsForm.getPrice();
			// Kiểm tra xem có ký tự full-size không
			if (Pattern.compile("[\\uFF00-\\uFFFF]").matcher(priceString).find()) {
				return "error: Price must use half-size characters only.";
			}
			// Kiểm tra có ký tự đặc biệt không
			if (!priceString.matches("^[0-9]+(\\.[0-9]{1,2})?$")) {
				return "error: Price must contain only numeric characters.";
			}
			// Chuyển đổi từ String sang BigDecimal
			BigDecimal priceBigDecimal = new BigDecimal(priceString);
			// Kiểm tra giá trị tối thiểu
			if (priceBigDecimal.compareTo(BigDecimal.valueOf(1000)) < 0) {
				return "error: Price must be at least 1000.";
			}

			// Tạo đối tượng product, gán từ form vào entity rồi lưu vào database
			products.setId(productsForm.getId());
			products.setProductName(productsForm.getProductName());
			products.setPrice(new BigDecimal(productsForm.getPrice()));
			products.setType(productsForm.getType());
			products.setQuantity(productsForm.getQuantity());
			
			// Cập nhật ảnh nếu có ảnh mới
			if (!multipartFile.isEmpty()) {
			    long fileSize = multipartFile.getSize(); // Lấy kích thước file (bytes)
			    if (fileSize > 10 * 1024 * 1024) {  // 10MB
			        return "error: File size exceeds limit (10MB).";
			    }
			    // Tạo biến để lưu ảnh vào thư mục
			    String imagePath = imageService.saveFile(multipartFile);
			    // Gán từ form vào entity
			    products.setImagePath(imagePath);
			} else {
			    // Giữ nguyên đường dẫn ảnh cũ nếu không có ảnh mới
			    products.setImagePath(products.getImagePath());
			}

			productsRepository.save(products);
			// Trả về thông báo thành công với tên sản phẩm và loại sản phẩm
			return "success: " + products.getProductName() + " (" + products.getType() + ") has been updated successfully!";

		} catch (NumberFormatException e) {
			return "error: Price must be a valid number.";
		} catch (IOException e) {
			// Lỗi không upload được hình ảnh
			return "error: Failed to upload image.";
		}
	}
	
	// Phương thức xóa thông tin với id
	public void deleteInfoById (Long id) {
		productsRepository.deleteById(id);
	}
	
	// Phương thức lấy mô tả sản phẩm dựa theo id sản phẩm
	public String getProductDescription(Long id) {
		// Gọi phương thức từ productDetailRepository
		ProductDetail productDetail = productDetailRepository.findByProductId(id);
		// Dùng if-else rút gọn để trả về mô tả
		String product = (productDetail != null) ? productDetail.getDescription() : "Chưa có mô tả cho sản phẩm này";
		
		return product;		
	}
	
	// Phương thức để lưu hoặc cập nhập mô tả sản phẩm
	public void saveProductDescription(Long id, String description) {
		// Tìm kiếm sản phẩm bằng id
		ProductDetail productDetail = productDetailRepository.findByProductId(id);
		
		// Nếu trả về null, gọi đối tượng ProductDetail mới rồi gán id vào
		if (productDetail == null) {
			// Gán đối tượng mới của ProductDetail
			productDetail = new ProductDetail();
			// Gán id vào đối tượng mới
			productDetail.setProductId(id);
		}
		
		// Gán mô tả vào đối tượng ProductDetail
		productDetail.setDescription(description);
		// Lưu vào database
		productDetailRepository.save(productDetail);
	}
}
