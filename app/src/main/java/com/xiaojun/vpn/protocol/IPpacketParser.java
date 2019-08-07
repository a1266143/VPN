package com.xiaojun.vpn.protocol;

import android.util.Log;

/**
 * IP包头解析类
 * Crated by xiaojun on 2019/7/29 15:41
 */
public class IPpacketParser {

    private byte[] bytes;
    private TcpParser mTcpParser;

    public enum Version{
        IPv4,IPv6,unknow
    }

    //服务类型(PPP:定义包的优先级，取值越大数据越重要)
    public enum TypeServicePPP{
        Routine,//普通的
        Priority,//优先的
        Immediate,//立即发送
        Flash,//闪电式
        FlashOverride,//比闪电还快
        CRI_TIC_ECP,
        InternetworkControl,//网间控制
        NetworkContorl,//网络控制
    }

    public enum Protocol{
        ICMP,TCP,UDP,IGMP,UNKNOW
    }

    public static class InValidBytesException extends RuntimeException {
        public InValidBytesException(){
            super("not valid bytes");
        }
    }

    public IPpacketParser(byte[] bytes){
        this.bytes = bytes;
    }

    /**
     * 判断byte数组是否有效
     * @return
     */
    private void isByteValid(){
        if (bytes == null || bytes.length == 0)
            throw new InValidBytesException();
    }

    /**
     * 获取Version IP包头版本号
     * 0x0100为IPv4
     * 0x0110为IPv6
     * @return 返回IPv4或者IPv6
     */
    public Version getVersion(){
        isByteValid();
        //如果第一个字节的高4位等于0100就是IPv4
        if(((bytes[0]>>4)&0x04)==0x04){
            return Version.IPv4;
        }else if(((bytes[0]>>4)&0x06)==0x06){
            return Version.IPv6;
        }else{
            return Version.unknow;
        }
    }

    /**
     * 返回IP包头长度，单位：字节
     * @return 返回包头所占字节数
     */
    public int getHeaderLength(){
        return ((byte)(((byte)(bytes[0]<<4))>>4))*4;
    }

    /**
     * 获取包的优先级(服务类型)
     * @return TypeServicePPP
     */
    public TypeServicePPP getPPP(){
        byte tmpByte = (byte) (bytes[1]>>5);
        /*if ((tmpByte|0x00000000)==0x00000000){
            return TypeServicePPP.Routine;
        }else */if ((tmpByte&0x01)==0x01){
            return TypeServicePPP.Priority;
        }else if((tmpByte&0x02)==0x02){
            return TypeServicePPP.Immediate;
        }else if ((tmpByte&0x03)==0x03){
            return TypeServicePPP.Flash;
        }else if ((tmpByte&0x04)==0x04){
            return TypeServicePPP.FlashOverride;
        }else if ((tmpByte&0x05)==0x05){
            return TypeServicePPP.CRI_TIC_ECP;
        }else if((tmpByte&0x06)==0x06){
            return TypeServicePPP.InternetworkControl;
        }else if ((tmpByte&0x07)==0x07){
            return TypeServicePPP.NetworkContorl;
        }else{
            return TypeServicePPP.Routine;
        }
    }

    /**
     * 获取IP包总长度
     * @return
     */
    public int getTotalLength(){
        int totalLength = bytes[2]+bytes[3];
        int headerLength = getHeaderLength();
        byte[] byteRemain = new byte[(totalLength-headerLength)];
        Log.e("xiaojun","headerLength="+headerLength+",byteRemain.length="+byteRemain.length+",bytes.length="+bytes.length);
        //去掉IP包头后的数据
        System.arraycopy(bytes,headerLength,byteRemain,0,totalLength-headerLength);
        mTcpParser = new TcpParser(byteRemain);
        Log.e("xiaojun","headerLength = "+headerLength+",获取到的端口="+mTcpParser.getSourcePort()+",目的端口="+mTcpParser.getDesPort());
        return totalLength;
    }

    /**
     * 获取标识符（唯一标识一个数据报，可以将之当成一个计数器，每发送一个数据报，该值加1，如果数据报
     * 分片，则每个分片的标识都一样，各个分片共享一个标识）
     * @return 返回标识符
     */
    public short getIdentifier(){
//        return bytes[4]*bytes[5];
        return 0;
    }

    /**
     * 获取生存时间（表示数据报最多可经过的路由器的数量，取值0~255，每经过一个路由器，TTL（生存时间）减1，
     * 为0时被丢弃，并发送ICMP报文通知源主机，TTL可以避免数据报在路由器之间不断循环
     * @return
     */
    public byte getLifetime(){
        return bytes[8];
    }

    /**
     * 获取IP包协议
     * @return
     */
    public Protocol getProtocol(){
        if ((bytes[9]&0x06)==0x06){
            return Protocol.TCP;
        }else if ((bytes[9]&0x11)==0x11){
            return Protocol.UDP;
        }else if((bytes[9]&0x1)==0x1){
            return Protocol.ICMP;
        }else if((bytes[9]&0x02)==0x02){
            return Protocol.IGMP;
        }
        return Protocol.UNKNOW;
    }

    /**
     * 获取IP包源地址
     * @return
     */
    public String getSourceAddress(){
        return (bytes[12]&0xff)+"."+(bytes[13]&0xff)+"."+(bytes[14]&0xff)+"."+(bytes[15]&0xff);
    }

    /**
     * 获取目的地地址
     * @return
     */
    public String getDestAddress(){
        return (bytes[16]&0xff)+"."+(bytes[17]&0xff)+"."+(bytes[18]&0xff)+"."+(bytes[19]&0xff);
    }

    @Override
    public String toString() {
        return "版本号:"+getVersion()+"\n"+
                "HeaderLength:"+getHeaderLength()+"\n"+
                "TypeOfServicePPP:"+getPPP()+"\n"+
                "TotalLength:"+getTotalLength()+"\n"+
                "Protocol:"+getProtocol()+"\n"+
                "SourceAddress:"+getSourceAddress()+"\n"+
                "DestAddress:"+getDestAddress();
    }
}
