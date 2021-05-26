package com.example.parkingapp.model;

import java.io.Serializable;

public class ParkingListDTO implements Serializable {
    private int usedNo;
    private int memberNo;
    private String plateNumOfCar;
    private String currentOfState;
    private int timeOfused; //분단위
    private String timeOfParked;
    private String timeOfOut;
    private int paid;
    private String coupon;
    private String photo_link;

    public ParkingListDTO() {
    }

    public ParkingListDTO(int usedNo, int memberNo, String plateNumOfCar, String currentOfState, int timeOfused, String timeOfParked, String timeOfOut, int paid, String coupon, String photo_link) {
        this.usedNo = usedNo;
        this.memberNo = memberNo;
        this.plateNumOfCar = plateNumOfCar;
        this.currentOfState = currentOfState;
        this.timeOfused = timeOfused;
        this.timeOfParked = timeOfParked;
        this.timeOfOut = timeOfOut;
        this.paid = paid;
        this.coupon = coupon;
        this.photo_link = photo_link;
    }

    public int getUsedNo() {
        return usedNo;
    }

    public void setUsedNo(int usedNo) {
        this.usedNo = usedNo;
    }

    public int getMemberNo() {
        return memberNo;
    }

    public void setMemberNo(int memberNo) {
        this.memberNo = memberNo;
    }

    public String getPlateNumOfCar() {
        return plateNumOfCar;
    }

    public void setPlateNumOfCar(String plateNumOfCar) {
        this.plateNumOfCar = plateNumOfCar;
    }

    public String getCurrentOfState() {
        return currentOfState;
    }

    public void setCurrentOfState(String currentOfState) {
        this.currentOfState = currentOfState;
    }

    public int getTimeOfused() {
        return timeOfused;
    }

    public void setTimeOfused(int timeOfused) {
        this.timeOfused = timeOfused;
    }

    public String getTimeOfParked() {
        return timeOfParked;
    }

    public void setTimeOfParked(String timeOfParked) {
        this.timeOfParked = timeOfParked;
    }

    public String getTimeOfOut() {
        return timeOfOut;
    }

    public void setTimeOfOut(String timeOfOut) {
        this.timeOfOut = timeOfOut;
    }

    public int getPaid() {
        return paid;
    }

    public void setPaid(int paid) {
        this.paid = paid;
    }

    public String getCoupon() {
        return coupon;
    }

    public void setCoupon(String coupon) {
        this.coupon = coupon;
    }

    public String getPhoto_link() {
        return photo_link;
    }

    public void setPhoto_link(String photo_link) {
        this.photo_link = photo_link;
    }
}
