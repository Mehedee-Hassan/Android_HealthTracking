package com.example.androidbtcontrol;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

public class SplashActivity extends AppCompatActivity {
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {

            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash2);
        getSupportActionBar().hide();

        //findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);

        final Runnable r = new Runnable() {
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        };

        handler.postDelayed(r, 3000);
    }

    final Handler handler = new Handler();


}
