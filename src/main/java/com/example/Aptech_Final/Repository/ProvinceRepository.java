package com.example.Aptech_Final.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Aptech_Final.Enity.Province;


// Đánh dấu interface này là một Spring Data Repository của Tinh
@Repository
public interface ProvinceRepository extends JpaRepository<Province, Long>{
	
}
