package com.singularityclub.shopping.Activity;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.singularityclub.shopping.R;
import com.singularityclub.shopping.Utils.http.BaseJsonHttpResponseHandler;
import com.singularityclub.shopping.Utils.http.HttpClient;
import com.singularityclub.shopping.Utils.http.HttpUrl;
import com.singularityclub.shopping.preferences.UserInfo_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.apache.http.Header;

/**
 * Created by fenghao on 2015/8/26.
 */
@EActivity(R.layout.layout_shop_id)
public class ShopIdActivity extends BaseActivity {

    @ViewById
    protected ImageView shop_complete;
    @ViewById
    protected TextView save;

    @ViewById
    protected EditText shop;

    @Pref
    protected UserInfo_ userInfo;

    @AfterViews
    protected void init(){

        shop_complete.setVisibility(View.VISIBLE);

        if (userInfo.shop().get() != -1){
            shop.setText(userInfo.shop().get() + "");
        }

        shop_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( !userInfo.id().get().equals("-1") && shop.getText().length() != 0){
                    userInfo.edit().shop().put(Integer.parseInt(shop.getText().toString())).apply();
                    completeShopId();
                }else{
                    if ( shop.getText().length() == 0){
                        Toast.makeText(ShopIdActivity.this, "店家信息为空", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(ShopIdActivity.this, "请录入客户信息", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(ShopIdActivity.this, MessageInputActivity_.class);
                        startActivity(intent);
                    }
                }
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shop.getText().length() != 0){
                    userInfo.edit().shop().put(Integer.parseInt(shop.getText().toString())).apply();
                    Toast.makeText(ShopIdActivity.this, "保存成功", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ShopIdActivity.this, MessageInputActivity_.class);
                    startActivity(intent);
                }
            }
        });
    }

    public void completeShopId(){
        RequestParams params = new RequestParams();
        params.put("customer_id", userInfo.id().get());
        params.put("shop_id", shop.getText().toString());
        HttpClient.post(this, HttpUrl.POST_SHOP_ID, params, new BaseJsonHttpResponseHandler(this) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                userInfo.edit().shop().put(Integer.parseInt(shop.getText().toString())).apply();
                goToShowProduction();
            }
        });
    }

    public void goToShowProduction(){
        Intent intent = new Intent();
        intent.setClass(this, ShowProductionActivity_.class);
        startActivity(intent);
    }

}
