package com.xiaojun.vpn.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import java.util.List;

import static android.content.pm.PackageManager.MATCH_UNINSTALLED_PACKAGES;

/**
 * Crated by xiaojun on 2019/7/29 14:16
 */
public class PmManagerUtils {

    public static void logPackages(Context context){
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> list = pm.getInstalledPackages(MATCH_UNINSTALLED_PACKAGES);
        for (int i = 0; i < list.size(); i++) {
            Log.e("xiaojun","APP:"+list.get(i).packageName);
        }
    }
}
