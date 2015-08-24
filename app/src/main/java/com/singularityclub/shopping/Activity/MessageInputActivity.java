package com.singularityclub.shopping.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
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

import java.util.Map;

/*信息录入页面*/
@EActivity(R.layout.activity_message_input)
public class MessageInputActivity extends Activity {

    @ViewById
    protected ImageView complete;
    @ViewById
    protected EditText edit_name, edit_phone, time;
    @ViewById
    protected RadioButton radio_man, radio_women;
    @Pref
    protected UserInfo_ userInfo;


    @AfterViews
    public void init(){
        listen();
    }

    public void listen(){
        //完成按钮的监听
        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if( edit_name.getText().length() != 0 && edit_phone.getText().length() != 0 && time.getText().length() != 0) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(MessageInputActivity.this);
                    builder.setTitle("确定提交吗？");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent();
                            intent.setClass(MessageInputActivity.this, ShowProductionActivity_.class);
                            startActivity(intent);
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    Dialog dialog = builder.create();
                    dialog.show();

                    RequestParams params = new RequestParams();
                    params.put("name", edit_name.getText().toString());
                    if ( radio_man.isChecked()){
                        params.put("sex", "男");
                    }else{
                        params.put("sex", "女");
                    }
                    params.put("birthday", time.getText().toString());
                    params.put("phone", edit_phone.getText().toString());
                    sendInfo(params);
                }else{
                    Toast.makeText(MessageInputActivity.this, "请将信息填写完整", Toast.LENGTH_LONG).show();
                }

            }
        });


        //点击选择时间的监听
        time.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MessageInputActivity.this);
                    View view = View.inflate(MessageInputActivity.this, R.layout.layout_datepicker, null);
                    builder.setView(view);
                    final DatePicker datePicker = (DatePicker) view.findViewById(R.id.datepicker);
                    builder.setPositiveButton("确 定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            int year = datePicker.getYear();
                            int month = datePicker.getMonth() + 1;
                            int day = datePicker.getDayOfMonth();
                            String date = year + "-" + month + "-" + day;
                            time.setText(date);
                            dialog.dismiss();
                        }
                    });
                    builder.setNegativeButton("取 消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    Dialog dialog = builder.create();
                    dialog.show();
                    time.clearFocus();
                }
            }
        });
    }


    //提交个人信息
    public void sendInfo(RequestParams params){
        HttpClient.post(this, HttpUrl.POST_USERINFO, params, new BaseJsonHttpResponseHandler(this){
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Map<String, Object> map = JacksonMapper.parse(responseString);
                String id = (String) map.get("customer_id");
                userInfo.edit().id().put(id).apply();

                Toast.makeText(MessageInputActivity.this, "提交信息成功", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            }
        });
    }
}
