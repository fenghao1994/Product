package com.singularityclub.shopping.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.singularityclub.shopping.Model.PriceRange;
import com.singularityclub.shopping.R;

import java.util.ArrayList;

/**
 * Created by fenghao on 2015/9/29.
 */
public class PriceAdapter extends BaseAdapter {

    TextView textView;
    Context context;
    ArrayList<PriceRange> list;
    Boolean[] color;

    public PriceAdapter(Context context, ArrayList<PriceRange> list) {
        this.context = context;
        this.list = list;
        color = new Boolean[list.size()];
        initColor();
    }

    public void initColor(){
        for (int i = 0 ; i < color.length ; i++){
            color[i] = false;
        }
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
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
        textView.setText(list.get(position).getLow() + " - " + list.get(position).getHigh());
        return convertView;
    }

    public Boolean[] getColor() {
        return color;
    }

    public void setColor(Boolean[] color) {
        this.color = color;
    }

    public ArrayList<PriceRange> getList() {
        return list;
    }

    public void setList(ArrayList<PriceRange> list) {
        this.list = list;
    }
}
