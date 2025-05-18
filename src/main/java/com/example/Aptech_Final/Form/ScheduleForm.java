package com.example.Aptech_Final.Form;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import com.example.Aptech_Final.Controller.DTO.ScheduleDTO;
import com.example.Aptech_Final.Controller.DTO.SlotInfo;


public class ScheduleForm {
	
	// Id của buổi học
	private Long scheduleId;
	    
    // Ngày của lịch
    private LocalDate date;   
    
    // Tạo map giờ và số lượng user
    private Map<Integer, SlotInfo> hours = new HashMap<>();
    
    // Phương thức static để chuyển đổi data từ ScheduleDTO -> ScheduleForm (gán giá trị các khung giờ, đảm bảo k có null)
    // Dùng static để gọi trực tiếp phương thức này, không cần phải tạo mới đối tượng ScheduleForm
    public static ScheduleForm convertDTOIntoForm(ScheduleDTO dto) {
        ScheduleForm form = new ScheduleForm();

        form.setScheduleId(dto.getScheduleId());
        form.setDate(dto.getDate());

        // Nếu map đã có thì lấy ra, chưa có thì tạo mới
        Map<Integer, SlotInfo> hourMap = form.getHours();
        
        // Tạo SlotInfo cho giờ cụ thể
        for (int hour = 5; hour <= 20; hour++) {
            SlotInfo slot = new SlotInfo();
            
            // Truy xuất các giá trị giờ từ getHourX() trực tiếp
            Integer ptCount = null;
            Integer userCount = null;
            switch (hour) {
                case 5:
                    ptCount = dto.getHour5();
                    userCount = dto.getHour5();
                    break;
                case 6:
                    ptCount = dto.getHour6();
                    userCount = dto.getHour6();
                    break;
                case 7:
                    ptCount = dto.getHour7();
                    userCount = dto.getHour7();
                    break;
                case 8:
                    ptCount = dto.getHour8();
                    userCount = dto.getHour8();
                    break;
                case 9:
                    ptCount = dto.getHour9();
                    userCount = dto.getHour9();
                    break;
                case 10:
                    ptCount = dto.getHour10();
                    userCount = dto.getHour10();
                    break;
                case 11:
                    ptCount = dto.getHour11();
                    userCount = dto.getHour11();
                    break;
                case 12:
                    ptCount = dto.getHour12();
                    userCount = dto.getHour12();
                    break;
                case 13:
                    ptCount = dto.getHour13();
                    userCount = dto.getHour13();
                    break;
                case 14:
                    ptCount = dto.getHour14();
                    userCount = dto.getHour14();
                    break;
                case 15:
                    ptCount = dto.getHour15();
                    userCount = dto.getHour15();
                    break;
                case 16:
                    ptCount = dto.getHour16();
                    userCount = dto.getHour16();
                    break;
                case 17:
                    ptCount = dto.getHour17();
                    userCount = dto.getHour17();
                    break;
                case 18:
                    ptCount = dto.getHour18();
                    userCount = dto.getHour18();
                    break;
                case 19:
                    ptCount = dto.getHour19();
                    userCount = dto.getHour19();
                    break;
                case 20:
                    ptCount = dto.getHour20();
                    userCount = dto.getHour20();
                    break;
            }

            slot.setPtCount(ptCount != null ? ptCount : 0);
            slot.setUserCount(userCount != null ? userCount : 0);
            slot.setBookedByCurrentUser(Boolean.TRUE.equals(dto.getBookedByCurrentUser()));

            hourMap.put(hour, slot);
        }

        return form;
    }
    
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

	public Map<Integer, SlotInfo> getHours() {
		return hours;
	}

	public void setHours(Map<Integer, SlotInfo> hours) {
		this.hours = hours;
	}

	@Override
	public String toString() {
		return "ScheduleForm [scheduleId=" + scheduleId +  ", date=" + date + ", hours=" + hours
				+ "]";
	}

    
}