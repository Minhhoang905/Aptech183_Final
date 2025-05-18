package com.example.Aptech_Final.Controller.DTO;
// Đây là lớp để tính toán số lượng PT, User và check xem số PT = số User không
public class SlotInfo {
	// Số lượng PT đã đăng ký
    private int ptCount;
    // Số lượng User đã đăng ký
    private int userCount;   
    // Kiểm tra xem người dùng hiện tại đã đăng ký chưa
    private boolean bookedByCurrentUser; 

    // Getters và Setters
    public int getPtCount() {
        return ptCount;
    }

    public void setPtCount(int ptCount) {
        this.ptCount = ptCount;
    }

    public int getUserCount() {
        return userCount;
    }

    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }

    public boolean isBookedByCurrentUser() {
        return bookedByCurrentUser;
    }

    public void setBookedByCurrentUser(boolean bookedByCurrentUser) {
        this.bookedByCurrentUser = bookedByCurrentUser;
    }

	@Override
	public String toString() {
		return "SlotInfo [ptCount=" + ptCount + ", userCount=" + userCount + ", bookedByCurrentUser="
				+ bookedByCurrentUser + "]";
	}

}

