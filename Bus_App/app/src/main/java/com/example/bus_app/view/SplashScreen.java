package com.example.bus_app.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.example.bus_app.R;
import com.example.bus_app.ui.AlertDialogHelper;

public class SplashScreen extends AppCompatActivity {

    private Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        hide_status();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent; intent = new Intent(SplashScreen.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }

    private void hide_status(){
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
    }


    private void init(){
        handler = new Handler();
    }

    @Override
    public void onBackPressed() {
        AlertDialogHelper.ExitBack(this);
    }
}