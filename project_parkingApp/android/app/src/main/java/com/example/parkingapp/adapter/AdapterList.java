package com.example.parkingapp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parkingapp.R;
import com.example.parkingapp.helper.CalendarHelper;
import com.example.parkingapp.model.ParkedDTO;

import java.text.DateFormat;
import java.text.Format;
import java.util.List;

public class AdapterList extends RecyclerView.Adapter<AdapterList.MyViewHolder> {
    Context context;

    private List<ParkedDTO> parkedList;
    String today;

    public AdapterList(Context context, List<ParkedDTO> parkedList) {
        this.context = context;
        this.parkedList = parkedList;

    }

    public void setToday(String today) {
        this.today = today;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView plateNumber, parkingDate, totalParkedTime, paid, etc;
        public View rootView;

        public MyViewHolder(@NonNull View v) {
            super(v);
            plateNumber = v.findViewById(R.id.textView10);
            parkingDate = v.findViewById(R.id.textView14);
            totalParkedTime = v.findViewById(R.id.totalParkedTime);
            paid = v.findViewById(R.id.paid);
            etc = v.findViewById(R.id.etc);
            rootView = v;

        }
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_account, parent, false);
        MyViewHolder vh = new MyViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //데이터 받기

        String plateOfNumber = parkedList.get(position).getPlateNumber();

        long inTime =  parkedList.get(position).getInTime();
        long outTime = parkedList.get(position).getOutTime();
        long currentTime = System.currentTimeMillis();
        long parkedTime = (outTime-inTime)/1000/60;
        if(parkedTime<0){
            parkedTime = (currentTime-inTime)/1000/60;
        }
        String convertTime = parkedTime+"분";
        if(parkedTime>60){
            convertTime = String.format("%02d시간 %02d분",(parkedTime/60),(parkedTime%60));
        }

        String parkedDate = CalendarHelper.getInstance().getReqular(inTime+"");
        String state = parkedList.get(position).getState();
        //Log.d("[test]","state?" + state);
        String paid = parkedList.get(position).getPaid() + "";
        String etc = "";
        if (parkedList.get(position).isCoupon()) {
            etc = "쿠폰";
        }
        if(state.equals("in")){
            etc = "주차중";
        }


        holder.parkingDate.setText(parkedDate);
        holder.plateNumber.setText(plateOfNumber);
        holder.totalParkedTime.setText(convertTime);
        holder.paid.setText(paid);
        holder.etc.setText(etc);

    }

    @Override
    public int getItemCount() {
        return parkedList == null ? 0 : parkedList.size();
    }

}
