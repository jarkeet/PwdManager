package com.cmcc.jeff.pwdmanager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import com.cmcc.jeff.pwdmanager.event.MessageAddEvent;
import com.cmcc.jeff.pwdmanager.utils.KeyboardUtils;

import org.greenrobot.eventbus.EventBus;

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

    @OnClick(R.id.btn_add_userinfo)
    public void addUserInfo(Button button) {
        procAddUserInfo();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_addinfo);
        ButterKnife.bind(this);

        KeyboardUtils.showSoftInput(this, tagEdt);

    }

    private void procAddUserInfo() {
        tagStr = tagEdt.getText().toString();
        usernameStr = usernameEdt.getText().toString();
        passwordStr = passwordEdt.getText().toString();
        if(TextUtils.isEmpty(tagStr) || TextUtils.isEmpty(usernameStr) || TextUtils.isEmpty(passwordStr)) {
            KeyboardUtils.hideSoftInput(this);
//            Toast.makeText(this, "请输入完整", Toast.LENGTH_SHORT).show();
            Snackbar.make(this.getWindow().getDecorView(), "请完成所有输入！", Snackbar.LENGTH_SHORT)
                    .show();

            return;
        }

        UserInfo userInfo = new UserInfo(tagStr ,usernameStr, passwordStr);
        UserManager.saveUserInfo(this, userInfo);
        EventBus.getDefault().post(new MessageAddEvent( "MessageAddEvent", userInfo.getTag()));
        finish();
    }


}
