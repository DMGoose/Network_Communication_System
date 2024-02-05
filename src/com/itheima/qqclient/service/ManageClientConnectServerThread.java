package com.itheima.qqclient.service;

import java.util.HashMap;

/**
 * 该类管理客户端连接到服务器端的线程的类
 */
public class ManageClientConnectServerThread {
    //把多个线程放入到HashMap集合中，key就是用户id，value 就是线程
    public static HashMap<String,ClinetConnectServerThread> hashMap = new HashMap<>();

    //将线程加入到集合中
    public static void  addClientConnectServerThread(String userId, ClinetConnectServerThread clinetConnectServerThread){
        hashMap.put(userId,clinetConnectServerThread);
    }

    //通过userId，可以得到对应的线程
    public static ClinetConnectServerThread getClinetConnectServerThread(String userId){
        return hashMap.get(userId);
    }
}
