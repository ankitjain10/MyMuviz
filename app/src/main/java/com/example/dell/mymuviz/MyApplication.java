package com.example.dell.mymuviz;

import android.app.Application;
import android.content.Context;

import extras.MySqlAdapter;


public class MyApplication extends Application {
    private static MyApplication mInstance;
    private static MySqlAdapter mySqlAdapter;
    public static final String API_KEY_ROTTEN_TOMATOES = "54wzfswsa4qmjg8hjwa64d4c";

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance=this;
        mySqlAdapter=new MySqlAdapter(this);
    }

    public static MyApplication getmInstance(){
        return mInstance;
    }

    public static Context getAppcontext(){
        return mInstance.getApplicationContext();

    }

    public  synchronized static MySqlAdapter getWritableAdapter(){
        if(mySqlAdapter==null) {
            mySqlAdapter = new MySqlAdapter(getAppcontext());
        }return mySqlAdapter;
        }
    }

