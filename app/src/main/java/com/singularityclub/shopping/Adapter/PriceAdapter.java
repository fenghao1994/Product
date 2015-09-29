package com.singularityclub.shopping.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.singularityclub.shopping.R;

/**
 * Created by fenghao on 2015/9/29.
 */
public class PriceAdapter extends BaseAdapter {

    TextView textView;
    Context context;
    String[] strings;
    Boolean[] color;

    public PriceAdapter(Context context, String[] strings) {
        this.context = context;
        this.strings = strings;
        color = new Boolean[strings.length];
        initColor();
    }

    public void initColor(){
        for (int i = 0 ; i < color.length ; i++){
            color[i] = false;
        }
    }

    @Override
    public int getCount() {
        return strings.length;
    }

    @Override
    public Object getItem(int position) {
        return strings[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_price_content, null);
            textView = (TextView) convertView.findViewById(R.id.price);
            convertView.setTag(textView);
        }else{
            textView = (TextView) convertView.getTag();
        }
        if (color[position]){
            textView.setTextColor(context.getResources().getColor(R.color.new_red));
        }else{
            textView.setTextColor(context.getResources().getColor(R.color.new_black));
        }
        textView.setText(strings[position]);
        return convertView;
    }

    public Boolean[] getColor() {
        return color;
    }

    public void setColor(Boolean[] color) {
        this.color = color;
    }
}
