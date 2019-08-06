package com.xiaojun.vpn.util;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

/**
 * 对话框工具
 * Crated by xiaojun on 2019/7/25 17:20
 */
public class DialogUtils {

    //显示一个tradition警告对话框
    public static AlertDialog showAlertDialog(Context context, String title, String message, AlertDialogButton positiveBtn,AlertDialogButton negativeBtn,boolean show){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (!TextUtils.isEmpty(title))builder.setTitle(title);
        if (!TextUtils.isEmpty(message))builder.setMessage(message);
        if (positiveBtn!=null)builder.setPositiveButton(positiveBtn.text,positiveBtn.listener);
        if (negativeBtn!=null)builder.setNegativeButton(negativeBtn.text,negativeBtn.listener);
        AlertDialog dialog = builder.create();
        if (show)
            dialog.show();
        return dialog;
    }

    /**
     * 对话框按钮类
     */
    public static class AlertDialogButton{
        private String text;
        private DialogInterface.OnClickListener listener;
        public AlertDialogButton(String text, DialogInterface.OnClickListener listener){
            this.text = text;
            this.listener = listener;
        }
    }

}
