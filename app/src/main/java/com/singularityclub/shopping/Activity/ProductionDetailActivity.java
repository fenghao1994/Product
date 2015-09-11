package com.singularityclub.shopping.Activity;


import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.singularityclub.shopping.Adapter.AudioAdapter;
import com.singularityclub.shopping.Application.MyApplication;
import com.singularityclub.shopping.Model.Music;
import com.singularityclub.shopping.Model.Picture;
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
    protected TextView product_name, product_price, audio_name, title, total_time, name, price, biref, character, area, weight;
    @ViewById
    protected EditText search_text;
    @ViewById
    protected ImageView photo, back, play, pause, add_attention, cancel_attention;
    @ViewById
    protected RelativeLayout search;
    @ViewById
    protected ListView menu;
    @ViewById
    protected SeekBar procesee,sound;
    @ViewById
    protected WebView webview;
    @ViewById
    protected ScrollView detail_body;
    @Pref
    protected UserInfo_ userInfo;
    protected ProductionItem product;

    protected String productId;     //商品id
    protected String code;          //二维码
    protected ArrayList<Music> audioList;  //音频对象list
    protected Boolean isPlay;       //判断是否有mediaplayer对象在运行
    protected Boolean isFirstPlay;  //判断是否第一次点击播放按钮
    protected Boolean isClick;      //录音菜单点击判断
    protected AudioManager audioManager;
    protected MediaPlayer player;
    protected AudioAdapter audioAdapter;
    protected int maxSound;
    protected int currentSound;
    protected Handler handler;
    protected MyApplication myApplication;
    protected Boolean showName;     //展示全部文字
    protected ArrayList<String> pictures;   //全部介绍图片

    //消费者行为记录的时间
    protected Long startTime;
    protected Long endTime;
    protected float totalTime;

    @AfterViews
    public void init(){

        Intent intent = getIntent();
        productId = intent.getStringExtra("product_id");
        code = intent.getStringExtra("qrcode");

        myApplication = (MyApplication) getApplication();
        myApplication.setProductID(productId);

        //页面顶部显示调整
        back.setVisibility(View.VISIBLE);
        search.setVisibility(View.GONE);
        title.setVisibility(View.VISIBLE);

        //音频相关数据
        audioList = new ArrayList<>();
        isPlay = false;
        isClick = false;
        isFirstPlay = false;
        handler = new Handler();
        player = new MediaPlayer();
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);   //获取音量服务
        maxSound = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);  //获取系统音量最大值
        sound.setMax(maxSound);
        currentSound = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        sound.setProgress(currentSound);
        sound.setOnSeekBarChangeListener(new SeekBarListener());
        procesee.setOnSeekBarChangeListener(new ProcessListener());

        showName = false;
        pictures = new ArrayList<>();

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
                selectPicture();
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

                updateData(product);
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
                selectPicture();

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

                updateData(product);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(ProductionDetailActivity.this, "数据出错--" + statusCode, Toast.LENGTH_LONG).show();
            }
        });
    }

    //数据显示
    @UiThread
    public void updateData(ProductionItem product){
        //表格数据
        name.setText(product.getName());
        price.setText(product.getPrice());
        biref.setText(product.getBrief());
        character.setText(product.getCharacter());
        area.setText(product.getArea());
        weight.setText(product.getWeight());

        //webView数据
        webview.getSettings().setJavaScriptEnabled(true);
        String html = product.getIntro();
        webview.setVerticalScrollBarEnabled(false);
        webview.loadDataWithBaseURL(null, "<style>img{width:100%;}</style>  <base href=\"" + HttpUrl.ROOT + "\" />" + html, "text/html", "utf-8", null);
    }

    //关注图片选择
    protected void selectPicture(){
        if(product.getAttention().equals("0") || product.getAttention().equals("")){
            add_attention.setVisibility(View.VISIBLE);
            cancel_attention.setVisibility(View.GONE);
        } else {
            add_attention.setVisibility(View.GONE);
            cancel_attention.setVisibility(View.VISIBLE);
        }
    }

    //展示全部标题
    @Click(R.id.product_name)
    protected void showProductName(){
        if(!showName) {
            product_name.setEllipsize(null);
            product_name.setMaxLines(5);
            showName = true;
        } else {
            product_name.setEllipsize(TextUtils.TruncateAt.valueOf("END"));
            product_name.setMaxLines(2);
            showName = false;
        }
    }

    //返回
    @Click(R.id.layout_person)
    protected void doBack(){
        //计时结束
        endTime = System.currentTimeMillis();
        totalTime = ((float)(endTime - startTime)) / 1000;
        actionMark();

        isPlay = false;
        try{
            player.stop();
            player.release();
        } catch(Exception e){
            e.printStackTrace();
        }

        ProductionDetailActivity.this.finish();
    }

    //购物车
    @Click(R.id.layout_shop)
    protected void goShopCar(){

        //计时结束
        endTime = System.currentTimeMillis();
        totalTime = ((float)(endTime - startTime)) / 1000;
        actionMark();

        player.pause();

        Intent intent = new Intent(ProductionDetailActivity.this, ShopCarActivity_.class);
        startActivity(intent);
    }

    //关注
    @Click(R.id.attention)
    protected void addCar(){
        RequestParams parms = new RequestParams();
        parms.put("customer_id", userInfo.id().get());
        parms.put("product_id", productId);

        if (product.getAttention().equals("1")) {
            product.setAttention("0");
            selectPicture();
        } else {
            product.setAttention("1");
            selectPicture();
        }

        HttpClient.post(this, HttpUrl.POST_PRODUCTION_ATTENTION, parms, new BaseJsonHttpResponseHandler(this) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                if (product.getAttention().equals("1")) {
                    Toast.makeText(ProductionDetailActivity.this, "加入购物车成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ProductionDetailActivity.this, "已取消", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(getApplication(), "加入购物车失败---" + statusCode, Toast.LENGTH_LONG).show();
            }
        });
    }

    //获取图片数据,点击查看图片
    @Click(R.id.photo)
    public void getPhotoUri(){
        RequestParams parms = new RequestParams();
        parms.put("product_id", productId);

        HttpClient.post(this, HttpUrl.POST_GET_PHOTO, parms, new BaseJsonHttpResponseHandler(this) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                ArrayList<Picture> list = JacksonMapper.parseToList(responseString, new TypeReference<ArrayList<Picture>>() {
                });

                for (int n = 0; n < list.size(); n++) {
                    pictures.add(list.get(n).getImageURL());
                }

                if (pictures.size() == 0) {
                    Toast.makeText(ProductionDetailActivity.this, "当前没有可查看的图片", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(ProductionDetailActivity.this, ShowPhotoActivity_.class);
                    intent.putExtra("products", pictures);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(getApplication(), "数据提交失败---" + statusCode, Toast.LENGTH_LONG).show();
            }
        });
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
            detail_body.scrollTo(0,0);

            if(!isClick){
                menu.setVisibility(View.VISIBLE);
                isClick = true;
                hideMenu();
            } else {
                menu.setVisibility(View.GONE);
                isClick = false;
                hideMenu();
            }

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
                                        startBarUpdate();
                                    }
                                });
                                isPlay = true;
                                isFirstPlay = true;
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

    //页面滑动时音频菜单隐藏
    public void hideMenu(){
        detail_body.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_MOVE) {
                    menu.setVisibility(View.GONE);
                    isClick = false;
                }

                return false;
            }
        });
    }

    //音频播放控制
    @Click(R.id.play)
    public void audioPlay(){
        if(!isFirstPlay){
            Toast.makeText(ProductionDetailActivity.this, "请选择音频", Toast.LENGTH_LONG).show();
        } else {
            player.start();
            isPlay = true;
            play.setVisibility(View.GONE);
            pause.setVisibility(View.VISIBLE);
        }
    }
    @Click(R.id.pause)
    public void audioPause(){
        player.pause();
        isPlay = false;
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
            if(isPlay) {
                int currentPosition = player.getCurrentPosition();
                int mMax = player.getDuration();
                procesee.setMax(mMax);
                procesee.setProgress(currentPosition);
                handler.postDelayed(r, 100);
            }
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

            if(minute > 60){
                minute %= 60;
            }
        } else {
            second = time;
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

    @Override
    protected void onRestart() {
        super.onRestart();
        if(player != null){
            play.setVisibility(View.VISIBLE);
            pause.setVisibility(View.GONE);
        }
    }
}
