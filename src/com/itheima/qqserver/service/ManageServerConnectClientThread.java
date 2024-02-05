package com.itheima.qqserver.service;

import java.util.HashMap;
import java.util.Iterator;

/**
 * 用于管理客户端通信的线程
 */
public class ManageServerConnectClientThread {
    private static HashMap<String , ServerConnectClientThread> hashMap = new HashMap<>();

    //添加线程对象到集合的方法
    public static void addServerConnectClientThread(String UserId , ServerConnectClientThread serverConnectClientThread){
        hashMap.put(UserId, serverConnectClientThread);
    }

    //根据UserId 返回 ServerConnectClientThread
    public static ServerConnectClientThread getServerConnectClientThread(String UserId){
        return hashMap.get(UserId);
    }

    //编写方法，可以在线用户列表
    public static String getOnlineUser(){
        //集合的遍历,遍历hashMap的key就可以
        Iterator<String> iterator = hashMap.keySet().iterator();
        String onlineUserList = " ";
        while (iterator.hasNext()) {
            onlineUserList += iterator.next().toString() + " ";
        }
        return onlineUserList;
    }

    //判断receiver是否在线
    //在线返回false，不在线返回true
    public static boolean onlineOrNot(String receiverId){
        Iterator<String> iterator = hashMap.keySet().iterator();
        while (iterator.hasNext()) {
            String key =  iterator.next();
            if(receiverId.equals(key)){
                return false;
            }
        }
        return true;
    }

    //写一个从集合中移除某个线程对象的方法
    public static void removeServerConnectClientThread(String userId){
        hashMap.remove(userId);
    }

    //返回一个hashMap
    public static HashMap<String, ServerConnectClientThread> getHashMap() {
        return hashMap;
    }
}
