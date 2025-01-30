package com.example.Aptech_Final.Enity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

// Đánh dấu class này là 1 entity API, đại diện cho table "Province (tỉnh/thành)" trong database
@Entity
@Table(name = "PROVINCE",schema = "dbo")
public class Province {
	
	public Province() {}
	// ID của tỉnh
	@Id
	@NotNull
	@Column(name = "PROVINCE_ID")
	private Long provinceId;
	// Tên tỉnh
	@Column(name = "PROVINCE_NAME")
	@NotNull
	private String provinceName;
	// Mô tả
	@Column(name = "NOTE")
	private String Note;
	
	// Getter và setter
	public Long getProvinceId() {
		return provinceId;
	}
	public void setProvinceId(Long provinceId) {
		this.provinceId = provinceId;
	}
	public String getProvinceName() {
		return provinceName;
	}
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
	public String getNote() {
		return Note;
	}
	public void setNote(String note) {
		Note = note;
	}
			
}
