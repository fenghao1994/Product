package com.singularityclub.shopping.Activity;

import android.graphics.Color;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.singularityclub.shopping.Adapter.PersonAdapter;
import com.singularityclub.shopping.Model.Person;
import com.singularityclub.shopping.R;
import com.singularityclub.shopping.Utils.http.BaseJsonHttpResponseHandler;
import com.singularityclub.shopping.Utils.http.HttpClient;
import com.singularityclub.shopping.Utils.http.HttpUrl;
import com.singularityclub.shopping.Utils.http.JacksonMapper;
import com.singularityclub.shopping.preferences.UserInfo_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.apache.http.Header;
import org.codehaus.jackson.type.TypeReference;

import java.util.ArrayList;

/**
 * Created by fenghao on 2015/9/1.
 */
@EActivity(R.layout.activity_person)
public class PersonActivity extends BaseActivity {


    @ViewById
    protected LinearLayout layout_back;
    @ViewById
    protected RelativeLayout relativelayout1, relativelayout2, relativelayout3, relativelayout4,relativelayout5, relativelayout6, relativelayout7, relativelayout8, relativelayout9;
    @ViewById
    protected ImageView person1, person2, person3, person4, person5, person6, person7,
            person8,person9;
    @ViewById
    protected TextView text1, text2, text3, text4, text5, text6, text7, text8, text9;

    protected ImageView[] images = new ImageView[9];
    protected TextView[] texts = new TextView[9];
    protected RelativeLayout[] relatives = new RelativeLayout[9];



    @ViewById
    protected ImageView person_back;
  /*  @ViewById
    protected GridView person_gridview;*/
    @ViewById
    protected TextView person_title, birth, sex;
    @ViewById
    protected WebView webview;
    @Pref
    protected UserInfo_ userInfo;

    protected PersonAdapter personAdapter;
//    protected Person person;

    protected ArrayList<Person> arrayList;

    @AfterViews
    public void init(){
        webview.setBackgroundResource(R.mipmap.bg_down);
        webview.setBackgroundColor(Color.argb(0, 0, 0, 0));
        initArray();
        if (userInfo.person().get() == 0){
            birth.setVisibility(View.GONE);
            sex.setVisibility(View.GONE);
        }else{
            birth.setVisibility(View.VISIBLE);
            birth.setText("出生日期：" + userInfo.birthday().get().toString());
            sex.setVisibility(View.VISIBLE);
            sex.setText("性别：" + userInfo.sex().get().toString());
        }

        getPerson();

        person_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PersonActivity.this.finish();
            }
        });

        layout_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PersonActivity.this.finish();
            }
        });

        listen();

    }

    public void initArray(){
        images[0] = person1;
        images[1] = person2;
        images[2] = person3;
        images[3] = person4;
        images[4] = person5;
        images[5] = person6;
        images[6] = person7;
        images[7] = person8;
        images[8] = person9;
        relatives[0] = relativelayout1;
        relatives[1] = relativelayout2;
        relatives[2] = relativelayout3;
        relatives[3] = relativelayout4;
        relatives[4] = relativelayout5;
        relatives[5] = relativelayout6;
        relatives[6] = relativelayout7;
        relatives[7] = relativelayout8;
        relatives[8] = relativelayout9;
        texts[0] = text1;
        texts[1] = text2;
        texts[2] = text3;
        texts[3] = text4;
        texts[4] = text5;
        texts[5] = text6;
        texts[6] = text7;
        texts[7] = text8;
        texts[8] = text9;
    }


    /**
     * 获得所以的主题信息
     */
    public void getPerson(){
        HttpClient.get(this, HttpUrl.GET_PERSON, null, new BaseJsonHttpResponseHandler(this) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                ArrayList<Person> list = JacksonMapper.parseToList(responseString, new TypeReference<ArrayList<Person>>() {
                });
//                personAdapter = new PersonAdapter(PersonActivity.this, list);
//                person_gridview.setAdapter(personAdapter);
                arrayList = list;
                loadWebview();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(PersonActivity.this, "获取人格失败", Toast.LENGTH_LONG).show();
            }
        });
    }

    //loadWebview

    public void loadWebview(){
        String mime = "text/html";
        String encoding = "utf-8";
        webview.getSettings().setJavaScriptEnabled(true);

        if (userInfo.person().get() != 0){
            for (int i = 0 ; i < arrayList.size() ; i++){
                if ( userInfo.person().get() == arrayList.get(i).getId()){
                    webview.loadDataWithBaseURL(null, "<style>img{width:100%;}</style>  <base href=\"" + HttpUrl.ROOT + "\" />" + arrayList.get(i).getUrl(), mime, encoding, null);

                    changeState(i);
                    //改变左侧颜色
//                    personAdapter.color[i] = true;
//                    personAdapter.notifyDataSetChanged();
                    return;
                }
            }
        }else{
            webview.loadDataWithBaseURL(null, "<style>img{width:100%;}</style>  <base href=\"" + HttpUrl.ROOT + "\" />" + arrayList.get(0).getUrl(), mime, encoding, null);
            noMessageShow();
        }
    }

    public void changeState(int num){
        backToinit();
        images[num].setImageDrawable(getResources().getDrawable(R.mipmap.person4));
        texts[num].setTextColor(getResources().getColor(R.color.new_red));
    }

    //没有输入个人信息的显示

    public void noMessageShow(){
        /*personAdapter.color[0] = true;
        personAdapter.notifyDataSetChanged();*/
        changeState(0);
        person_title.setText(arrayList.get(0).getName());
    }

    public void listen(){

        for (int i = 0 ; i < relatives.length ; i++){
            final int finalI = i;
            relatives[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    backToinit();
                    images[finalI].setImageDrawable(getResources().getDrawable(R.mipmap.person4));
                    texts[finalI].setTextColor(getResources().getColor(R.color.new_red));
                    loadWebView(finalI);
                }
            });
        }
    }

    public void loadWebView(int num){
        if (userInfo.person().get() == arrayList.get(num).getId()){
            person_title.setText("我的人格");
            birth.setVisibility(View.VISIBLE);
            birth.setText( userInfo.birthday().get());
            sex.setVisibility(View.VISIBLE);
            sex.setText( userInfo.sex().get());
        }else{
            person_title.setText(arrayList.get(num).getName());
            birth.setVisibility(View.GONE);
            sex.setVisibility(View.GONE);
        }
        String mime = "text/html";
        String encoding = "utf-8";
        webview.getSettings().setJavaScriptEnabled(true);
        webview.loadDataWithBaseURL(null, "<style>img{width:100%;}</style>  <base href=\"" + HttpUrl.ROOT + "\" />" + arrayList.get(num).getUrl(), mime, encoding, null);
    }

    public void backToinit(){
        for (int i = 0 ; i < images.length ; i++){
            images[i].setImageDrawable( getResources().getDrawable(R.mipmap.person3));
            texts[i].setTextColor(getResources().getColor(R.color.new_black));
        }
    }

}
