package com.singularityclub.shopping.Activity;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.apache.http.Header;
import org.codehaus.jackson.type.TypeReference;

import java.util.ArrayList;

/**
 * Created by fenghao on 2015/8/19.
 * 商品展示页面
 */
@EActivity(R.layout.activity_show_production)
public class ShowProductionActivity extends Activity {

    @ViewById
    protected ListView listview;
    @ViewById
    protected EditText search_text;
    @ViewById
    protected ImageView shop_car, erweima_img, yinyin;
    @ViewById
    protected com.handmark.pulltorefresh.library.PullToRefreshGridView main_gridview;
    @ViewById
    protected GridView second_gridview, first_gridview;
    @ViewById
    protected LinearLayout frame;

    protected SecondLevelAdapter secondLevelAdapter;

    protected GridViewAdapter gridViewAdapter;

    protected MyApplication myApplication;

    protected FirstLevelAdapter firstLevelAdapter;
    @Pref
    protected UserInfo_ userInfo;

    //点击了一级分类的位置
    protected int mainPosition;

    @AfterViews
    protected void init() {


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initShowProduction();
        initMianClassify();
        myApplication = (MyApplication) getApplication();
        main_gridview.setMode(PullToRefreshBase.Mode.BOTH);

        listen();

    }


    protected void listen() {

        frame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float f = second_gridview.getTranslationX();
                if( f != 0){
                    ObjectAnimator.ofFloat(second_gridview, "translationX", 290F, 0).setDuration(300).start();
                }
            }
        });

        yinyin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstLevelAdapter.color[mainPosition] = false;
                firstLevelAdapter.notifyDataSetChanged();
                second_gridview.setVisibility(View.GONE);
                yinyin.setVisibility(View.GONE);
            }
        });

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

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(500);
                            showYinying();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                }).start();

                backToInit();
                showSecondClassify( firstLevelAdapter.array.get(position).getMainClassifyId());
                mainPosition = position;
                second_gridview.setVisibility(View.VISIBLE);
                for (int i = 0; i < firstLevelAdapter.color.length; i++) {
                    firstLevelAdapter.color[i] = false;
                }
                firstLevelAdapter.color[position] = true;
                firstLevelAdapter.notifyDataSetChanged();
                ObjectAnimator.ofFloat(second_gridview, "translationX", 0, 290F).setDuration(500).start();
            }
        });
        //商品gridview
        main_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final int p = position;
                final ImageView imageView = (ImageView) view.findViewById(R.id.like_img);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if( gridViewAdapter.array.get(p).getAttention().equals("1")){
                            gridViewAdapter.array.get(p).setAttention("0");
                        }else{
                            gridViewAdapter.array.get(p).setAttention("1");
                        }

                        gridViewAdapter.notifyDataSetChanged();
                        RequestParams params = new RequestParams();
                        params.put("customer_id", userInfo.id().get());
                        params.put("product_id", gridViewAdapter.array.get(p).getId());
                        addAttention(params);
                    }
                });
                if (second_gridview.getTranslationX() != 0.0f) {
                    backToInit();
                } else {
                    Intent intent = new Intent(ShowProductionActivity.this, ProductionDetailActivity_.class);
                    intent.putExtra("product_id", gridViewAdapter.array.get(p).getId());
                    startActivity(intent);
                }
            }
        });
        //二级分类gridview
        second_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //TODO 刷新商品内容
                yinyin.setVisibility(View.GONE);
                showSecondProduction(secondLevelAdapter.array.get(position).getSecondClassifyId());
                backToInit();
                firstLevelAdapter.color[mainPosition] = false;
                firstLevelAdapter.notifyDataSetChanged();
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
                second_gridview.setVisibility(View.GONE);
                if (search_text.getText().length() == 0) {
                    listview.setAdapter(new ArrayAdapter<String>(ShowProductionActivity.this, R.layout.layout_search_item, R.id.search_item, myApplication.getList()));
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(100);
                            showHistorySearch();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

        search_text.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    ((InputMethodManager) search_text.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(ShowProductionActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    showSearchProduction(search_text.getText().toString());
                    myApplication.getList().add(0, search_text.getText().toString());
                    return true;
                }
                return false;
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


    /**
     * 匹配历史搜索
     * @param name
     * @return
     */
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
    public void backToInit() {

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

    /**
     * 与人格关联的商品展示
     */
    protected void initShowProduction() {
        RequestParams params = new RequestParams();
        params.put("customer_id", userInfo.id().get());
        HttpClient.post(this, HttpUrl.POST_SHOW_PRODUCTION, params, new BaseJsonHttpResponseHandler(this) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                ArrayList<ProductionItem> list = JacksonMapper.parseToList(responseString, new TypeReference<ArrayList<ProductionItem>>() {
                });
                if (list != null) {
                    gridViewAdapter = new GridViewAdapter(ShowProductionActivity.this, list);
                    main_gridview.setAdapter(gridViewAdapter);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(ShowProductionActivity.this, "查看关联人格", Toast.LENGTH_LONG).show();
            }
        });
    }


    /**
     * 显示搜索的商品
     */
    public void showSearchProduction(String search) {
        RequestParams params = new RequestParams();
        params.put("content", search);
        params.put("customer_id", userInfo.id().get());
        HttpClient.post(this, HttpUrl.POST_SEARCH, params, new BaseJsonHttpResponseHandler(this) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                ArrayList<ProductionItem> list = JacksonMapper.parseToList(responseString, new TypeReference<ArrayList<ProductionItem>>() {
                });
                gridViewAdapter = new GridViewAdapter(ShowProductionActivity.this, list);
                main_gridview.setAdapter(gridViewAdapter);
                if (list.size() == 0){
                    Toast.makeText(ShowProductionActivity.this, "无搜索结果", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(ShowProductionActivity.this, "code " + statusCode, Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * 初始化一级菜单
     */
    public void initMianClassify() {
        HttpClient.get(this, HttpUrl.GET_MAIN_CALSSIFY, null, new BaseJsonHttpResponseHandler(this) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                ArrayList<MainClassify> list = JacksonMapper.parseToList(responseString, new TypeReference<ArrayList<MainClassify>>() {
                });
                firstLevelAdapter = new FirstLevelAdapter(ShowProductionActivity.this, list);
                first_gridview.setAdapter(firstLevelAdapter);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(ShowProductionActivity.this, "获取一级分类失败", Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * 二级菜单
     */
    public void showSecondClassify(String id) {
        RequestParams params = new RequestParams();
        params.put("main_classify_id", id);
        HttpClient.post(this, HttpUrl.POST_SECOND_CALSSIFY_ID, params, new BaseJsonHttpResponseHandler(this) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                ArrayList<SecondClassify> list = JacksonMapper.parseToList(responseString, new TypeReference<ArrayList<SecondClassify>>() {
                });
                secondLevelAdapter = new SecondLevelAdapter(ShowProductionActivity.this, list);
                second_gridview.setAdapter(secondLevelAdapter);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(ShowProductionActivity.this, "获取二级分类失败", Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * 关注商品OR取消关注
     */

    public void addAttention(RequestParams params) {
        HttpClient.post(this, HttpUrl.POST_PRODUCTION_ATTENTION, params, new BaseJsonHttpResponseHandler(this) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            }
        });
    }

    /**
     * 点击二级二类后的商品
     */
    public void showSecondProduction(String id){
        RequestParams params = new RequestParams();
        params.put("second_classify_id", id);
        params.put("customer_id", userInfo.id().get());
        HttpClient.post(this, HttpUrl.POST_SECOND_CLASSIFY, params, new BaseJsonHttpResponseHandler(this){
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                ArrayList<ProductionItem> list = JacksonMapper.parseToList(responseString, new TypeReference<ArrayList<ProductionItem>>() {
                });
                gridViewAdapter = new GridViewAdapter(ShowProductionActivity.this, list);
                main_gridview.setAdapter(gridViewAdapter);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(ShowProductionActivity.this, "二级分类里面的商品错误" + statusCode, Toast.LENGTH_LONG).show();
            }
        });
    }

    @UiThread
    public void showHistorySearch() {
        listview.setVisibility(View.VISIBLE);
    }

    /**
     * 显示阴影
     */
    @UiThread
    public void showYinying(){
        yinyin.setVisibility(View.VISIBLE);
    }
}
