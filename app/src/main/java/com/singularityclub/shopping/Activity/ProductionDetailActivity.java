package com.singularityclub.shopping.Activity;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.singularityclub.shopping.Adapter.AudioAdapter;
import com.singularityclub.shopping.Adapter.MyFragmentPagerAdapter;
import com.singularityclub.shopping.Model.Music;
import com.singularityclub.shopping.Model.ProductionItem;
import com.singularityclub.shopping.R;
import com.singularityclub.shopping.Utils.http.BaseJsonHttpResponseHandler;
import com.singularityclub.shopping.Utils.http.HttpClient;
import com.singularityclub.shopping.Utils.http.HttpUrl;
import com.singularityclub.shopping.Utils.http.JacksonMapper;
import com.singularityclub.shopping.preferences.UserInfo_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.apache.http.Header;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Howe on 2015/8/21.
 */

@EActivity(R.layout.activity_production_detail)
public class ProductionDetailActivity extends FragmentActivity {

    @ViewById
    protected ViewPager viewpager;
    @ViewById
    protected PagerSlidingTabStrip tabs;
    @ViewById
    protected TextView product_name, product_price, audio_name, title, total_time;
    @ViewById
    protected EditText search_text;
    @ViewById
    protected ImageView photo, back, play, pause;
    @ViewById
    protected RelativeLayout search;
    @ViewById
    protected ListView menu;
    @ViewById
    protected SeekBar procesee,sound;
    @Pref
    protected UserInfo_ userInfo;
    protected ProductionItem product;

    protected String productId;     //商品id
    protected String code;          //二维码
    protected ArrayList<Music> audioList;
    protected Boolean isPlay;
    protected AudioManager audioManager;
    protected MediaPlayer player;
    protected AudioAdapter audioAdapter;
    protected int maxSound;
    protected int currentSound;
    protected Handler handler;

    //消费者行为记录的时间
    protected Long startTime;
    protected Long endTime;
    protected float totalTime;

    @AfterViews
    public void init(){

        Intent intent = getIntent();
        productId = intent.getStringExtra("product_id");
        code = intent.getStringExtra("qrcode");

        back.setVisibility(View.VISIBLE);
        search.setVisibility(View.GONE);
        title.setVisibility(View.VISIBLE);

        audioList = new ArrayList<>();
        isPlay = false;
        handler = new Handler();
        player = new MediaPlayer();
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);   //获取音量服务
        maxSound = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);  //获取系统音量最大值
        sound.setMax(maxSound);
        currentSound = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        sound.setProgress(currentSound);
        sound.setOnSeekBarChangeListener(new SeekBarListener());
        procesee.setOnSeekBarChangeListener(new ProcessListener());

        //浏览计时开始
        startTime = System.currentTimeMillis();

        //判断进入方式
        if(code == null || code.equals("")){
            showProduct();
        } else if(productId == null || productId.equals("")){
            showByCode();
        }

        //获取音频数据
        getAudio();


        viewpager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager()));
        tabs.setViewPager(viewpager);
        tabs.setTextSize(30);
        tabs.setTabPaddingLeftRight(100);
        tabs.setDividerColor(Color.TRANSPARENT);
        tabs.setUnderlineHeight(2);

    }

    //点击商品查看商品详细
    protected void showProduct(){
        RequestParams parms = new RequestParams();
        parms.put("product_id", productId);
        parms.put("customer_id", userInfo.id().get());

        HttpClient.post(this, HttpUrl.POST_PRODUCT_DETAIL, parms, new BaseJsonHttpResponseHandler(this) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                product = JacksonMapper.parseToList(responseString, new TypeReference<ProductionItem>() {
                });

                product_name.setText(product.getName());
                product_price.setText(product.getPrice());

                // DisplayImageOptions的初始化
                DisplayImageOptions options = new DisplayImageOptions.Builder()
                        .showImageForEmptyUri(R.mipmap.goods_demo)
                        .showImageOnLoading(R.mipmap.goods_demo)
                        .showImageOnFail(R.mipmap.goods_demo)
                        .cacheInMemory(true)
                        .cacheOnDisk(true)
                        .bitmapConfig(Bitmap.Config.RGB_565)
                        .build();

                ImageLoader imageLoader = ImageLoader.getInstance();
                imageLoader.init(ImageLoaderConfiguration.createDefault(ProductionDetailActivity.this));
                imageLoader.displayImage(product.getUrlImg(), photo, options);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(ProductionDetailActivity.this, "数据出错--" + statusCode, Toast.LENGTH_LONG).show();
            }
        });
    }


    //二维码方式查看商品
    protected void showByCode(){
        RequestParams parms = new RequestParams();
        parms.put("qrcode", code);
        parms.put("shop_id", userInfo.shop().get());
        parms.put("customer_id", userInfo.id().get());

        HttpClient.post(this, HttpUrl.POST_SHOW_PRODUCT_CODE, parms, new BaseJsonHttpResponseHandler(this) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                product = JacksonMapper.parseToList(responseString, new TypeReference<ProductionItem>() {
                });

                product_name.setText(product.getName());
                product_price.setText(product.getPrice());

                // DisplayImageOptions的初始化
                DisplayImageOptions options = new DisplayImageOptions.Builder()
                        .showImageForEmptyUri(R.mipmap.goods_demo)
                        .showImageOnLoading(R.mipmap.goods_demo)
                        .showImageOnFail(R.mipmap.goods_demo)
                        .cacheInMemory(true)
                        .cacheOnDisk(true)
                        .bitmapConfig(Bitmap.Config.RGB_565)
                        .build();

                ImageLoader imageLoader = ImageLoader.getInstance();
                imageLoader.init(ImageLoaderConfiguration.createDefault(ProductionDetailActivity.this));
                imageLoader.displayImage(product.getUrlImg(), photo, options);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(ProductionDetailActivity.this, "数据出错--" + statusCode, Toast.LENGTH_LONG).show();
            }
        });
    }

    //返回
    @Click(R.id.back)
    protected void doBack(){
        ProductionDetailActivity.this.finish();

        //计时结束
        endTime = System.currentTimeMillis();
        totalTime = ((float)(endTime - startTime)) / 1000;
        actionMark();
    }

    //购物车
    @Click(R.id.shop_car)
    protected void goShopCar(){

        //计时结束
        endTime = System.currentTimeMillis();
        totalTime = ((float)(endTime - startTime)) / 1000;
        actionMark();

        Intent intent = new Intent(ProductionDetailActivity.this, ShopCarActivity_.class);
        startActivity(intent);
    }

    //关注
    @Click(R.id.add_attention)
    protected void addCar(){
        RequestParams parms = new RequestParams();
        parms.put("customer_id", userInfo.id().get());
        parms.put("product_id", productId);

        HttpClient.post(this, HttpUrl.POST_PRODUCTION_ATTENTION, parms, new BaseJsonHttpResponseHandler(this) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Toast.makeText(getApplication(), "加入购物车成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(getApplication(), "加入购物车失败---" + statusCode, Toast.LENGTH_LONG).show();
            }
        });
    }

    //点击查看图片
    @Click(R.id.photo)
    protected void goWatch(){
        Intent intent = new Intent(ProductionDetailActivity.this, ShowPhotoActivity_.class);
        intent.putExtra("product_id", productId);
        startActivity(intent);
    }

    //获取音频数据
    public void getAudio(){
        RequestParams params = new RequestParams();
        params.put("product_id", productId);

        HttpClient.post(this, HttpUrl.POST_GET_AUDIO, params, new BaseJsonHttpResponseHandler(this) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                final ArrayList<Music> list = JacksonMapper.parseToList(responseString, new TypeReference<ArrayList<Music>>() {
                });
                audioList = list;
                audioAdapter = new AudioAdapter(ProductionDetailActivity.this, audioList);
                menu.setAdapter(audioAdapter);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(getApplication(), "数据获取失败---" + statusCode, Toast.LENGTH_LONG).show();
            }
        });

    }

    //音频列表
    @Click(R.id.audio_list)
    public void audioList(){
        if (audioList.size() != 0) {
            menu.setVisibility(View.VISIBLE);

            menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                player.stop();
                                String path = "http://dt.tavern.name/" + audioList.get(position).getAudioUrl();
                                player.reset();
                                player.setDataSource(path);
                                player.prepareAsync();
                                player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                    @Override
                                    public void onPrepared(MediaPlayer mp) {
                                        player = mp;
                                        player.start();
                                        int allTime = player.getDuration();
                                        update(allTime);
                                    }
                                });
                                isPlay = true;
                                startBarUpdate();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();

                    audio_name.setText(audioList.get(position).getName());
                    play.setVisibility(View.GONE);
                    pause.setVisibility(View.VISIBLE);
                    menu.setVisibility(View.GONE);
                }
            });
        } else {
            Toast.makeText(ProductionDetailActivity.this, "没有可播放音频", Toast.LENGTH_LONG).show();
        }
    }

    //音频播放控制
    @Click(R.id.play)
    public void audioPlay(){
        if(!isPlay){
            Toast.makeText(ProductionDetailActivity.this, "请选择音频", Toast.LENGTH_LONG).show();
        } else {
            player.start();
            play.setVisibility(View.GONE);
            pause.setVisibility(View.VISIBLE);
        }
    }
    @Click(R.id.pause)
    public void audioPause(){
        player.pause();

        play.setVisibility(View.VISIBLE);
        pause.setVisibility(View.GONE);
    }

    //进度条控制
    public void startBarUpdate(){
        handler.post(r);
    }
    Runnable r=new Runnable() {

        @Override
        public void run() {
            int currentPosition = player.getCurrentPosition();
            int mMax=player.getDuration();
            procesee.setMax(mMax);
            procesee.setProgress(currentPosition);
            handler.postDelayed(r, 100);
        }
    };

    //音量进度条
    private class SeekBarListener implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            if (fromUser) {
                int SeekPosition=seekBar.getProgress();
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, SeekPosition, 0);
            }
        }
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {}
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {}
    }

    //播放进度条
    private class ProcessListener implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            if (fromUser == true) {
                player.seekTo(progress);
            }
        }
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {}
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {}
    }

    //时间转换
    public String showTime(int time){
        int minute = 0, second = 0;
        time /= 1000;
        if(time > 60){
            minute = time / 60;
            second = time % 60;
        }
        if(minute > 60){
            minute %= 60;
        }
        return String.format("%02d:%02d", minute, second);
    }

    //消费者行为记录
    public void actionMark(){
        RequestParams params = new RequestParams();
        params.put("customer_id", userInfo.id().get());
        params.put("startTime", new Date(startTime));
        params.put("endTime", new Date(endTime));
        params.put("extra_id", productId);
        params.put("extra_type", 0);
        params.put("total_minutes", totalTime);

        HttpClient.post(this, HttpUrl.POST_ACTION, params, new BaseJsonHttpResponseHandler(this) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            }
        });
    }

    @UiThread
    public void update(int allTime){
        total_time.setText(showTime(allTime));
    }

}
