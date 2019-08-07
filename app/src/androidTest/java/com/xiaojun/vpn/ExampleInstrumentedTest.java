package com.xiaojun.vpn;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.xiaojun.vpn", appContext.getPackageName());

        //00010001 : 17
//        byte tmp = 0x11;
//        Log.e("xiaojun","00010001左移4位的值为:"+(byte)(tmp<<4)+",之后再右移4位的值为:"+((byte)(((byte)(tmp<<4))>>4)));
        Log.e("xiaojun","0xff无符号右移7位之后的值为:"+(0xff>>>7));
        Log.e("xiaojun","0xff::"+((byte)(((byte)0xff)>>>7)));
        //ByteBuffer字节数组缓冲区
        /*ByteBuffer buffers = ByteBuffer.allocate(Short.MAX_VALUE);
        //mark标记，position标记，limit界限，capacity容量

        //get和put方法向缓冲区读取数据
        buffers.put((byte) 9);//put方法,将position指针后移一位，在没有越界的情况下，然后再存入数据
//        int position = buffers.position();
        byte value = buffers.get(0);
        byte[] bytes = buffers.array();
        buffers.flip();//重置位置
        Log.e("xiaojun","position="+buffers.position()+",value="+value+",bytes.length="+bytes.length+",bytes.limit="+buffers.limit());
        try {
            Thread.currentThread().sleep(100000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/


        //ByteBuffer不是线程安全



    }
}
