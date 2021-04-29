package com.example.parkingapp.setting;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class S_monthActivity extends AppCompatActivity implements View.OnClickListener {

    Button buttonApply, buttonCancel,buttonSelect1,buttonSelect2;
    EditText editTextPlateOfNumber, editTextMemberName, editTextMemberPhone, editTextStartOfDate, editTextexpiredDate,
            editTextComment;
    TextView textView;
    int YEAR, DAY, HOUR, MINUTE, MONTH;
    HashMap<String, Object> hashMap;
    Context activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s_month);
        activity = this;
        buttonApply = findViewById(R.id.buttonApply);
        buttonCancel = findViewById(R.id.buttonCancel);
        editTextPlateOfNumber = findViewById(R.id.editTextPlateOfNumber);
        editTextMemberName = findViewById(R.id.editTextMemberName);
        editTextMemberPhone = findViewById(R.id.editTextMemberPhone);
        editTextStartOfDate = findViewById(R.id.editTextStartOfDate);
        editTextexpiredDate = findViewById(R.id.editTextExpiredDate);
        editTextComment = findViewById(R.id.editTextComment);
        textView = findViewById(R.id.textViewEnrollDate);

        buttonSelect1 = findViewById(R.id.buttonSelect1);
        buttonSelect2 = findViewById(R.id.buttonSelect2);


        buttonCancel.setOnClickListener(this);
        buttonApply.setOnClickListener(this);
        //editTextStartOfDate.setOnClickListener(this);
        //editTextexpiredDate.setOnClickListener(this);
        buttonSelect1.setOnClickListener(this);
        buttonSelect2.setOnClickListener(this);

        editTextStartOfDate.setEnabled(false);
        editTextexpiredDate.setEnabled(false);

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

        textView.setText(String.format("%04d-%02d-%02d", YEAR, MONTH, DAY));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonApply) {

            /*입력검사필요
            각종 입력검사
             */


            String plateOfNumber = editTextPlateOfNumber.getText() + "";
            String enrollDate = textView.getText() + "";
            String expiredDate = editTextexpiredDate.getText() + "";

            String memberName = editTextMemberName.getText() + "";
            String memberPhone = editTextMemberPhone.getText() + "";
            String startDate = editTextStartOfDate.getText() + "";
            String comment = editTextComment.getText()+"";
            int usedCount = 0;

            memberPhone = memberPhone.substring(0,3)+"-"+memberPhone.substring(3,7)+"-"+memberPhone.substring(7);
            hashMap = new HashMap<>();
            hashMap.put("enrollDate", enrollDate);
            hashMap.put("expiredDate", expiredDate);
            hashMap.put("memberName", memberName);
            hashMap.put("memberPhone", memberPhone);
            hashMap.put("startDate", startDate);
            hashMap.put("usedCount", usedCount);
            hashMap.put("comment", comment);


            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("member").child(plateOfNumber);

            ref.setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(activity, plateOfNumber + "차주 등록 성공", Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(activity, plateOfNumber + "차주 등록 실패", Toast.LENGTH_SHORT).show();
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
                    editTextexpiredDate.setText(resultY + "-" + resultM + "-" + resultD);
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


