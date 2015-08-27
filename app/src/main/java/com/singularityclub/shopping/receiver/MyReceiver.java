package com.singularityclub.shopping.receiver;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.singularityclub.shopping.Activity.ShowProductionActivity;
import com.singularityclub.shopping.Adapter.SecondLevelAdapter;
import com.singularityclub.shopping.Model.MainClassify;
import com.singularityclub.shopping.Model.SecondClassify;
import com.singularityclub.shopping.Service.UpdateService;
import com.singularityclub.shopping.Utils.Cache.ACache;
import com.singularityclub.shopping.Utils.http.BaseJsonHttpResponseHandler;
import com.singularityclub.shopping.Utils.http.HttpClient;
import com.singularityclub.shopping.Utils.http.HttpUrl;
import com.singularityclub.shopping.Utils.http.JacksonMapper;
import com.singularityclub.shopping.preferences.UserInfo;
import com.singularityclub.shopping.preferences.UserInfo_;

import org.androidannotations.annotations.EReceiver;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.apache.http.Header;
import org.codehaus.jackson.type.TypeReference;

import java.util.ArrayList;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by fenghao on 2015/8/27.
 */
@EReceiver
public class MyReceiver extends BroadcastReceiver {
    @Pref
    protected UserInfo_ userInfo;
    protected ACache aCache;

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();
        String jsonStr = bundle.getString(JPushInterface.EXTRA_EXTRA);
        int notificationId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
        Map<String, Object> json = JacksonMapper.parse(jsonStr);
        Log.i("ddd", json.get("message") + "");
        if ( json.containsKey("message")){
            Intent t = new Intent(context, UpdateService.class);
            context.startService(t);
        }
    }
}
