package com.singularityclub.shopping.Fragment;

import android.support.v4.app.Fragment;
import android.webkit.WebView;

import com.singularityclub.shopping.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * Created by Howe on 2015/8/24.
 */
@EFragment(R.layout.fragment_product_data)
public class ProductionDataFragment extends Fragment {
    @ViewById
    WebView product_data;
    private String url;

    @AfterViews
    public void init(){
        //设置WebView属性，能够执行Javascript脚本
        product_data.getSettings().setJavaScriptEnabled(true);
        //加载需要显示的网页
        product_data.loadUrl(url);
        //设置Web视图
        product_data.setWebViewClient(new WebViewClient ());
    }

    //Web视图
    private class WebViewClient extends android.webkit.WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

}
