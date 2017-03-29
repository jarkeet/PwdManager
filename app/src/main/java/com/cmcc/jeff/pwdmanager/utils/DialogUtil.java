package com.cmcc.jeff.pwdmanager.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * Created by jeff on 2017/3/29.
 */

public class DialogUtil {

    public static void showOKDialog(Context context, String title, String content, String okBtnMsg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title).setMessage(content).setPositiveButton(okBtnMsg, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
}
