package com.example.parkingapp;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.parkingapp.model.CostDTO;
import com.example.parkingapp.model.MemberDTO;

public class SessionManager {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    String SHARED_PREF_NAME = "session";
    String SESSION_KEY = "session_user";

    public SessionManager(Context context){
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveSession(MemberDTO memberDTO){
        int no = memberDTO.getMemberNo();
        editor.putInt(SESSION_KEY, no).commit();

        String memberId = memberDTO.getMemberId();
        editor.putString("memberId",memberId).commit();

        String pw = memberDTO.getPw();
        editor.putString("pw", pw).commit();

    }

    public int getSession() {
        return sharedPreferences.getInt(SESSION_KEY, -1);
    }

    public String getSessionMemberId() {
        return sharedPreferences.getString("memberId", null);
    }
    public String getSessionMemberNo() {
        return sharedPreferences.getString("memberNo", null);
    }
    public String getSessionPassword() {
        return sharedPreferences.getString("pw", null);
    }

    public void removeSession() {
        editor.putInt(SESSION_KEY, -1).commit();
    }

}
