package com.example.parkingapp.setting;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.parkingapp.R;
import com.example.parkingapp.helper.ProgressDialogHelper;
import com.example.parkingapp.model.MetaDTO;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class S_costActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editTextbasetime, editTextbasecost, editTextAdditionalTime, editTextAdditionalCost,
            editTextFlatTime, editTextFlatLate;
    Button buttonApply, buttonCancel;
    Activity activity;
    DatabaseReference firebaseDatabase;

    long maxArea,monthCost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s_cost);
        firebaseDatabase = FirebaseDatabase.getInstance().getReference("meta");
        editTextbasetime = findViewById(R.id.editTextbasetime);
        editTextbasecost = findViewById(R.id.editTextbasecost);
        editTextAdditionalTime = findViewById(R.id.editTextAdditionalTime);
        editTextAdditionalCost = findViewById(R.id.editTextAdditionalCost);
        editTextFlatTime = findViewById(R.id.editTextFlatTime);
        editTextFlatLate = findViewById(R.id.editTextFlatLate);
        buttonApply = findViewById(R.id.buttonApply);
        buttonCancel = findViewById(R.id.buttonCancel);
        activity = this;
        buttonApply.setOnClickListener(this);
        buttonCancel.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        ProgressDialogHelper.getInstance().getProgressbar(this,"적용중입니다.");

        firebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MetaDTO metaDTO = snapshot.getValue(MetaDTO.class);
                //Log.d("[test]",snapshot.getRef()+".");
                editTextbasetime.setText(metaDTO.getBaseTime() + "");
                editTextbasecost.setText(metaDTO.getBaseCost() + "");
                editTextAdditionalTime.setText(metaDTO.getAdditionalTime() + "");
                editTextAdditionalCost.setText(metaDTO.getAdditionalCost() + "");
                editTextFlatTime.setText(metaDTO.getFlatTime() + "");
                editTextFlatLate.setText(metaDTO.getFlatCost() + "");
                monthCost = metaDTO.getMonthCost();
                maxArea = metaDTO.getMaxArea();
                ProgressDialogHelper.getInstance().removeProgressbar();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                ProgressDialogHelper.getInstance().removeProgressbar();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonApply) {

            long additionalTime,addtionalCost,baseCost,baseTime,flatCost,flatTime=0;
            baseTime = Long.parseLong(String.valueOf(editTextbasetime.getText()));
            baseCost = Long.parseLong(String.valueOf(editTextbasecost.getText()));
            additionalTime = Long.parseLong(String.valueOf(editTextAdditionalTime.getText()));
            addtionalCost = Long.parseLong(String.valueOf(editTextAdditionalCost.getText()));
            flatTime = Long.parseLong(String.valueOf(editTextFlatTime.getText()));
            flatCost = Long.parseLong(String.valueOf(editTextFlatLate.getText()));

            if(baseCost != 0 && additionalTime != 0 && addtionalCost !=0 && baseTime != 0 && flatCost !=0 &&flatTime != 0){
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("additionalTime",additionalTime);
                hashMap.put("additionalCost",addtionalCost);
                hashMap.put("baseCost",baseCost);
                hashMap.put("baseTime",baseTime);
                hashMap.put("flatCost",flatCost);
                hashMap.put("flatTime",flatTime);
                hashMap.put("monthCost",monthCost);
                hashMap.put("maxArea",maxArea);


                firebaseDatabase = FirebaseDatabase.getInstance().getReference("meta");

                firebaseDatabase.setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(activity,"업로드 성공",Toast.LENGTH_LONG).show();
                        onResume();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(activity,"업로드 실패",Toast.LENGTH_LONG).show();
                    }
                });


            }else{
                Toast.makeText(this,"설정에 오류 발생. 빈칸을 모두 채워주세요",Toast.LENGTH_LONG).show();
            }


        } else if (v.getId() == R.id.buttonCancel) {
            finish();
        }
    }
}