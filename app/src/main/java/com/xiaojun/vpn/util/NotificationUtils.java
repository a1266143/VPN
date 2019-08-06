package com.xiaojun.vpn.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.support.annotation.DrawableRes;

/**
 * 通知工具
 * Crated by xiaojun on 2019/8/1 10:03
 */
public class NotificationUtils {

    //通知渠道，这是前台Service渠道
    public static String CHANNEL_FORGROUND = "1";

    public static void startForegroundService(Service service, NotificationChannel channel, String content, @DrawableRes int icon, PendingIntent pendingIntent){
        Notification notification = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationManager manager = (NotificationManager) service.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);
            notification = new Notification.Builder(service, channel.getId())
                    .setContentTitle("VpnV2ray")
                    .setContentText(content)
                    .setSmallIcon(icon)
                    .setContentIntent(pendingIntent)
                    .setTicker("VpnV2ray")
                    .build();
        }else{
            notification = new Notification(icon,"VpnV2ray",System.currentTimeMillis());
            notification.contentIntent = pendingIntent;
            notification.icon = icon;
        }
        service.startForeground(-1, notification);
    }

}
