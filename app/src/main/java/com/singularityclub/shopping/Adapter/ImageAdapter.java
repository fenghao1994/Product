package com.singularityclub.shopping.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.singularityclub.shopping.R;

import java.util.ArrayList;

/**
 * Created by Howe on 2015/8/26.
 */
public class ImageAdapter extends BaseAdapter {

    ArrayList<String> photoList = new ArrayList<>();
    Context context;

    public ImageAdapter(Context c, ArrayList<String> list){
        context = c;
        photoList = list;
    }

    // DisplayImageOptions初始化
    DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageForEmptyUri(R.mipmap.goods_demo)
            .showImageOnLoading(R.mipmap.goods_demo)
            .showImageOnFail(R.mipmap.goods_demo)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .build();

    @Override
    public int getCount() {
        return photoList.size();
    }

    @Override
    public Object getItem(int position) {
        return photoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setLayoutParams(new Gallery.LayoutParams(750, 900));

        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        imageLoader.displayImage("http://dt.tavern.name/" + photoList.get(position), imageView, options);

        return imageView;
    }
}
