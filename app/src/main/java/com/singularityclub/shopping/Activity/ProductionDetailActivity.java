package com.singularityclub.shopping.Activity;


import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.loopj.android.http.RequestParams;
import com.singularityclub.shopping.Adapter.MyFragmentPagerAdapter;
import com.singularityclub.shopping.Model.ProductionItem;
import com.singularityclub.shopping.R;
import com.singularityclub.shopping.Utils.http.BaseJsonHttpResponseHandler;
import com.singularityclub.shopping.Utils.http.HttpClient;
import com.singularityclub.shopping.Utils.http.HttpUrl;
import com.singularityclub.shopping.preferences.UserInfo_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.apache.http.Header;

/**
 * Created by Howe on 2015/8/21.
 */

@EActivity(R.layout.activity_production_detail)
public class ProductionDetailActivity extends FragmentActivity {

    @ViewById
    protected ViewPager viewpager;
    @ViewById
    protected PagerSlidingTabStrip tabs;
    @ViewById
    protected TextView product_name, product_price, audio_name;
    @ViewById
    protected EditText search_text;
    @ViewById
    protected Spinner audio_list;
    @Pref
    protected UserInfo_ userInfo;

    protected ProductionItem product;

    @AfterViews
    public void init(){

        showProduct();

        viewpager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager()));
        tabs.setViewPager(viewpager);
        tabs.setTextSize(30);
        tabs.setTabPaddingLeftRight(100);
        tabs.setDividerColor(Color.TRANSPARENT);
        tabs.setUnderlineHeight(2);

    }

    //展示商品详细
    public void showProduct(){

    }

    @Click(R.id.add_attention)
    protected void addCar(){
        RequestParams parms = new RequestParams();
        parms.put("customer_id", userInfo.id().get());
        parms.put("product_id", product.getId());

        HttpClient.post(this, HttpUrl.POST_PRODUCTION_ATTENTION, parms, new BaseJsonHttpResponseHandler(this) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Toast.makeText(getApplication(), "加入购物车成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(getApplication(), "加入购物车失败---" + statusCode, Toast.LENGTH_LONG).show();
            }
        });
    }

}
