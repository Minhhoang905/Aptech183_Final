package com.example.Aptech_Final.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.Aptech_Final.Controller.DTO.ScheduleDTO;
import com.example.Aptech_Final.Enity.Schedule;
import com.example.Aptech_Final.Form.ScheduleForm;
import com.example.Aptech_Final.Repository.ScheduleRepository;
import com.example.Aptech_Final.Repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class ScheduleService {

	@Autowired
	private ScheduleRepository scheduleRepository;
	@Autowired
	private UserRepository userRepository;
	
    
}
