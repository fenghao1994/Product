package com.singularityclub.shopping.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
public class ShopCarActivity extends BaseActivity {
    @ViewById
    protected ImageView back, complete;
    @ViewById
    protected TextView price;
    @ViewById
    protected com.handmark.pulltorefresh.library.PullToRefreshGridView car_gridview;
    @Pref
    protected UserInfo_ userInfo;
    @ViewById
    protected LinearLayout layout_back;


    protected GridViewAdapter gridViewAdapter;
    @AfterViews
    protected void init(){
        showProdaction();
        car_gridview.setMode(PullToRefreshBase.Mode.BOTH);

        layout_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShopCarActivity.this.finish();
            }
        });

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
                        userInfo.edit().id().put("-1").shopNumber().put(0).apply();
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

        car_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ShopCarActivity.this, ProductionDetailActivity_.class);
                intent.putExtra("product_id", gridViewAdapter.array.get(position).getId());
                startActivity(intent);
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
                gridViewAdapter = new GridViewAdapter(ShopCarActivity.this, list);
                car_gridview.setAdapter(gridViewAdapter);
                int sum = 0;
                for (int i = 0 ; i < gridViewAdapter.array.size() ; i++){
                    sum += Double.parseDouble(gridViewAdapter.array.get(i).getPrice());
                }
                price.setText(sum + "");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(ShopCarActivity.this, "购物车 " + statusCode , Toast.LENGTH_LONG).show();
            }
        });
    }
}
