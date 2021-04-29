package com.example.parkingapp.model;

import java.io.Serializable;

public class ParkedDTO implements Serializable {
    String state,plateNumber,imageUrl;
    long paid,inTime,outTime;
    boolean coupon,isMember;

    public ParkedDTO() {
    }

    public ParkedDTO(String state, String plateNumber, String imageUrl, long paid, long inTime, long outTime, boolean coupon, boolean isMember) {
        this.state = state;
        this.plateNumber = plateNumber;
        this.imageUrl = imageUrl;
        this.paid = paid;
        this.inTime = inTime;
        this.outTime = outTime;
        this.coupon = coupon;
        this.isMember = isMember;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public long getPaid() {
        return paid;
    }

    public void setPaid(long paid) {
        this.paid = paid;
    }

    public long getInTime() {
        return inTime;
    }

    public void setInTime(long inTime) {
        this.inTime = inTime;
    }

    public long getOutTime() {
        return outTime;
    }

    public void setOutTime(long outTime) {
        this.outTime = outTime;
    }

    public boolean isCoupon() {
        return coupon;
    }

    public void setCoupon(boolean coupon) {
        this.coupon = coupon;
    }

    public boolean isMember() {
        return isMember;
    }

    public void setMember(boolean member) {
        isMember = member;
    }
}

