package com.xiaojun.vpn.protocol;

import com.xiaojun.vpn.util.ByteUtils;

/**
 * TCP协议解析类
 * Crated by xiaojun on 2019/8/6 15:21
 */
public class TcpParser {

    private byte[] bytes;

    public TcpParser(byte[] bytes){
        this.bytes = bytes;
    }

    /**
     * 获取源端口
     * @return
     */
    public short getSourcePort(){
        byte[] byte01 = new byte[2];
        System.arraycopy(bytes,0,byte01,0,2);
        return (short) (ByteUtils.getInt(byte01,0)&0xff);
    }

    /**
     * 获取目的地端口
     * @return
     */
    public short getDesPort(){
        byte[] byte23 = new byte[2];
        System.arraycopy(byte23,0,byte23,0,2);
        return (short) (ByteUtils.getInt(byte23,0)&0xff);
    }

}
