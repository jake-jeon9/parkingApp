package com.example.parkingapp.sign;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.parkingapp.R;
import com.example.parkingapp.SessionManager;
import com.example.parkingapp.helper.UrlHelper;
import com.example.parkingapp.model.MemberDTO;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpResponse;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    Button btsignup,btcheckDupId;
    ImageButton btCancel;
    TextView textViewId,textViewEmail,textViewPw;
    EditText id,pw1,pw2,nameOfParkingSpace,email,phone;

    AsyncHttpClient client;
    HttpResponse response;
    String url = UrlHelper.getInstance().getUrl();

    LinearLayout layout1, layout2, layout3, layout4, layout5, layout6;

    // 레이아웃 애니메이션
    Animation animation = new AlphaAnimation(0, 1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

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
        textViewPw = findViewById(R.id.textView6);

        id = findViewById(R.id.editText1);
        pw1 = findViewById(R.id.editText5);
        pw2 = findViewById(R.id.editText6);
        nameOfParkingSpace = findViewById(R.id.editText3);
        email = findViewById(R.id.editText4);
        phone= findViewById(R.id.editText2);

        client = new AsyncHttpClient();
        response = new HttpResponse();

        animation.setDuration(1000); // 레이아웃 애니메이션

        id.requestFocus();
    }

    @Override
    public void onClick(View v) {
        RequestParams params = null;
        switch (v.getId()){
            case R.id.button1 :
                    finish();
                break;
            case R.id.button2 :
                checkId();
                url+="/parker/member/member_insert.do";
                params =  new RequestParams();
                params.put("memberId",id.getText().toString().trim());
                params.put("pw", pw1.getText().toString().trim());
                params.put("email",email.getText().toString().trim());
                params.put("nameOfParkingArea",nameOfParkingSpace.getText().toString().trim());
                params.put("phone",phone.getText().toString().trim());
                params.put("device_token","");
                client.post(url, params, response);
                break;

            case R.id.button3 :
                url+="/parker/member/check_id.do";
                params =  new RequestParams();
                String idCheck = id.getText().toString().trim();
                if(idCheck.equals("")){
                    Toast.makeText(this,"아이디를 입력해주세요.",Toast.LENGTH_LONG).show();
                    return;
                }
                params.put("memberId", idCheck);
                client.post(url, params, response);
                break;
        }


    }

    private boolean checkId() {

        if(id.getText().toString().trim().equals("")) {
            Toast.makeText(this, "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
        layout2.setAnimation(animation);
        layout2.setVisibility(View.VISIBLE);

        pw1.requestFocus();

        String pwfirst = pw1.getText().toString().trim();
        if(pwfirst.equals("")) {
            Toast.makeText(this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
        pw2.setVisibility(View.VISIBLE);
        layout2.setAnimation(animation);
        pw2.requestFocus();

        String pwsecond =pw2.getText().toString().trim();

        if (pwsecond.equals("")) {
            textViewPw.setText("비밀번호를 입력해주세요.");
            textViewPw.setVisibility(View.VISIBLE);
            return false;
        } else if(!pwsecond.equals(pwfirst)) {
            textViewPw.setText("비밀번호가 다릅니다.");
            textViewPw.setVisibility(View.VISIBLE);
            return false;
        } else {
            textViewPw.setText("비밀번호가 일치합니다.");
            textViewPw.setVisibility(View.VISIBLE);
        }

        layout3.setVisibility(View.VISIBLE);
        layout3.setAnimation(animation);
        nameOfParkingSpace.requestFocus();

        String nameofParkingArea = nameOfParkingSpace.getText().toString().trim();
        if(nameofParkingArea.equals("")) {
            Toast.makeText(this, "사업소 명을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
        layout4.setVisibility(View.VISIBLE);
        layout4.setAnimation(animation);
        email.requestFocus();

        String checkEmail = email.getText().toString().trim();
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(checkEmail);
        if(checkEmail.equals("")) {
            textViewEmail.setText("이메일을 입력해주세요.");
            textViewEmail.setVisibility(View.VISIBLE);

            return false;
        }else if(!matcher.matches()){
            textViewEmail.setText("이메일 형식이 맞지 않습니다.");
            textViewEmail.setVisibility(View.VISIBLE);
            return false;
        }


        layout5.setVisibility(View.VISIBLE);
        layout5.setAnimation(animation);
        phone.requestFocus();
        String phoneCheck= phone.getText().toString().trim();
        String regexPhone = "^01(?:0|1|[6-9])[.-]?(\\d{3}|\\d{4})[.-]?(\\d{4})$";
        Pattern patternP = Pattern.compile(regexPhone);
        Matcher matcherP = pattern.matcher(phoneCheck);
        if(phoneCheck.equals("")) {
            textViewEmail.setText("휴대폰 번호를 입력해주세요.");
            textViewEmail.setVisibility(View.VISIBLE);
            return false;
        }else if(matcherP.matches()){
            textViewEmail.setText("휴대폰 번호가 올바르지 않습니다.");
            textViewEmail.setVisibility(View.VISIBLE);
            return false;
        }
        
        return true;
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

                }
                if(true){
                    //아이디 사용 불가능할 겨웅
                    id.setVisibility(View.VISIBLE);
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