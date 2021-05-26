package com.example.parkingapp.sign;

import android.app.Activity;

import android.graphics.Color;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.parkingapp.R;
import com.example.parkingapp.helper.UrlHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;


import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.EncryptedPrivateKeyInfo;

import cz.msebera.android.httpclient.Header;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    Button btsignup,btcheckDupId;
    ImageButton btCancel;
    TextView textViewId,textViewEmail,textViewPw,textViewNameOfParking;
    EditText id,pw1,pw2,nameOfParkingSpace,email,phone;

    AsyncHttpClient client;
    HttpResponse responseCheckMail;
    String url = UrlHelper.getInstance().getUrl();

    LinearLayout layout1, layout2, layout3, layout4, layout5, layout6;

    // 레이아웃 애니메이션
    Animation animation = new AlphaAnimation(0, 1);

    String device_token;
    SignUpActivity context;
    String colorBlue = "#1e1eeb";
    String colorRed =  "#fc1414";

    String takenEmail;
    boolean checker_id = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        context = this;
        findViewById();

        client = new AsyncHttpClient();
        responseCheckMail = new HttpResponse();
        animation.setDuration(1000); // 레이아웃 애니메이션

        getDeviceToken();
        id.requestFocus();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        takenEmail = getIntent().getStringExtra("email");
        if(takenEmail != null){
            email.setText(takenEmail);
            email.setEnabled(true);
        }
    }



    private void getDeviceToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.e("[test]", "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        device_token = task.getResult();
                        Log.e("[test]", "deviceToken?" + device_token);
                    }
                });
    }

    private void findViewById() {
        layout1 = findViewById(R.id.layout1);
        layout2 = findViewById(R.id.layout2);
        layout3 = findViewById(R.id.layout3);
        layout4 = findViewById(R.id.layout4);
        layout5 = findViewById(R.id.layout5);
        layout6 = findViewById(R.id.layout6);

        btCancel = findViewById(R.id.button1);
        btsignup =findViewById(R.id.button2);
        btcheckDupId = findViewById(R.id.button3);

        textViewId = findViewById(R.id.textViewIdUnavailable);
        textViewEmail = findViewById(R.id.textView3);
        textViewPw = findViewById(R.id.textView7);
        textViewNameOfParking = findViewById(R.id.textView10);

        id = findViewById(R.id.editText1);
        pw1 = findViewById(R.id.editText5);
        pw2 = findViewById(R.id.editText6);
        nameOfParkingSpace = findViewById(R.id.editText3);
        email = findViewById(R.id.editText4);
        phone= findViewById(R.id.editText2);
    }

    @Override
    public void onClick(View v) {
        RequestParams params = null;
        switch (v.getId()){
            case R.id.button1 :  //x버튼
                    finish();
                break;
            case R.id.button2 : // 회원가입
                checkForm();
                url+="/parker/member/member_insert.do";
                params =  new RequestParams();
                params.put("memberId",id.getText().toString().trim());
                params.put("pw", pw1.getText().toString().trim());
                params.put("email",email.getText().toString().trim());
                params.put("nameOfParkingArea",nameOfParkingSpace.getText().toString().trim());
                params.put("phone",phone.getText().toString().trim());
                params.put("device_token",device_token);


                client.post(url, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String str = new String(responseBody);

                        try {
                            JSONObject json = new JSONObject(str);
                            String memberRT = json.getString("RT"); // 중복확인
                            String costRT = json.getString("RT"); // 중복확인

                            if(memberRT.equals("OK")&&costRT.equals("OK")){
                               int memberNO = Integer.parseInt(json.getString("memberNo"));
                               Toast.makeText(SignUpActivity.this,"가입 성공! 로그인을 해주세요.",Toast.LENGTH_LONG).show();
                                if(takenEmail != null){
                                    //유저 등록
                                    registerUser(takenEmail,pw2.getText().toString().trim(),memberNO);
                                }

                            }else{
                                Toast.makeText(SignUpActivity.this,"가입 실패 관리자에 문의해주세요.",Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Toast.makeText(SignUpActivity.this,statusCode+" error : "+error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
                break;

            case R.id.button3 : // 중복확인
                id.setVisibility(View.INVISIBLE);
                url+="/parker/member/check_id.do";
                params =  new RequestParams();
                String idCheck = id.getText().toString().trim();
                if(idCheck.equals("")){
                    Toast.makeText(this,"아이디를 입력해주세요.",Toast.LENGTH_LONG).show();
                    return;
                }
                params.put("memberId", idCheck);
                client.post(url, params, responseCheckMail);
                break;
        }


    }

    private void registerUser(String takenEmail,String pw,int memberNo) {
        Log.e("[test]", "유저등록시작");
        FirebaseAuth mAuth;

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        // 유저 정보 얻기
        String email = user.getEmail();
        String uid = user.getUid();
        SimpleDateFormat reg_date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        // 해쉬멥에 담아서 저장
        HashMap<Object, String> hashMap = new HashMap<>();
        hashMap.put("email", email);
        hashMap.put("memberId",id.getText().toString().trim());
        hashMap.put("memberNo",String.valueOf(memberNo));
        hashMap.put("reg_date",reg_date.format(new Timestamp(System.currentTimeMillis())));

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        // 파이어 베이스에 유저 등록하기
        DatabaseReference reference = database.getReference("Users");
        // 유저를 헤쉬맵을 통해 등록하기
        reference.child(uid).setValue(hashMap);


    }

    private boolean checkForm() {

        if(id.getText().toString().trim().equals("")) {
            id.requestFocus();
            textViewId.setVisibility(View.VISIBLE);
            textViewId.setText("아이디를 입력해주세요.");
            textViewId.setTextColor(Color.parseColor(colorRed));
            return false;
        }

        if(checker_id){
            id.requestFocus();
            textViewId.setVisibility(View.VISIBLE);
            textViewId.setText("아이디 중복확인을 해주세요.");
            textViewId.setTextColor(Color.parseColor(colorRed));
            return false;
        }

        String pwfirst = pw1.getText().toString().trim();
        if(pwfirst.equals("")) {
            pw1.requestFocus();
            textViewPw.setVisibility(View.VISIBLE);
            textViewPw.setText("비밀번호를 입력해주세요.");
            return false;
        }

        String pwsecond =pw2.getText().toString().trim();

        if (pwsecond.equals("")) {
            pw2.requestFocus();
            textViewPw.setText("재확인 비밀번호를 입력해주세요.");
            textViewPw.setTextColor(Color.parseColor(colorRed));
            textViewPw.setVisibility(View.VISIBLE);
            return false;
        } else if(!pwsecond.equals(pwfirst)) {
            pw2.requestFocus();
            textViewPw.setText("비밀번호가 다릅니다.");
            textViewPw.setTextColor(Color.parseColor(colorRed));
            textViewPw.setVisibility(View.VISIBLE);
            return false;
        } else {
            textViewPw.setText("비밀번호가 일치합니다.");
            textViewPw.setTextColor(Color.parseColor(colorBlue));
            textViewPw.setVisibility(View.VISIBLE);
        }

        String nameofParkingArea = nameOfParkingSpace.getText().toString().trim();
        if(nameofParkingArea.equals("")) {
            nameOfParkingSpace.requestFocus();
            textViewNameOfParking.setVisibility(View.VISIBLE);
            return false;
        }else{
            textViewNameOfParking.setVisibility(View.INVISIBLE);
        }



        String checkEmail = email.getText().toString().trim();
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(checkEmail);
        if(checkEmail.equals("")) {
            email.requestFocus();
            textViewEmail.setText("이메일을 입력해주세요.");
            textViewEmail.setVisibility(View.VISIBLE);

            return false;
        }else if(!matcher.matches()){
            email.requestFocus();
            Toast.makeText(this,"영문,숫자,특수문자 '.'와'_' 만 가능합니다.",Toast.LENGTH_LONG).show();
            textViewEmail.setText("이메일 형식이 맞지 않습니다. ");
            textViewEmail.setVisibility(View.VISIBLE);
            return false;
        }else{
            textViewEmail.setVisibility(View.INVISIBLE);
        }



        String phoneCheck= phone.getText().toString().trim();
        String regexPhone = "^01(?:0|1|[6-9])[.-]?(\\d{3}|\\d{4})[.-]?(\\d{4})$";
        Pattern patternP = Pattern.compile(regexPhone);
        Matcher matcherP = patternP.matcher(phoneCheck);
        if(phoneCheck.equals("")) {
            phone.requestFocus();
            textViewEmail.setText("휴대폰 번호를 입력해주세요.");
            textViewEmail.setVisibility(View.VISIBLE);
            return false;
        }else if(matcherP.matches()){
            phone.requestFocus();
            textViewEmail.setText("휴대폰 번호가 올바르지 않습니다.");
            textViewEmail.setVisibility(View.VISIBLE);
            return false;
        }else{
            textViewEmail.setVisibility(View.INVISIBLE);
        }
        
        return true;
    }

    class HttpResponse extends AsyncHttpResponseHandler {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String str = new String(responseBody);

            try {
                JSONObject json = new JSONObject(str);
                String RT = json.getString("RT"); // 중복확인

                if(RT.equals("OK")){
                    textViewId.setVisibility(View.VISIBLE);
                    textViewId.setText("사용 가능한 아이디입니다.");
                    textViewId.setTextColor(Color.parseColor(colorBlue));

                }else{
                    textViewId.setVisibility(View.VISIBLE);
                    textViewId.setText("이미 사용중인 아이디입니다.");
                    textViewId.setTextColor(Color.parseColor(colorRed));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(SignUpActivity.this,error.getMessage()+" 오류",Toast.LENGTH_LONG).show();
        }
    }
}