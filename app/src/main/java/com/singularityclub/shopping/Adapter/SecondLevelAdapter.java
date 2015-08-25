package com.singularityclub.shopping.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.singularityclub.shopping.Model.SecondClassify;
import com.singularityclub.shopping.R;

import java.util.ArrayList;

/**
 * Created by fenghao on 2015/8/21.
 * 二级菜单
 */
public class SecondLevelAdapter extends BaseAdapter{

    public ArrayList<SecondClassify> array;
    LayoutInflater inflater = null;
    Context context;

    static class ViewHolder{
        TextView secondLevel;
    }

    public SecondLevelAdapter( Context context, ArrayList<SecondClassify> array) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.array = array;
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
            convertView = inflater.inflate(R.layout.layout_second_level, null);
            holder.secondLevel = (TextView) convertView.findViewById(R.id.second_level);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.secondLevel.setText(array.get(position).getSecondClassifyName());
        return convertView;
    }
}
