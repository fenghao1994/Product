package com.singularityclub.shopping.Activity;

import android.app.Activity;
import android.os.Bundle;

import com.singularityclub.shopping.Application.MyApplication;

/**
 * Created by fenghao on 2015/8/28.
 */
public class BaseActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().addActivity(BaseActivity.this);
    }
}
