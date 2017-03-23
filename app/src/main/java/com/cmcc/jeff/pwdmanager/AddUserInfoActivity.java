package com.cmcc.jeff.pwdmanager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

/**
 * Created by jeff on 2017/3/22.
 */

public class AddUserInfoActivity extends AppCompatActivity {


    private EditText usernameEdt;
    private EditText passwordEdt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_addinfo);
        initView();
    }

    private void initView() {
        usernameEdt = (EditText) findViewById(R.id.tv_username);
        passwordEdt = (EditText) findViewById(R.id.tv_password);
    }
}
