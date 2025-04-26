package com.example.Aptech_Final.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.Aptech_Final.Controller.DTO.ScheduleBookingDTO;
import com.example.Aptech_Final.Enity.ScheduleBooking;

@Repository
public interface ScheduleBookingRepository extends JpaRepository<ScheduleBooking, Long>{

    // Query trả về slot đã có PT đăng ký
    @Query(value = """
        SELECT 
            SB.BOOKING_ID AS bookingId,
            SB.SCHEDULE_ID AS scheduleId,
            SB.USER_ID AS userId,
            SB.HOUR AS hour
        FROM SCHEDULE_BOOKING SB
        LEFT JOIN USERS U ON SB.USER_ID = U.USER_ID
        WHERE SB.SCHEDULE_ID = :scheduleId AND SB.HOUR = :hour AND U.USER_ROLE = 'PT'
        """, nativeQuery = true)
    List<ScheduleBookingDTO> findPTBookingsInSlot(@Param("scheduleId") Long scheduleId, @Param("hour") int hour);

    // Query trả về slot đã có USER đăng ký
    @Query(value = """
        SELECT 
            SB.BOOKING_ID AS bookingId,
            SB.SCHEDULE_ID AS scheduleId,
            SB.USER_ID AS userId,
            SB.HOUR AS hour
        FROM SCHEDULE_BOOKING SB
        LEFT JOIN USERS U ON SB.USER_ID = U.USER_ID
        WHERE SB.SCHEDULE_ID = :scheduleId AND SB.HOUR = :hour AND U.USER_ROLE = 'USER'
        """, nativeQuery = true)
    List<ScheduleBookingDTO> findUserBookingsInSlot(@Param("scheduleId") Long scheduleId, @Param("hour") int hour);

    // Query tính tổng số PT đã đặt 1 khung giờ
    @Query(value = """
        SELECT COUNT(*) 
        FROM SCHEDULE_BOOKING SB
        LEFT JOIN USERS U ON SB.USER_ID = U.USER_ID
        WHERE SB.SCHEDULE_ID = :scheduleId AND SB.HOUR = :hour AND U.USER_ROLE = 'PT'
        """, nativeQuery = true)
    int countPTInSlot(@Param("scheduleId") Long scheduleId, @Param("hour") int hour);

    // Query tính tổng số USER đã đặt 1 khung giờ
    @Query(value = """
        SELECT COUNT(*) 
        FROM SCHEDULE_BOOKING SB
        LEFT JOIN USERS U ON SB.USER_ID = U.USER_ID
        WHERE SB.SCHEDULE_ID = :scheduleId AND SB.HOUR = :hour AND U.USER_ROLE = 'USER'
        """, nativeQuery = true)
    int countUserInSlot(@Param("scheduleId") Long scheduleId, @Param("hour") int hour);
    
    // Bổ sung phương thức lấy tất cả các booking cho một scheduleId
    @Query(value = """
    	    SELECT SB.BOOKING_ID AS bookingId, SB.SCHEDULE_ID AS scheduleId, SB.USER_ID AS userId, SB.HOUR AS hour
    	    FROM SCHEDULE_BOOKING SB
    	    WHERE SB.SCHEDULE_ID = :scheduleId
    	""", nativeQuery = true)
    	List<ScheduleBookingDTO> findAllBookingsByScheduleId(@Param("scheduleId") Long scheduleId);

    @Query(value = """
    	    SELECT CASE WHEN EXISTS (
    	        SELECT 1
    	        FROM SCHEDULE_BOOKING SB
    	        WHERE SB.SCHEDULE_ID = :scheduleId AND SB.USER_ID = :userId AND SB.HOUR = :hour
    	    ) THEN 1 ELSE 0 END
    	    """, nativeQuery = true)
    	int isUserBookedForSlot(@Param("scheduleId") Long scheduleId, @Param("userId") Long userId, @Param("hour") int hour);

}
