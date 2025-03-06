package com.example.Aptech_Final.Controller.DTO;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Interface DuLichResultDTO là một Data Transfer Object (DTO) dùng để truyền tải
 * các data cần thiết của đối tượng DuLich mà không cần toàn bộ entity
 * Interface này định nghĩa các phương thức getter cần có, giúp việc lấy data dễ dàng hơn.
 */

public interface UserManagementDTO {
	// Lấy các getter từ các table dựa theo câu lệnh query ở Repo
	// Lấy gettter của ID
	Long getId();
	// Lấy gettter của name	
	String getName();
	// Lấy getter của họ tên đầy đủ
	String getFullName();
	// Lấy getter của ngày tháng năm sinh
	LocalDate getDateOfBirth();
	// Lấy getter của email người dùng
	String getEmail();
	// Lấy getter của số điện thoại người dùng
	String getPhoneNumber();
	// Lấy getter của id tỉnh mà người dùng đã đăng ký
	String getProvinceName();
	// Lấy getter của id quận mà người dùng đã đăng ký
	String getDistrictName();
	// Lấy getter của id xã mà người dùng đã đăng ký
	String getWardName();
	// Lấy getter của số nhà cụ thể
	String getAddress();
	// Lấy getter của vai trò người dùng
	String getRole();
	
	// Format định dạng ngày sinh thành `dd/MM/yyyy` để hiển thị đúng trên HTML
    default String getFormattedDateOfBirth() {
        if (getDateOfBirth() == null) return "";
        return getDateOfBirth().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
}
