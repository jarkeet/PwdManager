package com.cmcc.jeff.pwdmanager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.cmcc.jeff.pwdmanager.event.MessageEvent;
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
    }

    private void procAddUserInfo() {
        tagStr = tagEdt.getText().toString();
        usernameStr = usernameEdt.getText().toString();
        passwordStr = passwordEdt.getText().toString();
        if(TextUtils.isEmpty(tagStr) || TextUtils.isEmpty(usernameStr) || TextUtils.isEmpty(passwordStr)) {
            Toast.makeText(this, "请输入完整", Toast.LENGTH_SHORT).show();
            return;
        }

        UserInfo userInfo = new UserInfo(tagStr ,usernameStr, passwordStr);
        UserManager.saveUserInfo(this, userInfo);
        EventBus.getDefault().post(new MessageEvent( "MessageEvent", userInfo.getTag()));
        finish();
    }


}
