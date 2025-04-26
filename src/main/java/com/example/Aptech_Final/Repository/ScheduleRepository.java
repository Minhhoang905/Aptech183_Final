package com.example.Aptech_Final.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

}
