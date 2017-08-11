package com.cool.baigu.udp_client;

import com.alibaba.fastjson.JSON;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by baigu on 2017/8/9.
 */

public class NetManager {
    static Integer packIndex = 0;
    static String host = "192.168.1.86";
//    static String host = "121.41.20.206";
    static int port = 41234;
    static DatagramSocket socket = null;
    static NetManager instance = new NetManager();
    static LinkedList ackPool = new LinkedList();    //等待确认队列
    static HashMap<String, String> dataPool = new HashMap<>();

    private NetManager() {

//        final long timeInterval = 1000;
//        Runnable runnable = new Runnable() {
//            public void run() {
//                while (true) {
//                    // ------- code for task to run
////                    System.out.println("Hello !!");
//                    int size = ackPool.size();
//                    for (int i=0; i<size; i++) {
//                        NetManager.sendPack(dataPool.get(ackPool.get(i)));
//                    }
//
//
//                    // ------- ends here
//                    try {
//                        Thread.sleep(timeInterval);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        };
//        Thread thread = new Thread(runnable);
//        thread.start();


        TimerTask task = new TimerTask() {
            @Override
            public void run() {
//                int size = ackPool.size();
//                for (int i = 0; i < size; i++) {
//                    NetManager.sendPack(dataPool.get(ackPool.get(i)));
//                }
            }
        };
        Timer timer = new Timer();
        long delay = 0;
        long intevalPeriod = 10;
        // schedules the task to be run in an interval
        timer.scheduleAtFixedRate(task, delay, intevalPeriod);

}

    static ArrayList<ICallbackImp> callbacks = new ArrayList<>();

    static NetManager getInstance() {
        return instance;
    }

    static String getPackIndex() {
        ++packIndex;
        return Game.self_pid + "_" + String.valueOf(packIndex);
    }

static {
        try{
        InetAddress address=InetAddress.getByName(host);  //服务器地址
        socket=new DatagramSocket();  //创建套接字
        }catch(Exception e){
        e.printStackTrace();
        }
        new Thread(){
@Override
public void run(){
        try{
        //接收服务器反馈数据
        while(true){
        byte[]backbuf=new byte[1024];
        DatagramPacket backPacket=new DatagramPacket(backbuf,backbuf.length);
        socket.receive(backPacket);  //接收返回数据
        String backMsg=new String(backbuf,0,backPacket.getLength());
//        System.out.println("服务器返回的数据为:"+backMsg);

        for(ICallbackImp it:callbacks
        ){
        it.onMsg(backMsg);
        }
        }
        }catch(Exception e){
        e.printStackTrace();
        socket.close();
        }
        }
        }.start();
        }

static void joinRoom(String roomId){
        HashMap<String, String> req=new HashMap<>();
        req.put("action","joinRoom");
        req.put("roomId",roomId);
        sendPack(JSON.toJSONString(req));
        }

static void addAction(String roomId,String pid,String a){
        HashMap<String, String> req=new HashMap<>();
        req.put("action","recPlayAction");
        req.put("roomId",roomId);
        req.put("pid",pid);
        req.put("operate",a);
        sendPack(JSON.toJSONString(req));
        }

static void sendPack(String msg1){
        try{

        //添加包编号
        Map maps=(Map)JSON.parse(msg1);
        maps.put("packIndex",NetManager.getPackIndex());
        String msg=JSON.toJSONString(maps);


        if(maps.get("action").equals("recPlayAction")){
        ackPool.add(maps.get("packIndex"));
        dataPool.put((String)maps.get("packIndex"),msg);
        }


        byte[]buf=msg.getBytes();
        InetAddress address=InetAddress.getByName(host);  //服务器地址
        DatagramPacket dataGramPacket=new DatagramPacket(buf,buf.length,address,port);
        socket.send(dataGramPacket);  //通过套接字发送数据
        }catch(Exception e){
        e.printStackTrace();
//           socket.close();
        }
        }

static void addMsgCallback(ICallbackImp func){
        callbacks.add(func);
        }

        }
