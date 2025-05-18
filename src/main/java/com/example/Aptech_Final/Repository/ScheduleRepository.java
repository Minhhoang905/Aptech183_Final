package com.example.Aptech_Final.Repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.Aptech_Final.Controller.DTO.ScheduleDTO;
import com.example.Aptech_Final.Enity.Schedule;


@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long>{
    // Dành cho PT hoặc ADMIN: lấy toàn bộ lịch
    @Query(value = """
        SELECT 
            s.schedule_id AS scheduleId,
            s.date AS date,
            s.hour_5 AS hour5,
            s.hour_6 AS hour6,
            s.hour_7 AS hour7,
            s.hour_8 AS hour8,
            s.hour_9 AS hour9,
            s.hour_10 AS hour10,
            s.hour_11 AS hour11,
            s.hour_12 AS hour12,
            s.hour_13 AS hour13,
            s.hour_14 AS hour14,
            s.hour_15 AS hour15,
            s.hour_16 AS hour16,
            s.hour_17 AS hour17,
            s.hour_18 AS hour18,
            s.hour_19 AS hour19,
            s.hour_20 AS hour20
        FROM schedule s
        WHERE s.date BETWEEN :startDate AND :endDate
        ORDER BY s.date
    """, nativeQuery = true)
    List<ScheduleDTO> findAllSchedulesForPTOrAdmin(@Param("startDate") LocalDate start, @Param("endDate") LocalDate end);

    // Dành cho USER: chỉ lấy các lịch có PT đăng ký (giá trị > 0)
    @Query(value = """
        SELECT 
            s.schedule_id AS scheduleId,
            s.date AS date,
            s.hour_5 AS hour5,
            s.hour_6 AS hour6,
            s.hour_7 AS hour7,
            s.hour_8 AS hour8,
            s.hour_9 AS hour9,
            s.hour_10 AS hour10,
            s.hour_11 AS hour11,
            s.hour_12 AS hour12,
            s.hour_13 AS hour13,
            s.hour_14 AS hour14,
            s.hour_15 AS hour15,
            s.hour_16 AS hour16,
            s.hour_17 AS hour17,
            s.hour_18 AS hour18,
            s.hour_19 AS hour19,
            s.hour_20 AS hour20
        FROM schedule s
        WHERE s.date BETWEEN :startDate AND :endDate
        AND (
            s.hour_5 > 0 OR s.hour_6 > 0 OR s.hour_7 > 0 OR s.hour_8 > 0 OR
            s.hour_9 > 0 OR s.hour_10 > 0 OR s.hour_11 > 0 OR s.hour_12 > 0 OR
            s.hour_13 > 0 OR s.hour_14 > 0 OR s.hour_15 > 0 OR s.hour_16 > 0 OR
            s.hour_17 > 0 OR s.hour_18 > 0 OR s.hour_19 > 0 OR s.hour_20 > 0
        )
        ORDER BY s.date
    """, nativeQuery = true)
    List<ScheduleDTO> findAllSchedulesForUser(@Param("startDate") LocalDate start, @Param("endDate") LocalDate end);

    // Bổ sung phương thức lấy userId theo scheduleId
    @Query(value = """
        SELECT SB.USER_ID 
        FROM SCHEDULE_BOOKING SB
        WHERE SB.SCHEDULE_ID = :scheduleId
        LIMIT 1
    """, nativeQuery = true)
    Long findUserIdByScheduleId(@Param("scheduleId") Long scheduleId);
    
	// Query để kiểm tra có tồn tại ngày trong DB không
    @Query(value = "SELECT COUNT(*) FROM SCHEDULE WHERE DATE = :date", nativeQuery = true)
    int existsByDate(@Param("date") LocalDate date);
    
    // Query để lấy scheduleId theo điều kiện date
    @Query(value = "SELECT SCHEDULE_ID FROM SCHEDULE WHERE DATE = :date", nativeQuery = true)
    Long getScheduleIdByDate(@Param("date") LocalDate date);

    // Query để insert ngày mới (tạo các khung giờ rỗng nếu chưa có trong DB)
    @Modifying
    @Query(value = "INSERT INTO SCHEDULE (DATE) VALUES (:date)", nativeQuery = true)
    void insertSchedule(@Param("date") LocalDate date);
    
    // Query để cộng giờ (dùng dynamite SQL cập nhập ngày có sẵn trong DB)
    @Modifying
    @Query(value = "UPDATE SCHEDULE SET " +
                   "HOUR_5 = CASE WHEN :hour = 5 THEN HOUR_5 + 1 ELSE HOUR_5 END, " +
                   "HOUR_6 = CASE WHEN :hour = 6 THEN HOUR_6 + 1 ELSE HOUR_6 END, " +
                   "HOUR_7 = CASE WHEN :hour = 7 THEN HOUR_7 + 1 ELSE HOUR_7 END, " +
                   "HOUR_8 = CASE WHEN :hour = 8 THEN HOUR_8 + 1 ELSE HOUR_8 END, " +
                   "HOUR_9 = CASE WHEN :hour = 9 THEN HOUR_9 + 1 ELSE HOUR_9 END, " +
                   "HOUR_10 = CASE WHEN :hour = 10 THEN HOUR_10 + 1 ELSE HOUR_10 END, " +
                   "HOUR_11 = CASE WHEN :hour = 11 THEN HOUR_11 + 1 ELSE HOUR_11 END, " +
                   "HOUR_12 = CASE WHEN :hour = 12 THEN HOUR_12 + 1 ELSE HOUR_12 END, " +
                   "HOUR_13 = CASE WHEN :hour = 13 THEN HOUR_13 + 1 ELSE HOUR_13 END, " +
                   "HOUR_14 = CASE WHEN :hour = 14 THEN HOUR_14 + 1 ELSE HOUR_14 END, " +
                   "HOUR_15 = CASE WHEN :hour = 15 THEN HOUR_15 + 1 ELSE HOUR_15 END, " +
                   "HOUR_16 = CASE WHEN :hour = 16 THEN HOUR_16 + 1 ELSE HOUR_16 END, " +
                   "HOUR_17 = CASE WHEN :hour = 17 THEN HOUR_17 + 1 ELSE HOUR_17 END, " +
                   "HOUR_18 = CASE WHEN :hour = 18 THEN HOUR_18 + 1 ELSE HOUR_18 END, " +
                   "HOUR_19 = CASE WHEN :hour = 19 THEN HOUR_19 + 1 ELSE HOUR_19 END, " +
                   "HOUR_20 = CASE WHEN :hour = 20 THEN HOUR_20 + 1 ELSE HOUR_20 END " +
                   "WHERE SCHEDULE_ID = :scheduleId", nativeQuery = true)
    void incrementHour(@Param("scheduleId") Long scheduleId, @Param("hour") Integer hour);

    // Query để trừ giờ (dùng dynamite SQL cập nhập ngày có sẵn trong DB)
    @Modifying
    @Query(value = "UPDATE SCHEDULE SET " +
                   "HOUR_5 = CASE WHEN :hour = 5 THEN HOUR_5 - 1 ELSE HOUR_5 END, " +
                   "HOUR_6 = CASE WHEN :hour = 6 THEN HOUR_6 - 1 ELSE HOUR_6 END, " +
                   "HOUR_7 = CASE WHEN :hour = 7 THEN HOUR_7 - 1 ELSE HOUR_7 END, " +
                   "HOUR_8 = CASE WHEN :hour = 8 THEN HOUR_8 - 1 ELSE HOUR_8 END, " +
                   "HOUR_9 = CASE WHEN :hour = 9 THEN HOUR_9 - 1 ELSE HOUR_9 END, " +
                   "HOUR_10 = CASE WHEN :hour = 10 THEN HOUR_10 - 1 ELSE HOUR_10 END, " +
                   "HOUR_11 = CASE WHEN :hour = 11 THEN HOUR_11 - 1 ELSE HOUR_11 END, " +
                   "HOUR_12 = CASE WHEN :hour = 12 THEN HOUR_12 - 1 ELSE HOUR_12 END, " +
                   "HOUR_13 = CASE WHEN :hour = 13 THEN HOUR_13 - 1 ELSE HOUR_13 END, " +
                   "HOUR_14 = CASE WHEN :hour = 14 THEN HOUR_14 - 1 ELSE HOUR_14 END, " +
                   "HOUR_15 = CASE WHEN :hour = 15 THEN HOUR_15 - 1 ELSE HOUR_15 END, " +
                   "HOUR_16 = CASE WHEN :hour = 16 THEN HOUR_16 - 1 ELSE HOUR_16 END, " +
                   "HOUR_17 = CASE WHEN :hour = 17 THEN HOUR_17 - 1 ELSE HOUR_17 END, " +
                   "HOUR_18 = CASE WHEN :hour = 18 THEN HOUR_18 - 1 ELSE HOUR_18 END, " +
                   "HOUR_19 = CASE WHEN :hour = 19 THEN HOUR_19 - 1 ELSE HOUR_19 END, " +
                   "HOUR_20 = CASE WHEN :hour = 20 THEN HOUR_20 - 1 ELSE HOUR_20 END " +
                   "WHERE SCHEDULE_ID = :scheduleId", nativeQuery = true)
    void decrementHour(@Param("scheduleId") Long scheduleId, @Param("hour") Integer hour, @Param("date") LocalDate date);

    
}