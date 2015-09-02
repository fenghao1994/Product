package com.singularityclub.shopping.Application;

import android.app.Activity;
import android.app.Application;

import java.util.ArrayList;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by fenghao on 2015/8/24.
 */
public class MyApplication extends Application{


    // 存放activity的集合
    private ArrayList<Activity> list1 = new ArrayList<Activity>();
    private static MyApplication instance;
    public String productID;

    /**
     * 利用单例模式获取MyAppalication实例
     *
     * @return
     */
    public static MyApplication getInstance() {
        if (null == instance) {
            instance = new MyApplication();
        }
        return instance;
    }


    /**
     * 添加activity到list集合
     *
     * @param activity
     */
    public void addActivity(Activity activity) {
        list1.add(activity);

    }

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

    /**
     * 退出集合所有的activity
     */
    public void exit() {
        try {
            for (Activity activity : list1) {
                activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }

    public ArrayList<String> getList() {
        return list;
    }

    public void setList(ArrayList<String> list) {
        this.list = list;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }
}
