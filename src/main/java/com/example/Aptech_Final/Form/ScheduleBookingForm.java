package com.example.Aptech_Final.Form;

public class ScheduleBookingForm {

    private Long bookingId;
    private Long scheduleId;
    private Long userId;
    private int hour;
    
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
	
	// Constructor
	public ScheduleBookingForm(Long bookingId, Long scheduleId, Long userId, int hour) {
		this.bookingId = bookingId;
		this.scheduleId = scheduleId;
		this.userId = userId;
		this.hour = hour;
	}
	
	public ScheduleBookingForm() {
	}


}
