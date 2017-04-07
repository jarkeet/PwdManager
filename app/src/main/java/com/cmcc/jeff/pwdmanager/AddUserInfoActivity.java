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

    @BindView(R.id.edt_tag)
    EditText tagEdt;
    @BindView(R.id.edt_username)
    EditText usernameEdt;
    @BindView(R.id.edt_password)
    EditText passwordEdt;
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
        passwordEdt.setText("jiasndfnasjdifjasodnfaksdfsdfMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAs8gdZJFYUWXk2W9zuVT2FoM1K/erUdWb\n" +
                "06pUdNrKcT7cwXl/2o+zXxd7n2oo5oaDlVuNdVzXAlQFKlh1mMaO0zoiRTz3VlVqHgIH1ya5Zfub\n" +
                "y+cb505geeNELImxzcrm1JaWChkFqi+Kc0CtcoU1V4g+1h8P7EZz3NWP4tOc5dN9Tb3ZA8txpAAL\n" +
                "W2JHRU8+lzyTb/ayTABMCZkyk1thLgmngYNxMRmAEU41OlLS6R+Tee/lgPmwzUlO2PpDIBR+1JYS\n" +
                "2NNkRTSTo9sApcLj31MCFymsyEd4/HSgMaWbtpp4EhAk6h15kGC/XB42DYkVRiFSPdFj4EUDsdGd\n" +
                "EMdcJQIDAQABMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAs8gdZJFYUWXk2W9zuVT2FoM1K/erUdWb\n" +
                "06pUdNrKcT7cwXl/2o+zXxd7n2oo5oaDlVuNdVzXAlQFKlh1mMaO0zoiRTz3VlVqHgIH1ya5Zfub\n" +
                "y+cb505geeNELImxzcrm1JaWChkFqi+Kc0CtcoU1V4g+1h8P7EZz3NWP4tOc5dN9Tb3ZA8txpAAL\n" +
                "W2JHRU8+lzyTb/ayTABMCZkyk1thLgmngYNxMRmAEU41OlLS6R+Tee/lgPmwzUlO2PpDIBR+1JYS\n" +
                "2NNkRTSTo9sApcLj31MCFymsyEd4/HSgMaWbtpp4EhAk6h15kGC/XB42DYkVRiFSPdFj4EUDsdGd\n" +
                "EMdcJQIDAQABMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAs8gdZJFYUWXk2W9zuVT2FoM1K/erUdWb\n" +
                "06pUdNrKcT7cwXl/2o+zXxd7n2oo5oaDlVuNdVzXAlQFKlh1mMaO0zoiRTz3VlVqHgIH1ya5Zfub\n" +
                "y+cb505geeNELImxzcrm1JaWChkFqi+Kc0CtcoU1V4g+1h8P7EZz3NWP4tOc5dN9Tb3ZA8txpAAL\n" +
                "W2JHRU8+lzyTb/ayTABMCZkyk1thLgmngYNxMRmAEU41OlLS6R+Tee/lgPmwzUlO2PpDIBR+1JYS\n" +
                "2NNkRTSTo9sApcLj31MCFymsyEd4/HSgMaWbtpp4EhAk6h15kGC/XB42DYkVRiFSPdFj4EUDsdGd\n" +
                "EMdcJQIDAQAB");

    }

    private void procAddUserInfo() {
        tagStr = tagEdt.getText().toString();
        usernameStr = usernameEdt.getText().toString();
        passwordStr = passwordEdt.getText().toString();
        if (TextUtils.isEmpty(tagStr) || TextUtils.isEmpty(usernameStr) || TextUtils.isEmpty(passwordStr)) {
            KeyboardUtils.hideSoftInput(this);
//            Toast.makeText(this, "请输入完整", Toast.LENGTH_SHORT).show();
            Snackbar.make(this.getWindow().getDecorView(), "请完成所有输入！", Snackbar.LENGTH_SHORT)
                    .show();

            return;
        }

        UserInfo userInfo = new UserInfo(tagStr, usernameStr, passwordStr);
        try {
            UserManager.saveUserInfo(this, userInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        EventBus.getDefault().post(new MessageAddEvent("MessageAddEvent", userInfo.getTag()));
        finish();
    }


}
