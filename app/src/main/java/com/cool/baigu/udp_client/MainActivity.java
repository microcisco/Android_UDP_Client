package com.cool.baigu.udp_client;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button goRoom = (Button) findViewById(R.id.goRoom);

        goRoom.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Thread thread=new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        NetManager.addMsgCallback(new ICallbackImp() {
                            @Override
                            public void onMsg(String msg) {
                                //登陆返回包处理
                                Map maps = (Map) JSON.parse(msg);
                                if (maps.get("action").equals("sendPid")) {
                                    Game.self_pid = maps.get("param1") + "";
                                    Game.self_roomId = maps.get("param2") + "";
                                }
                                Intent intent = new Intent(MainActivity.this, Game.class);
                                startActivity(intent);

                            }
                        });

                        EditText editText = (EditText) findViewById(R.id.textView2);
                        NetManager.joinRoom(editText.getText().toString());
                    }
                });
                thread.start();


            }
        });


    }
}
