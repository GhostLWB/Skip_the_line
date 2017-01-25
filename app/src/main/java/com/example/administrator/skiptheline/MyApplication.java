package com.example.administrator.skiptheline;

import android.app.Application;
import android.content.Context;

/**
 * Created by 黎文彬 on 2017/1/19 0019.
 */
public class MyApplication extends Application{
    private static Context myContext;

    @Override
    public void onCreate() {
        super.onCreate();
        myContext=getApplicationContext();
    }
    public static Context getContext(){
        return myContext;
    }
}
