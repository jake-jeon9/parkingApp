package com.example.parkingapp.model;

public class MemberDTO {
    private int memberNo;
    private String memberId;
    private String email;
    private String pw;
    private String nameOfParkingSpace;
    private String phone;
    private String reg_date;

    public MemberDTO() {
    }

    public MemberDTO(int memberNo, String memberId, String email, String pw, String nameOfParkingSpace, String phone, String reg_date) {
        this.memberNo = memberNo;
        this.memberId = memberId;
        this.email = email;
        this.pw = pw;
        this.nameOfParkingSpace = nameOfParkingSpace;
        this.phone = phone;
        this.reg_date = reg_date;
    }

    public int getMemberNo() {
        return memberNo;
    }

    public void setMemberNo(int memberNo) {
        this.memberNo = memberNo;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public String getNameOfParkingSpace() {
        return nameOfParkingSpace;
    }

    public void setNameOfParkingSpace(String nameOfParkingSpace) {
        this.nameOfParkingSpace = nameOfParkingSpace;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getReg_date() {
        return reg_date;
    }

    public void setReg_date(String reg_date) {
        this.reg_date = reg_date;
    }
}
