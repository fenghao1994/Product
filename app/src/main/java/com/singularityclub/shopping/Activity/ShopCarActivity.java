package com.singularityclub.shopping.Activity;

import android.app.Activity;
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
    }
}
