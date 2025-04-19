package com.example.Aptech_Final.Form;

import java.time.LocalDateTime;

public class ScheduleForm {
	// Id của lịch tập
	private Long scheduleId;
	// Id của người dùng
    private Long userId; 
    // Thời gian bắt đầu của slot
    private LocalDateTime startTime;
    // Thời gian kết thúc của slot
    private LocalDateTime endTime;
    // Số lượng PT đã tạo cho slot này
    private Integer ptCount;
    // Số lượng User đã đăng ký cho slot này
    private Integer userCount;
    // Ngày của slot, định dạng yyyy-MM-dd
    private String date;
    // Giờ của slot (ví dụ: 5-20)
    private int hour;
    // Boolean xác nhận PT hiện tại đã đăng ký slot này hay chưa
    private Integer ptBooked;
    // Boolean xác nhận User hiện tại đã đăng ký slot này hay chưa
    private Integer userBooked;
    
    private Integer ptRegisteredByUser;

    // Getter và setter

}
