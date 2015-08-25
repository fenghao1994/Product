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

@EFragment(R.layout.fragment_product_intro)
public class ProIntroductionFragment extends Fragment {

    @ViewById
    WebView introduct_intro;
    private String url;

    @AfterViews
    public void init(){
        //设置WebView属性，能够执行Javascript脚本
        introduct_intro.getSettings().setJavaScriptEnabled(true);
        //加载需要显示的网页
        introduct_intro.loadUrl(url);
        //设置Web视图
        introduct_intro.setWebViewClient(new WebViewClient ());
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
