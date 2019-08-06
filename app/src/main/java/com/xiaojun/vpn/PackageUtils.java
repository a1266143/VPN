package com.xiaojun.vpn;

import android.content.Context;
import android.content.pm.ApplicationInfo;

/**
 * Package工具
 * Crated by xiaojun on 2019/8/1 9:27
 */
public class PackageUtils {

    /**
     * 判断当前应用是否是debug状态
     */
    public static boolean isApkInDebug(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }
}
