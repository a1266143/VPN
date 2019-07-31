package com.xiaojun.vpn;

import android.util.Log;

/**
 * 日志工具类
 * <p>
 * Crated by xiaojun on 2019/7/31 17:02
 */
public class LogUtils {

    public static void d(Class t, String content) {
        if (currentMode == Mode.Debug)
            Log.d("xiaojun:"+t.getSimpleName(), content);
    }

    private static Mode currentMode = Mode.Debug;

    private enum Mode {
        Debug, Release
    }
}
