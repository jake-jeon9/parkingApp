package com.example.parkingapp.setting;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.parkingapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class S_parkingAreaActivity extends AppCompatActivity implements View.OnClickListener {

    TextView textViewCurrent;
    Button buttonApply, buttonCancel;
    EditText editTextSetMaxArea;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s_parking_area);

        textViewCurrent = findViewById(R.id.textViewCurrent);
        buttonApply = findViewById(R.id.buttonApply);
        buttonCancel = findViewById(R.id.buttonCancel);
        editTextSetMaxArea = findViewById(R.id.editTextSetMaxArea);
        context = this;

        buttonApply.setOnClickListener(this);
        buttonCancel.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();

        DatabaseReference database = FirebaseDatabase.getInstance().getReference("meta").child("maxArea");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                textViewCurrent.setText("현재 설정된 구역 수 : "+snapshot.getValue(long.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "데이터 설정 오류", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (R.id.buttonApply == v.getId()) {

            long value = Long.parseLong(String.valueOf(editTextSetMaxArea.getText()));
            DatabaseReference database = FirebaseDatabase.getInstance().getReference("meta").child("maxArea");
            database.setValue(value);

            editTextSetMaxArea.setText("");
            onResume();
        } else if (R.id.buttonCancel == v.getId()) {
            finish();
        }
    }
}