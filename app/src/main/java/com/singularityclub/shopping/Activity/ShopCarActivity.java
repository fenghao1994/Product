package com.singularityclub.shopping.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.singularityclub.shopping.Adapter.GridViewAdapter;
import com.singularityclub.shopping.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by fenghao on 2015/8/21.
 */
@EActivity(R.layout.activity_shopcar)
public class ShopCarActivity extends Activity {
    @ViewById
    protected ImageView back, complete;
    @ViewById
    protected com.handmark.pulltorefresh.library.PullToRefreshGridView car_gridview;

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
}
