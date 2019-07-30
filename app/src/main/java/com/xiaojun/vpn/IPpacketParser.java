package com.xiaojun.vpn;

/**
 * IP包头解析类
 * Crated by xiaojun on 2019/7/29 15:41
 */
public class IPpacketParser {

    private byte[] bytes;
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
        Unknow
    }

    public enum Protocol{
        ICMP,TCP,UDP,Unknow
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
        //如果第一个字节的高4位等于0x00000100就是IPv4
        if(((bytes[0]>>4)&0x4)==0x4){
            return Version.IPv4;
        }else if(((bytes[0]>>4)&0x6)==0x6){
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
     * 获取包的优先级
     * @return TypeServicePPP
     */
    public TypeServicePPP getPPP(){
        byte tmpByte = (byte) (bytes[1]>>5);
        /*if ((tmpByte|0x00000000)==0x00000000){
            return TypeServicePPP.Routine;
        }else */if ((tmpByte&0x00000001)==0x00000001){
            return TypeServicePPP.Priority;
        }else if((tmpByte&0x00000010)==0x00000010){
            return TypeServicePPP.Immediate;
        }else if ((tmpByte&0x00000011)==0x00000011){
            return TypeServicePPP.Flash;
        }else if ((tmpByte&0x00000100)==0x00000100){
            return TypeServicePPP.FlashOverride;
        }else if ((tmpByte&0x00000101)==0x00000101){
            return TypeServicePPP.CRI_TIC_ECP;
        }else if((tmpByte&0x00000110)==0x00000110){
            return TypeServicePPP.InternetworkControl;
        }else if ((tmpByte&0x00000111)==0x00000111){
            return TypeServicePPP.NetworkContorl;
        }else{
            return TypeServicePPP.Routine;
        }
    }

    /**
     * 获取IP包总长
     * @return
     */
    public int getTotalLength(){
        return bytes[2]+bytes[3];
    }

    /**
     * 获取IP包协议
     * @return
     */
    public Protocol getProtocol(){
        if ((bytes[9]&0x6)==0x6){
            return Protocol.TCP;
        }else if ((bytes[9]&0x11)==0x11){
            return Protocol.UDP;
        }else{
            return Protocol.Unknow;
        }
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
