package com.singularityclub.shopping.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.singularityclub.shopping.Model.MainClassify;
import com.singularityclub.shopping.R;

import java.util.ArrayList;

/**
 * Created by fenghao on 2015/8/24.
 */
public class FirstLevelAdapter extends BaseAdapter {

    public ArrayList<MainClassify> array;
    public boolean[] color;
    LayoutInflater inflater = null;
    Context context;

    static class ViewHolder{
        TextView firstLevel;
    }

    public FirstLevelAdapter( Context context, ArrayList<MainClassify> array) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.array = array;
        color = new boolean[array.size()];
        for(int i = 0 ; i < color.length; i++){
            color[i] = false;
        }
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
            convertView = inflater.inflate(R.layout.layout_first_level, null);
            holder.firstLevel = (TextView) convertView.findViewById(R.id.first_level);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.firstLevel.setText(array.get(position).getMainClassifyName());

        if( color[position]){
            holder.firstLevel.setTextColor(context.getResources().getColor(R.color.new_red));
        }else{
            holder.firstLevel.setTextColor(context.getResources().getColor(R.color.new_black));

        }
        return convertView;
    }

}
