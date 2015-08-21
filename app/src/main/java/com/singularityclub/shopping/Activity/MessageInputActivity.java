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

import com.singularityclub.shopping.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/*信息录入页面*/
@EActivity(R.layout.activity_message_input)
public class MessageInputActivity extends Activity {

    @ViewById
    protected ImageView complete;
    @ViewById
    protected EditText edit_name, edit_phone, time;
    @ViewById
    protected RadioButton radio_man, radio_women;
//    @ViewById
//    protected DatePicker datePicker;


    @AfterViews
    public void init(){
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
                }else{
                    Toast.makeText(MessageInputActivity.this, "请将信息填写完整", Toast.LENGTH_LONG).show();
                }

            }
        });
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MessageInputActivity.this);
                View view =  View.inflate(MessageInputActivity.this, R.layout.layout_datepicker, null);
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
            }
        });
    }
}
