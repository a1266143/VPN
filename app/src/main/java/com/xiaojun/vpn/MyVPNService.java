package com.xiaojun.vpn;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.VpnService;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.charset.Charset;

/**
 * VPNService实例
 * Crated by xiaojun on 2019/7/25 17:14
 * Your app connects the system networking for a user (or a work profile) to a VPN gateway. Each user (or work profile) can run a different VPN app.
 * You create a VPN service that the system uses to start and stop your VPN, and track the connection status. Your VPN service inherits from VpnService.
 * The service also acts as your container for the VPN gateway connections and their local device interfaces.
 * Your service instance call VpnService.Builder methods to establish a new local interface.
 */
public class MyVPNService extends VpnService {

    String[] appPackages = {"com.xiaojun.nio"};

    private void log(String content){
        Log.e("xiaojun",content);
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
        try {
            final DatagramChannel channel = DatagramChannel.open();
            channel.configureBlocking(false);
            boolean isProtected = protect(channel.socket());
            if (!isProtected){
                log("channel can not protect");
            }else{
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            startVPN(channel);
                        }catch (IOException e){
                            log("IOException:"+e.getLocalizedMessage());
                        }
                    }
                }).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
            log("catch exception:"+e.getLocalizedMessage());
        }

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

    private void startVPN(DatagramChannel channel) throws IOException {
        log("start connect 192.168.0.116......");
        channel.connect(new InetSocketAddress("192.168.0.116",8080));
        ByteBuffer packet = ByteBuffer.allocate(Short.MAX_VALUE);
        channel.write(packet);
//        channel.send(packet,new InetSocketAddress("192.168.0.116",8080));
        //当上面的代码连接成功之后就可以先进行握手，验证等操作
        //....这里忽略
        VpnService.Builder builder = new VpnService.Builder();
        //添加哪些APP使用VPN
        PackageManager packageManager = getPackageManager();
        for (String appPackage:appPackages){
            try {
                packageManager.getPackageInfo(appPackage,0);
                builder.addAllowedApplication(appPackage);
                log("add appPackage:"+appPackage);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        ParcelFileDescriptor localTunnel = builder
                .addAddress("192.168.0.116",24)
                .addRoute("0.0.0.0",0)
                .addDnsServer("192.168.2.1")
                .setMtu(Short.MAX_VALUE)//设置虚拟网络端口的最大传输单元
                .setSession("MyVPNService")
                .establish();//建立VPN通道
        updateForegroundNotification("VPN已连接");
        FileInputStream is = new FileInputStream(localTunnel.getFileDescriptor());
        FileOutputStream os = new FileOutputStream(localTunnel.getFileDescriptor());
        ByteBuffer buffer = ByteBuffer.allocate(Short.MAX_VALUE);
        while(true){
            try{
                buffer.clear();//会循环，所以这里清除一下标记
//            InetSocketAddress socketAddress = (InetSocketAddress) channel.receive(buffer);//读取channel中的数据到buffer
                int length = is.read(buffer.array()); //从输入流读取输出包（这里我的理解是，输入流实际上是系统里的APP发送出去的数据，经由这个输入流，所以叫输入流的输出包）
                if (length>0){
                    buffer.limit(length);
                    //查看读取的数据
//                buffer.flip();//转换成读模式
                    byte[] bytes = new byte[length];
                    for (int i = 0; i < bytes.length; i++) {
                        bytes[i] = buffer.get();
                    }
                    log("读取到的数据:"+new String(bytes, Charset.forName("UTF-8")));
                }
                packet.clear();
                length = channel.read(packet);//读取从服务器那边返回的数据
                if (length>0){
                    os.write(packet.array(),0,length);//将返回的数据写给特定的app
                }
            }catch (Exception e){
                log("getException:"+e.getLocalizedMessage());
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
