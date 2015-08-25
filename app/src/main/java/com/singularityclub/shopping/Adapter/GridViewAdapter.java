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
import com.singularityclub.shopping.Model.ProductionItem;
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


    public ArrayList<ProductionItem> array;

    public boolean[] img;

    LayoutInflater inflater = null;
    Context context;

    static class ViewHolder{
        ImageView goods_img, like_img;
        TextView goods_title, money;
    }

    public GridViewAdapter(Context context, ArrayList<ProductionItem> array) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.array = array;
        img = new boolean[array.size()];
        for( int i = 0 ; i < img.length ; i++){
            img[i] = false;
        }
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
        return array.size();
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
        holder.goods_title.setText(array.get(position).getName());
        holder.money.setText(array.get(position).getName());

        if(img[position]){
            holder.like_img.setImageDrawable(context.getResources().getDrawable(R.mipmap.cart_like));
        }else{
            holder.like_img.setImageDrawable(context.getResources().getDrawable(R.mipmap.detail_like));
        }
        //imageLoader加载图像
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        imageLoader.displayImage(null, holder.goods_img, options);
        return convertView;
    }

}
