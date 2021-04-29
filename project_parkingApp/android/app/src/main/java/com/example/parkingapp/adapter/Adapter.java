package com.example.parkingapp.adapter;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.parkingapp.R;
import com.example.parkingapp.model.ParkedDTO;

import java.util.List;
import java.util.Locale;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder>{
    Context context;

    private List<ParkedDTO> parkedList;

    public Adapter(Context context,List<ParkedDTO> parkedList) {
        this.context = context;
        this.parkedList = parkedList;
        //Log.d("[test]","어뎁터에서 리스트 사이즈? " + parkedList.size());
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView plateNumber,parkingTime;
        public ImageView imageOfCar;
        public View rootView;

        public MyViewHolder(@NonNull View v) {
            super(v);
            plateNumber = v.findViewById(R.id.plateNumber);
            parkingTime = v.findViewById(R.id.parkingTime);
            imageOfCar = v.findViewById(R.id.imageOfCar);
            rootView = v;

        }
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_car, parent, false);
        MyViewHolder vh = new MyViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //데이터 받기
        String plateOfNumber = parkedList.get(position).getPlateNumber();
        long parkedTime = parkedList.get(position).getInTime();

        java.util.Calendar cal = java.util.Calendar.getInstance(Locale.KOREAN);
        cal.setTimeInMillis(parkedTime);
        String dateTime = DateFormat.format("MM/dd HH:mm aa", cal).toString();

        holder.parkingTime.setText(dateTime);
        holder.plateNumber.setText(plateOfNumber);

        try {
            String user_photo_root = parkedList.get(position).getImageUrl();
            Glide.with(context)
                    .load(user_photo_root)
                    .fallback(R.drawable.ic_launcher_foreground)
                    .into(holder.imageOfCar);

        } catch (Exception e) {
            Glide.with(context)
                    .load(R.drawable.ic_launcher_foreground)
                    .into(holder.imageOfCar);
        }

    }

    @Override
    public int getItemCount() {
        return parkedList == null ? 0 : parkedList.size();
    }

}
