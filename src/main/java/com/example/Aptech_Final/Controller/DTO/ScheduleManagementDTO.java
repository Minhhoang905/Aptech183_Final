package com.example.Aptech_Final.Controller.DTO;

import java.time.LocalDate;

public interface ScheduleManagementDTO {
    LocalDate getDate();   
    Integer getHour();     
    String getFullName();  
    String getUserRole();
    String getUserPhoneNumber();

}
