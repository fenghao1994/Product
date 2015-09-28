package com.singularityclub.shopping.Activity;

import android.app.Activity;
import android.webkit.WebView;

import com.singularityclub.shopping.R;
import com.singularityclub.shopping.Utils.http.BaseJsonHttpResponseHandler;
import com.singularityclub.shopping.Utils.http.HttpClient;
import com.singularityclub.shopping.Utils.http.HttpUrl;
import com.singularityclub.shopping.Utils.http.JacksonMapper;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.ViewsById;
import org.apache.http.Header;

/**
 * Created by fenghao on 2015/9/24.
 */
@EActivity(R.layout.activity_dialog)
public class DialogActivity extends Activity{


    @ViewById
    protected WebView webview;


    @AfterViews
    public void init(){
        getImage();
    }

    public void getImage(){
        HttpClient.get(this, HttpUrl.GET_ACTIVITY_IMAGE, null, new BaseJsonHttpResponseHandler(this){
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                String html = (String) JacksonMapper.parse(responseString).get("content");
                show(html);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    public void show(String html){
        String mime = "text/html";
        String encoding = "utf-8";
        webview.getSettings().setJavaScriptEnabled(true);
        webview.loadDataWithBaseURL(null, "<style>img{width:100%;}</style>  <base href=\"" + HttpUrl.ROOT + "\" />" + html, mime, encoding, null);
    }
}
