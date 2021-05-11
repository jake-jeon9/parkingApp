package com.example.parkingapp.model;

import java.io.Serializable;

public class MetaDTO implements Serializable {
    long flatCost,flatTime,additionalCost,additionalTime,maxArea,baseCost,baseTime,monthCost;

    public MetaDTO(long flatCost, long flatTime, long additionalCost, long additionalTime, long maxArea, long baseCost, long baseTime, long monthCost) {
        this.flatCost = flatCost;
        this.flatTime = flatTime;
        this.additionalCost = additionalCost;
        this.additionalTime = additionalTime;
        this.maxArea = maxArea;
        this.baseCost = baseCost;
        this.baseTime = baseTime;
        this.monthCost = monthCost;
    }

    public MetaDTO() {
    }

    public long getFlatCost() {
        return flatCost;
    }

    public void setFlatCost(long flatCost) {
        this.flatCost = flatCost;
    }

    public long getFlatTime() {
        return flatTime;
    }

    public void setFlatTime(long flatTime) {
        this.flatTime = flatTime;
    }

    public long getAdditionalCost() {
        return additionalCost;
    }

    public void setAdditionalCost(long additionalCost) {
        this.additionalCost = additionalCost;
    }

    public long getAdditionalTime() {
        return additionalTime;
    }

    public void setAdditionalTime(long additionalTime) {
        this.additionalTime = additionalTime;
    }

    public long getMaxArea() {
        return maxArea;
    }

    public void setMaxArea(long maxArea) {
        this.maxArea = maxArea;
    }

    public long getBaseCost() {
        return baseCost;
    }

    public void setBaseCost(long baseCost) {
        this.baseCost = baseCost;
    }

    public long getBaseTime() {
        return baseTime;
    }

    public void setBaseTime(long baseTime) {
        this.baseTime = baseTime;
    }

    public long getMonthCost() {
        return monthCost;
    }

    public void setMonthCost(long monthCost) {
        this.monthCost = monthCost;
    }
}


