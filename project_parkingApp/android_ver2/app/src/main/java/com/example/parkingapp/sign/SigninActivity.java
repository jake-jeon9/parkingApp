package com.example.parkingapp.sign;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.parkingapp.MainActivity;
import com.example.parkingapp.R;
import com.example.parkingapp.helper.UrlHelper;
import com.example.parkingapp.model.MemberDTO;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SigninActivity extends AppCompatActivity implements View.OnClickListener {

    private boolean saveLoginData;
    private String email;
    private String pwd;

    AsyncHttpClient client;
    HttpResponse response;
    String url = UrlHelper.getInstance().getUrl()+"/foret/search/member.do";

    Button button0;
    TextView button1, button2, button3, button4;
    EditText idEditText, passwordEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        String pw1 = "sdasd221";

        button0 = findViewById(R.id.button0);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        idEditText = findViewById(R.id.editText1);
        passwordEditText = findViewById(R.id.editText2);

        client = new AsyncHttpClient();
        response = new HttpResponse();

        button0.setOnClickListener(this); //로그인
        button1.setOnClickListener(this); //구글 로그인
        button2.setOnClickListener(this); //카카오 로그인
        button3.setOnClickListener(this); //비밀번호 찾기
        button4.setOnClickListener(this); //회원가입



    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.button0 :
                RequestParams params = new RequestParams();
                params.put("email", emailEditText.getText().toString().trim());
                params.put("password", passwordEditText.getText().toString().trim());
                client.post(url, params, response);
                break;
            case R.id.button1 :
                Toast.makeText(this, "구글 로그인", Toast.LENGTH_SHORT).show();
                break;
            case R.id.button2 :
                Toast.makeText(this, "카카오 로그인", Toast.LENGTH_SHORT).show();
                break;
            case R.id.button3 :
                Toast.makeText(this, "이름, 이메일 주소로 비밀번호 찾기", Toast.LENGTH_SHORT).show();
                break;
            case R.id.button4 :
                intent = new Intent(this, JoinUsActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    private void moveToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
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
                    // 세션에 담아서 로그인 페이지로
                    SessionManager sessionManager = new SessionManager(LoginActivity.this);
                    sessionManager.saveSession(memberDTO);
                    moveToMainActivity();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(LoginActivity.this, "이메일과 비밀번호를 확인해 주세요", Toast.LENGTH_SHORT).show();
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