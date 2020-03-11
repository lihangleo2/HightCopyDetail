package com.lihang.scrollandmove;

import android.app.Application;
import android.content.Context;

/**
 * Created by leo
 * on 2020/3/11.
 */
public class MyApplication extends Application {
    private static MyApplication context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    public static Context getContext() {
        return context;
    }
}
