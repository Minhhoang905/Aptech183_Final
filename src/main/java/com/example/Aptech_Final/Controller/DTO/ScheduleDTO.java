package com.example.Aptech_Final.Controller.DTO;

import java.time.LocalDate;

public interface ScheduleDTO {
    Long getScheduleId();          // ID lịch
    LocalDate getDate();           // Ngày tập
    Integer getHour();             // Giờ tập (5 - 20)
    Integer getPtCount();          // Số PT đã đăng ký
    Integer getUserCount();        // Số user đã đặt
    Boolean getBookedByCurrentUser(); // Người dùng hiện tại đã đặt chưa
    
    int getHour5();
    int getHour6();
    int getHour7();
    int getHour8();
    int getHour9();
    int getHour10();
    int getHour11();
    int getHour12();
    int getHour13();
    int getHour14();
    int getHour15();
    int getHour16();
    int getHour17();
    int getHour18();
    int getHour19();
    int getHour20();

}
