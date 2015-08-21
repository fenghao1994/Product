package com.singularityclub.shopping.Activity;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.singularityclub.shopping.Adapter.GridViewAdapter;
import com.singularityclub.shopping.Adapter.SecondLevelAdapter;
import com.singularityclub.shopping.R;
import com.singularityclub.shopping.zxing.activity.CaptureActivity;

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
    @ViewById
    protected GridView second_gridview;

    protected SecondLevelAdapter secondLevelAdapter;

    protected GridViewAdapter gridViewAdapter;

    @AfterViews
    protected void init(){

        gridViewAdapter = new GridViewAdapter(this);
        main_gridview.setMode(PullToRefreshBase.Mode.BOTH);
        main_gridview.setAdapter(gridViewAdapter);

        secondLevelAdapter = new SecondLevelAdapter(this);
        second_gridview.setAdapter(secondLevelAdapter);

        shop_car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent t = new Intent(ShowProductionActivity.this, ShopCarActivity_.class);
                startActivity(t);
            }
        });

        caizhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToInit();
                second_gridview.setVisibility(View.VISIBLE);
                caizhi.setBackgroundColor( getResources().getColor( R.color.dark_blue));
                ObjectAnimator.ofFloat(second_gridview, "translationX", 0, 138F).setDuration(500).start();
            }
        });
        meaning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToInit();
                second_gridview.setVisibility(View.VISIBLE);
                meaning.setBackgroundColor(getResources().getColor(R.color.dark_blue));
                ObjectAnimator.ofFloat(second_gridview, "translationX", 0, 138F).setDuration(500).start();
            }
        });
        personality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToInit();
                second_gridview.setVisibility(View.VISIBLE);
                personality.setBackgroundColor(getResources().getColor(R.color.dark_blue));
                ObjectAnimator.ofFloat(second_gridview, "translationX", 0, 138F).setDuration(500).start();
            }
        });
        theme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToInit();
                second_gridview.setVisibility(View.VISIBLE);
                theme.setBackgroundColor(getResources().getColor(R.color.dark_blue));
                ObjectAnimator.ofFloat(second_gridview, "translationX", 0, 138F).setDuration(500).start();
            }
        });
        main_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (second_gridview.getTranslationX() != 0.0f) {
                    backToInit();
                } else {
                    //TODO 进入详细页面
                }
            }
        });

        second_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //TODO 刷新商品内容
                backToInit();
            }
        });

        erweima_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  startScan = new Intent(ShowProductionActivity.this, CaptureActivity.class);
                startActivityForResult(startScan, 0);
            }

        });


    }

    //通过onActivityResult方法将数据返回
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            //得到二维码里面包含的数据
            String result = data.getExtras().getString("result");
            Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        }
    }

    public void backToInit(){
        caizhi.setBackgroundColor(getResources().getColor(R.color.blue));
        meaning.setBackgroundColor(getResources().getColor(R.color.blue));
        theme.setBackgroundColor(getResources().getColor(R.color.blue));
        personality.setBackgroundColor(getResources().getColor(R.color.blue));
        float x = second_gridview.getTranslationX();
        second_gridview.setVisibility(View.GONE);
        ObjectAnimator.ofFloat(second_gridview, "translationX", x, 0).setDuration(100).start();
    }
}
