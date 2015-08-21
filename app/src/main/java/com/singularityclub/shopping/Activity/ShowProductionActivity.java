package com.singularityclub.shopping.Activity;

import android.app.Activity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.singularityclub.shopping.Adapter.GridViewAdapter;
import com.singularityclub.shopping.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by fenghao on 2015/8/19.
 * 商品展示页面
 */
@EActivity(R.layout.activity_show_production)
public class ShowProductionActivity extends Activity{

    @ViewById
    protected EditText search_text;
    @ViewById
    protected ImageView shop_car, erweima_img;
    @ViewById
    protected Button caizhi, meaning, personality, theme;
    @ViewById
    protected com.handmark.pulltorefresh.library.PullToRefreshGridView main_gridview;

    protected GridViewAdapter gridViewAdapter;

    @AfterViews
    protected void init(){

        gridViewAdapter = new GridViewAdapter(this);
        main_gridview.setMode(PullToRefreshBase.Mode.BOTH);
        main_gridview.setAdapter(gridViewAdapter);

    }
}
