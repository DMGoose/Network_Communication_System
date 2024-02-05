package com.itheima.qqclient.service;

import com.itheima.qqclinet.utils.Utility;
import com.itheima.qqcommon.Message;
import com.itheima.qqcommon.MessegeTpye;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * 一个线程，是ClinetConnectServerThread线程
 */
public class ClinetConnectServerThread extends Thread{
    //该线程需要持有Socket
    private Socket socket;

    //写个构造器，可以传进来一个Socket对象
    public ClinetConnectServerThread(Socket socket) {
        this.socket = socket;
    }

    //为了更方便得到Socket，提供get
    public Socket getSocket() {
        return socket;
    }

    @Override
    public void run() {
        //因为Thread需要在后台和服务器不断通信，因此用while循环控制
        while(true){
            try {
                System.out.println("Client Thread，Waiting to read messages sent from the server side");
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                //如果服务器没有发送Message对象，线程会阻塞
                Message message = (Message) objectInputStream.readObject(); //还是转成Message
                //判断message的类型，然后做相应的处理业务
                  //   如果读到的是服务端返回的在线用户列表
                if(message.getMesTpye().equals(MessegeTpye.MESSAGE_RET_ONLINE_FRIEND)) {
                    //取出在线列表信息并显示
                    //规定：
                    String[] onlineUsers = message.getContent().split(" ");
                    System.out.println("\nOnline user list：");
                    for (int i = 1; i < onlineUsers.length; i++) {
                        System.out.println("用户 " + onlineUsers[i]);
                    }
                } else if(message.getMesTpye().equals(MessegeTpye.MESSAGE_COMM_MES)) {  //普通的聊天消息
                    //把从服务器端转发的消息，显示到控制台即可
                    System.out.println("\n" + message.getSender() + "对"
                            + message.getReceiver() + "说：" + message.getContent());
                } else if (message.getMesTpye().equals(MessegeTpye.MESSAGE_FIND_MY_MESSAGE)) {  //寻找消息
                    //把服务器端发过来的message打印出来
                    System.out.println("\n" + message.getSender() + "对"
                            + message.getReceiver() + "说：" + message.getContent());
                } else if(message.getMesTpye().equals(MessegeTpye.MESSAGE_TO_ALL)){  //群发消息
                        //把从服务器端转发的消息，显示到控制台即可
                        System.out.println("\n"+ message.getSender() + "对所有人说"
                                + message.getContent());
                }else if (message.getMesTpye().equals(MessegeTpye.MESSAGE_FILE)){  //文件消息
                    System.out.println("The other party saved the file in your default directory：" + message.getDest());
                    System.out.println("\n" + message.getSender() + "给" + message.getReceiver() +
                            "sent a file：" + message.getSrc() + "to my computer: " + message.getDest());
                    //取出message文件字节数组，输出流 写到磁盘
                    FileOutputStream fileOutputStream = new FileOutputStream(message.getDest());
                    fileOutputStream.write(message.getFileByte());
                    fileOutputStream.close();
                    System.out.println("\nFile saved successfully");
                }else{
                    System.out.println("we won't handle it for now");
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
