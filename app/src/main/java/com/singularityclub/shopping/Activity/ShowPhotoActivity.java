package com.singularityclub.shopping.Activity;

import android.content.Intent;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Gallery;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.singularityclub.shopping.Adapter.ImageAdapter;
import com.singularityclub.shopping.Model.Picture;
import com.singularityclub.shopping.R;
import com.singularityclub.shopping.Utils.http.BaseJsonHttpResponseHandler;
import com.singularityclub.shopping.Utils.http.HttpClient;
import com.singularityclub.shopping.Utils.http.HttpUrl;
import com.singularityclub.shopping.Utils.http.JacksonMapper;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.apache.http.Header;
import org.codehaus.jackson.type.TypeReference;

import java.util.ArrayList;

/**
 * Created by Howe on 2015/8/26.
 */
@EActivity(R.layout.activity_show_photo)
public class ShowPhotoActivity extends BaseActivity {

    @ViewById
    Gallery show_photo;

    protected String productId;
    protected ArrayList<String> pictures;

    @AfterViews
    public void init(){
        Intent intent = getIntent();
        productId = intent.getStringExtra("product_id");

        pictures = new ArrayList<>();

        WindowManager m = getWindowManager();
        Display display = m.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();  //获取对话框当前的参数值
        p.height = (int) (display.getHeight() * 0.8);   //高度设置为屏幕的0.8
        p.width = (int) (display.getWidth() * 0.8);    //宽度设置为屏幕的0.8
        getWindow().setAttributes(p);     //设置生效

        getPhotoUri();
    }


    //获取图片地址
    public void getPhotoUri(){
        RequestParams parms = new RequestParams();
        parms.put("product_id", productId);

        HttpClient.post(this, HttpUrl.POST_GET_PHOTO, parms, new BaseJsonHttpResponseHandler(this){
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                ArrayList<Picture> list = JacksonMapper.parseToList(responseString, new TypeReference<ArrayList<Picture>>() {
                });

                for(int n = 0; n < list.size(); n++){
                    pictures.add(list.get(n).getImageURL());
                }

                if(list.size() == 0){
                    Toast.makeText(ShowPhotoActivity.this, "当前没有可查看的图片", Toast.LENGTH_SHORT).show();
                    ShowPhotoActivity.this.finish();
                } else {
                    show_photo.setAdapter(new ImageAdapter(ShowPhotoActivity.this, pictures));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(getApplication(), "数据提交失败---" + statusCode, Toast.LENGTH_LONG).show();
            }
        });
    }
}
