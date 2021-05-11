package com.example.parkingapp.Fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parkingapp.R;
import com.example.parkingapp.adapter.AdapterList;
import com.example.parkingapp.helper.DateTimeHelper;
import com.example.parkingapp.helper.ProgressDialogHelper;
import com.example.parkingapp.model.DailyData;
import com.example.parkingapp.model.ParkedDTO;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ListFragment extends Fragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    Context context;

    RadioGroup radioGroup;
    RadioButton today, week, month, etc;

    Button buttonSelect1, buttonSelect2, buttonApply;
    AdapterList adapterList;

    EditText editTextStartOfDate, editTextEndOfDate;

    TextView textViewSelectedDate, textViewTotalCount, textViewTotalAccount, textViewEnrollDate;
    List<ParkedDTO> list;
    RecyclerView recyclerView;
    LinearLayout etc1, etc2;

    int YEAR, MONTH, DAY;
    int startOfDay, endOfDay;

    String T_year, T_month, T_day1, T_day2;
    String startYear, endYear, startMonth, endMonth, startDay, endDay;

    long totalAccount = 0, totalParked = 0;
    String i_convert, x_convert;

    boolean isDefault = false;

    public ListFragment() {
    }

    public ListFragment(Context context) {
        this.context = context;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list, container, false);
        context = getContext();

        radioGroup = view.findViewById(R.id.radioGroup);
        today = view.findViewById(R.id.radioButton1);
        week = view.findViewById(R.id.radioButton2);
        month = view.findViewById(R.id.radioButton3);
        etc = view.findViewById(R.id.radioButton4);

        etc1 = view.findViewById(R.id.etc1);
        etc2 = view.findViewById(R.id.etc2);

        editTextStartOfDate = view.findViewById(R.id.editTextStartOfDate);
        editTextEndOfDate = view.findViewById(R.id.editTextEndOfDate);
        textViewEnrollDate = view.findViewById(R.id.textViewEnrollDate);

        buttonSelect1 = view.findViewById(R.id.buttonSelect1);
        buttonSelect2 = view.findViewById(R.id.buttonSelect2);
        buttonApply = view.findViewById(R.id.buttonApply);

        recyclerView = view.findViewById(R.id.recycleViewOfRowAccount);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(container.getContext());
        recyclerView.setLayoutManager(linearLayoutManager1);

        textViewSelectedDate = view.findViewById(R.id.textViewSelectedDate);
        textViewTotalCount = view.findViewById(R.id.textViewTotalCount);
        textViewTotalAccount = view.findViewById(R.id.textViewTotalAccount);

        buttonApply.setOnClickListener(this);
        buttonSelect1.setOnClickListener(this);
        buttonSelect2.setOnClickListener(this);

        selectDateSwitch(0);
        radioGroup.setOnCheckedChangeListener(this);

        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonApply:
                textViewTotalCount.setText("");
                textViewTotalAccount.setText("");
                getDate();
                break;
            case R.id.buttonSelect1:
                showDatePickerDialog(1);

                break;
            case R.id.buttonSelect2:
                showDatePickerDialog(2);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        int[] date = DateTimeHelper.getInstance().getDate();
        YEAR = date[0];
        MONTH = date[1];
        DAY = date[2];
        textViewEnrollDate.setText(String.format("%04d-%02d-%02d", YEAR, MONTH, DAY));
    }

    private void getDate() {
        list = new ArrayList<>();
        list.clear();
        totalAccount = 0;
        totalParked = 0;
        //여기에 검색결과 가져오는 함수 셋팅

        if (isDefault) {
            Log.d("[test]", "날짜 1 : " + startYear + "/" + startMonth + "/" + startDay);
            Log.d("[test]", "날짜 2 : " + endYear + "/" + endMonth + "/" + endDay);

            DatabaseReference def = FirebaseDatabase.getInstance().getReference("parkingList").child(startYear);
            ProgressDialogHelper.getInstance().getProgressbar(context, "잠시만 기다려주세요.");
            for (int start = Integer.parseInt(startMonth); start <= Integer.parseInt(endMonth); start++) {

                x_convert = "" + start;
                if (start < 10) {
                    x_convert = "0" + start;
                }
                for (int i = Integer.parseInt(startDay); i <= Integer.parseInt(endDay); i++) {

                    i_convert = "" + i;
                    if (i < 10) {
                        i_convert = "0" + i;
                    }
                    def.child(x_convert).child(i_convert).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if (snapshot.hasChild("totalParked")) {

                                DailyData dailyData = snapshot.getValue(DailyData.class);
                                totalAccount += dailyData.getTotalAccount();
                                totalParked += dailyData.getTotalParked();

                                textViewTotalAccount.setText(totalAccount + "");
                                textViewTotalCount.setText(totalParked + "");

                            }
                            ProgressDialogHelper.getInstance().removeProgressbar();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(context, "통신 오류", Toast.LENGTH_LONG).show();
                            ProgressDialogHelper.getInstance().removeProgressbar();
                        }
                    });

                    def.child(x_convert).child(i_convert).child("parkedList").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                //Log.d("[test]", "second : snapshot.getRef() : " + snapshot.getRef());
                                String plateOfNumber = ds.getKey();
                                ParkedDTO parkedDTO = ds.getValue(ParkedDTO.class);
                                parkedDTO.setPlateNumber(plateOfNumber);
                                //Log.d("[test]", plateOfNumber + "파크드리스트 넘버");
                                list.add(parkedDTO);

                            }
                            adapterList = new AdapterList(context, list);
                            adapterList.setToday(T_year + "/" + T_month + "/" + i_convert);
                            recyclerView.setAdapter(adapterList);
                            ProgressDialogHelper.getInstance().removeProgressbar();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(context, "통신 오류", Toast.LENGTH_LONG).show();
                            ProgressDialogHelper.getInstance().removeProgressbar();
                        }
                    });
                }
            }
        } else {
            Log.d("[test]", "T_year :" + T_year);
            Log.d("[test]", "T_month :" + T_month);
            Log.d("[test]", "T_day1 :" + T_day1);
            Log.d("[test]", "T_day2 :" + T_day2);

            DatabaseReference def = FirebaseDatabase.getInstance().getReference("parkingList").child(T_year).child(T_month);
            ProgressDialogHelper.getInstance().getProgressbar(context, "잠시만 기다려주세요.");
            for (int i = Integer.parseInt(T_day1); i <= Integer.parseInt(T_day2); i++) {
                i_convert = "" + i;
                if (i < 10) {
                    i_convert = "0" + i;
                }
                def.child(i_convert).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.hasChild("totalParked")) {

                            DailyData dailyData = snapshot.getValue(DailyData.class);
                            totalAccount += dailyData.getTotalAccount();
                            totalParked += dailyData.getTotalParked();

                            textViewTotalAccount.setText(totalAccount + "");
                            textViewTotalCount.setText(totalParked + "");

                        }
                        ProgressDialogHelper.getInstance().removeProgressbar();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(context, "통신 오류", Toast.LENGTH_LONG).show();
                        ProgressDialogHelper.getInstance().removeProgressbar();
                    }
                });

                def.child(i_convert).child("parkedList").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            String plateOfNumber = ds.getKey();
                            ParkedDTO parkedDTO = ds.getValue(ParkedDTO.class);
                            parkedDTO.setPlateNumber(plateOfNumber);
                            //Log.d("[test]", plateOfNumber + "파크드리스트 넘버");
                            list.add(parkedDTO);

                        }
                        adapterList = new AdapterList(context, list);
                        adapterList.setToday(T_year + "/" + T_month + "/" + i_convert);
                        recyclerView.setAdapter(adapterList);
                        ProgressDialogHelper.getInstance().removeProgressbar();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(context, "통신 오류", Toast.LENGTH_LONG).show();
                        ProgressDialogHelper.getInstance().removeProgressbar();
                    }
                });
            }

        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.radioButton1:
                isDefault = false;
                selectDateSwitch(0);
                textViewSelectedDate.setText(textViewEnrollDate.getText());
                textViewSelectedDate.setVisibility(View.VISIBLE);
                onResume();
                String temp[] = adaptDate(MONTH, DAY);
                dateSet(YEAR + "", temp[0], temp[1], temp[1]);
                break;
            case R.id.radioButton2:
                isDefault = false;
                selectDateSwitch(0);
                int numOfWeek = getNumOfWeek();//일~ 월 1~7
                if (DAY < 8) {
                    startOfDay = 1;
                } else {
                    if (numOfWeek == 1) {
                        startOfDay = DAY - 6;
                    } else {
                        startOfDay = DAY - numOfWeek + 2;
                    }
                }
                endOfDay = DAY;

                String tempM = MONTH + "";
                String tmepSD = startOfDay + "";
                String tmepED = endOfDay + "";

                if (MONTH < 10) {
                    tempM = "0" + MONTH;
                }

                if (startOfDay < 10) {
                    tmepSD = "0" + startOfDay;
                }

                if (endOfDay < 10) {
                    tmepED = "0" + endOfDay;
                }

                String result1 = YEAR + "-" + tempM + "-" + tmepSD;
                String result2 = YEAR + "-" + tempM + "-" + tmepED;

                textViewSelectedDate.setText(result1 + " ~ " + result2);
                textViewSelectedDate.setVisibility(View.VISIBLE);
                onResume();
                dateSet(YEAR + "", tempM, tmepSD, tmepED);

                break;
            case R.id.radioButton3:
                isDefault = false;
                selectDateSwitch(0);

                String temp1[] = adaptDate(MONTH, DAY);

                String result3 = YEAR + "-" + temp1[0] + "-" + "01";
                String result4 = YEAR + "-" + temp1[0] + "-" + temp1[1];
                textViewSelectedDate.setText(result3 + " ~ " + result4);
                textViewSelectedDate.setVisibility(View.VISIBLE);

                onResume();
                dateSet(YEAR + "", temp1[0], "01", temp1[1]);

                break;
            case R.id.radioButton4:
                isDefault = true;
                selectDateSwitch(1);
                textViewSelectedDate.setVisibility(View.GONE);

                break;
        }


    }

    private void dateSet(String year, String month, String startDate, String endDate) {
        T_year = year;
        T_month = month;
        T_day1 = startDate;
        T_day2 = endDate;

    }

    private String[] adaptDate(int month, int day) {
        String[] result = new String[2];
        result[0] = MONTH + "";
        if (MONTH < 10) {
            result[0] = "0" + MONTH;
        }

        result[1] = "" + DAY;
        if (DAY < 10) result[1] = "0" + DAY;

        return result;
    }

    private int getNumOfWeek() {
        Calendar calendar = Calendar.getInstance();
        int dateOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return dateOfWeek;
    }

    public void selectDateSwitch(int power) {
        if (power == 0) {
            etc1.setVisibility(View.GONE);
            etc2.setVisibility(View.GONE);
        } else if (power == 1) {
            etc1.setVisibility(View.VISIBLE);
            etc2.setVisibility(View.VISIBLE);
        }
    }

    //날짜선택창
    private void showDatePickerDialog(int type) {

        final int temp_yy = YEAR;
        final int temp_mm = MONTH;
        final int temp_dd = DAY;

        //DatePickerDialog(액티비티,이벤트처리,년,월,일
        DatePickerDialog dialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                YEAR = year;
                MONTH = month + 1;
                DAY = dayOfMonth;

                String resultY = YEAR + "";
                String resultM = MONTH + "";
                String resultD = DAY + "";

                if (MONTH < 10) {
                    resultM = "0" + MONTH;
                }
                if (DAY < 10) {
                    resultD = "0" + DAY;
                }

                if (type == 1) {
                    editTextStartOfDate.setText(resultY + "-" + resultM + "-" + resultD);

                    startYear = String.valueOf(editTextStartOfDate.getText()).substring(0, 4);
                    startMonth = String.valueOf(editTextStartOfDate.getText()).substring(5, 7);
                    startDay = String.valueOf(editTextStartOfDate.getText()).substring(8);


                } else if (type == 2) {
                    editTextEndOfDate.setText(resultY + "-" + resultM + "-" + resultD);

                    endYear = String.valueOf(editTextEndOfDate.getText()).substring(0, 4);
                    endMonth = String.valueOf(editTextEndOfDate.getText()).substring(5, 7);
                    endDay = String.valueOf(editTextEndOfDate.getText()).substring(8);

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

        dialog.setTitle("날짜선택");
        dialog.show();

    }
}