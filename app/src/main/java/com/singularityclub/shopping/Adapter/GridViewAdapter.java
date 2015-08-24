package com.singularityclub.shopping.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.singularityclub.shopping.R;
import com.singularityclub.shopping.Utils.http.BaseJsonHttpResponseHandler;
import com.singularityclub.shopping.Utils.http.HttpClient;
import com.singularityclub.shopping.Utils.http.HttpUrl;
import com.singularityclub.shopping.preferences.UserInfo_;
import com.singularityclub.shopping.zxing.camera.PlanarYUVLuminanceSource;

import org.androidannotations.annotations.sharedpreferences.Pref;
import org.apache.http.Header;

import java.util.ArrayList;

/**
 * Created by fenghao on 2015/8/19.
 * gridview适配器
 */
public class GridViewAdapter extends BaseAdapter {


    public int[] array = {1,2,3,4,5,6,7,8,9,0,1,2,3,4,5,6,7,8,9,0};

    @Pref
    protected UserInfo_ userInfo;

    LayoutInflater inflater = null;
    Context context;

    static class ViewHolder{
        ImageView goods_img, like_img;
        TextView goods_title, money;
    }

    public GridViewAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    // DisplayImageOptions的初始化
    DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageForEmptyUri(R.mipmap.goods_demo)
            .showImageOnLoading(R.mipmap.ic_launcher)
            .showImageOnFail(R.mipmap.ic_launcher)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .build();


    @Override
    public int getCount() {
        return array.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.layout_goods, null);
            holder.goods_img = (ImageView) convertView.findViewById(R.id.goods_img);
            holder.like_img = (ImageView) convertView.findViewById(R.id.like_img);
            holder.goods_title = (TextView) convertView.findViewById(R.id.goods_title);
            holder.money = (TextView) convertView.findViewById(R.id.money);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        ImageView img = holder.like_img;
        holder.like_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 放入商品id,测试点击关注后是否发生了变化
                RequestParams params = new RequestParams();
                params.put("customer_id", userInfo.id().get());
                params.put("product_id", "");
                clickAttention(params, img);
            }
        });

        //imageLoader加载图像
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        imageLoader.displayImage(null, holder.goods_img, options);
        return convertView;
    }

    /**
     * 单击关注时
     */
    public void clickAttention(RequestParams params, ImageView imageView){
        HttpClient.post(context, HttpUrl.POST_PRODUCTION_ATTENTION, params, new BaseJsonHttpResponseHandler(context){
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                     imageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.cart_like));
            }
        });
    }
}
