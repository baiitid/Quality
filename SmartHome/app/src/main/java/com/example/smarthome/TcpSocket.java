package com.example.smarthome;

import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class TcpSocket {

    //TCP客户端通信类
    public Socket socket = new Socket();
    //封装IP和端口的类
    public InetSocketAddress inetSocketAddress = new InetSocketAddress("172.19.43.190",1000);
    //定义输入流和输出流
    public OutputStream outputStream;
    public InputStream inputStream;


    public void connectTcp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //我们运行代码之后，发现出错网络在主函数线程异常（NetworkOnMainThreadException）
                    //关于网络链接的代码，不能在主线程里面运行
                    //解决方案-把这些代码，交给另外一个线程来运行
                    socket.connect(inetSocketAddress);
                    //判断链接是否成功
                    if (socket.isConnected()) {
                        Log.d("MSG","链接成功！！");
                        MyThread2();
                    }
                }catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }
    //发送数据的函数
    public void sendData(String msg) {
        //我们运行代码之后，发现出错网络在主函数线程异常（NetworkOnMainThreadException）
        //关于网络链接的代码，不能在主线程里面运行
        //解决方案-把这些代码，交给另外一个线程来运行
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    outputStream = socket.getOutputStream();
                    if (outputStream != null) {
                        outputStream.write(msg.getBytes("GBK"));
                    }
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public void MyThread2() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        inputStream = socket.getInputStream();
                        byte b[] = new  byte[1024];
                        int len = inputStream.read(b);
                        Log.d("MSG","服务器发来数据量：" + len);
                        String res = new String(b,0,len,"GBK");
                        Log.d("MSG","数据：" + res);
                        //静态调用对象，把文本数据设置在界面上
                        //代码虽然没错，但是这样写会报一个错误
                        //错误意思是，只有主线程才能去调用setText函数，子线程没权限调用
                        //MainActivity.tv_1.setText("数据显示：" + res);
                        //因为tv_1.setText这个函数，子线程不能调用，所以强行写了一个主线程代码，把这个函数丢到主线程
                        //匿名内部类线程写法
                        MainActivity.oc.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {


                                //根据字符_来切割字符数据（字符数组）
                                //于是这个数据被切割四份
                                String r[] = res.split("_");
                                MainActivity.tv_1.setText("温度:" + r[1] + "°C");
                                MainActivity.tv_2.setText("湿度:" + r[3] + "%");
                                Toast.makeText(MainActivity.oc,"数据显示："+res,Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }catch (Exception e) {}
            }
        }).start();
    }
}
