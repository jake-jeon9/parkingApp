package com.example.parkingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.example.parkingapp.Fragment.InAndOutFragment;
import com.example.parkingapp.Fragment.ListFragment;
import com.example.parkingapp.Fragment.MainFragment;
import com.example.parkingapp.Fragment.SetFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView nav_bottom;
    MainFragment mainFragment;
    InAndOutFragment inAndOutFragment;
    ListFragment listFragment;
    SetFragment setFragment;

    Context context;
    String plateOfNum,inTime,totalParkedTime,cost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        context = this;
        mainFragment = new MainFragment(context);
        inAndOutFragment = new InAndOutFragment(context);
        listFragment = new ListFragment(context);
        setFragment = new SetFragment(context);

        nav_bottom = findViewById(R.id.nav_bottom);

        nav_bottom.setOnNavigationItemSelectedListener(this);




        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container, mainFragment).commit();
        }

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_Home:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, mainFragment).commit();
                break;
            case R.id.navigation_InAndOut:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, inAndOutFragment).commit();
                break;
            case R.id.navigation_List:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, listFragment).commit();
                break;
            case R.id.navigation_Set:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, setFragment).commit();
                break;


        }
        return true;
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);


    }
}