package com.example.Aptech_Final.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import io.jsonwebtoken.io.IOException;

@Service
public class ImageService {
	// Thư mục lưu ảnh trong project
    private static final String UPLOAD_DIR = "src/main/resources/static/images/"; 
    
    // Phương thức upload hình ảnh
    public String saveFile(MultipartFile multipartFile) throws java.io.IOException {
    	// Xét nếu file rỗng
    	if (multipartFile.isEmpty()) {
			throw new IOException("File is empty!");
		}
    	
    	// Tạo tên file duy nhất
    	String fileName = System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();
    	// Tạo đối tượng path chứa đường dẫn file khởi tạo
    	Path path = Paths.get(UPLOAD_DIR + fileName);
    	
    	// Kiểm tra và tạo thư mục (nếu chưa tồn tại thư mục cha)
    	Files.createDirectories(path.getParent());
    	
    	// Lưu file vào thư mực cha
    	Files.copy(multipartFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
    	
    	// Trả về đường dẫn để hiển thị ảnh
    	return "/images/" + fileName;
    }
}
