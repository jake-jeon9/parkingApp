package com.example.parkingapp.sign;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.parkingapp.MainActivity;
import com.example.parkingapp.R;
import com.example.parkingapp.SessionManager;
import com.example.parkingapp.helper.UrlHelper;
import com.example.parkingapp.model.CostDTO;
import com.example.parkingapp.model.MemberDTO;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class SigninActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton imageButtonGoogle,imageButtonKakao;

    AsyncHttpClient client;
    HttpResponse response;
    String url = UrlHelper.getInstance().getUrl()+"/parker/member/member_Login.do";

    Button buttonSignIn;
    TextView buttonSignUp, buttonFindPassword;
    EditText editID, editPassword;
    int memberNo;


    Context context;

    FirebaseAuth mAuth;
    FirebaseUser user;
    String deviceToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        context = this;

        buttonSignIn = findViewById(R.id.buttonSignIn);
        buttonSignUp = findViewById(R.id.buttonSignUp);
        buttonFindPassword = findViewById(R.id.buttonFindPassword);

        imageButtonGoogle = findViewById(R.id.imageButtonGoogle);
        imageButtonKakao = findViewById(R.id.imageButtonKakao);

        editID = findViewById(R.id.editID);
        editPassword = findViewById(R.id.editPassword);

        client = new AsyncHttpClient();
        response = new HttpResponse();

        buttonSignIn.setOnClickListener(this); //로그인

        buttonSignUp.setOnClickListener(this); //회원가입
        buttonFindPassword.setOnClickListener(this); //비밀번호 찾기

        imageButtonGoogle.setOnClickListener(this); //구글 로그인
        imageButtonKakao.setOnClickListener(this); //카카오 로그인

        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.buttonSignIn :
                RequestParams params = new RequestParams();
                params.put("memberId", editID.getText().toString().trim());
                params.put("pw", editPassword.getText().toString().trim());
                client.post(url, params, response);
                break;
            case R.id.imageButtonGoogle :
                Toast.makeText(this, "구글 로그인", Toast.LENGTH_SHORT).show();
                break;
            case R.id.imageButtonKakao :
                Toast.makeText(this, "카카오 로그인", Toast.LENGTH_SHORT).show();
                break;
            case R.id.buttonFindPassword :
                Toast.makeText(this, "이름, 이메일 주소로 비밀번호 찾기", Toast.LENGTH_SHORT).show();
                break;
            case R.id.buttonSignUp :
                intent = new Intent(this, SignUpActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    private void moveToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    class HttpResponse extends AsyncHttpResponseHandler {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String str = new String(responseBody);
            Gson gson = new Gson();
            try {
                JSONObject json = new JSONObject(str);
                String RT = json.getString("RT");
                if(RT.equals("OK")) {
                    JSONArray member = json.getJSONArray("member");
                    JSONObject temp = member.getJSONObject(0);
                    MemberDTO memberDTO = gson.fromJson(temp.toString(), MemberDTO.class);
                    CostDTO costDTO = gson.fromJson(temp.toString(), CostDTO.class);

                    // 세션에 담아서 로그인 페이지로
                    SessionManager sessionManager = new SessionManager(context);
                    sessionManager.saveSession(memberDTO);
                    moveToMainActivity();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        SessionManager sessionManager = new SessionManager(this);
        int userID = sessionManager.getSession();

        if(userID != -1) {
            moveToMainActivity();
        }
    }

}