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
import com.singularityclub.shopping.customview.GoodsItem;
import com.singularityclub.shopping.customview.GoodsItem_;
import com.singularityclub.shopping.preferences.UserInfo_;
import com.singularityclub.shopping.zxing.camera.PlanarYUVLuminanceSource;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.apache.http.Header;

import java.util.ArrayList;

/**
 * Created by fenghao on 2015/8/19.
 * gridview适配器
 */

public class GridViewAdapter extends BaseAdapter {


    public ArrayList<ProductionItem> array = new ArrayList<>();
    protected Context context;
    LayoutInflater inflater = null;
    static class ViewHolder{
        ImageView goods_img;
        TextView money, goods_title;
    }

    // DisplayImageOptions的初始化
    DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageForEmptyUri(R.mipmap.load)
            .showImageOnLoading(R.mipmap.load)
            .showImageOnFail(R.mipmap.load)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .build();

    public GridViewAdapter(Context context, ArrayList<ProductionItem> array) {
        this.array = array;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public interface ClickChange{
        void changePrice( int num);
    }

    public ClickChange clickChange;

    public void setClickChange(ClickChange clickChange) {
        this.clickChange = clickChange;
    }

//    public void init( ArrayList<ProductionItem> array){
//        this.array = array;
//        this.notifyDataSetChanged();
//    }

    public void add( ArrayList<ProductionItem> list){
        array.addAll(list);
        this.notifyDataSetChanged();
    }

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
            holder.goods_title = (TextView) convertView.findViewById(R.id.goods_title);
            holder.money = (TextView) convertView.findViewById(R.id.money);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.goods_title.setText( array.get(position).getName());
        holder.money.setText( array.get(position).getSelfPrice());
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        imageLoader.displayImage(array.get(position).getUrlImg(), holder.goods_img, options);

        return convertView;
       /* if (convertView == null){
            goodsItem = GoodsItem_.build(context);
        }else{
            goodsItem = (GoodsItem)convertView;
        }
        goodsItem.setClickInterface(new GoodsItem.ClickInterface() {
            @Override
            public void change(int num) {
                if ( clickChange!= null){
                    clickChange.changePrice( num);
                }
            }
        });
        goodsItem.update( array, position);
        return goodsItem;*/

    }

}
