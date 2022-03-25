package com.lau.front_end;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        //The code in line 21-->25 is not my own code, it was retrieved when I searched how to change the status bar color from:
        //https://www.geeksforgeeks.org/how-to-change-the-color-of-status-bar-in-an-android-app/
        //N.b: Button color and action bar were also changed and hidden respectively from the themes and values folders (as well as of course layout)
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.teal_200));
        }
    }
    public void Next( View view){ //Question: should i also make a button that takes the user from the converting page to homepage or let them use the back button on phone?
        Intent obj = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(obj);
    }






}