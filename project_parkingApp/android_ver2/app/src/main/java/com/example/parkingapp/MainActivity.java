package com.example.parkingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.example.parkingapp.Fragment.InAndOutFragment;
import com.example.parkingapp.Fragment.ListFragment;
import com.example.parkingapp.Fragment.MainFragment;
import com.example.parkingapp.Fragment.SetFragment;
import com.example.parkingapp.helper.ProgressDialogHelper;
import com.example.parkingapp.helper.UrlHelper;
import com.example.parkingapp.model.CostDTO;
import com.example.parkingapp.model.MemberDTO;
import com.example.parkingapp.sign.SignUpActivity;
import com.example.parkingapp.sign.SigninActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpResponse;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView nav_bottom;
    MainFragment mainFragment;
    InAndOutFragment inAndOutFragment;
    ListFragment listFragment;
    SetFragment setFragment;

    Context context;
    static MemberDTO memberDTO;
    static CostDTO costDTO;

    String url = UrlHelper.getInstance().getUrl()+"/parker/member/member_Login.do";

    AsyncHttpClient client;
    HttpResponse response;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        context = this;
        mainFragment = new MainFragment(context);
        inAndOutFragment = new InAndOutFragment(context);
        listFragment = new ListFragment(context);
        setFragment = new SetFragment(context);

        nav_bottom = findViewById(R.id.nav_bottom);
        nav_bottom.setOnNavigationItemSelectedListener(this);
        client = new AsyncHttpClient();
        response = new HttpResponse();



        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container, mainFragment).commit();
        }

        if(costDTO == null | memberDTO==null){
            RequestParams params = new RequestParams();
            SessionManager sessionManager = new SessionManager(this);
            String memberNo = sessionManager.getSessionMemberNo();
            params.put("memberNo", memberNo);
            ProgressDialogHelper.getInstance().getProgressbar(this,"잠시만 기다려주세요.");
            client.post(url, params, response);
        }

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_Home:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, mainFragment).commit();
                break;
            case R.id.navigation_InAndOut:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, inAndOutFragment).commit();
                break;
            case R.id.navigation_List:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, listFragment).commit();
                break;
            case R.id.navigation_Set:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, setFragment).commit();
                break;


        }
        return true;
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);


    }

    class HttpResponse extends AsyncHttpResponseHandler {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String str = new String(responseBody);
            Gson gson = new Gson();
            try {
                JSONObject json = new JSONObject(str);
                String memberRT = json.getString("memberRT");
                String costRT = json.getString("costRT");
                if (memberRT.equals("OK") && costRT.equals("OK")) {
                    JSONObject memeberDTO1 = json.getJSONObject("memberDTO");
                    memberDTO = gson.fromJson(memeberDTO1.toString(), MemberDTO.class);

                    JSONObject costDTO1 = json.getJSONObject("costDTO");
                    costDTO = gson.fromJson(costDTO1.toString(), CostDTO.class);

                    ProgressDialogHelper.getInstance().removeProgressbar();

                } else {
                    Toast.makeText(MainActivity.this, "아이디 혹은 비밀번호를 확인주세요.", Toast.LENGTH_SHORT).show();
                    ProgressDialogHelper.getInstance().removeProgressbar();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                ProgressDialogHelper.getInstance().removeProgressbar();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            ProgressDialogHelper.getInstance().removeProgressbar();
            Log.d("[test]", error.getMessage());
        }
    }
}