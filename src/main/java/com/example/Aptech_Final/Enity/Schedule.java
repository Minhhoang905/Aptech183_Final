package com.example.Aptech_Final.Enity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "SCHEDULE")
public class Schedule {
	
	// Id của buổi học
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SCHEDULE_ID")
	private Long scheduleId;
	    
    // Ngày của lịch
    @Column(name = "DATE", nullable = false)
    private LocalDate date;   
    
    // Khung giờ lúc 5h
    @Column(name = "HOUR_5", nullable = false, columnDefinition = "int default 0")
    private int hour5;
    
    // Khung giờ lúc 6h
    @Column(name = "HOUR_6", nullable = false, columnDefinition = "int default 0")
    private int hour6;

    // Khung giờ lúc 7h
    @Column(name = "HOUR_7", nullable = false, columnDefinition = "int default 0")
    private int hour7;
    
    // Khung giờ lúc 8h
    @Column(name = "HOUR_8", nullable = false, columnDefinition = "int default 0")
    private int hour8;
    
    // Khung giờ lúc 9h
    @Column(name = "HOUR_9", nullable = false, columnDefinition = "int default 0")
    private int hour9;
    
    // Khung giờ lúc 10h
    @Column(name = "HOUR_10", nullable = false, columnDefinition = "int default 0")
    private int hour10;
    
    // Khung giờ lúc 11h
    @Column(name = "HOUR_11", nullable = false, columnDefinition = "int default 0")
    private int hour11;
    
    // Khung giờ lúc 12h
    @Column(name = "HOUR_12", nullable = false, columnDefinition = "int default 0")
    private int hour12;
    
    // Khung giờ lúc 13h
    @Column(name = "HOUR_13", nullable = false, columnDefinition = "int default 0")
    private int hour13;
    
    // Khung giờ lúc 14h
    @Column(name = "HOUR_14", nullable = false, columnDefinition = "int default 0")
    private int hour14;
    
    // Khung giờ lúc 15h
    @Column(name = "HOUR_15", nullable = false, columnDefinition = "int default 0")
    private int hour15;
    
    // Khung giờ lúc 16h
    @Column(name = "HOUR_16", nullable = false, columnDefinition = "int default 0")
    private int hour16;
    
    // Khung giờ lúc 17h
    @Column(name = "HOUR_17", nullable = false, columnDefinition = "int default 0")
    private int hour17;
    
    // Khung giờ lúc 18h
    @Column(name = "HOUR_18", nullable = false, columnDefinition = "int default 0")
    private int hour18;
    
    // Khung giờ lúc 19h
    @Column(name = "HOUR_19", nullable = false, columnDefinition = "int default 0")
    private int hour19;
    
    // Khung giờ lúc 20h
    @Column(name = "HOUR_20", nullable = false, columnDefinition = "int default 0")
    private int hour20;
    
    // Getter và setter
	public Long getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(Long scheduleId) {
		this.scheduleId = scheduleId;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public int getHour5() {
		return hour5;
	}

	public void setHour5(int hour5) {
		this.hour5 = hour5;
	}

	public int getHour6() {
		return hour6;
	}

	public void setHour6(int hour6) {
		this.hour6 = hour6;
	}

	public int getHour7() {
		return hour7;
	}

	public void setHour7(int hour7) {
		this.hour7 = hour7;
	}

	public int getHour8() {
		return hour8;
	}

	public void setHour8(int hour8) {
		this.hour8 = hour8;
	}

	public int getHour9() {
		return hour9;
	}

	public void setHour9(int hour9) {
		this.hour9 = hour9;
	}

	public int getHour10() {
		return hour10;
	}

	public void setHour10(int hour10) {
		this.hour10 = hour10;
	}

	public int getHour11() {
		return hour11;
	}

	public void setHour11(int hour11) {
		this.hour11 = hour11;
	}

	public int getHour12() {
		return hour12;
	}

	public void setHour12(int hour12) {
		this.hour12 = hour12;
	}

	public int getHour13() {
		return hour13;
	}

	public void setHour13(int hour13) {
		this.hour13 = hour13;
	}

	public int getHour14() {
		return hour14;
	}

	public void setHour14(int hour14) {
		this.hour14 = hour14;
	}

	public int getHour15() {
		return hour15;
	}

	public void setHour15(int hour15) {
		this.hour15 = hour15;
	}

	public int getHour16() {
		return hour16;
	}

	public void setHour16(int hour16) {
		this.hour16 = hour16;
	}

	public int getHour17() {
		return hour17;
	}

	public void setHour17(int hour17) {
		this.hour17 = hour17;
	}

	public int getHour18() {
		return hour18;
	}

	public void setHour18(int hour18) {
		this.hour18 = hour18;
	}

	public int getHour19() {
		return hour19;
	}

	public void setHour19(int hour19) {
		this.hour19 = hour19;
	}

	public int getHour20() {
		return hour20;
	}

	public void setHour20(int hour20) {
		this.hour20 = hour20;
	}
 
}
