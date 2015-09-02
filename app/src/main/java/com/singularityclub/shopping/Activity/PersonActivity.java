package com.singularityclub.shopping.Activity;

import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    protected ImageView person_back;
    @ViewById
    protected GridView person_gridview;
    @ViewById
    protected TextView person_title;
    @ViewById
    protected WebView webview;
    @Pref
    protected UserInfo_ userInfo;

    protected PersonAdapter personAdapter;
    protected Person person;

    @AfterViews
    public void init(){
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

        person_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < personAdapter.array.size(); i++) {
                    personAdapter.color[i] = false;
                }
                personAdapter.color[position] = true;
                personAdapter.notifyDataSetChanged();
                person = personAdapter.array.get(position);
                if (userInfo.person().get() == person.getId()){
                    person_title.setText("我的人格");
                }else{
                    person_title.setText(person.getName());
                }
                String mime = "text/html";
                String encoding = "utf-8";
                webview.getSettings().setJavaScriptEnabled(true);
                webview.loadDataWithBaseURL(null, "<style>img{width:100%;}</style>  <base href=\"" + HttpUrl.ROOT + "\" />" + person.getUrl(), mime, encoding, null);
            }
        });
    }

    /**
     * 获得所以的主题信息
     */
    public void getPerson(){
        HttpClient.get(this, HttpUrl.GET_PERSON, null, new BaseJsonHttpResponseHandler(this){
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                ArrayList<Person> list = JacksonMapper.parseToList(responseString, new TypeReference<ArrayList<Person>>() {
                });
                personAdapter = new PersonAdapter(PersonActivity.this, list);
                person_gridview.setAdapter( personAdapter);
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
        for (int i = 0 ; i < personAdapter.array.size() ; i++){
            if ( userInfo.person().get() == personAdapter.array.get(i).getId()){
                String mime = "text/html";
                String encoding = "utf-8";
                webview.getSettings().setJavaScriptEnabled(true);
                webview.loadDataWithBaseURL(null, "<style>img{width:100%;}</style>  <base href=\"" + HttpUrl.ROOT + "\" />" + personAdapter.array.get(i).getUrl(), mime, encoding, null);

                //改变左侧颜色
                personAdapter.color[i] = true;
                personAdapter.notifyDataSetChanged();
                return;
            }
        }
    }
}
