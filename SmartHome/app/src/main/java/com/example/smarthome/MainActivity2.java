package com.example.smarthome;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import java.net.*;
import java.io.*;
import java.util.Scanner;

public class MainActivity2 extends AppCompatActivity {
    EditText et_1;
    Button bt_1;
    TextView tv_1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        et_1 = findViewById(R.id.et_1);
        bt_1 = findViewById(R.id.bt_1);
        tv_1 = findViewById(R.id.tv_1);


        bt_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //主线程异常错误，需要另外一个线程来处理这个代码
                String city = et_1.getText().toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getAqiData(city);
                    }
                }).start();
            }
        });
    }
    public void getAqiData(String city) {
        String tianapi_data = "";
        try {
            //URL网址 （空气质量请求地址）
            URL url = new URL( "https://apis.tianapi.com/aqi/index");
            //Http链接对象
            HttpURLConnection  conn = (HttpURLConnection) url.openConnection();
            //请求方式POST
            conn.setRequestMethod("POST");
            //设置请求超时时间 5秒
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setDoOutput(true);
            conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            //输出流（发送数据）
            OutputStream outputStrea = conn.getOutputStream();
            //填数据
            String content = "key=d4cbc6c2df8ffdfae660426619dbd4cf&area=" + city;
            //发送数据
            outputStrea.write(content.getBytes("utf-8"));
            outputStrea.flush();
            outputStrea.close();
            //输入流（接收数据）
            InputStream inputStream = conn.getInputStream();
            //接收格式UTF-8
            InputStreamReader inputStreamReader = new InputStreamReader (inputStream,"utf-8");
            //读取数据和解析数据
            BufferedReader bufferedReader = new BufferedReader (inputStreamReader);
            StringBuilder tianapi = new StringBuilder();
            String temp = null;
            while ( null != (temp = bufferedReader.readLine())){
                tianapi.append(temp);
            }
            //把数据转成字符串
            tianapi_data = tianapi.toString();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //打印输出
        //System.out.println(tianapi_data);

        // 字符串删除-我们删除了前面没用的JSON数据，和最后一个}号
        String res = tianapi_data.replace("{\"code\":200,\"msg\":\"success\",\"result\":", "");
        String res2 = res.substring(0, res.length() - 1);
        // 如何解析JSON数据，把它一个一个打印出来
        // GSON解析包-解析JSON数据
        Gson gson = new Gson();
        // 解析
        Bean bean = gson.fromJson(res2, Bean.class);

        System.out.println("城市:" + bean.area);
        System.out.println("臭氧:" + bean.o3);
        System.out.println("二氧化硫:" + bean.so2);
        System.out.println("pm2.5:" + bean.pm2_5);
        System.out.println("空气质量:" + bean.aqi);


        //子线程不能操作组件，因为这里的代码是子线程运行的
        //强行调回主线程
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv_1.setText("城市:" + bean.area + "\n" + "当前时间:" + bean.time + "\n" + "臭氧:" + bean.o3 + "\n" + "二氧化硫:" + bean.so2 + "\n" + "pm2.5:" + bean.pm2_5 + "\n" + "空气质量:" + bean.aqi );

            }
        });
    }
}