package com.example.parkingapp.model;

public class MemberDTO {
    String plateNumber,enrollDate,startDate,expiredDate,memberName,memberPhone,comment;
    int usedCount;

    public MemberDTO() {

    }

    public MemberDTO(String plateNumber, String enrollDate, String startDate, String expiredDate, String memberName, String memberPhone, String comment, int usedCount) {
        this.plateNumber = plateNumber;
        this.enrollDate = enrollDate;
        this.startDate = startDate;
        this.expiredDate = expiredDate;
        this.memberName = memberName;
        this.memberPhone = memberPhone;
        this.comment = comment;
        this.usedCount = usedCount;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getEnrollDate() {
        return enrollDate;
    }

    public void setEnrollDate(String enrollDate) {
        this.enrollDate = enrollDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(String expiredDate) {
        this.expiredDate = expiredDate;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberPhone() {
        return memberPhone;
    }

    public void setMemberPhone(String memberPhone) {
        this.memberPhone = memberPhone;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getUsedCount() {
        return usedCount;
    }

    public void setUsedCount(int usedCount) {
        this.usedCount = usedCount;
    }
}
