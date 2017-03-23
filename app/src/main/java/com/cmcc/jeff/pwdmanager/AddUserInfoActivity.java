package com.cmcc.jeff.pwdmanager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cmcc.jeff.pwdmanager.utils.SpUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jeff on 2017/3/22.
 */

public class AddUserInfoActivity extends AppCompatActivity {


    private String tagStr;
    private String usernameStr;
    private String passwordStr;

    @BindView(R.id.edt_tag) EditText tagEdt;
    @BindView(R.id.edt_username) EditText usernameEdt;
    @BindView(R.id.edt_password) EditText passwordEdt;
//    @BindView(R.id.btn_add) Button addBtn;

    @OnClick(R.id.btn_add)
    public void btn_add(Button button) {
        Log.i("dd", "ddd");
        procAddUserInfo();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_addinfo);
        ButterKnife.bind(this);
    }

    private void procAddUserInfo() {
        tagStr = tagEdt.getText().toString();
        usernameStr = usernameEdt.getText().toString();
        passwordStr = passwordEdt.getText().toString();
        if(TextUtils.isEmpty(tagStr) || TextUtils.isEmpty(usernameStr) || TextUtils.isEmpty(passwordStr)) {
            Snackbar.make(usernameEdt, "输入不能为空", Snackbar.LENGTH_SHORT).show();
            return;
        }

        UserInfo userInfo = new UserInfo(tagStr ,usernameStr, passwordStr);
        SpUtil.saveUserInfo(this, userInfo);
        Toast.makeText(this, "Save UserInfo success.", Toast.LENGTH_SHORT).show();
        finish();
    }


}
