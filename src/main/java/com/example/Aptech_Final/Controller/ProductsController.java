package com.example.Aptech_Final.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.Aptech_Final.Controller.DTO.ProductDTO;
import com.example.Aptech_Final.Enity.Products;
import com.example.Aptech_Final.Form.ProductsForm;
import com.example.Aptech_Final.Repository.ProductsRepository;
import com.example.Aptech_Final.Service.HomeService;
import com.example.Aptech_Final.Service.ProductsService;

import org.springframework.ui.Model;

@Controller
@RequestMapping("/products")
public class ProductsController {
    @Autowired
    private ProductsRepository productsRepository;
    @Autowired
    private ProductsService productsService;
    @Autowired
    private HomeService homeService;
    // Phương thức hiển thị danh sách sản phẩm Supplements
    @GetMapping("/supplements")
    public String getSupplement(Model model, @AuthenticationPrincipal UserDetails userDetails) {
    	// Lấy sản phẩm từ DB
        List<Products> supplements = productsRepository.findByType("supplements"); 
    	// Gọi phương thức xác định vai trò của user từ @Service
    	String role = homeService.getCurrentUserRole();
		// Thêm thông tin về role vào form ở html
		model.addAttribute("role", role);
        model.addAttribute("products", supplements);
        return "supplements"; 
    }

    // Phương thức hiển thị danh sách sản phẩm Gears
    @GetMapping("/gears")
    public String getGears(Model model) {
    	// Gọi phương thức xác định vai trò của user từ @Service
    	String role = homeService.getCurrentUserRole();
		// Thêm thông tin về role vào form ở html
		model.addAttribute("role", role);

        List<Products> gears = productsRepository.findByType("gears"); // Lấy sản phẩm từ DB
        model.addAttribute("products", gears);
        return "gears";
    }
        
    // Phương thức để vào trang thêm sản phẩm
    @GetMapping("/addProduct")
    public String addProductPage(Model model) {
    	// Gọi phương thức xác định vai trò của user từ @Service
    	String role = homeService.getCurrentUserRole();
		// Thêm thông tin về role vào form ở html
		model.addAttribute("role", role);

		// Đối tượng rỗng để binding với th:object ở form
		model.addAttribute("productForm", new ProductsForm());
	
        return "add-product";
    }
    
    // Phương thức để lưu sản phẩm vào db
    @PostMapping("/saveProduct")
    public String saveProduct(@ModelAttribute("productForm")ProductsForm productsForm, Model model, RedirectAttributes redirectAttributes, @RequestParam("imagePath") MultipartFile imageFile) {
    	// Gọi phương thức xác định vai trò của user từ @Service
    	String role = homeService.getCurrentUserRole();
		// Thêm thông tin về role vào form ở html
		model.addAttribute("role", role);

    	// Đối tượng rỗng để binding với th:object ở form
		model.addAttribute("productForm", new ProductsForm());
		
    	String result = productsService.saveProduct(productsForm, imageFile);
        
        // Nếu có lỗi, redirect về trang add-product với thông báo lỗi
        if (result.startsWith("error: ")) {
        	redirectAttributes.addFlashAttribute("errorMessage", result.substring(6));
            return "redirect:/products/addProduct";
        }
        // Nếu thành công, redirect về trang danh sách sản phẩm theo loại
        redirectAttributes.addFlashAttribute("successMessage", result.substring(8));
        return "redirect:/products/addProduct";
    }
    
	// Phương thức để xử lý yêu cầu GET cho đường dẫn `product management`
    @GetMapping("/productManagement")
    public String getProductList(Model model, @ModelAttribute("productManagementObject") Products products) {
		// Gọi phương thức `getProductDTO ở @service để lấy dữ liệu cần thiết cho @controller
    	ProductDTO productDTO = productsService.getProductDTO(products);
    	// Gọi phương thức xác định vai trò của user (là admin) từ @Service
    	String role = homeService.getCurrentUserRole();
		// Thêm thông tin về role vào form ở html
		model.addAttribute("role", role);
		// Thêm đối tượng rỗng để binding với th:object ở form
    	model.addAttribute("productManagementObject", new Products());
		// Thêm  danh sách kết quả tìm kiếm ở `ProductDTO` vào model
    	model.addAttribute("productResults", productDTO.getProductManagementDTOs());
    	
		// Trả về tên file template Thymeleaf trong back-end (productManagement.html)			
    	return "productManagement";
    }
      
	// Phương thức để tìm kiếm thông tin ở đường dẫn `productManagement`
    @PostMapping("/searchProductInformation")
    public String searchProducts(@ModelAttribute("productManagementObject") Products products, @RequestParam(value = "keyword", required = false) String keyword, Model model) {
        // Gọi service để lấy productDTO dựa trên keyword
    	ProductDTO productDTO = productsService.getProductsDTOByKeyword(keyword);
    	// Gọi phương thức xác định vai trò của user từ @Service
    	String role = homeService.getCurrentUserRole();
		// Thêm thông tin về role vào form ở html
		model.addAttribute("role", role);
		// Thêm đối tượng rỗng để binding với th:object ở form
    	model.addAttribute("productManagementObject", new Products());
        // Đưa danh sách kết quả (List<productManagementDTO>) vào model
    	model.addAttribute("productResults", productDTO.getProductManagementDTOs());
    	
		// Trả về tên file template Thymeleaf trong back-end (productManagement.html)			
    	return "productManagement";
    }
    
	// Phương thức xử lý yêu cầu GET cho đường dẫn "/products/updateProduct"
    @GetMapping("/updateProduct")
    public String getUpdateProducts(Model model, @RequestParam("id") Long id) {
    	// Gọi phương thức xác định vai trò của user (là admin) từ @Service
    	String role = homeService.getCurrentUserRole();
		// Thêm thông tin về role vào form ở html
		model.addAttribute("role", role);
		// Lấy đối tượng Product để cập nhật (gọi từ service)
		Products product = productsService.findProductByID(id).orElse(new Products());
		// Thêm đối tượng để binding với th:object ở form
		model.addAttribute("productUpdate", product);
		
		return "updateProducts";
    }
    
	// Phương thức thực hiện cập nhập thông tin theo id
    @PostMapping("/doUpdateProducts")
    public String doUpdateProducts(@ModelAttribute("productUpdate")ProductsForm productsForm, Model model, RedirectAttributes redirectAttributes, @RequestParam("imagePath") MultipartFile imageFile) {
    	// Gọi phương thức xác định vai trò của user từ @Service
    	String role = homeService.getCurrentUserRole();
		// Thêm thông tin về role vào form ở html
		model.addAttribute("role", role);

    	// Đối tượng rỗng để binding với th:object ở form
		model.addAttribute("productForm", new ProductsForm());
		
    	String result = productsService.doUpdateProducts(productsForm, imageFile);
        
        // Nếu có lỗi, redirect về trang add-product với thông báo lỗi
        if (result.startsWith("error: ")) {
        	
        	// Thêm thông báo vào Flash Attribute để hiển thị sau khi chuyển hướng
        	redirectAttributes.addFlashAttribute("errorMessage", result.substring(6));
        	//Giữ nguyên đối tượng để Thymeleaf có thể hiển thị lại giá trị đã nhập
        	model.addAttribute("productManagementObject", new Products());
        	
            return "redirect:/products/updateProducts?id=" + productsForm.getId();
        }else {
            // Nếu thành công, redirect về trang danh sách sản phẩm theo loại
            redirectAttributes.addFlashAttribute("successMessage", result.substring(8));
            return "redirect:/products/productManagement";       	
        }
    }
    
	// Phương thức xóa thông tin người dùng bằng id
	@GetMapping("/deleteProduct")
	public String deleteInfoById(@RequestParam(value = "id") Long id, Model model) {
		// Gọi phương thức xác định vai trò của user (là admin) từ @Service
		String role = homeService.getCurrentUserRole();
		// Thêm thông tin về role vào form ở html
		model.addAttribute("role", role);		
		// Xóa thông tin cần xóa bằng method ở service 
		productsService.deleteInfoById(id);
		
		// Trả về html `userManagement`
		return "redirect:/products/productManagement";
	}
	
	// Phương thức để đi vào trang chi tiết sản phẩm
    @GetMapping("/detailProduct")
    public String getProductDetail(Model model, @RequestParam("id") Long id) {
        // Gọi phương thức xác định vai trò của user (là admin) từ @Service
        String role = homeService.getCurrentUserRole();
        model.addAttribute("role", role);
        
        // Lấy đối tượng Product để cập nhật (gọi từ service)
        Products product = productsService.findProductByID(id).orElse(new Products());
        model.addAttribute("product", product);
        
        // Lấy mô tả sản phẩm từ bảng PRODUCT_DETAIL qua ProductDetailService
        String description = productsService.getProductDescription(id);
        model.addAttribute("description", description);
		
        return "productDetail";
    }
    
    @PostMapping("/saveDescription")
    public String saveProductDescription(Model model, @RequestParam("id") Long id,
                                         @RequestParam("description") String description) {
        // Gọi phương thức xác định vai trò của user (là admin) từ @Service
        String role = homeService.getCurrentUserRole();
        model.addAttribute("role", role);
    	// Gọi service để lưu hoặc cập nhật mô tả sản phẩm
    	productsService.saveProductDescription(id, description);
        // Chuyển hướng trở lại trang chi tiết sản phẩm
        return "redirect:/products/detailProduct?id=" + id;
    }

}
