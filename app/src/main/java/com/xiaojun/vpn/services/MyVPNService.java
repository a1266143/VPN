package com.xiaojun.vpn.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.VpnService;

import com.xiaojun.vpn.JsonConfig;
import com.xiaojun.vpn.MainActivity;
import com.xiaojun.vpn.R;
import com.xiaojun.vpn.util.LogUtils;
import com.xiaojun.vpn.util.NotificationUtils;
import com.xiaojun.vpn.vpn_instance.VPNV2ray;
import com.xiaojun.vpn.vpn_instance.vpn_config.ConfigV2ray;

/**
 * VPNService实例
 * Crated by xiaojun on 2019/7/25 17:14
 * Your app connects the system networking for a user (or a work profile) to a VPNV2ray gateway. Each user (or work profile) can run a different VPNV2ray app.
 * You create a VPNV2ray service that the system uses to start and stop your VPNV2ray, and track the connection status. Your VPNV2ray service inherits from VpnService.
 * The service also acts as your container for the VPNV2ray gateway connections and their local device interfaces.
 * Your service instance call VpnService.Builder methods to establish a new local interface.
 */
public class MyVPNService extends VpnService implements VPNV2ray.VpnStateCallback {

//    String[] appPackages = {"com.xiaojun.nio", "com.example.myapplication"/*, "com.android.browser"*/};
    String[] appPackages = {};

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startVPN();
        return START_STICKY;
    }



    private void startVPN() {
        VPNV2ray vpn = new VPNV2ray(this);
        vpn.addAllowApps(appPackages);
        vpn.startVPN(JsonConfig.huangsuoGoogle, this);
    }

    //-----------------------------建立VPN回调------------------------------
    @Override
    public Builder establish(String protocoInfo) {
        VpnService.Builder builder = new VpnService.Builder();
        ConfigV2ray configV2ray = new ConfigV2ray();
        return configV2ray.config(protocoInfo,builder);
    }

    @Override
    public void success() {
        LogUtils.d(MyVPNService.this.getClass(), "VPN建立成功");
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0, new Intent(this, MainActivity.class),0);
        NotificationChannel channel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                channel = new NotificationChannel(NotificationUtils.CHANNEL_FORGROUND,"Forground",NotificationManager.IMPORTANCE_HIGH);
            }
        }
        NotificationUtils.startForegroundService(this,channel,"连接成功",R.mipmap.vpn,pendingIntent);
    }

    @Override
    public void error(String info) {
        LogUtils.d(MyVPNService.this.getClass(), "VPN建立失败:" + info);
    }

}
