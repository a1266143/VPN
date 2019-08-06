package com.xiaojun.vpn;

import android.app.Application;

/**
 * Crated by xiaojun on 2019/8/1 9:28
 */
public class MyApplication extends Application {

    private static MyApplication myApplication;

    public static MyApplication getInstance(){
        return myApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
    }
}
