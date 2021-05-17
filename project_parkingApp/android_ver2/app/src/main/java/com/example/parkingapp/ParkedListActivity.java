package com.example.parkingapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.parkingapp.adapter.RowAdapter;
import com.example.parkingapp.helper.ConvertDateHelper;
import com.example.parkingapp.helper.ProgressDialogHelper;
import com.example.parkingapp.model.CostDTO;
import com.example.parkingapp.model.ParkedDTO;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ParkedListActivity extends AppCompatActivity implements View.OnClickListener {
    RadioButton radioButton[];
    Context context;
    Activity activity;
    List<ParkedDTO> parkedList;
    TextView textView53,textView532;
    RowAdapter rowAdapter;
    RecyclerView recyclerView;

    RadioGroup radioGroup;
    LinearLayout layout;
    Button buttonSelect, buttonCancel;

    int selectedNumber = -1;

    int YEAR, MONTH, DAY;
    CostDTO costDTO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parked_list);

        context = this;
        activity = this;
        layout = findViewById(R.id.listRow);
        textView53 = findViewById(R.id.textView531);
        recyclerView = findViewById(R.id.listView);

        buttonSelect = findViewById(R.id.buttonSelectedOnList);
        buttonCancel = findViewById(R.id.buttonCancelOnList);
        radioGroup = findViewById(R.id.radioGroup);

        YEAR = Integer.parseInt(getIntent().getStringExtra("YEAR"));
        MONTH = Integer.parseInt(getIntent().getStringExtra("MONTH"));
        DAY = Integer.parseInt(getIntent().getStringExtra("DAY"));

        buttonCancel.setOnClickListener(this);
        buttonSelect.setOnClickListener(this);
                textView53.setText(YEAR + "/" + MONTH + "/" + DAY + " 주차 목록");

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager1);
        textView532 = findViewById(R.id.textView532);
        textView532.setVisibility(View.GONE);
        setDate();
    }

    private void setDate() {
        DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference("meta");

        firebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                costDTO = snapshot.getValue(CostDTO.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();

        ProgressDialogHelper.getInstance().getProgressbar(context, "잠시만 기다려주세요.");
        parkedList = new ArrayList<>();

        String date[] = ConvertDateHelper.getInstance().adaptDate(MONTH, DAY);

        DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference("parkingList")
                .child(YEAR + "").child(date[0]).child(date[1]).child("parkedList");

        firebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                parkedList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String plateOfnum = ds.getKey();
                    ParkedDTO parkedDTO = ds.getValue(ParkedDTO.class);
                    parkedDTO.setPlateNumber(plateOfnum);
                    if (parkedDTO.getState().equals("in")) {
                        DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference("member");
                        firebaseDatabase.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot ds : snapshot.getChildren()) {
                                    if (ds.getKey().equals(plateOfnum)) {
                                        parkedDTO.setMember(true);
                                        // Log.d("[test]",ds.getKey()+"키값이고 현재 차량번호?"+ plateOfnum+", 일치");

                                    }
                                }
                                parkedList.add(parkedDTO);
                                //Log.d("[test]","리스트 사이즈?"+parkedList.size());
                                rowAdapter = new RowAdapter(context, parkedList, costDTO, radioGroup);

                                rowAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        //Toast.makeText(context,"라디오 버튼 클릭됨"+position+"번. ",Toast.LENGTH_LONG).show();
                                        selectedNumber = position;
                                        radioButton = new RadioButton[parkedList.size()];
                                        /*
                                        for(int i = 0 ; i<parkedList.size();i++){
                                            if(i != position){
                                                radioButton[i] = (RadioButton)rowAdapter.getRadioButton();
                                                radioButton[i].setChecked(false);
                                            }
                                        }

                                         */
                                    }
                                });
                                recyclerView.setAdapter(rowAdapter);
                                if (parkedList.size() == 0) {
                                    layout.setVisibility(View.GONE);
                                    textView532.setVisibility(View.VISIBLE);
                                } else {
                                    layout.setVisibility(View.VISIBLE);
                                    textView532.setVisibility(View.GONE);

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }


                }

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
        if (v.getId() == R.id.buttonCancelOnList) {

            finish();
        } else if(R.id.buttonSelectedOnList == v.getId()){
            //Toast.makeText(this,"선택클릭됨.",Toast.LENGTH_LONG).show();

            if (selectedNumber != -1) {

                ParkedDTO parkedDTO = parkedList.get(selectedNumber);
                Log.d("[test]","sss?" + selectedNumber);
                Log.d("[test]","plateOfNum?" + parkedDTO.getPlateNumber());
                //InAndOutFragment.setChoiceNum(plateOfNum);
                //여기다가 리절트 셋하기
                Intent intent = new Intent();
                //intent.putExtra("plateOfNumplateOfNum",plateOfNum);
                intent.putExtra("parkedDTO",parkedDTO);
                intent.putExtra("metaDTO", costDTO);
                setResult(RESULT_OK,intent);
                finish();
            }
        }
    }

}