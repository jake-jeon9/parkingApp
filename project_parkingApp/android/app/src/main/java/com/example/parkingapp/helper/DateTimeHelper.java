package com.example.parkingapp.helper;

import java.util.Calendar;

public class DateTimeHelper {
    //
    private static DateTimeHelper instance=null;

    public static DateTimeHelper getInstance(){
        if(instance==null)instance=new DateTimeHelper();

        return instance;
    }

    private DateTimeHelper(){}

    public int[]getDate(){
        Calendar calendar=Calendar.getInstance();
        int yy=calendar.get(Calendar.YEAR);
        int mm=calendar.get(Calendar.MONTH)+1;
        int dd=calendar.get(Calendar.DAY_OF_MONTH);

        int[]day={yy,mm,dd};
        return day;
    }

    public int[]getTime(){
        Calendar calendar=Calendar.getInstance();
        int hh=calendar.get(Calendar.HOUR_OF_DAY);
        int mi=calendar.get(Calendar.MINUTE);
        int ss=calendar.get(Calendar.SECOND);
        int[]time={hh,mi,ss};
        return time;
    }

}

