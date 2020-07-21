package com.pmh.iotblog;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // pause app in 1.5s
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                SharedPreferences userPerf = getApplicationContext()
                        .getSharedPreferences("user",Context.MODE_PRIVATE);
                isFirstTime();
                boolean isLoggedIn = userPerf.getBoolean("isLoggedIn", false);

                if(isLoggedIn){
                    startActivity(new Intent(MainActivity.this,HomeActivity.class));
                    finish();
                }
                else{
                    isFirstTime();
                }

            }
        }, 1500);
    }

    private void isFirstTime() {
        //
        SharedPreferences preferences = getApplication().getSharedPreferences("onBoard", Context.MODE_PRIVATE) ;
        boolean isFirstTime = preferences.getBoolean("isFirstTime", true);
        // default value true
         if(isFirstTime){
             // if true then  its firt time
             SharedPreferences.Editor editor = preferences.edit();
             editor.putBoolean("isFirstTime",false);
             editor.apply();
             // start OnBoard Activity
             startActivity(new Intent(MainActivity.this, OnBoardActivity.class));
             finish();
         }
         else{
             // start Auth Activity
             startActivity(new Intent(MainActivity.this, AuthActivity.class));
             finish();

         }
    }
}