package com.example.Aptech_Final.Enity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
@Entity
@Table(name = "WARD", schema = "dbo")
public class Ward {
	// Tạo các thuộc tính = tên trường trong database

	// id (Tương đương với khóa chính)
	// ID của xã
	@Id
	@NotNull
	@Column(name = "WARD_ID")
	private Long wardId;
	// Tên xã
	@Column(name = "WARD_NAME")
	@NotNull
	private String wardName;
	// Mô tả
	@Column(name = "NOTE")
	@Null
	private String note;
	// Id Quận (lấy từ Quận, k thực hiện liên kết trong spring)
	@NotNull
	@Column(name = "DISTRICT_ID")
	private Long districtId;
	
	// Getter và Setter
	public Long getWardId() {
		return wardId;
	}
	public void setWardId(Long wardId) {
		this.wardId = wardId;
	}
	public String getWardName() {
		return wardName;
	}
	public void setWardName(String wardName) {
		this.wardName = wardName;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public Long getDistrictId() {
		return districtId;
	}
	public void setDistrictId(Long districtId) {
		this.districtId = districtId;
	}	
}
