package com.itheima.qqclient.service;

import com.itheima.qqcommon.Message;
import com.itheima.qqcommon.MessegeTpye;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;

/**
 * 该类专门提供消息相关的服务方法
 */

public class MessageClientService {
    public void sendMessageToOne(String content, String senderId, String receiverId){
        //构建message
        Message message = new Message();
        message.setMesTpye(MessegeTpye.MESSAGE_COMM_MES);
        message.setSender(senderId);
        message.setReceiver(receiverId);
        message.setContent(content);
        //message.setSendTime(new Date().toString());  //发送时间也设置到message对象里
        System.out.println(senderId + " said: "+ content + " to" + receiverId );

        //发送给服务端
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream
                    (ManageClientConnectServerThread.getClinetConnectServerThread(senderId).getSocket().getOutputStream());
            objectOutputStream.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ////编写方法，实现信息发送
        //    public void
        //    //setMeesageType,setSender,setReceiver
        //    //get socket
        //    //sned!
        //    //print(message.getContent)
    }

    public void sendMessageToAll(String content, String senderId){
        Message message = new Message();
        message.setMesTpye(MessegeTpye.MESSAGE_TO_ALL);  //群发消息
        message.setSender(senderId);
        message.setContent(content);
        System.out.println(senderId + " said：" + content + " to everyone");

        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream
                    (ManageClientConnectServerThread.getClinetConnectServerThread(senderId).getSocket().getOutputStream());
            objectOutputStream.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
