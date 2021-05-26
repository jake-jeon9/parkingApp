package com.example.parkingapp.adapter;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parkingapp.R;
import com.example.parkingapp.model.CostDTO;
import com.example.parkingapp.model.ParkedDTO;

import java.util.List;
import java.util.Locale;

public class RowAdapter extends RecyclerView.Adapter<RowAdapter.MyViewHolder> {

    Context context;

    private List<ParkedDTO> parkedList;
    CostDTO costDTO;

    RadioGroup radioGroup;
    AdapterView.OnItemClickListener onItemClickListener;
    int selectedPosition = -1;

    RadioButton radioButton;

    public RadioButton getRadioButton() {
        return radioButton;
    }

    public RowAdapter(Context context, List<ParkedDTO> parkedList, CostDTO costDTO, RadioGroup radioGroup) {
        this.context = context;
        this.parkedList = parkedList;
        this.costDTO = costDTO;
        this.radioGroup = radioGroup;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_listlayout, parent, false);
        MyViewHolder vh = new MyViewHolder(view,this);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //데이터 받기

        String plateOfNumber = parkedList.get(position).getPlateNumber();
        long inTime =  parkedList.get(position).getInTime();

        java.util.Calendar cal = java.util.Calendar.getInstance(Locale.KOREAN);
        cal.setTimeInMillis(inTime);

        String dateTime = DateFormat.format("HH:mm", cal).toString();

        long currentTime = System.currentTimeMillis() - inTime;
        long calTime = currentTime/1000/60;

        String gepTime = calTime+"분";
        if(calTime>60){
            long hour = calTime/60;
            long min =calTime % 60;
            gepTime = hour + "시간 "+min + "분";

        }

        String member = "일반";
        if (parkedList.get(position).isMember()){
            member = "등록";
        }

        long expectCost = 0;

        //Log.d("[test]","caltime? " + calTime);
        if (calTime < costDTO.getBaseTime()){
            expectCost = costDTO.getBaseCost();
            //Log.d("[test]","기본요금");
        }else if( calTime >= costDTO.getMaxtime()*60 ){
            expectCost = costDTO.getMaxcost();
            //Log.d("[test]","정액요금");
        }else{
            //Log.d("[test]","일반요금");
            expectCost = ((calTime- costDTO.getBaseTime())/ costDTO.getAdditionalTime() * costDTO.getAdditionalCost()) + costDTO.getBaseCost();
        }

        holder.textViewPlateOfNum.setText(plateOfNumber);
        holder.textViewIntime.setText(dateTime);
        holder.textViewIsMember.setText(member);
        holder.textViewExpectCost.setText(expectCost+"원");
        holder.textViewTotalParkedTime.setText(gepTime);
        radioButton = holder.radioButton;
    }

    @Override
    public int getItemCount() {
        return parkedList == null ? 0 : parkedList.size();
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public void onItemHolderClick(MyViewHolder myViewHolder){
        if(onItemClickListener !=null){
            onItemClickListener.onItemClick(null,myViewHolder.itemView,myViewHolder.getAdapterPosition(),myViewHolder.getItemId());
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView textViewPlateOfNum, textViewIntime, textViewIsMember, textViewExpectCost,textViewTotalParkedTime;
        public RadioButton radioButton;
        public View rootView;
        RowAdapter rowAdapter;

        public MyViewHolder(@NonNull View v,final RowAdapter rowAdapter) {
            super(v);
            this.rowAdapter = rowAdapter;
            textViewPlateOfNum = v.findViewById(R.id.textViewPlateOfNum);
            textViewIntime = v.findViewById(R.id.textViewIntime);
            textViewIsMember = v.findViewById(R.id.textViewIsMember);
            textViewExpectCost = v.findViewById(R.id.textViewExpectCost);
            textViewTotalParkedTime = v.findViewById(R.id.textViewTotalParkedTime);
            radioButton = v.findViewById(R.id.radioButton);
            radioButton.setOnClickListener(this);
            rootView = v;
            rootView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            selectedPosition = getAdapterPosition();
            rowAdapter.onItemHolderClick(MyViewHolder.this);
        }
    }



}
