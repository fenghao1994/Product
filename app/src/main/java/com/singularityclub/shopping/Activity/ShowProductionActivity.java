package com.singularityclub.shopping.Activity;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.singularityclub.shopping.Adapter.GridViewAdapter;
import com.singularityclub.shopping.Adapter.SecondLevelAdapter;
import com.singularityclub.shopping.Application.MyApplication;
import com.singularityclub.shopping.R;
import com.singularityclub.shopping.zxing.activity.CaptureActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by fenghao on 2015/8/19.
 * 商品展示页面
 */
@EActivity(R.layout.activity_show_production)
public class ShowProductionActivity extends Activity{

    @ViewById
    protected ListView listview;
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

    protected MyApplication myApplication;

    @AfterViews
    protected void init(){

        myApplication = (MyApplication) getApplication();

        gridViewAdapter = new GridViewAdapter(this);
        main_gridview.setMode(PullToRefreshBase.Mode.BOTH);
        main_gridview.setAdapter(gridViewAdapter);

        secondLevelAdapter = new SecondLevelAdapter(this);
        second_gridview.setAdapter(secondLevelAdapter);
        listen();

    }


    protected void listen(){

        //购物车按钮
        shop_car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent t = new Intent(ShowProductionActivity.this, ShopCarActivity_.class);
                startActivity(t);
            }
        });

        //材质按钮
        caizhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToInit();
                second_gridview.setVisibility(View.VISIBLE);
                caizhi.setBackgroundColor( getResources().getColor( R.color.dark_blue));
                ObjectAnimator.ofFloat(second_gridview, "translationX", 0, 138F).setDuration(500).start();
            }
        });
        //含义按钮
        meaning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToInit();
                second_gridview.setVisibility(View.VISIBLE);
                meaning.setBackgroundColor(getResources().getColor(R.color.dark_blue));
                ObjectAnimator.ofFloat(second_gridview, "translationX", 0, 138F).setDuration(500).start();
            }
        });
        //人格按钮
        personality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToInit();
                second_gridview.setVisibility(View.VISIBLE);
                personality.setBackgroundColor(getResources().getColor(R.color.dark_blue));
                ObjectAnimator.ofFloat(second_gridview, "translationX", 0, 138F).setDuration(500).start();
            }
        });

        //主题按钮
        theme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToInit();
                second_gridview.setVisibility(View.VISIBLE);
                theme.setBackgroundColor(getResources().getColor(R.color.dark_blue));
                ObjectAnimator.ofFloat(second_gridview, "translationX", 0, 138F).setDuration(500).start();
            }
        });

        //商品gridview
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
        //二级分类gridview
        second_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //TODO 刷新商品内容
                backToInit();
            }
        });

        //二维码按钮
        erweima_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startScan = new Intent(ShowProductionActivity.this, CaptureActivity.class);
                startActivityForResult(startScan, 0);
            }
        });

        //搜索框
        search_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if( search_text.getText().length() == 0){
                    listview.setVisibility(View.VISIBLE);
                    listview.setAdapter(new ArrayAdapter<String>(ShowProductionActivity.this, R.layout.layout_search_item, R.id.search_item, myApplication.getList()));
                }
            }
        });

        search_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    ArrayList<String> list = searchItem(s.toString());
                    listview.setAdapter(new ArrayAdapter<String>(ShowProductionActivity.this, R.layout.layout_search_item, R.id.search_item, list));
                } else {
                    listview.setAdapter(new ArrayAdapter<String>(ShowProductionActivity.this, R.layout.layout_search_item, R.id.search_item, myApplication.getList()));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        search_text.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    listview.setVisibility(View.GONE);
                }
            }
        });

        main_gridview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                listview.setVisibility(View.GONE);
            }
        });
    }

    public ArrayList<String> searchItem(String name) {
        ArrayList<String> mSearchList = new ArrayList<String>();
        for (int i = 0; i < myApplication.getList().size(); i++) {
            int index = myApplication.getList().get(i).indexOf(name);
            // 存在匹配的数据
            if (index != -1) {
                mSearchList.add(myApplication.getList().get(i));
            }
        }
        return mSearchList;
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

    /**
     * 回到初始化状态
     */
    public void backToInit(){
        caizhi.setBackgroundColor(getResources().getColor(R.color.blue));
        meaning.setBackgroundColor(getResources().getColor(R.color.blue));
        theme.setBackgroundColor(getResources().getColor(R.color.blue));
        personality.setBackgroundColor(getResources().getColor(R.color.blue));
        listview.setVisibility(View.GONE);
        float x = second_gridview.getTranslationX();
        second_gridview.setVisibility(View.GONE);
        ObjectAnimator.ofFloat(second_gridview, "translationX", x, 0).setDuration(100).start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        listview.setVisibility(View.GONE);
    }
}
