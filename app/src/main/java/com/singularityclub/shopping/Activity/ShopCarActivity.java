package com.singularityclub.shopping.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.loopj.android.http.RequestParams;
import com.singularityclub.shopping.Adapter.GridViewAdapter;
import com.singularityclub.shopping.Model.ProductionItem;
import com.singularityclub.shopping.R;
import com.singularityclub.shopping.Utils.http.BaseJsonHttpResponseHandler;
import com.singularityclub.shopping.Utils.http.HttpClient;
import com.singularityclub.shopping.Utils.http.HttpUrl;
import com.singularityclub.shopping.Utils.http.JacksonMapper;
import com.singularityclub.shopping.preferences.UserInfo_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.apache.http.Header;
import org.codehaus.jackson.type.TypeReference;

import java.util.ArrayList;

/**
 * Created by fenghao on 2015/8/21.
 */
@EActivity(R.layout.activity_shopcar)
public class ShopCarActivity extends Activity {
    @ViewById
    protected ImageView back, complete;
    @ViewById
    protected com.handmark.pulltorefresh.library.PullToRefreshGridView car_gridview;
    @Pref
    protected UserInfo_ userInfo;

    protected GridViewAdapter gridViewAdapter;
    @AfterViews
    protected void init(){
        gridViewAdapter = new GridViewAdapter(this);
        car_gridview.setMode(PullToRefreshBase.Mode.BOTH);
        car_gridview.setAdapter(gridViewAdapter);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShopCarActivity.this.finish();
            }
        });

        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ShopCarActivity.this);
                builder.setTitle("确定完成吗？");
                builder.setMessage("确定后,将清除客户信息,无法恢复！");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO  清除本地客户数据
                        dialog.dismiss();
                        ShopCarActivity.this.finish();
                        Intent intent = new Intent(ShopCarActivity.this, MessageInputActivity_.class);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                Dialog dialog = builder.create();
                dialog.show();
            }
        });
    }


    /**
     * 购物车初始化
     */

    public void showProdaction(){
        RequestParams params = new RequestParams();
        params.put("customer_id", userInfo.id().get());
        HttpClient.post(this, HttpUrl.POST_LOOK_CAR, params, new BaseJsonHttpResponseHandler(this){
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                ArrayList<ProductionItem> list = JacksonMapper.parseToList(responseString, new TypeReference<ArrayList<ProductionItem>>() {
                });
                //TODO 得到了list数据，加载数据
            }
        });
    }
}
