package com.example.Aptech_Final.Controller.DTO;
//Tạo lớp DTO để chứa data cần hiển thị ở search và truyền data từ service sang controller
import java.util.List;

import com.example.Aptech_Final.Enity.District;
import com.example.Aptech_Final.Enity.Province;
import com.example.Aptech_Final.Enity.Ward;


public class UsersDTO {
	// Danh sách tỉnh (để hiển thị trong dropdown)
	private List<Province> provinceList;
	// Danh sách quận (để hiển thị trong dropdown)
	private List<District> districtList;
	// Danh sách xã (để hiển thị trong dropdown)
	private List<Ward> wardList;
	// Danh sách hiển thị thông tin user
	private List<UserManagementDTO> userManagementDTO;
	
	// Thuộc tính để lấy các id trong dropdown
    private Long provinceId;
    private Long districtId;
    private Long wardId;
    
	//Getter và setter	
	public List<Province> getProvinceList() {
		return provinceList;
	}
	public void setProvinceList(List<Province> provinceList) {
		this.provinceList = provinceList;
	}
	public List<District> getDistrictList() {
		return districtList;
	}
	public void setDistrictList(List<District> districtList) {
		this.districtList = districtList;
	}
	public List<Ward> getWardList() {
		return wardList;
	}
	public void setWardList(List<Ward> wardList) {
		this.wardList = wardList;
	}
	public List<UserManagementDTO> getUserManagementDTO() {
		return userManagementDTO;
	}
	public void setUserManagementDTO(List<UserManagementDTO> userManagementDTO) {
		this.userManagementDTO = userManagementDTO;
	}
	public Long getProvinceId() {
		return provinceId;
	}
	public void setProvinceId(Long provinceId) {
		this.provinceId = provinceId;
	}
	public Long getDistrictId() {
		return districtId;
	}
	public void setDistrictId(Long districtId) {
		this.districtId = districtId;
	}
	public Long getWardId() {
		return wardId;
	}
	public void setWardId(Long wardId) {
		this.wardId = wardId;
	}	
}
