package com.singularityclub.shopping.Activity;

import android.content.Intent;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Gallery;

import com.singularityclub.shopping.Adapter.ImageAdapter;
import com.singularityclub.shopping.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

/**
 * Created by Howe on 2015/8/26.
 */
@EActivity(R.layout.activity_show_photo)
public class ShowPhotoActivity extends BaseActivity {

    @ViewById
    Gallery show_photo;

    protected ArrayList<String> pictures = new ArrayList<>();

    @AfterViews
    public void init(){
        Intent intent = getIntent();
        pictures = (ArrayList<String>) intent.getSerializableExtra("products");

        WindowManager m = getWindowManager();
        Display display = m.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();  //获取对话框当前的参数值
        p.height = (int) (display.getHeight() * 0.8);   //高度设置为屏幕的0.8
        p.width = (int) (display.getWidth() * 0.8);    //宽度设置为屏幕的0.8
        getWindow().setAttributes(p);     //设置生效

        show_photo.setAdapter(new ImageAdapter(ShowPhotoActivity.this, pictures));
    }
}
