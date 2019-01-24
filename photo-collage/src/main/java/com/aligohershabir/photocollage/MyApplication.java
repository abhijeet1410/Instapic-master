package com.aligohershabir.photocollage;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

public class MyApplication extends MultiDexApplication {
    public static final String TAG = MyApplication.class.getSimpleName();

    private static MyApplication ourInstance = new MyApplication();

    static MyApplication getInstance() {
        return ourInstance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public MyApplication() {
        ourInstance = this;
    }

}