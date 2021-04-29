package com.example.parkingapp.model;

public class DailyData {
    long totalAccount, totalParked, currentUsed;

    public DailyData(long totalAccount, long totalParked, long currentUsed) {
        this.totalAccount = totalAccount;
        this.totalParked = totalParked;
        this.currentUsed = currentUsed;

    }

    public DailyData() {
    }

    public long getTotalAccount() {
        return totalAccount;
    }

    public void setTotalAccount(long totalAccount) {
        this.totalAccount = totalAccount;
    }

    public long getTotalParked() {
        return totalParked;
    }

    public void setTotalParked(long totalParked) {
        this.totalParked = totalParked;
    }

    public long getCurrentUsed() {
        return currentUsed;
    }

    public void setCurrentUsed(long currentUsed) {
        this.currentUsed = currentUsed;
    }

}
