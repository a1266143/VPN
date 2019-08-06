package com.xiaojun.vpn.vpn_instance.vpn_config;

import android.net.VpnService;
import android.text.TextUtils;

/**
 * V2Ray配置类
 * Crated by xiaojun on 2019/8/1 9:51
 */
public class ConfigV2ray {

    public VpnService.Builder config(String protocoInfo,VpnService.Builder builder){
        String[] splits = protocoInfo.split(" ");
        if (splits.length > 0) {
            for (String param : splits) {
                String[] it = param.split(",");
                String first = it[0];
                if (TextUtils.equals("m", first)) {
                    //虚拟网络最大传输单元，如果发送的包长度超过这个数字，则会被分包
                    builder.setMtu(Integer.parseInt(it[1]));
                } else if (TextUtils.equals("a", first)) {
                    //虚拟网络端口的IP地址
                    builder.addAddress(it[1], Integer.parseInt(it[2]));
                } else if (TextUtils.equals("r", first)) {
                    //只有匹配上的IP包，才会被路由到虚拟端口上去，如果是0.0.0.0/0的话，则将所有的IP包都路由到虚拟端口上
                    builder.addRoute(it[1], Integer.parseInt(it[2]));
                } else if (TextUtils.equals("s", first)) {
                    //就是添加DNS域名的自动补齐。DNS服务器必须通过全域名进行搜索，
                    // 但每次查找都输入全域名太麻烦了，可以通过配置域名的自动补齐
                    // 规则予以简化；
                    builder.addSearchDomain(it[1]);
                }
            }
        }
        builder.setSession("VPNV2ray");
        //TODO 添加DNS服务器地址
        //------------------------------
        return builder;
    }

}
