package com.xiaojun.vpn.vpn_instance;

/**
 * Crated by xiaojun on 2019/8/1 9:45
 */
public class VpnMine {

    private String localIP = "192.168.0.116";
    private String tunLocalIP = "10.0.2.0";
    private String tunDNS = "114.114.114.114";
    private int localPort = 808;

    private void startVPN() {
        /*LogUtils.d(getClass(),"start connect " + localIP + ",Port=" + localPort);
        ParcelFileDescriptor localTunnel = null;
        try {
            final DatagramChannel channel = DatagramChannel.open();
            channel.configureBlocking(false);
            boolean isProtected = protect(channel.socket());
            if (!isProtected) {
                LogUtils.d(getClass(),"Protected failed");
                return;
            }
            channel.connect(new InetSocketAddress(localIP, localPort));
            ByteBuffer packet = ByteBuffer.allocate(Short.MAX_VALUE);
            channel.write(packet);
            //当上面的代码连接成功之后就可以先进行握手，验证等操作
            //TODO ....这里忽略
            VpnService.Builder builder = new VpnService.Builder();
            //添加哪些APP使用VPN
            PackageManager packageManager = getPackageManager();
            for (String appPackage : appPackages) {
                try {
                    PackageInfo info = packageManager.getPackageInfo(appPackage, 0);
                    builder.addAllowedApplication(appPackage);
                    LogUtils.d(getClass(),"已添加监控流量的App:" + info.packageName);
//                    builder.addDisallowedApplication(appPackage);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
            localTunnel = builder
                    .addAddress(tunLocalIP, 24)//建立此虚拟网卡的地址
//                    .addAddress("fd00:1:fd00:1:fd00:1:fd00:1", 128)
                    .addRoute("0.0.0.0", 0)
//                    .addDnsServer(tunDNS)
                    .setMtu(Short.MAX_VALUE)//设置虚拟网络端口的最大传输单元
                    .setSession("MyVPNService")
                    .establish();//建立VPN通道
            log("VPN已连接,虚拟网卡IP:" + tunLocalIP + ",DNS服务器:" + tunDNS);
//            updateForegroundNotification("VPN已连接");
            FileInputStream is = new FileInputStream(localTunnel.getFileDescriptor());
            FileOutputStream os = new FileOutputStream(localTunnel.getFileDescriptor());
            ByteBuffer buffer = ByteBuffer.allocate(Short.MAX_VALUE);
            while (true) {
                buffer.clear();//会循环，所以这里清除一下标记
//            InetSocketAddress socketAddress = (InetSocketAddress) channel.receive(buffer);//读取channel中的数据到buffer
                int length = is.read(buffer.array()); //从输入流读取输出包（这里我的理解是，输入流实际上是系统里的APP发送出去的数据，经由这个输入流，所以叫输入流的输出包）
                if (length > 0) {
                    buffer.limit(length);
                    //查看读取的数据
//                    buffer.flip();//转换成读模式
                    byte[] bytes = new byte[length];
                    for (int i = 0; i < bytes.length; i++) {
                        bytes[i] = buffer.get();
                    }
                    LogUtils.d(getClass(),"拦截到" + length + "byte数据:\n" + new IPpacketParser(bytes));
                    LogUtils.d(getClass(),"开始向" + localIP + ":" + localPort + "发送...");
                    buffer.rewind();//重新设置position的位置位0
                    int num = channel.write(buffer);//写入到服务器上
                    LogUtils.d(getClass(),"发送了" + num + "byte");
                }
                packet.clear();
                length = channel.read(packet);//读取从服务器那边返回的数据
                if (length > 0) {
                    LogUtils.d(getClass(),"从" + localIP + ":" + localPort + "读取了" + length + "byte");
                    LogUtils.d(getClass(),"开始向虚拟网卡中写入数据...");
                    os.write(packet.array(), 0, length);//将返回的数据写给特定的app
                }

            }
        } catch (IOException e) {
            LogUtils.d(getClass(),"接收到异常:" + e.getClass().getCanonicalName() + "\n" +
                    "异常信息:" + e.getMessage() +
                    "\n----------------------开始重新初始化虚拟网卡------------------------");
        } finally {
            if (localTunnel != null) {
                try {
                    localTunnel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    LogUtils.d(getClass(),"ParcelFileDescriptor close error");
                }
            }
        }*/
    }
}
