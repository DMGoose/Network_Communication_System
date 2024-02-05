package com.itheima.qqserver.service;

import com.itheima.qqcommon.Message;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * 模拟是一个数据库，用来存储离线发送的消息的
 */
public class DateBase {
    private static ArrayList<Message> messageList = new ArrayList<>();
    private static HashMap<String, Message> offlineDB = new HashMap<>();  //存放receiverId和ArrayList的HashMap


    public static void addMessageToOfflineDB(String receiverId, Message message){
        offlineDB.put(receiverId, message);
    }

    //
    public static void removeMessage(String receiverId){
        offlineDB.remove(receiverId);
    }

    //看看有没有这个key
    public static boolean haveIdOrNot(String receiverId){
        return offlineDB.containsKey(receiverId);
    }

    //根据key取出message
    public static Message obtainMessageFromDB(String receiverId){
        return offlineDB.get(receiverId);
    }

//    public static ArrayList haveMessageOrNot(){
//        Iterator<String> iterator = offlineDB.keySet().iterator();
//        while (iterator.hasNext()) {
//            String receiverId =  iterator.next();
//            Message message = offlineDB.get(receiverId);
//            messageList.add(message);
//        }
//        return messageList;
//    }


}
