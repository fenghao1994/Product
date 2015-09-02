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
@EBean
public class GridViewAdapter extends BaseAdapter {


    public ArrayList<ProductionItem> array = new ArrayList<>();
    @RootContext
    protected Context context;

    public GridViewAdapter(Context context) {
        this.context = context;
    }

    public void init( ArrayList<ProductionItem> array){
        this.array = array;
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

        GoodsItem goodsItem = null;
        if (convertView == null){
            goodsItem = GoodsItem_.build(context);
        }else{
            goodsItem = (GoodsItem)convertView;
        }
        goodsItem.update( array, position);
        return goodsItem;

    }

}
