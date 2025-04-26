package com.example.Aptech_Final.Enity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "SCHEDULE_BOOKING")
public class ScheduleBooking {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "BOOKING_ID")
	private Long bookingId;
	
	// Khóa ngoại tham chiếu đến table SCHEDULE 
	@Column(name = "SCHEDULE_ID", nullable = false)
	private Long scheduleId;
	
	// Khóa ngoại tham chiếu đến table USERS
	@Column(name = "USER_ID", nullable = false)
	private Long userId;
	
	// Khung giờ mà người dùng đăng ký
	@Column(name = "HOUR", nullable = false)
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
	
}
