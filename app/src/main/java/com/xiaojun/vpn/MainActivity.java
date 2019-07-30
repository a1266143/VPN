package com.xiaojun.vpn;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.VpnService;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private MyVPNService mVPNService;
    private EditText edtIP,edtPort;
    private final int REQUEST_CODE=11;
    //线程池
    private ExecutorService mExecutorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtIP = findViewById(R.id.edt_ip);
        edtPort = findViewById(R.id.edt_port);
        mExecutorService = Executors.newSingleThreadExecutor();
//        PmManagerUtils.logPackages(this);
    }

    public void openVPN(View view) {
        open();
    }

    private void open() {
        final Intent intent = VpnService.prepare(this);//this call is ask for permission
        if (intent!=null){//permission is not granted
            DialogUtils.showAlertDialog(this,null,
                    "还未获得VPN打开权限，需要打开VPN权限",new DialogUtils.AlertDialogButton("去打开",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivityForResult(intent,REQUEST_CODE);
                                }
                            }),
                    null,true);
        }
        //VPNService prepare成功
        else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    setVPN();
                }
            }).start();
        }
    }

    private void setVPN() {
        /*String host = edtIP.getText().toString();
        int port = Integer.parseInt(edtPort.getText().toString());
//        startService(new Intent(this,MyVPNService.class));
        //2.Call VpnService.protect() to keep your app's tunnel socket outside of the system VPN and avoid a circular connection.
        VpnService service = new MyVPNService();
        DatagramSocket socket = new DatagramSocket();
        boolean isSuccess = service.protect(socket);//保护本APP不要使用VPN通道
        if (isSuccess){
            //3------------------------------------连接到VPN通道
            DatagramSocket vpnSocket = new DatagramSocket(port);
            SocketAddress address = new InetSocketAddress(host,port);
            vpnSocket.connect(address);
            //startService to start VPN
            startService(new Intent(MainActivity.this,MyVPNService.class));
        }else{
            Toast.makeText(MainActivity.this,"保护本APP通道失败",Toast.LENGTH_SHORT).show();
        }*/
        startService(new Intent(this,MyVPNService.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_CODE){
            open();
        }
    }
}
