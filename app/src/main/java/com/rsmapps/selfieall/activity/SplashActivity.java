package com.rsmapps.selfieall.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.android.gms.ads.MobileAds;
import com.rsmapps.selfieall.R;

public class SplashActivity extends BaseActivity {
 
    private static int SPLASH_TIME_OUT = 1700;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //MobileAds.initialize(this, getString(R.string.admob_app_id));


        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                //Intent i = new Intent(SplashActivity.this, StartActivity.class);
                Intent i = new Intent(SplashActivity.this, HomeActivity.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}