package com.example.parkingapp.helper;

public class ConvertDateHelper {

    private static ConvertDateHelper instance = null;

    public static ConvertDateHelper getInstance() {
        if (instance == null) instance = new ConvertDateHelper();
        return instance;
    }

    private ConvertDateHelper() {
    }

    public String[] adaptDate(int month, int day) {
        String[] result = new String[2];
        result[0] = month + "";
        if (month < 10) {
            result[0] = "0" + month;
        }

        result[1] = "" + day;
        if (day < 10) result[1] = "0" + day;

        return result;
    }


}
