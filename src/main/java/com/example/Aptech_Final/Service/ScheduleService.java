package com.example.Aptech_Final.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Aptech_Final.Controller.DTO.ScheduleDTO;
import com.example.Aptech_Final.Controller.DTO.ScheduleManagementDTO;
import com.example.Aptech_Final.Controller.DTO.SlotInfo;
import com.example.Aptech_Final.Form.ScheduleBookingForm;
import com.example.Aptech_Final.Form.ScheduleForm;
import com.example.Aptech_Final.Repository.ScheduleBookingRepository;
import com.example.Aptech_Final.Repository.ScheduleRepository;

import jakarta.transaction.Transactional;


@Service
public class ScheduleService {
	@Autowired
	private ScheduleRepository scheduleRepository;
	@Autowired
	private ScheduleBookingRepository scheduleBookingRepository;

	// Phương thức để hiển thị trên view của người dùng
	public Map<LocalDate, List<ScheduleForm>> getScheduleByUserRole(Long userId, String role) {
	    // B0: Lấy lịch của ngày hôm nay
	    LocalDate startDate = LocalDate.now();
	    // Lấy lịch của 6 ngày tiếp theo
	    LocalDate endDate = startDate.plusDays(6);

	    // B1: Tạo 1 map bao gồm key là ngày và data là danh sách lấy từ database
	    Map<LocalDate, List<ScheduleForm>> resultMapByList = new LinkedHashMap<>();

	    // B2: Tạo danh sách lấy từ scheduleDTO
	    List<ScheduleDTO> scheduleDTOs;

	    // B3: Tạo if-else check điều kiện theo role và gọi query ứng theo kết quả
	    if ("ROLE_PT".equals(role) || "ROLE_ADMIN".equals(role)) {
	        // PT/ADMIN: lấy tất cả các lịch, bao gồm cả slot trống và đã có PT đăng ký
	        scheduleDTOs = scheduleRepository.findAllSchedulesForPTOrAdmin(startDate, endDate);
	    } else {
	        // USER: chỉ lấy lịch của những slot đã có PT đăng ký
	        scheduleDTOs = scheduleRepository.findAllSchedulesForUser(startDate, endDate);
	    }

	    // B4: Tạo map cho các ngày từ startDate đến endDate, hết 1 vòng lặp thì + 1 date 
	    for (LocalDate date = startDate; date.isBefore(endDate.plusDays(1)); date = date.plusDays(1)) {
	    	// Thêm vào map ở B1 với key là date hiện tại, value là 1 arraylist rỗng để chứa data
	    	resultMapByList.put(date, new ArrayList<>());
	    }
	    
	    // B5: Duyệt qua các thông tin DTO đã lấy từ scheduleDTOs (database)
	    for (ScheduleDTO dto : scheduleDTOs) {
	        // Gọi phương thức từ class ScheduleForm rồi gán id và ngày tập vào.
	        ScheduleForm obj = ScheduleForm.convertDTOIntoForm(dto);

	        // Tạo map chứa thông tin PT và User đã đăng ký cho từng giờ trong 1 ngày
	        Map<Integer, SlotInfo> hourMap = new LinkedHashMap<>();
	        
	        // Tạo vòng lặp từ 5h -> 20h, hết 1 vòng lặp + 1h
	        for (int hour = 5; hour <= 20; hour++) {
	            // Đếm số lượng PT đã đăng ký ở từng khung giờ (gọi từ ScheduleBookingRepository)
	            int ptCount = scheduleBookingRepository.countPTInSlot(dto.getScheduleId(), hour);
	            int userCount = scheduleBookingRepository.countUserInSlot(dto.getScheduleId(), hour);

	            // Gọi đối tượng SlotInfo và gán cho từng khung giờ
	            SlotInfo slotInfo = new SlotInfo();
	            slotInfo.setPtCount(ptCount);
	            slotInfo.setUserCount(userCount);
	            slotInfo.setBookedByCurrentUser(false); // <= Mặc định là chưa đặt

	            // If-else để kiểm tra xem người dùng đã đăng ký cho slot này chưa
	            if (userId != null) {
	            	// Gọi scheduleBookingRepository để thực hiện kiểm tra xem userId đã đặt lịch tâp chưa
	                int booked = scheduleBookingRepository.isUserBookedForSlot(dto.getScheduleId(), userId, hour);
	                
	                // Tạo 1 biến boolean
	                boolean isBooked;
	                // Tạo if-else để kiểm tra xem giá trị từ repository trả về true/false
	                if (booked == 1) {
						isBooked = true;
					} else {
						isBooked = false;
					}
	                
	                // Gán kết quả boolean vào đối tượng SlotInfo
	                slotInfo.setBookedByCurrentUser(isBooked);

	            }

	            // Thêm SlotInfo vào map theo khung giờ
	            hourMap.put(hour, slotInfo);
	        }

	        // Gán map giờ (hourMap) vào đối tượng ScheduleForm
	        obj.setHours(hourMap);
	        
	        // Tạo biến date để lấy ngày từ đối tượng ScheduleForm 
	        LocalDate date = obj.getDate();
	        // Thêm list ở ScheduleForm vào list của ngày tương ứng trong map ở B1.
	        List<ScheduleForm> dailyForms = resultMapByList.get(date);
	        
	        // Tạo if-else kiểm tra xem list của ScheduleForm có bị rỗng không
	        if (dailyForms.isEmpty()) {
	            // Add thêm ScheduleForm nếu như không có ScheduleForm cho ngày đã get ở ScheduleForm 
	            dailyForms.add(obj);
	        } else {
	            // Tạo đối tượng ScheduleForm mới, lấy thông tin tin đầu tiên trong List<ScheduleForm>.
	            ScheduleForm existingList = dailyForms.get(0);
	            // Tạo map chứa thông tin giờ get từ đối tượng mới tạo 
	            // (key là số giờ, value là lớp SlotInfo chứa thông tin của khung giờ đó)
	            Map<Integer, SlotInfo> mergedHourMap = existingList.getHours();
	            
	            // Tạo for để duyệt qua các khung giờ trong obj, và thêm vào mergedHourMap nếu giờ đó chưa tồn tại
	            for (Map.Entry<Integer, SlotInfo> entryMap : obj.getHours().entrySet()) {
	            	// Thêm khung giờ vào map mergedHourMap nếu như chưa tồn tại khung giờ đó.
	            	// Dùng putIfAbsent để tránh ghi đè key - value đã tồn tại.
	            	mergedHourMap.putIfAbsent(entryMap.getKey(), entryMap.getValue());
	            }
	            existingList.setHours(mergedHourMap);
	        }
	    }

	    // B6: Thêm SlotInfo mặc định cho ADMIN và PT nếu ngày không có dữ liệu
	    if ("ROLE_PT".equals(role) || "ROLE_ADMIN".equals(role)) {
	    	// Dùng for để lặp qua các ngày trong map ở B1
	        for (LocalDate date : resultMapByList.keySet()) {
	        	// Tạo list của ScheduleForm chứa các ngày đã lấy ở Map ở B1.
	            List<ScheduleForm> forms = resultMapByList.get(date);
	            
	            // If-else kiểm tra xem list đó có bị rỗng không
	            if (forms.isEmpty()) {
	            	// Nếu bị rỗng, tạo thêm 1 đối tượng ScheduleForm mới
	                ScheduleForm emptyForm = new ScheduleForm();
	                // Set ngày đã lấy từ resultMapByList để gán vào đối tượng mới
	                emptyForm.setDate(date);
	                
	                // Tạo 1 map mới (key là số giờ, value là thông tin từ lớp SlotInfo
	                Map<Integer, SlotInfo> hours = new LinkedHashMap<>();
	                
	                // Tạo for để lặp từ 5h -> 20h
	                for (int hour = 5; hour <= 20; hour++) {
	                	// Tạo đối tượng SlotInfo mới
	                    SlotInfo slotInfo = new SlotInfo();
	                    // Gán số lượng PT/User là 0, boolean check user đã book là false (mặc định)
	                    slotInfo.setPtCount(0);
	                    slotInfo.setUserCount(0);
	                    slotInfo.setBookedByCurrentUser(false);
	                    // Gán giờ và thông tin ở SlotInfo vào map đã tạo ở trên
	                    hours.put(hour, slotInfo);
	                }
	                
	                // Gán giờ ở Map<Integer, SlotInfo> vào đối tượng ScheduleForm đã tạo ở trên
	                emptyForm.setHours(hours);
	                // Sau đó, add đối tượng ScheduleForm đã tạo ở trên vào lại List<ScheduleForm>
	                forms.add(emptyForm);
	            }
	        }
	    }

	    // B7: Nếu là USER, lọc các khung giờ có ptCount > 0 và chưa đầy (ptCount > userCount)
	    if ("ROLE_USER".equals(role)) {
	    	// Dùng for để lặp qua các ngày trong map ở B1
	        for (Map.Entry<LocalDate, List<ScheduleForm>> entry : resultMapByList.entrySet()) {
	        	// Tạo list ở ScheduleForm chứa các giá trị ở Map.Entry<LocalDate, List<ScheduleForm>> 
	            List<ScheduleForm> forms = entry.getValue();
	            
	            // Tạo đối tượng ScheduleForm mới, dùng for để lặp qua List<ScheduleForm> ở trên. 
	            for (ScheduleForm form : forms) {
	            	// Tạo map (key là số giờ, value là giá trị ở lớp SlotInfor) chứa số giờ đã lấy ở đối tượng ScheduleForm
	                Map<Integer, SlotInfo> originalHours = form.getHours();
	                // Tạo map mới (key là số giờ, value là giá trị ở lớp SlotInfor) để chưa giờ hợp lệ
	                // Note: Dùng LinkedHashMap để lấy số giờ theo thứ tự
	                Map<Integer, SlotInfo> filtered = new LinkedHashMap<>();
	                
	                // Tạo mới Map.Entry<Integer, SlotInfo>, dùng for để lặp qua từng mục trong originalHours
	                for (Map.Entry<Integer, SlotInfo> hourEntry : originalHours.entrySet()) {
	                	// Tạo đối tượng SlotInfo chứa các giá trị ở  Map.Entry<Integer, SlotInfo>
	                    SlotInfo info = hourEntry.getValue();
	                    
	                    /* Tạo điều kiện để hiển thị:
	                     * - Khung giờ có PT đăng ký (ptCount > 0 và ptCount > userCount)
	                     * - Khung giờ mà bản thân user đó đã đăng ký (boolean bookedByCurrentUser = true)
	                     */ 
	                    if ((info.getPtCount() > 0 && info.getPtCount() > info.getUserCount()) || info.isBookedByCurrentUser()) {
	                        // Put key (key lấy từ vòng lặp Map.Entry<Integer, SlotInfo>), value (lấy từ SlotInfo ở trên vào map rỗng.
	                    	filtered.put(hourEntry.getKey(), info);
	                    }
	                }
	                // Set khung giờ (ở map mới) vào đối tượng ScheduleForm ở vòng lặp.
	                form.setHours(filtered);
	            }
	        }
	    }

	    // B8: Trả về map đã có dữ liệu
	    return resultMapByList;
	}
	

	// Phương thức để lưu thông tin đặt lịch vào trong database 
	@Transactional
	public String bookSchedule(ScheduleBookingForm bookingForm, Long userId) {
		
		// Lấy thông tin user hiện tại
	    Long scheduleId = bookingForm.getScheduleId();
	    Integer hour = bookingForm.getHour();
	    LocalDate date = bookingForm.getDate();

		// Tạo if-else để kiểm tra xem có sẵn scheduleId không 
	    if (scheduleId == null) {
			// Gọi query để kiểm tra xem có ngày đó trong DB không
		    int dateExisted = scheduleRepository.existsByDate(date);	    
		    if (dateExisted == 0) {
		    	// Nếu bằng 0 thì insert ngày đó vào DB
		        scheduleRepository.insertSchedule(date);
		    }
		    // Dùng query để get scheduleId mới sau khi đã insert vào database
		    scheduleId = scheduleRepository.getScheduleIdByDate(date);
		}

		// Kiểm tra xem người dùng đã đặt slot này chưa
		int isUserExists = scheduleBookingRepository.existsByScheduleIdAndUserIdAndHour(scheduleId, userId, hour);
		// Trả về lỗi nếu đã có user đặt slot này
		if (isUserExists  == 1) {
			return "error: You have already scheduled this time slot!";
		}
				
		// Gọi query ở ScheduleRepo để cập nhập số lượng (tăng) người dùng ở bảng SCHEDULE
		scheduleRepository.incrementHour(scheduleId, hour);
		
		// Gọi query ở ScheduleBookingRepo để lưu thông tin vào bảng SCHEDULE_BOOKING
		scheduleBookingRepository.insertBooking(scheduleId, userId, hour);
		
		return "success: Register successful!";
	}
	
	// Phương thức để xóa thông tin đặt lịch
	@Transactional
	public String cancelSchedule(ScheduleBookingForm bookingForm, Long userId) {
		
	    Long scheduleId = bookingForm.getScheduleId();
	    Integer hour = bookingForm.getHour();
	    LocalDate date = bookingForm.getDate();

		// Gọi query để xóa giờ ra khỏi table SCHEDULE
		scheduleRepository.decrementHour(scheduleId, hour, date);
		
		// Gọi query để xóa thông tin người dùng đã đặt ra khỏi table SCHEDULE_BOOKING
		scheduleBookingRepository.cancelBooking(scheduleId, userId, hour);
		
		return "success: Cancel slot successfully!";
	}
	
	// Phương thức để hiển thị toàn bộ người dùng đang đặt lịch
	public List<ScheduleManagementDTO> getAllUserBookings() {
		return scheduleBookingRepository.getAllScheduleUserBookings();
	}
}

