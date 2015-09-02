package com.singularityclub.shopping.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.singularityclub.shopping.Model.ProductionItem;
import com.singularityclub.shopping.R;
import com.singularityclub.shopping.Utils.http.BaseJsonHttpResponseHandler;
import com.singularityclub.shopping.Utils.http.HttpClient;
import com.singularityclub.shopping.Utils.http.HttpUrl;
import com.singularityclub.shopping.preferences.UserInfo_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.apache.http.Header;

import java.util.ArrayList;

/**
 * Created by fenghao on 2015/9/2.
 */
@EViewGroup(R.layout.layout_goods)
public class GoodsItem extends RelativeLayout {

    @ViewById
    protected ImageView goods_img, like_img;
    @ViewById
    protected TextView money, goods_title;
    @Pref
    protected UserInfo_ userInfo;

    protected Context context;

    // DisplayImageOptions的初始化
    DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageForEmptyUri(R.mipmap.goods_demo)
            .showImageOnLoading(R.mipmap.ic_launcher)
            .showImageOnFail(R.mipmap.ic_launcher)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .build();


    public GoodsItem(Context context) {
        super(context);
        this.context = context;
    }

    public void update( final ArrayList<ProductionItem> productionItems ,final int position){
        money.setText(productionItems.get(position).getPrice());
        goods_title.setText( productionItems.get(position).getName());
        if ( productionItems.get(position).getAttention().equals("1")){
            like_img.setImageDrawable(context.getResources().getDrawable(R.mipmap.cart_like));
        }else{
            like_img.setImageDrawable(context.getResources().getDrawable(R.mipmap.detail_like));
        }
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        imageLoader.displayImage(productionItems.get(position).getUrlImg(), goods_img, options);

        like_img.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( productionItems.get(position).getAttention().equals("1")){
                    like_img.setImageDrawable(context.getResources().getDrawable(R.mipmap.detail_like));
                    productionItems.get(position).setAttention("0");
                }else{
                    like_img.setImageDrawable(context.getResources().getDrawable(R.mipmap.cart_like));
                    productionItems.get(position).setAttention("1");
                }
                RequestParams params = new RequestParams();
                params.put("customer_id", userInfo.id().get());
                params.put("product_id", productionItems.get(position).getId());
                addAttention(params);
            }
        });
    }

    public void addAttention(RequestParams params) {
        HttpClient.post(context, HttpUrl.POST_PRODUCTION_ATTENTION, params, new BaseJsonHttpResponseHandler(context.getApplicationContext() ) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            }
        });
    }
}
