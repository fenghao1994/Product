package com.singularityclub.shopping.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.singularityclub.shopping.R;

/**
 * Created by fenghao on 2015/8/21.
 * 二级菜单
 */
public class SecondLevelAdapter extends BaseAdapter{

    public int[] array = {1,2,3,4,5,6,7,8,9,0,1,2,3,4,5,6,7,8,9,0};
    LayoutInflater inflater = null;
    Context context;

    static class ViewHolder{
        TextView secondLevel;
    }

    public SecondLevelAdapter( Context context) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

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
            convertView = inflater.inflate(R.layout.layout_second_level, null);
            holder.secondLevel = (TextView) convertView.findViewById(R.id.second_level);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }
}
