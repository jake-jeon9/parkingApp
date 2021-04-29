package com.example.parkingapp.model;

public class AgencyDTO {
    String Agency,enrollDate,phone,expiredDate,contact,startDate,comment;
    long usedCount;

    public AgencyDTO(String agency, String enrollDate, String phone, String expiredDate, String contact, String startDate, String comment, long usedCount) {
        Agency = agency;
        this.enrollDate = enrollDate;
        this.phone = phone;
        this.expiredDate = expiredDate;
        this.contact = contact;
        this.startDate = startDate;
        this.comment = comment;
        this.usedCount = usedCount;
    }

    public AgencyDTO() {
    }

    public String getAgency() {
        return Agency;
    }

    public void setAgency(String agency) {
        Agency = agency;
    }

    public String getEnrollDate() {
        return enrollDate;
    }

    public void setEnrollDate(String enrollDate) {
        this.enrollDate = enrollDate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(String expiredDate) {
        this.expiredDate = expiredDate;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public long getUsedCount() {
        return usedCount;
    }

    public void setUsedCount(long usedCount) {
        this.usedCount = usedCount;
    }
}
