package com.example.Aptech_Final.Form;

import java.time.LocalDate;

public class ScheduleBookingForm {

    private Long bookingId;
    private Long scheduleId;
    private Long userId;
    private int hour;
    private LocalDate date; 
    
    // Getter và setter
	public Long getBookingId() {
		return bookingId;
	}
	public void setBookingId(Long bookingId) {
		this.bookingId = bookingId;
	}
	public Long getScheduleId() {
		return scheduleId;
	}
	public void setScheduleId(Long scheduleId) {
		this.scheduleId = scheduleId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public int getHour() {
		return hour;
	}
	public void setHour(int hour) {
		this.hour = hour;
	}
	
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	// Constructor
	public ScheduleBookingForm(Long bookingId, Long scheduleId, Long userId, int hour, LocalDate date) {
		this.bookingId = bookingId;
		this.scheduleId = scheduleId;
		this.userId = userId;
		this.hour = hour;
		this.date = date;
	}
	
	public ScheduleBookingForm() {
	}

}

