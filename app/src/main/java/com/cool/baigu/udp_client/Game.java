package com.cool.baigu.udp_client;

import android.os.Bundle;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;

import java.util.Map;

/**
 * Created by baigu on 2017/8/9.
 */

public class Game extends AppCompatActivity {

    static long it = 0;
    static float X;
    static float Y;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        final int action = MotionEventCompat.getActionMasked(event);
        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                X = event.getX();
                Y = event.getY();
                break;
            }
            case MotionEvent.ACTION_MOVE: {

                if(System.currentTimeMillis() - it < 50) {
                    break;
                }
                it = System.currentTimeMillis();

                WindowManager wm = this.getWindowManager();
                int height = wm.getDefaultDisplay().getWidth();

                final String s1 = String.valueOf(event.getX());
                final String s2 = String.valueOf(event.getY() - 200);

                new Thread() {
                    @Override
                    public void run() {


                        NetManager.addAction(self_roomId, self_pid, s1 + "_" + s2);
                    }
                }.start();

                X = event.getX();
                Y = event.getY();
                break;
            }
            case MotionEvent.ACTION_UP: {
                break;
            }
            case MotionEvent.ACTION_CANCEL: {
                break;
            }
            case MotionEvent.ACTION_POINTER_UP: {
                break;
            }
        }
        return true;
    }

    View hero;
    static String self_pid;
    static String self_roomId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);


        NetManager.addMsgCallback(new ICallbackImp() {
            @Override
            public void onMsg(String msg) {

                final Map maps = (Map) JSON.parse(msg);

                //服务器下发指令处理
                if (maps.get("action").equals("sendPlayActions")) {

                    for (final Object k : maps.keySet()
                            ) {
                        if (!(k.equals("action"))) {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //操作指令处理对象
                                    String pid = (String) k;
                                    View _self = findViewById(Integer.parseInt(pid));
                                    if (_self == null) {
                                        AbsoluteLayout llWindLayout = (AbsoluteLayout) findViewById(R.id.kaka);
                                        ImageView imageView = new ImageView(Game.this);
                                        imageView.setLayoutParams(new ViewGroup.LayoutParams(72, 72));  //设置图片宽高
                                        imageView.setX(50);
                                        imageView.setY(50);
                                        imageView.setId(Integer.parseInt(pid));
                                        imageView.setImageResource(R.mipmap.ic_launcher); //图片资源
                                        llWindLayout.addView(imageView); //动态添加图片
                                        _self = imageView;
                                    }
                                    //操作指令处理动作
                                    /*
                                    String a = (String) maps.get(k);
                                    if (a.equals("shang")) {
                                        _self.setY(_self.getY() - 10);
                                    } else if (a.equals("xia")) {
                                        _self.setY(_self.getY() + 10);
                                    } else if (a.equals("zuo")) {
                                        _self.setX(_self.getX() - 10);
                                    } else if (a.equals("you")) {
                                        _self.setX(_self.getX() + 10);
                                    }
                                    */
                                    String a = (String) maps.get(k);
                                    String[] p = a.split("_");
                                    _self.setX( Float.parseFloat(p[0]));
                                    _self.setY( Float.parseFloat(p[1]));

                                }
                            });

                        }
                    }
                }
                else if(maps.get("action").equals("ackPck")) {
                    NetManager.ackPool.remove(maps.get("index"));
                    NetManager.dataPool.remove(maps.get("index"));
                }

            }
        });

        Button shang = (Button) findViewById(R.id.shang);
        Button xia = (Button) findViewById(R.id.xia);
        Button zuo = (Button) findViewById(R.id.zuo);
        Button you = (Button) findViewById(R.id.you);
        shang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                hero.setY(hero.getY() - 10);

                new Thread() {
                    @Override
                    public void run() {
                        NetManager.addAction(self_roomId, self_pid, "shang");
                    }
                }.start();


            }
        });
        xia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                hero.setY(hero.getY() + 10);

                new Thread() {
                    @Override
                    public void run() {
                        NetManager.addAction(self_roomId, self_pid, "xia");
                    }
                }.start();

            }
        });
        zuo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                hero.setX(hero.getX() - 10);

                new Thread() {
                    @Override
                    public void run() {
                        NetManager.addAction(self_roomId, self_pid, "zuo");
                    }
                }.start();

            }
        });
        you.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                hero.setX(hero.getX() + 10);

                new Thread() {
                    @Override
                    public void run() {
                        NetManager.addAction(self_roomId, self_pid, "you");
                    }
                }.start();

            }
        });

//        AbsoluteLayout llWindLayout = (AbsoluteLayout) findViewById(R.id.kaka);
//        ImageView imageView = new ImageView(this);
//        imageView.setLayoutParams(new ViewGroup.LayoutParams(72, 72));  //设置图片宽高
//        imageView.setX(50);
//        imageView.setY(50);
//        imageView.setImageResource(R.mipmap.ic_launcher); //图片资源
//        llWindLayout.addView(imageView); //动态添加图片
//        hero = imageView;


    }
}
