package com.example.smarthome;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    //当前主线程类对象
    public static MainActivity oc;


    //把这个文本组件设置为公共静态，这样我们就能直接在TCP那边就可以直接调用
    public static TextView tv_1,tv_2,tv_3;
    Button bt_1,bt_2,bt_3,bt_4,bt_5,bt_6,bt_7,bt_8;
    TcpSocket tcpSocket = new TcpSocket();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //当前类对象赋值
        oc = this;
        tv_1 = findViewById(R.id.tv_1);
        tv_2 = findViewById(R.id.tv_2);
        tv_3 = findViewById(R.id.tv_3);
        bt_1 = findViewById(R.id.bt_1);
        bt_2 = findViewById(R.id.bt_2);
        bt_3 = findViewById(R.id.bt_3);
        bt_4 = findViewById(R.id.bt_4);
        bt_5 = findViewById(R.id.bt_5);
        bt_6 = findViewById(R.id.bt_6);
        bt_7 = findViewById(R.id.bt_7);
        bt_8 = findViewById(R.id.bt_8);
        BtListener btListener = new BtListener();
        bt_1.setOnClickListener(btListener);
        bt_2.setOnClickListener(btListener);
        bt_3.setOnClickListener(btListener);
        bt_4.setOnClickListener(btListener);
        bt_5.setOnClickListener(btListener);
        bt_6.setOnClickListener(btListener);
        bt_7.setOnClickListener(btListener);
        bt_8.setOnClickListener(btListener);
        tcpSocket.connectTcp();

    }

    private class BtListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v == bt_1) {
                Toast.makeText(MainActivity.this,"开灯",Toast.LENGTH_SHORT).show();
                tcpSocket.sendData("CR_R1_OPEN");
            }
            if (v == bt_2) {
                Toast.makeText(MainActivity.this,"关灯",Toast.LENGTH_SHORT).show();
                tcpSocket.sendData("CR_R1_CLOSE");
            }
            if (v == bt_3) {
                Toast.makeText(MainActivity.this,"开启加湿器",Toast.LENGTH_SHORT).show();
                tcpSocket.sendData("CR_R2_OPEN");
            }
            if (v == bt_4) {
                Toast.makeText(MainActivity.this,"关闭加湿器",Toast.LENGTH_SHORT).show();
                tcpSocket.sendData("CR_R2_CLOSE");
            }
            if (v == bt_5) {
                Toast.makeText(MainActivity.this,"开启空调",Toast.LENGTH_SHORT).show();
                tcpSocket.sendData("CR_R3_OPEN");
            }
            if (v == bt_6) {
                Toast.makeText(MainActivity.this,"关闭空调",Toast.LENGTH_SHORT).show();
                tcpSocket.sendData("CR_R3_CLOSE");
            }
            if (v == bt_7) {
                Toast.makeText(MainActivity.this,"开启风扇",Toast.LENGTH_SHORT).show();
                tcpSocket.sendData("CR_R4_OPEN");
            }
            if (v == bt_8) {
                Toast.makeText(MainActivity.this,"关闭风扇",Toast.LENGTH_SHORT).show();
                tcpSocket.sendData("CR_R4_CLOSE");
            }
        }
    }
}
