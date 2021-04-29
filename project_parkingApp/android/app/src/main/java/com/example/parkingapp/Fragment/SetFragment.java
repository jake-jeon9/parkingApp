package com.example.parkingapp.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.parkingapp.R;
import com.example.parkingapp.setting.S_agencyActivity;
import com.example.parkingapp.setting.S_costActivity;
import com.example.parkingapp.setting.S_monthActivity;
import com.example.parkingapp.setting.S_parkingAreaActivity;

public class SetFragment extends Fragment implements View.OnClickListener {
    Context context;

    Button buttonForCost,buttonForAgency,buttonForSetParkingArea,buttonForEnrollMonth;

    public SetFragment() {
    }

    public SetFragment(Context context) {
        this.context = context;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_set, container, false);

        buttonForCost = view.findViewById(R.id.buttonToBasic);
        buttonForAgency = view.findViewById(R.id.buttonToEnrollmentNewAgency);
        buttonForSetParkingArea = view.findViewById(R.id.buttonToTotalArea);
        buttonForEnrollMonth = view.findViewById(R.id.buttonToEnrollment);

        buttonForCost.setOnClickListener(this);
        buttonForAgency.setOnClickListener(this);
        buttonForSetParkingArea.setOnClickListener(this);
        buttonForEnrollMonth.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()){
            case R.id.buttonToBasic :
                intent = new Intent(context,S_costActivity.class);
                break;
            case R.id.buttonToEnrollment :
                intent = new Intent(context, S_monthActivity.class);
                break;
            case R.id.buttonToTotalArea :
                intent = new Intent(context, S_parkingAreaActivity.class);
                break;
            case R.id.buttonToEnrollmentNewAgency :
                intent = new Intent(context, S_agencyActivity.class);
                break;

        }
        if(intent!=null){
            context.startActivity(intent);
        }

    }
}