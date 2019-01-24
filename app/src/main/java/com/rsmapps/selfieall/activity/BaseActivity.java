package com.rsmapps.selfieall.activity;

import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

/**
 * Created by A1VSZHT2 on 4/14/2018.
 */

public class BaseActivity extends AppCompatActivity {

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.inject(this);
    }
}
