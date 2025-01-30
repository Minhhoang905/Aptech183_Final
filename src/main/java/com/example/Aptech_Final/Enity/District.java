package com.example.Aptech_Final.Enity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

// Đánh dấu class này là 1 entity API, đại diện cho table "Quan" trong database
@Entity
@Table(name = "DISTRICT", schema = "dbo")
public class District {
	// Tạo các thuộc tính = tên trường trong database

	// id (Tương đương với khóa chính)
	//ID của quận
	@Id
	@NotNull
	@Column(name = "DISTRICT_ID")
	private Long districtId;
	//Tên quận
	@Column(name = "DISTRICT_NAME")
	@NotNull
	private String districtName;
	//Mô tả
	@Column(name = "NOTE")
	@Null
	private String note;
	// Lấy id của Tỉnh từ table `Province`
    // ID của tỉnh
	@NotNull
	@Column(name = "PROVINCE_ID")
	private Long provinceId;
	
	//Getter và Setter
	public Long getDistrictId() {
		return districtId;
	}
	public void setDistrictId(Long districtId) {
		this.districtId = districtId;
	}
	public String getDistrictName() {
		return districtName;
	}
	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public Long getProvinceId() {
		return provinceId;
	}
	public void setProvinceId(Long provinceId) {
		this.provinceId = provinceId;
	}
	
}
