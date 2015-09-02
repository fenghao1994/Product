package com.singularityclub.shopping.Fragment;

import android.support.v4.app.Fragment;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.singularityclub.shopping.Application.MyApplication;
import com.singularityclub.shopping.Model.ProductionItem;
import com.singularityclub.shopping.R;
import com.singularityclub.shopping.Utils.http.BaseJsonHttpResponseHandler;
import com.singularityclub.shopping.Utils.http.HttpClient;
import com.singularityclub.shopping.Utils.http.HttpUrl;
import com.singularityclub.shopping.Utils.http.JacksonMapper;
import com.singularityclub.shopping.preferences.UserInfo_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.apache.http.Header;
import org.codehaus.jackson.type.TypeReference;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by Howe on 2015/8/24.
 */

@EFragment(R.layout.fragment_product_intro)
public class ProIntroductionFragment extends Fragment {
    @ViewById
    WebView introduct_intro;
    @Pref
    protected UserInfo_ userInfo;
    protected String product;

    MyApplication myApplication;

    @AfterViews
    public void init(){
        myApplication = (MyApplication) getActivity().getApplication();
        product = myApplication.getProductID();

        getProduct();
    }

    public void getProduct(){
        RequestParams parms = new RequestParams();
        parms.put("product_id", product);
        parms.put("customer_id", userInfo.id().get());

        HttpClient.post(getActivity(), HttpUrl.POST_PRODUCT_DETAIL, parms, new BaseJsonHttpResponseHandler(getActivity()) {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(getActivity(), "数据出错", Toast.LENGTH_SHORT);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                ProductionItem productData = JacksonMapper.parseToList(responseString, new TypeReference<ProductionItem>() {
                });
                update(productData);
            }
        });
    }

    @UiThread
    public void update(ProductionItem data){

        WebSettings webSettings = introduct_intro.getSettings();
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setJavaScriptEnabled(true);

        String html = data.getIntro();
        introduct_intro.loadDataWithBaseURL(null, "<style>img{width:100%;}</style>  <base href=\"" + HttpUrl.ROOT + "\" />" + html, "text/html", "utf-8", null);


//        introduct_intro.setWebViewClient(new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                view.loadUrl(url);
//                return true;
//            }
//        });
    }

    //对html内容编码
    public String fmtString(String html){
        String notice = "";
        if(html == "" || html == null) {
            try {
                notice = URLEncoder.encode(html, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return notice;
    }
}
