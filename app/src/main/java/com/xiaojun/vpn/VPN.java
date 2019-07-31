package com.xiaojun.vpn;

import android.content.pm.PackageManager;
import android.net.VpnService;
import android.os.ParcelFileDescriptor;

import libv2ray.Libv2ray;
import libv2ray.V2RayCallbacks;
import libv2ray.V2RayPoint;
import libv2ray.V2RayVPNServiceSupportsSet;

/**
 * VPN相关
 * Crated by xiaojun on 2019/7/31 16:53
 */
public class VPN implements V2RayCallbacks, V2RayVPNServiceSupportsSet {

    private V2RayPoint mV2RayPoint;
    private VpnService mVpnService;
    private VpnStateCallback mVpnStateCallback;
    private String[] mAllowedApps = new String[0];
    private int fd;//虚拟网卡 fd

    public VPN(VpnService service) {
        mV2RayPoint = Libv2ray.newV2RayPoint();
        mVpnService = service;
    }

    /**
     * 添加允许哪些APP经过VPN
     *
     * @param apps app包名数组
     */
    public void addAllowApps(String[] apps) {
        mAllowedApps = apps;
    }

    /**
     * 开启VPN
     *
     * @param configContent 配置文件内容
     */
    public void startVPN(String configContent, VpnStateCallback callback) {
        LogUtils.d(getClass(),"startVPN");
        mVpnStateCallback = callback;
        mV2RayPoint.setCallbacks(this);
        mV2RayPoint.setVpnSupportSet(this);
        mV2RayPoint.setConfigureFile("V2Ray_internal/ConfigureFileContent");
        mV2RayPoint.setConfigureFileContent(configContent);
        mV2RayPoint.runLoop();
    }

    //----------------------------V2RayCallbacks-------------------------
    @Override
    public long onEmitStatus(long l, String s) {
        LogUtils.d(getClass(), "onEmitStatus," + s);
        return 0;
    }

    //----------------------------V2RayVPNServiceSupportsSet--------------------------
    @Override
    public long getVPNFd() {
        LogUtils.d(getClass(), "getVPNFd");
        return fd;
    }

    @Override
    public long prepare() {
        LogUtils.d(getClass(), "prepare");
        mV2RayPoint.vpnSupportReady();
        return 1;
    }

    @Override
    public long protect(long l) {
        LogUtils.d(getClass(), "protect");
        boolean protect = mVpnService.protect((int) l);
        return protect ? 0 : 1;
    }

    @Override
    public long setup(String s) {//v2ray传递回来相关VPNService.Builder中的参数
        LogUtils.d(getClass(), "setup");
        try {
            VpnService.Builder builder = mVpnStateCallback.establish(s);
            //添加允许哪些APP走VPN通道
            PackageManager packageManager = mVpnService.getPackageManager();
            for (String app : mAllowedApps) {
                //添加哪些APP使用VPN
                try {
                    packageManager.getPackageInfo(app, 0);
                    builder.addAllowedApplication(app);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
            ParcelFileDescriptor parcelFileDescriptor = builder.establish();//建立VPN
            fd = parcelFileDescriptor.getFd();
            mVpnStateCallback.success();
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            mVpnStateCallback.error(e.getMessage());
        }
        return -1;
    }

    @Override
    public long shutdown() {
        LogUtils.d(getClass(), "shutdown");
        return 0;
    }

    //VPN状态回调
    public interface VpnStateCallback {
        VpnService.Builder establish(String protocoInfo);//可以建立Builder的回调

        void success();

        void error(String info);
    }
}
