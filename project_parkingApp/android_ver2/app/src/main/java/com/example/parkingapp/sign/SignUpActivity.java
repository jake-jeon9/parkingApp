package com.example.parkingapp.sign;

import android.app.Activity;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
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
import androidx.appcompat.widget.Toolbar;

import com.example.parkingapp.R;
import com.example.parkingapp.helper.ProgressDialogHelper;
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
    ImageButton btCancel,imageButtonSeePw;
    TextView textViewId,textViewEmail,textViewPw,textViewNameOfParking,textViewPhone;
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
    boolean toggleSeePw = false;

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
        //id.requestFocus();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        takenEmail = getIntent().getStringExtra("email");
        if(takenEmail != null){
            email.setText(takenEmail);
            email.setEnabled(false);
            Toast.makeText(this,"회원가입을 먼저 해주세요.",Toast.LENGTH_LONG).show();
        }

        btsignup.setOnClickListener(this);
        btcheckDupId.setOnClickListener(this);
        btCancel.setOnClickListener(this);
        imageButtonSeePw.setOnClickListener(this);

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
        imageButtonSeePw = findViewById(R.id.imageButtonSeePw);

        textViewId = findViewById(R.id.textViewIdUnavailable);
        textViewEmail = findViewById(R.id.textView5);
        textViewPw = findViewById(R.id.textView7);
        textViewNameOfParking = findViewById(R.id.textView10);
        textViewPhone = findViewById(R.id.textView9);

        id = findViewById(R.id.editText1);
        pw1 = findViewById(R.id.editText5);
        pw2 = findViewById(R.id.editText6);
        nameOfParkingSpace = findViewById(R.id.editText3);
        email = findViewById(R.id.editText4);
        phone= findViewById(R.id.editText2);


        //pw 조합 검사기
        pw1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String pwfirst = pw1.getText().toString().trim();
                String pwValidation = "^(?=.*[a-zA-Z0-9])(?=.*[a-zA-Z!@#$%^&*])(?=.*[0-9!@#$%^&*])[A-Za-z[0-9]$@$!%*#?&].{5,12}$";
                if(pwfirst.length()<8){
                    textViewPw.setText("비밀번호를 8자 이상 입력해주세요.");
                    textViewPw.setTextColor(Color.parseColor(colorRed));
                    textViewPw.setVisibility(View.VISIBLE);
                }else if(!pwfirst.matches(pwValidation)){
                    textViewPw.setText("문자,숫자,특수문자 2가지 이상 포함해주세요.");
                    textViewPw.setTextColor(Color.parseColor(colorRed));
                    textViewPw.setVisibility(View.VISIBLE);
                }else{
                    textViewPw.setVisibility(View.INVISIBLE);
                }

            }

        });
        //pw 검사기
        pw2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                String pwfirst = pw1.getText().toString().trim();
                String pwsecond =pw2.getText().toString().trim();

                if(pw2.getText().toString().length()<8){
                    textViewPw.setText("비밀번호를 8자 이상 입력해주세요.");
                    textViewPw.setTextColor(Color.parseColor(colorRed));
                    textViewPw.setVisibility(View.VISIBLE);
                }else if(!pwsecond.equals(pwfirst)){
                    textViewPw.setText("비밀번호가 다릅니다.");
                    textViewPw.setTextColor(Color.parseColor(colorRed));
                    textViewPw.setVisibility(View.VISIBLE);
                }else{
                    textViewPw.setText("비밀번호가 일치합니다.");
                    textViewPw.setTextColor(Color.parseColor(colorBlue));
                    textViewPw.setVisibility(View.VISIBLE);
                }


            }
        });
        //이메일 형식 검사기
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (email.getText().toString().contains("@") && email.getText().toString().contains(".")) {
                    if (Patterns.EMAIL_ADDRESS.matcher(editable).matches()) {
                        textViewEmail.setVisibility(View.INVISIBLE);
                    }
                }else{
                    textViewEmail.setText("이메일 형식이 맞지 않습니다.");
                    textViewEmail.setVisibility(View.VISIBLE);
                }
            }

        });

    }

    @Override
    public void onClick(View v) {
        RequestParams params = null;
        switch (v.getId()){
            case R.id.button1 :  //x버튼
                Toast.makeText(this,"X눌림",Toast.LENGTH_SHORT).show();
                    finish();
                break;
            case R.id.button2 : // 회원가입
                //Toast.makeText(this,"회원가입 눌림",Toast.LENGTH_SHORT).show();

                if(!checkForm()) return; // 빈항목 있으면 멈춤.

                url=UrlHelper.getInstance().getUrl()+"/parker/member/member_insert.do";
                params =  new RequestParams();
                params.put("memberId",id.getText().toString().trim());
                params.put("pw", pw1.getText().toString().trim());
                params.put("email",email.getText().toString().trim());
                params.put("nameOfParkingArea",nameOfParkingSpace.getText().toString().trim());
                params.put("phone",phone.getText().toString().trim());
                params.put("device_token",device_token);
                ProgressDialogHelper.getInstance().getProgressbar(this,"회원가입 중입니다.");

                client.post(url, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String str = new String(responseBody);

                        try {
                            JSONObject json = new JSONObject(str);
                            String memberRT = json.getString("memberRT"); // 중복확인
                            String costRT = json.getString("costRT"); // 중복확인

                            if(memberRT.equals("OK")&&costRT.equals("OK")){
                               int memberNO = Integer.parseInt(json.getString("memberNo"));
                               Toast.makeText(SignUpActivity.this,"가입 성공! 로그인을 해주세요.",Toast.LENGTH_LONG).show();
                                finish();
                                if(takenEmail != null){
                                    //유저 등록
                                    registerUser(takenEmail,pw2.getText().toString().trim(),memberNO);
                                }

                            }else{
                                if(json.getString("memberNo").equals("0")){
                                    Toast.makeText(SignUpActivity.this,"이미 사용중인 아이디 입니다.\n 다른 아이디를 사용해주세요.",Toast.LENGTH_LONG).show();
                                }else{
                                    Toast.makeText(SignUpActivity.this,"가입 실패 관리자에 문의해주세요.",Toast.LENGTH_LONG).show();
                                }

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
                ProgressDialogHelper.getInstance().removeProgressbar();
                break;

            case R.id.button3 : // 중복확인
                //Toast.makeText(this,"중복확인 눌림",Toast.LENGTH_SHORT).show();
                textViewId.setVisibility(View.INVISIBLE);
                url=UrlHelper.getInstance().getUrl()+"/parker/member/check_id.do";
                params =  new RequestParams();
                String idCheck = id.getText().toString().trim();
                if(idCheck.equals("")){
                    Toast.makeText(this,"아이디를 입력해주세요.",Toast.LENGTH_LONG).show();
                    return;
                }
                params.put("memberId", idCheck);
                ProgressDialogHelper.getInstance().getProgressbar(SignUpActivity.this,"잠시만 기다려주세요.");
                client.post(url, params, responseCheckMail);
                break;
            case R.id.imageButtonSeePw :
                if(!toggleSeePw){ // 보여달라 한상태
                    toggleSeePw = true;
                    pw1.setInputType(InputType.TYPE_CLASS_TEXT);
                    pw2.setInputType(InputType.TYPE_CLASS_TEXT);
                    imageButtonSeePw.setImageResource(R.drawable.block_eye_icon);


                }else{ // 보여 주지말라고 한상태
                    toggleSeePw = false;
                    pw1.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    pw2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    imageButtonSeePw.setImageResource(R.drawable.eye_icon);
                }

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
        finish();

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
        } else{
            textViewPw.setVisibility(View.INVISIBLE);
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
//        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
//        Pattern pattern = Pattern.compile(regex);
//        Matcher matcher = pattern.matcher(checkEmail);
        boolean isEmail = Patterns.EMAIL_ADDRESS.matcher(checkEmail).matches();
        if(checkEmail.equals("")) {
            email.requestFocus();
            textViewEmail.setText("이메일을 입력해주세요.");
            textViewEmail.setVisibility(View.VISIBLE);

            return false;
        }else{
            textViewEmail.setVisibility(View.INVISIBLE);
        }

        String phoneCheck= phone.getText().toString().trim();
        String oriP = phoneCheck;
        phoneCheck.replaceAll("-","");
        if(phoneCheck.length()==10){
            String temp = phoneCheck;
            phoneCheck = temp.substring(0,3)+"-";
            phoneCheck+= temp.substring(3,6)+"-";
            phoneCheck+= temp.substring(6);
        }else{
            String temp = phoneCheck;
            phoneCheck = temp.substring(0,3)+"-";
            phoneCheck+= temp.substring(3,7)+"-";
            phoneCheck+= temp.substring(7);
        }
        String regexPhone = "^01(?:0|1|[6-9])[-]?(\\d{3}|\\d{4})[-]?(\\d{4})$";
        Pattern patternP = Pattern.compile(regexPhone);
        Matcher matcherP = patternP.matcher(phoneCheck);
        if(phoneCheck.equals("")) {
            phone.requestFocus();
            textViewPhone.setText("휴대폰 번호를 입력해주세요.");
            textViewPhone.setVisibility(View.VISIBLE);
            return false;
        }else if(matcherP.matches()& phoneCheck.length()==10 ||phoneCheck.length()==11  ){
            phone.requestFocus();
            textViewPhone.setText("핸드폰 번호를 확인해주세요.");
            textViewPhone.setVisibility(View.VISIBLE);
            return false;
        }else{
            textViewPhone.setVisibility(View.INVISIBLE);
        }
        //Toast.makeText(this,"전번 orig : "+oriP +"\n 변경 phon : "+phoneCheck,Toast.LENGTH_LONG).show();
        return true;
    }

    class HttpResponse extends AsyncHttpResponseHandler {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String str = new String(responseBody);

            try {
                JSONObject json = new JSONObject(str);
                String RT = json.getString("RT"); // 중복확인

                if(RT.equals("사용가능")){
                    textViewId.setVisibility(View.VISIBLE);
                    textViewId.setText("사용 가능한 아이디입니다.");
                    textViewId.setTextColor(Color.parseColor(colorBlue));

                }else{
                    textViewId.setVisibility(View.VISIBLE);
                    textViewId.setText("이미 사용중인 아이디입니다.");
                    textViewId.setTextColor(Color.parseColor(colorRed));
                }
                ProgressDialogHelper.getInstance().removeProgressbar();

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
