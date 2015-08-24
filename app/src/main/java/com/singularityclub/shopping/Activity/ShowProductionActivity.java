package com.singularityclub.shopping.Activity;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.loopj.android.http.RequestParams;
import com.singularityclub.shopping.Adapter.FirstLevelAdapter;
import com.singularityclub.shopping.Adapter.GridViewAdapter;
import com.singularityclub.shopping.Adapter.SecondLevelAdapter;
import com.singularityclub.shopping.Application.MyApplication;
import com.singularityclub.shopping.Model.MainClassify;
import com.singularityclub.shopping.Model.ProductionItem;
import com.singularityclub.shopping.Model.SecondClassify;
import com.singularityclub.shopping.R;
import com.singularityclub.shopping.Utils.http.BaseJsonHttpResponseHandler;
import com.singularityclub.shopping.Utils.http.HttpClient;
import com.singularityclub.shopping.Utils.http.HttpUrl;
import com.singularityclub.shopping.Utils.http.JacksonMapper;
import com.singularityclub.shopping.preferences.UserInfo_;
import com.singularityclub.shopping.zxing.activity.CaptureActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.apache.http.Header;
import org.codehaus.jackson.type.TypeReference;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;

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
    /*@ViewById
    protected Button caizhi, meaning, personality, theme;*/
    @ViewById
    protected com.handmark.pulltorefresh.library.PullToRefreshGridView main_gridview;
    @ViewById
    protected GridView second_gridview, first_gridview;

    protected SecondLevelAdapter secondLevelAdapter;

    protected GridViewAdapter gridViewAdapter;

    protected MyApplication myApplication;

    protected FirstLevelAdapter firstLevelAdapter;
    @Pref
    protected UserInfo_ userInfo;

    @AfterViews
    protected void init(){

        myApplication = (MyApplication) getApplication();

        gridViewAdapter = new GridViewAdapter(this);
        main_gridview.setMode(PullToRefreshBase.Mode.BOTH);
        main_gridview.setAdapter(gridViewAdapter);

        secondLevelAdapter = new SecondLevelAdapter(this);
        second_gridview.setAdapter(secondLevelAdapter);

        firstLevelAdapter = new FirstLevelAdapter(this);
        first_gridview.setAdapter(firstLevelAdapter);

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

        first_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                backToInit();
                second_gridview.setVisibility(View.VISIBLE);
                for( int i = 0 ; i < firstLevelAdapter.color.length; i++){
                    firstLevelAdapter.color[i] = false;
                }
                firstLevelAdapter.color[position] = true;
                firstLevelAdapter.notifyDataSetChanged();
                ObjectAnimator.ofFloat(second_gridview, "translationX", 0, 140F).setDuration(500).start();
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

                if (search_text.getText().length() == 0) {
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

    //TODO 放入init方法中初始化数据
    protected void initShowProduction(){
        RequestParams params = new RequestParams();
        params.put("customer_id", userInfo.id().get());
        HttpClient.post(this, HttpUrl.POST_SHOW_PRODUCTION, params, new BaseJsonHttpResponseHandler(this) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                ArrayList<ProductionItem> list = JacksonMapper.parseToList(responseString, new TypeReference<ArrayList<ProductionItem>>() {
                });
                // TODO adapter 进行数据替换
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            }
        });
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        return super.dispatchKeyEvent(event);
    }

    /**
     * 显示搜索的商品
     * TODO 搜索api
     */
    public void showSearchProduction(String search){
        RequestParams params = new RequestParams();
        params.put("", search);
    }

    /**
     *初始化一级菜单
     * TODO 放入init中
     */
    public void initMianClassify(){
        HttpClient.get(this, HttpUrl.GET_MAIN_CALSSIFY, null, new BaseJsonHttpResponseHandler(this) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                ArrayList<MainClassify> list = JacksonMapper.parseToList(responseString, new TypeReference<ArrayList<MainClassify>>() {
                });
                //TODO 得到list数据，显示左边一级菜单
            }
        });
    }

    /**
     * 二级菜单
     */
    public void showSecondClassify(String id){
        RequestParams params = new RequestParams();
        params.put("main_classify_id", id);
        HttpClient.post(this, HttpUrl.POST_SECOND_CLASSIFY, params, new BaseJsonHttpResponseHandler(this) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                ArrayList<SecondClassify> list = JacksonMapper.parseToList(responseString, new TypeReference<ArrayList<SecondClassify>>() {
                });
                // TODO 得到数据，点击一级菜单时加载二级菜单
            }
        });
    }
}
