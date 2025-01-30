package com.example.Aptech_Final.Enity;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

//Đánh dấu lớp này là một entity JPA, đại diện cho bảng USERS trong database
@Entity
//Gọi tên của table
@Table(name = "USERS") //Sẽ lấy theo tên của Table này (nếu đặt tên class đúng với tên table thì k cần cũng được)
public class Users {
	// Tạo các thuộc tính = tên trường trong database (k cần đúng tên, nhưng nên đặt giống cho khỏe):
    // Đánh dấu trường này là khóa chính (primary key) của bảng LOGIN
	@Id
    // Tự động sinh giá trị cho khóa chính (ID) khi một bản ghi mới được tạo
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "USER_ID")
	private Long id;
	// Tên
	@Column(name = "USER_NAME")
	private String name;
	// Mật Khẩu
	@Column(name = "USER_PASSWORD")
	private String pass;
	// Quyền hạn
	@Column(name = "USER_ROLE")
	private String role;
	// Họ tên
	@Column(name = "FULL_NAME")
	private String fullName;
	// Email
	@Column(name = "USER_EMAIL")
	private String email;
	// Số điện thoại
	@Column(name = "USER_PHONE_NUMBER")
	private String phoneNumber;
	// Id của tỉnh/thành
	@Column(name = "PROVINCE_ID")
	private Long provinceId;
	// Id của quận/huyện
	@NotNull
	@Column(name = "DISTRICT_ID")
	private Long districtId;
	// Id của xã/phường
	@NotNull
	@Column(name = "WARD_ID")
	private Long wardId;
	// Địa chỉ cụ thể
	@Column(name = "CUSTOMER_ADDRESS")
	private String address;
	
	// Tạo getter và setter
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
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
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}	
}
