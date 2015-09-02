package com.singularityclub.shopping.Activity;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ProgressDialog;
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
import android.widget.Button;
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
import com.singularityclub.shopping.Adapter.FirstThemeAdapter;
import com.singularityclub.shopping.Adapter.GridViewAdapter;
import com.singularityclub.shopping.Adapter.SecondLevelAdapter;
import com.singularityclub.shopping.Adapter.SecondThemeAdapter;
import com.singularityclub.shopping.Application.MyApplication;
import com.singularityclub.shopping.Model.Action;
import com.singularityclub.shopping.Model.FirstThemeClassify;
import com.singularityclub.shopping.Model.MainClassify;
import com.singularityclub.shopping.Model.ProductionItem;
import com.singularityclub.shopping.Model.SecondClassify;
import com.singularityclub.shopping.Model.SecondThemeClassify;
import com.singularityclub.shopping.R;
import com.singularityclub.shopping.Utils.http.BaseJsonHttpResponseHandler;
import com.singularityclub.shopping.Utils.http.HttpClient;
import com.singularityclub.shopping.Utils.http.HttpUrl;
import com.singularityclub.shopping.Utils.http.JacksonMapper;
import com.singularityclub.shopping.preferences.UserInfo_;
import com.singularityclub.shopping.zxing.activity.CaptureActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.apache.http.Header;
import org.codehaus.jackson.type.TypeReference;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import com.singularityclub.shopping.Utils.Cache.*;

/**
 * Created by fenghao on 2015/8/19.
 * 商品展示页面
 */
@EActivity(R.layout.activity_show_production)
public class ShowProductionActivity extends BaseActivity {

    //历史搜索的下拉列表
    @ViewById
    protected ListView listview;
    //搜索框
    @ViewById
    protected EditText search_text;
    //购物车按钮，二维码，阴影
    @ViewById
    protected ImageView shop_car, erweima_img, yinyin;
    //展示商品的gridview
    @ViewById
    protected com.handmark.pulltorefresh.library.PullToRefreshGridView main_gridview;
    //一级分类和二级分类的gridview
    @ViewById
    protected GridView second_gridview, first_gridview;
    //人格按钮
    @ViewById
    protected TextView person;
    //主题，分类按钮
    @ViewById
    protected Button type, theme;
    @ViewById
    protected LinearLayout layout_shop, layout_person;
    @Pref
    protected UserInfo_ userInfo;
    //分类的二级分类Adapter
    protected SecondLevelAdapter secondLevelAdapter;
    //展示商品的gridview的adapter
    @Bean
    protected GridViewAdapter gridViewAdapter;

    protected MyApplication myApplication;

    //分类一级分类的adapter
    protected FirstLevelAdapter firstLevelAdapter;

    //访问网络的时候的旋转
    protected ProgressDialog progressDialog;

    //一二级主题分类adapter
    protected FirstThemeAdapter firstThemeAdapter;
    protected SecondThemeAdapter secondThemeAdapter;
    //消费者行为记录的model
    protected Action action;
    //缓存
    protected ACache aCache;
    //二次退出
    protected long mkeyTime;
    //消费者行为记录的时间
    protected Long startTime;
    protected Long endTime;
    //点击了一级分类的位置
    protected int mainPosition;
    //标记是点的主题还是分类按钮
    int flag = 0;

    //点击的二级主题的id
    protected String second = "-1";

    @AfterViews
    protected void init() {

        person.setVisibility(View.VISIBLE);
        main_gridview.setAdapter(gridViewAdapter);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        aCache = ACache.get(this);
        //返回与人格有关的商品
        initShowProduction();

        //用户与商家绑定
        bing();
        myApplication = (MyApplication) getApplication();
        main_gridview.setMode(PullToRefreshBase.Mode.BOTH);
        listen();

    }

    /**
     * 所以的监听事件
     */
    protected void listen() {

        layout_person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowProductionActivity.this, PersonActivity_.class);
                startActivity(intent);
            }
        });

        layout_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent t = new Intent(ShowProductionActivity.this, ShopCarActivity_.class);
                startActivity(t);
            }
        });

        //分类
        type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainPosition = 0;
                second_gridview.setVisibility(View.GONE);
                buttonBack();
                flag = 1;

                yinyin.setVisibility(View.VISIBLE);
                type.setBackgroundColor(getResources().getColor(R.color.dark_blue));
                ArrayList<MainClassify> list = (ArrayList<MainClassify>) aCache.getAsObject("first");
                if (list == null) {
                    initMianClassify();
                } else {
                    firstLevelAdapter = new FirstLevelAdapter(ShowProductionActivity.this, list);
                    first_gridview.setAdapter(firstLevelAdapter);
                }
                if (first_gridview.getVisibility() == View.GONE) {
                    ObjectAnimator.ofFloat(first_gridview, "translationX", -180F, 0F).setDuration(500).start();
                }
                first_gridview.setVisibility(View.VISIBLE);
            }
        });

        //主题点击事件
        theme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainPosition = 0;
                second_gridview.setVisibility(View.GONE);
                buttonBack();
                flag = 2;
                yinyin.setVisibility(View.VISIBLE);
                theme.setBackgroundColor(getResources().getColor(R.color.dark_blue));
                ArrayList<FirstThemeClassify> list = (ArrayList<FirstThemeClassify>) aCache.getAsObject("first_theme");
                if (list == null) {
                    getFirstTheme();
                } else {
                    firstThemeAdapter = new FirstThemeAdapter(ShowProductionActivity.this, list);
                    first_gridview.setAdapter(firstThemeAdapter);
                }
                if (first_gridview.getVisibility() == View.GONE) {
                    ObjectAnimator.ofFloat(first_gridview, "translationX", -180F, 0F).setDuration(500).start();
                }
                first_gridview.setVisibility(View.VISIBLE);
            }
        });

        //阴影部分点击事件
        yinyin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonBack();
                if (flag == 1) {
                    firstLevelAdapter.color[mainPosition] = false;
                    firstLevelAdapter.notifyDataSetChanged();
                } else {
                    firstThemeAdapter.color[mainPosition] = false;
                    firstThemeAdapter.notifyDataSetChanged();
                }
                second_gridview.setVisibility(View.GONE);
                first_gridview.setVisibility(View.GONE);
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

        //一级分类点击
        first_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                startTime = System.currentTimeMillis();
                action = new Action();
                action.setStartTime(new Date(startTime));
                action.setCustomerId(Integer.parseInt(userInfo.id().get()));
                if (flag == 1) {
                    //1表示分类
                    action.setExtraType(1);
                } else {
                    //2表示主题
                    action.setExtraType(2);
                }
                yinyin.setVisibility(View.VISIBLE);

                backToInit();
                if (flag == 1) {
                    if (aCache.getAsObject("second" + firstLevelAdapter.array.get(position).getMainClassifyId()) == null) {
                        showSecondClassify(firstLevelAdapter.array.get(position).getMainClassifyId());
                    } else {
                        ArrayList<SecondClassify> list = (ArrayList<SecondClassify>) aCache.getAsObject("second" + firstLevelAdapter.array.get(position).getMainClassifyId());
                        secondLevelAdapter = new SecondLevelAdapter(ShowProductionActivity.this, list);
                        second_gridview.setAdapter(secondLevelAdapter);
                    }
                    mainPosition = position;
                    for (int i = 0; i < firstLevelAdapter.color.length; i++) {
                        firstLevelAdapter.color[i] = false;
                    }
                    firstLevelAdapter.color[position] = true;
                    firstLevelAdapter.notifyDataSetChanged();
                } else {
                    if (aCache.getAsObject("second_theme" + firstThemeAdapter.array.get(position).getFirstThemeId()) == null) {
                        getSecondTheme(firstThemeAdapter.array.get(position).getFirstThemeId());
                    } else {
                        ArrayList<SecondThemeClassify> list = (ArrayList<SecondThemeClassify>) aCache.getAsObject("second_theme" + firstThemeAdapter.array.get(position).getFirstThemeId());
                        secondThemeAdapter = new SecondThemeAdapter(ShowProductionActivity.this, list);
                        second_gridview.setAdapter(secondThemeAdapter);
                    }
                    mainPosition = position;
                    for (int i = 0; i < firstThemeAdapter.color.length; i++) {
                        firstThemeAdapter.color[i] = false;
                    }
                    firstThemeAdapter.color[position] = true;
                    firstThemeAdapter.notifyDataSetChanged();
                }
                if (second_gridview.getVisibility() == View.GONE) {
                    ObjectAnimator.ofFloat(second_gridview, "translationX", -180F, 0F).setDuration(500).start();
                }
                second_gridview.setVisibility(View.VISIBLE);
            }
        });
        //商品gridview
        main_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(ShowProductionActivity.this, ProductionDetailActivity_.class);
                intent.putExtra("product_id", gridViewAdapter.array.get(position).getId());
                startActivity(intent);
            }
        });
        //二级分类gridview
        second_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                endTime = System.currentTimeMillis();
                action.setEndTime(new Date(endTime));
                if (flag == 1) {
                    action.setExtraId(Integer.parseInt(secondLevelAdapter.array.get(position).getSecondClassifyId()));
                } else {
                    action.setExtraId(Integer.parseInt(secondThemeAdapter.array.get(position).getSecondThemeId()));
                }
                float time = ((float) (endTime - startTime)) / 1000;
                action.setTotalMinutes(time);
                postAction(action);


                first_gridview.setVisibility(View.GONE);
                buttonBack();
                yinyin.setVisibility(View.GONE);
                if (flag == 1) {
                    showSecondProduction(secondLevelAdapter.array.get(position).getSecondClassifyId());
                    second = secondLevelAdapter.array.get(position).getSecondClassifyId();
                } else {
                    showSecondProduction(secondThemeAdapter.array.get(position).getSecondThemeId());
                    second = secondThemeAdapter.array.get(position).getSecondThemeId();
                }
                backToInit();
                second_gridview.setVisibility(View.GONE);
                if (flag == 1) {
                    firstLevelAdapter.color[mainPosition] = false;
                    firstLevelAdapter.notifyDataSetChanged();
                } else {
                    firstThemeAdapter.color[mainPosition] = false;
                    firstThemeAdapter.notifyDataSetChanged();
                }

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
                buttonBack();
                yinyin.setVisibility(View.GONE);
                first_gridview.setVisibility(View.GONE);
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

        //搜索框内容发生变化
        search_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    ArrayList<String> list = searchItem(s.toString());
                    if (list.size() == 0) {
                        listview.setVisibility(View.GONE);
                    }
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
                if (hasFocus) {
                    showHistorySearch();
                } else {
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


        //人格按钮
        person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowProductionActivity.this, PersonActivity_.class);
                startActivity(intent);
            }
        });
    }


    /**
     * 匹配历史搜索
     *
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
            Intent intent = new Intent(ShowProductionActivity.this, ProductionDetailActivity_.class);
            intent.putExtra("qrcode", result);
            startActivity(intent);
        }
    }

    /**
     * 回到初始化状态
     */
    public void backToInit() {

        listview.setVisibility(View.GONE);
        float x = second_gridview.getTranslationX();
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
        progressDialog = ProgressDialog.show(this, "", "加载中！！！", true);
        HttpClient.post(this, HttpUrl.POST_SHOW_PRODUCTION, params, new BaseJsonHttpResponseHandler(this) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                progressDialog.dismiss();
                ArrayList<ProductionItem> list = JacksonMapper.parseToList(responseString, new TypeReference<ArrayList<ProductionItem>>() {
                });
                if (list != null) {
                    gridViewAdapter.init( list);
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                progressDialog.dismiss();
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
                gridViewAdapter.init(list);
                if (list.size() == 0) {
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
                aCache.put("first", list, ACache.TIME_DAY);
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
        final String i = id;
        HttpClient.post(this, HttpUrl.POST_SECOND_CALSSIFY_ID, params, new BaseJsonHttpResponseHandler(this) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                ArrayList<SecondClassify> list = JacksonMapper.parseToList(responseString, new TypeReference<ArrayList<SecondClassify>>() {
                });
                secondLevelAdapter = new SecondLevelAdapter(ShowProductionActivity.this, list);
                second_gridview.setAdapter(secondLevelAdapter);
                aCache.put("second" + i, list, ACache.TIME_DAY);
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
    public void showSecondProduction(String id) {
        RequestParams params = new RequestParams();
        String url = "";
        if (flag == 1) {
            params.put("second_classify_id", id);
            params.put("customer_id", userInfo.id().get());
            url = HttpUrl.POST_SECOND_CLASSIFY;
        } else {
            params.put("second_theme_id", id);
            params.put("customer_id", userInfo.id().get());
            url = HttpUrl.POST_SECOND_THEME_PRODUCTION;
        }
        HttpClient.post(this, url, params, new BaseJsonHttpResponseHandler(this) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                ArrayList<ProductionItem> list = JacksonMapper.parseToList(responseString, new TypeReference<ArrayList<ProductionItem>>() {
                });
                gridViewAdapter.init(list);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(ShowProductionActivity.this, "二级分类里面的商品错误" + statusCode, Toast.LENGTH_LONG).show();
            }
        });
    }


    /**
     * 消费者行为记录
     */

    public void postAction(Action action) {
        RequestParams params = new RequestParams();
        params.put("customer_id", action.getCustomerId());
        params.put("startTime", action.getStartTime());
        params.put("endTime", action.getEndTime());
        params.put("extra_id", action.getExtraId());
        params.put("extra_type", action.getExtraType());
        params.put("total_minutes", action.getTotalMinutes());

        HttpClient.post(this, HttpUrl.POST_ACTION, params, new BaseJsonHttpResponseHandler(this) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            }
        });
    }

    //得到一级主题分类
    public void getFirstTheme() {
        HttpClient.get(this, HttpUrl.GET_FIRST_THEME, null, new BaseJsonHttpResponseHandler(this) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                ArrayList<FirstThemeClassify> list = JacksonMapper.parseToList(responseString, new TypeReference<ArrayList<FirstThemeClassify>>() {
                });
                aCache.put("first_theme", list, ACache.TIME_DAY);
                firstThemeAdapter = new FirstThemeAdapter(ShowProductionActivity.this, list);
                first_gridview.setAdapter(firstThemeAdapter);
            }
        });
    }

    //得到二级主题分类
    public void getSecondTheme(String id) {
        RequestParams params = new RequestParams();
        params.put("main_theme_id", id);
        final String i = id;
        HttpClient.post(this, HttpUrl.POST_SECOND_THEME, params, new BaseJsonHttpResponseHandler(this) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                ArrayList<SecondThemeClassify> list = JacksonMapper.parseToList(responseString, new TypeReference<ArrayList<SecondThemeClassify>>() {
                });
                secondThemeAdapter = new SecondThemeAdapter(ShowProductionActivity.this, list);
                second_gridview.setAdapter(secondThemeAdapter);
                aCache.put("second_theme" + i, list, ACache.TIME_DAY);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBytes, Throwable throwable) {
            }
        });
    }


    //绑定商家与 用户
    public void bing() {
        RequestParams params = new RequestParams();
        params.put("shop_id", userInfo.shop().get());
        params.put("customer_id", userInfo.id().get());
        HttpClient.post(this, HttpUrl.POST_BING, params, new BaseJsonHttpResponseHandler(this) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            }
        });
    }

    @UiThread
    public void showHistorySearch() {
        if (myApplication.getList().size() != 0) {
            listview.setVisibility(View.VISIBLE);
        } else {
            listview.setVisibility(View.GONE);
        }

    }

    /**
     * 显示阴影
     */
    @UiThread
    public void showYinying() {
        yinyin.setVisibility(View.VISIBLE);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mkeyTime) > 2000) {
                mkeyTime = System.currentTimeMillis();
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_LONG).show();
            } else {
                MyApplication.getInstance().exit();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //分类和主题按钮点击后 还原
    public void buttonBack() {
        theme.setBackgroundColor(getResources().getColor(R.color.blue));
        type.setBackgroundColor(getResources().getColor(R.color.blue));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if ( !second.equals("-1")){
            showSecondProduction(second);
        }
    }
}
