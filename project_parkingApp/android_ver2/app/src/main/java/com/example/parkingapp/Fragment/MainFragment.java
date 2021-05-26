package com.example.parkingapp.Fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parkingapp.R;
import com.example.parkingapp.adapter.Adapter;
import com.example.parkingapp.helper.CalendarHelper;
import com.example.parkingapp.helper.ConvertDateHelper;
import com.example.parkingapp.helper.DateTimeHelper;
import com.example.parkingapp.helper.ProgressDialogHelper;
import com.example.parkingapp.model.DailyData;
import com.example.parkingapp.model.CostDTO;
import com.example.parkingapp.model.ParkedDTO;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class MainFragment extends Fragment {
    TextView date, availableLot, usedLot, totalLot, textViewAccount,textViewCount,textViewNoList;
    RecyclerView recyclerView;

    Context context;
    Activity activity;
    Adapter adapter;


    //파이어베이스
    String today;
    String availableLots;
    List<ParkedDTO> parkedList;
    int YEAR, MONTH, DAY;


    public MainFragment() {
    }

    public MainFragment(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        date = rootView.findViewById(R.id.textViewDate);
        availableLot = rootView.findViewById(R.id.textViewAvailableLot);
        usedLot = rootView.findViewById(R.id.textViewUsedLot);
        totalLot = rootView.findViewById(R.id.textViewTotalLot);
        textViewAccount = rootView.findViewById(R.id.textViewAccount);
        textViewCount = rootView.findViewById(R.id.textViewCount);
        activity = getActivity();
        recyclerView = rootView.findViewById(R.id.recycleView);
        textViewNoList = rootView.findViewById(R.id.textViewNoList);

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(container.getContext());
        recyclerView.setLayoutManager(linearLayoutManager1);

        return rootView;
    }

    //퍼미션 얻기
    private void getPermission() {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

            } else {
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.INTERNET,
                                Manifest.permission.CAMERA}, 100);
            }
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        //시작시마다 날짜 얻어오기
        today = CalendarHelper.getInstance().getCurrentTimeNoType();
        int[] date2 = DateTimeHelper.getInstance().getDate();
        YEAR = date2[0];
        MONTH = date2[1];
        DAY = date2[2];
        date.setText(String.format("%04d-%02d-%02d", YEAR, MONTH, DAY));
        ProgressDialogHelper.getInstance().getProgressbar(context, "잠시만 기다려주세요");
        getChatData();
        getPermission();
        updateDate();

    }


    private void updateDate() {
        HashMap<String, Object> hashMap2 = new HashMap<>();
        String result[] = ConvertDateHelper.getInstance().adaptDate(MONTH, DAY);
        DatabaseReference def = FirebaseDatabase.getInstance().getReference("parkingList").child(YEAR + "").child(result[0]).child(result[1]);

        def.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long money = 0;
                long currunt = 0;
                for (DataSnapshot ds : snapshot.getChildren()){
                    //Log.d("[test]","ds.getRef()" +ds.getKey());

                    if(ds.getKey().equals("parkedList")){
                        //Log.d("[test]","parkedList진입?");
                        for(DataSnapshot ds2 : ds.getChildren()){
//
                            if(ds2.hasChild("paid")){
                                //Log.d("[test]","paid 진입");
                                money += ds2.child("paid").getValue(Long.class);
                            }
                            if(ds2.child("state").getValue().equals("in")){
                                //Log.d("[test]","in 진입");
                                currunt +=1;
                            }
                        }
                    }

                }
                hashMap2.put("currentUsed",currunt);
                hashMap2.put("totalAccount",money);
                DatabaseReference def2 = FirebaseDatabase.getInstance().getReference("parkingList").child(YEAR + "").child(result[0]).child(result[1]);
                def2.updateChildren(hashMap2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getChatData() {
        parkedList = new ArrayList<>();
        String temp[] = ConvertDateHelper.getInstance().adaptDate(MONTH, DAY);
        DatabaseReference getTodayItems = FirebaseDatabase.getInstance().getReference("parkingList").child("" + YEAR)
                .child(temp[0]).child(temp[1]);

        getTodayItems.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                parkedList.clear();

                Log.d("[test]","main : "+snapshot.getRef()+"");
                DailyData dailyData = snapshot.getValue(DailyData.class);

                String str_totalparked = "0";
                String str_totalAccount = "0";
                String str_currentUsed = "0";

                if (snapshot.hasChildren()){

                    if(!Objects.isNull(dailyData.getTotalParked())){
                        str_totalparked = dailyData.getTotalParked()+"";
                    }
                    if(!Objects.isNull(dailyData.getTotalAccount())){
                        str_totalAccount = dailyData.getTotalAccount() + "";
                    }
                    if(!Objects.isNull(dailyData.getCurrentUsed())){
                        str_currentUsed = dailyData.getCurrentUsed() + "";
                    }
                }

                textViewCount.setText(str_totalparked);
                textViewAccount.setText(str_totalAccount);
                usedLot.setText(str_currentUsed);
                for (DataSnapshot ds : snapshot.getChildren()) {
                    if (ds.hasChildren()) {
                        //Log.d("[test]", " if문 헤즈 칠드런 진입");
                        for (DataSnapshot ds2 : ds.getChildren()) {
                            String plateOfCar = ds2.getKey();
                            ParkedDTO parkedDTO = ds2.getValue(ParkedDTO.class);
                            parkedDTO.setPlateNumber(plateOfCar);
                            if(parkedDTO.getState().equals("in")){
                                parkedList.add(parkedDTO);
                            }
                        }

                        adapter = new Adapter(context, parkedList);
                        recyclerView.setAdapter(adapter);

                        if (parkedList.size() > 0){
                            textViewNoList.setVisibility(View.GONE);
                        }else{
                            textViewNoList.setVisibility(View.VISIBLE);
                        }

                    }
                }

                DatabaseReference getMeta = FirebaseDatabase.getInstance().getReference("meta");
                getMeta.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        CostDTO costDTO = snapshot.getValue(CostDTO.class);
                        totalLot.setText(costDTO.getMaxArea() + "");
                        availableLots = "" + (Integer.parseInt(String.valueOf(costDTO.getMaxArea())) - Integer.parseInt(String.valueOf(usedLot.getText())));
                        availableLot.setText(availableLots);
                        ProgressDialogHelper.getInstance().removeProgressbar();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(activity, "정보 불러오기 실패", Toast.LENGTH_LONG).show();
                        ProgressDialogHelper.getInstance().removeProgressbar();
                    }
                });

                //textViewCount.setText(totalParked+"");
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(activity, "정보 불러오기 실패", Toast.LENGTH_LONG).show();
                ProgressDialogHelper.getInstance().removeProgressbar();
            }
        });



    }
}