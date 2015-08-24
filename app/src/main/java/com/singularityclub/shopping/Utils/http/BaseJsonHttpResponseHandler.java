package com.singularityclub.shopping.Utils.http;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;

import java.util.Map;

/**
 * Created by fenghao on 2015/6/27.
 */
public class BaseJsonHttpResponseHandler extends TextHttpResponseHandler {
    private Context context;

    public BaseJsonHttpResponseHandler(Context context){
        this.context = context;
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        Toast.makeText(context, "加载失败", Toast.LENGTH_LONG).show();

        Log.w("http", throwable.getMessage() + "---" + statusCode);
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, String responseString) {
        Map<String, Object> map = JacksonMapper.parse(responseString);
        onSuccess(statusCode,headers,map);
    }
    public void onSuccess(int statusCode, Header[] headers, Map<String, Object> map) {

    }

}
