package com.example.parkingapp.sign;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.parkingapp.MainActivity;
import com.example.parkingapp.R;
import com.example.parkingapp.SessionManager;
import com.example.parkingapp.helper.ProgressDialogHelper;
import com.example.parkingapp.helper.UrlHelper;
import com.example.parkingapp.model.CostDTO;
import com.example.parkingapp.model.MemberDTO;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;

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

    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;
    private static final String T = "[text]";

    private GoogleSignInClient mGoogleSignInClient;

    boolean checker = false;


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


    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.buttonSignIn :
                RequestParams params = new RequestParams();
                params.put("memberId", editID.getText().toString().trim());
                params.put("pw", editPassword.getText().toString().trim());
                ProgressDialogHelper.getInstance().getProgressbar(this,"잠시만 기다려주세요.");
                client.post(url, params, response);
                break;
            case R.id.imageButtonGoogle :
                Toast.makeText(this, "구글 로그인", Toast.LENGTH_SHORT).show();
                ProgressDialogHelper.getInstance().getProgressbar(this,"잠시만 기다려주세요");
                startLoginWithGoogle();
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
                break;
        }
    }

    private void startLoginWithGoogle() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();

        signIn();
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        Log.d(T,"sign In ");
        startActivityForResult(signInIntent, RC_SIGN_IN);
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
                String memberRT = json.getString("memberRT");
                String costRT = json.getString("costRT");
                if(memberRT.equals("OK")&&costRT.equals("OK")) {
                    JSONObject memeberDTO = json.getJSONObject("memberDTO");
                    MemberDTO memberDTO = gson.fromJson(memeberDTO.toString(), MemberDTO.class);

                    // 세션에 담아서 로그인 페이지로
                    SessionManager sessionManager = new SessionManager(context);
                    sessionManager.saveSession(memberDTO);
                    ProgressDialogHelper.getInstance().removeProgressbar();
                    moveToMainActivity();

                }else{
                    Toast.makeText(SigninActivity.this,"아이디 혹은 비밀번호를 확인주세요.",Toast.LENGTH_SHORT).show();
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
            Log.d("[test]",error.getMessage());
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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(T,"Result in ");
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(T, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Toast.makeText(this,"로그인 실패",Toast.LENGTH_SHORT).show();
                Log.w(T, "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        Log.d(T,"Auth with google in");
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(T, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            Log.w(T, "signInWithCredential:failure", task.getException());
                            Toast.makeText(SigninActivity.this,"구글 로그인 실패.",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        Log.d(T,"udate UI in ");
        HashMap<String,Object> hash = new HashMap<>();
        String email = user.getEmail();
        String uid = user.getUid();
        Log.e(T,"email : " + email);
        Log.e(T,"uid : " + uid);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child(uid);
        Log.e(T,ref.getRef().toString());
        ref.setValue(hash);
        //ref.setValue(hash);
        Log.e(T,"푸쉬함");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                
                for (DataSnapshot ds : snapshot.getChildren()) {
                    if(ds.child("email").exists()){
                        Log.d(T,"realtime data goTO main try ");
                        int memberNo = snapshot.child(uid).child("memberNo").getValue(Integer.class);
                        RequestParams params = new RequestParams();
                        params.put("memberNo", memberNo);
                        client.post(url, params, response);
                        break;
                    }
                }
                Log.d(T,"realtime data goTO sign up ");
                //미등록 상태

                Intent intent = new Intent(context, SignUpActivity.class);
                intent.putExtra("email",email);
                startActivity(intent);
                ProgressDialogHelper.getInstance().removeProgressbar();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(T,"error ? " + error.getMessage());
                ProgressDialogHelper.getInstance().removeProgressbar();
            }
        });
        Log.e(T,"푸쉬 끝남");

    }



}