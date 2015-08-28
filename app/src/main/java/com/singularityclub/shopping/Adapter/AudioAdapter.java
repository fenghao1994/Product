package com.singularityclub.shopping.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.singularityclub.shopping.Model.Music;
import com.singularityclub.shopping.R;

import java.util.ArrayList;

/**
 * Created by Howe on 2015/8/27.
 */
public class AudioAdapter extends BaseAdapter {

    Context context;
    ArrayList<Music> audios;

    public AudioAdapter(Context c, ArrayList<Music> list){
        context = c;
        audios = list;
    }

    @Override
    public int getCount() {
        return audios.size();
    }

    @Override
    public Object getItem(int position) {
        return audios.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater _LayoutInflater=LayoutInflater.from(context);
        convertView=_LayoutInflater.inflate(R.layout.layout_audio_list_item, null);
        if(convertView!=null){
            TextView _TextView1=(TextView)convertView.findViewById(R.id.num);
            TextView _TextView2=(TextView)convertView.findViewById(R.id.name);
            _TextView1.setText(String.valueOf(position + 1));
            _TextView2.setText(audios.get(position).getName());
        }

        return convertView;
    }
}
