package com.itheima.qqserver.service;

import com.itheima.qqcommon.Message;
import com.itheima.qqcommon.MessegeTpye;
import com.itheima.utils.Utility;

import javax.swing.text.Utilities;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

/**
 * 一个线程
 */
public class SendNewsToAll implements Runnable{
    private Scanner scanner = new Scanner(System.in);

    @Override
    public void run() {
        while(true){  //为了可以多次推送新闻，使用while
            System.out.println("Please enter the news to be pushed by the server, Enter 'exit' to exit");
            String news = Utility.readString(100);
            if("exit".equals(news)){
                break;
            }
            //构建一个消息，类型为群发消息的类型
            Message message = new Message();
            message.setSender("Server");
            message.setMesTpye(MessegeTpye.MESSAGE_TO_ALL);
            message.setContent(news);
            message.setSendTime(new Date().toString());
            System.out.println("Server pushed a message：" + news);

            //遍历当前所有的通讯线程，并发送message
            HashMap<String, ServerConnectClientThread> hashMap = ManageServerConnectClientThread.getHashMap();
            Iterator<String> iterator = hashMap.keySet().iterator();
            while (iterator.hasNext()) {
                String onlineUserId = iterator.next();
                ServerConnectClientThread serverConnectClientThread = hashMap.get(onlineUserId);//根据id取出线程
                try {
                    ObjectOutputStream objectOutputStream =
                            new ObjectOutputStream(serverConnectClientThread.getSocket().getOutputStream());
                    //write
                    objectOutputStream.writeObject(message);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
