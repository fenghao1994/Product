package com.singularityclub.shopping.Activity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.singularityclub.shopping.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/*信息录入页面*/
@EActivity(R.layout.activity_message_input)
public class MessageInputActivity extends Activity {

    @ViewById
    protected TextView complete;
    @ViewById
    protected EditText edit_name, edit_phone;
    @ViewById
    protected RadioButton radio_man, radio_women;
    @ViewById
    protected DatePicker datePicker;


    @AfterViews
    public void init(){
        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MessageInputActivity.this, ShowProductionActivity_.class);
                startActivity(intent);
            }
        });
    }
}
