package com.example.parkingapp.helper;

public class UrlHelper {

    private static UrlHelper instance =null;
    String url = "http://192.168.55.172:8081";

    public static UrlHelper getInstance(){
        if(instance == null){
            instance = new UrlHelper();
        }
        return instance;
    }
    private UrlHelper (){};

    public String getUrl(){
        return url;
    }



}

