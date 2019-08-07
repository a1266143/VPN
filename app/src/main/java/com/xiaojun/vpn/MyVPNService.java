package com.xiaojun.vpn;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.VpnService;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import com.xiaojun.vpn.protocol.IPpacketParser;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

/**
 * VPNService实例
 * Crated by xiaojun on 2019/7/25 17:14
 * Your app connects the system networking for a user (or a work profile) to a VPN gateway. Each user (or work profile) can run a different VPN app.
 * You create a VPN service that the system uses to start and stop your VPN, and track the connection status. Your VPN service inherits from VpnService.
 * The service also acts as your container for the VPN gateway connections and their local device interfaces.
 * Your service instance call VpnService.Builder methods to establish a new local interface.
 */
public class MyVPNService extends VpnService {

    String[] appPackages = {"com.xiaojun.nio", "com.example.myapplication"/*, "com.android.browser"*/};
    String localIP = "192.168.0.116";
    String tunLocalIP = "10.0.2.0";
    String tunDNS = "114.114.114.114";
    int localPort = 808;

    private void log(String content) {
        Log.e("xiaojun", content);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //become a forground service
        updateForegroundNotification("正在连接");
        //1.Call VpnService.protect() to keep your app's tunnel socket outside of the system VPN and avoid a circular connection.
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    startVPN();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();

        //4--------------------------------
        // Configure a new interface from our VpnService instance. This must be done
        // from inside a VpnService.
        /*VpnService.Builder builder = new VpnService.Builder();
        // Create a local TUN interface using predetermined addresses. In your app,
        // you typically use values returned from the VPN gateway during handshaking.
        ParcelFileDescriptor localTunnel = builder
                .addAddress("192.168.0.116", 24)
                .addRoute("0.0.0.0", 0)
                .addDnsServer("192.168.0.1")
                .establish();//建立
        if (localTunnel == null) {
            Toast.makeText(MyVPNService.this, "没有获得VPN权限", Toast.LENGTH_SHORT).show();
        } else {
            FileInputStream is = new FileInputStream(localTunnel.getFileDescriptor());
            FileOutputStream os = new FileOutputStream(localTunnel.getFileDescriptor());
            ByteBuffer packet = ByteBuffer.allocate(Short.MAX_VALUE);

//            is.read
        }*/
        return START_STICKY;
    }

    private void startVPN() {
        log("start connect " + localIP + ",Port=" + localPort);
        ParcelFileDescriptor localTunnel = null;
        try {
            final DatagramChannel channel = DatagramChannel.open();
            channel.configureBlocking(false);
            boolean isProtected = protect(channel.socket());
            if (!isProtected){
                log("Protected failed");
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
                    log("已添加监控流量的App:" + info.packageName);
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
//                    .setMtu(Short.MAX_VALUE)//设置虚拟网络端口的最大传输单元
                    .setSession("MyVPNService")
                    .establish();//建立VPN通道
            log("VPN已连接,虚拟网卡IP:" + tunLocalIP + ",DNS服务器:" + tunDNS);
            updateForegroundNotification("VPN已连接");
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
                    log("拦截到" + length + "byte数据:\n" + new IPpacketParser(bytes));
                    log("开始向" + localIP + ":" + localPort + "发送...");
                    buffer.rewind();//重新设置position的位置位0
                    int num = channel.write(buffer);//写入到服务器上
                    log("发送了" + num + "byte");
                }
                packet.clear();
                length = channel.read(packet);//读取从服务器那边返回的数据
                if (length > 0) {
                    log("从" + localIP + ":" + localPort + "读取了" + length + "byte");
                    log("开始向虚拟网卡中写入数据...");
                    os.write(packet.array(), 0, length);//将返回的数据写给特定的app
                }

            }
        } catch (IOException e) {
            log("接收到异常:" + e.getClass().getCanonicalName() + "\n" +
                    "异常信息:" + e.getMessage() +
                    "\n----------------------开始重新初始化虚拟网卡------------------------");
        } finally {
            if (localTunnel != null) {
                try {
                    localTunnel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    log("ParcelFileDescriptor close error");
                }
            }
        }

    }

    private void updateForegroundNotification(final String message) {
        final String NOTIFICATION_CHANNEL_ID = "MyVpn";
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(new NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_ID, NotificationManager.IMPORTANCE_DEFAULT));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForeground(1, new Notification.Builder(this, NOTIFICATION_CHANNEL_ID)
                    .setSmallIcon(R.mipmap.vpn)
                    .setContentText(message)
                    .setContentIntent(null)
                    .build());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
