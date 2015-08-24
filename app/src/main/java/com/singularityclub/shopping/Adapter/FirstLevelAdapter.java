package com.singularityclub.shopping.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.singularityclub.shopping.R;

/**
 * Created by fenghao on 2015/8/24.
 */
public class FirstLevelAdapter extends BaseAdapter {

    public int[] array = {1,2,3,4,5,6,7,8,9,0,1,2,3,4,5,6,7,8,9,0};
    public boolean[] color = new boolean[array.length];
    LayoutInflater inflater = null;
    Context context;

    static class ViewHolder{
        TextView firstLevel;
    }

    public FirstLevelAdapter( Context context) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;

        for(int i = 0 ; i < color.length; i++){
            color[i] = false;
        }
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
            convertView = inflater.inflate(R.layout.layout_first_level, null);
            holder.firstLevel = (TextView) convertView.findViewById(R.id.first_level);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        if( color[position]){
            convertView.setBackgroundColor(context.getResources().getColor(R.color.dark_blue));
            holder.firstLevel.setBackgroundColor(context.getResources().getColor(R.color.dark_blue));
        }else{
            holder.firstLevel.setBackgroundColor(context.getResources().getColor(R.color.blue));
            convertView.setBackgroundColor(context.getResources().getColor(R.color.blue));

        }
        return convertView;
    }

}
