package com.singularityclub.shopping.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.singularityclub.shopping.Adapter.SecondLevelAdapter;
import com.singularityclub.shopping.Model.FirstThemeClassify;
import com.singularityclub.shopping.Model.MainClassify;
import com.singularityclub.shopping.Model.SecondClassify;
import com.singularityclub.shopping.Model.SecondThemeClassify;
import com.singularityclub.shopping.Utils.Cache.ACache;
import com.singularityclub.shopping.Utils.http.BaseJsonHttpResponseHandler;
import com.singularityclub.shopping.Utils.http.HttpClient;
import com.singularityclub.shopping.Utils.http.HttpUrl;
import com.singularityclub.shopping.Utils.http.JacksonMapper;

import org.apache.http.Header;
import org.codehaus.jackson.type.TypeReference;

import java.util.ArrayList;

/**
 * Created by fenghao on 2015/8/27.
 */
public class UpdateService extends Service {

    ACache aCache;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        aCache = ACache.get(getApplication());
        aCache.clear();
        updateFirst();
        updateFirstTheme();
    }

    //更新分类的一级分类
    public void updateFirst() {
        HttpClient.get(this, HttpUrl.GET_MAIN_CALSSIFY, null, new BaseJsonHttpResponseHandler(this) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                ArrayList<MainClassify> list = JacksonMapper.parseToList(responseString, new TypeReference<ArrayList<MainClassify>>() {
                });
                aCache.put("first", list, ACache.TIME_DAY);
                for (int i = 0; i < list.size(); i++) {
                    updateSecond(list.get(i).getMainClassifyId());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            }
        });
    }

    //更新分类的二级分类
    public void updateSecond(String id){
        RequestParams params = new RequestParams();
        params.put("main_classify_id", id);
        final String i = id;
        HttpClient.post(this, HttpUrl.POST_SECOND_CALSSIFY_ID, params, new BaseJsonHttpResponseHandler(this) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                ArrayList<SecondClassify> list = JacksonMapper.parseToList(responseString, new TypeReference<ArrayList<SecondClassify>>() {
                });
                aCache.put("second" + i, list, ACache.TIME_DAY);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            }
        });
    }

    //更新主题的一级主题
    public void updateFirstTheme(){
        HttpClient.get(this, HttpUrl.GET_FIRST_THEME, null, new BaseJsonHttpResponseHandler(this) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                ArrayList<FirstThemeClassify> list = JacksonMapper.parseToList(responseString, new TypeReference<ArrayList<FirstThemeClassify>>() {
                });
                aCache.put("first_theme", list, ACache.TIME_DAY);
                for (int i = 0; i < list.size(); i++) {
                    updateSecondTheme(list.get(i).getFirstThemeId());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            }
        });
    }

    //更新主题的二级主题
    public void updateSecondTheme( String id){
        RequestParams params = new RequestParams();
        params.put("main_theme_id", id);
        final String i = id;
        HttpClient.post(this, HttpUrl.POST_SECOND_THEME, params, new BaseJsonHttpResponseHandler(this) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                ArrayList<SecondThemeClassify> list = JacksonMapper.parseToList(responseString, new TypeReference<ArrayList<SecondThemeClassify>>() {
                });
                aCache.put("second_theme" + i, list, ACache.TIME_DAY);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            }
        });
    }
}
