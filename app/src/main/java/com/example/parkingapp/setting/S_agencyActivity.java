package com.example.parkingapp.setting;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.parkingapp.R;
import com.example.parkingapp.helper.DateTimeHelper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class S_agencyActivity extends AppCompatActivity implements View.OnClickListener {

    TextView textViewEnrollDate;
    EditText editTextAgency,editTextStartOfDate,
            editTextExpiredDate,editTextContact,editTextPhone,editTextComment;

    Button buttonSelect1,buttonSelect2,buttonApply,buttonCancel;

    int YEAR, DAY, HOUR, MINUTE, MONTH;
    HashMap<String, Object> hashMap;
    Context activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s_agency);

        activity = this;
        buttonApply = findViewById(R.id.buttonApply);
        buttonCancel = findViewById(R.id.buttonCancel);
        editTextAgency = findViewById(R.id.editTextAgency);
        editTextContact = findViewById(R.id.editTextContact);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextStartOfDate = findViewById(R.id.editTextStartOfDate);
        editTextExpiredDate = findViewById(R.id.editTextExpiredDate);
        editTextComment = findViewById(R.id.editTextComment);
        textViewEnrollDate = findViewById(R.id.textViewEnrollDate);

        buttonSelect1 = findViewById(R.id.buttonSelect1);
        buttonSelect2 = findViewById(R.id.buttonSelect2);


        buttonCancel.setOnClickListener(this);
        buttonApply.setOnClickListener(this);

        buttonSelect1.setOnClickListener(this);
        buttonSelect2.setOnClickListener(this);
    }
    @Override
    protected void onResume() {
        super.onResume();
        int[] date = DateTimeHelper.getInstance().getDate();
        YEAR = date[0];
        MONTH = date[1];
        DAY = date[2];
        int[] time = DateTimeHelper.getInstance().getTime();
        HOUR = time[0];
        MINUTE = time[1];

        textViewEnrollDate.setText(String.format("%04d-%02d-%02d", YEAR, MONTH, DAY));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonApply) {
            /*입력검사필요
            각종 입력검사
             */
            String Agency = editTextAgency.getText() + "";
            String enrollDate = textViewEnrollDate.getText() + "";
            String expiredDate = editTextExpiredDate.getText() + "";
            String contact = editTextContact.getText() + "";
            String phone = editTextPhone.getText() + "";
            String startDate = editTextStartOfDate.getText() + "";
            String comment = editTextComment.getText()+"";

            phone = phone.substring(0,3)+"-"+phone.substring(3,7)+"-"+phone.substring(7);
            int usedCount = 0;
            hashMap = new HashMap<>();
            hashMap.put("enrollDate", enrollDate);
            hashMap.put("expiredDate", expiredDate);
            hashMap.put("contact", contact);
            hashMap.put("phone", phone);
            hashMap.put("startDate", startDate);
            hashMap.put("usedCount", usedCount);
            hashMap.put("comment", comment);


            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("agencyList").child(Agency);

            ref.setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(activity, Agency + " 업체 등록 성공", Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(activity, Agency + "업체 등록 실패", Toast.LENGTH_SHORT).show();
                }
            });

        } else if (v.getId() == R.id.buttonCancel) {
            finish();
        } else if (v.getId() == R.id.buttonSelect1) {
            showDatePickerDialog(1);
        } else if (v.getId() == R.id.buttonSelect2) {
            showDatePickerDialog(2);
        }
    }

    //날짜선택창
    private void showDatePickerDialog(int type) {

        final int temp_yy = YEAR;
        final int temp_mm = MONTH;
        final int temp_dd = DAY;

        //DatePickerDialog(액티비티,이벤트처리,년,월,일
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                YEAR = year;
                MONTH = month + 1;
                DAY = dayOfMonth;

                String resultY =YEAR+"";
                String resultM = MONTH+"";
                String resultD= DAY+"";

                if (MONTH < 10) {
                    resultM ="0"+MONTH;
                }
                if(DAY<10){
                    resultD ="0"+DAY;
                }

                if(type == 1){
                    editTextStartOfDate.setText(resultY + "-" + resultM + "-" + resultD);
                }else if(type == 2){
                    editTextExpiredDate.setText(resultY + "-" + resultM + "-" + resultD);
                }
            }
        }, YEAR, MONTH - 1, DAY);

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                //백업된값을원복시킴
                YEAR = temp_yy;
                MONTH = temp_mm;
                DAY = temp_dd;
            }
        });

        //안드로이드버전에따라보이기도,안보이기도함.
        dialog.setTitle("날짜선택");
        //dialog.setMessage("생일을선택하세요");
        //dialog.setIcon(R.mipmap.ic_launcher);
        dialog.show();

    }
}