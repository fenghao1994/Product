package com.singularityclub.shopping.Application;

import android.app.Application;



import java.util.ArrayList;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by fenghao on 2015/8/24.
 */
public class MyApplication extends Application{


    /**
     * 用于存储搜索历史
     * 每次的增加历史的时候往最前增加
     * 取得时候只取前三个
     */
    ArrayList<String> list = null;
    @Override
    public void onCreate() {
        super.onCreate();
        list = new ArrayList<>();
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }

    public ArrayList<String> getList() {
        return list;
    }

    public void setList(ArrayList<String> list) {
        this.list = list;
    }
}
