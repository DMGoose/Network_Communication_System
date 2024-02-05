package com.itheima.qqserver.service;

import com.itheima.qqcommon.Message;
import com.itheima.qqcommon.MessegeTpye;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

/**
 * 该类的一个对象和某个客户端保持通讯，要持有socket
 */
public class ServerConnectClientThread extends Thread{
    private Socket socket;
    private String userId;  //要知道自己和哪个User端产生的连接

    public ServerConnectClientThread(Socket socket, String userId){
        this.socket = socket;
        this.userId = userId;
    }

    public Socket getSocket() {
        return socket;
    }

    @Override
    public void run() {  //这里线程处于run的状态。可以接受客户端发送的消息
       while(true){
           try {
               System.out.println("服务端和客户端"+ userId + "保持通讯，读取数据...");
               ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
               Message message = (Message) objectInputStream.readObject();
               //根据Message类型，做相应的业务
               if(message.getMesTpye().equals(MessegeTpye.MESSAGE_GET_ONLINE_FRIEND)){  //获取在线列表
                   //客户端要在线用户列表  100 200 300 紫霞仙子
                   System.out.println(message.getSender() + "要在线用户列表");
                   //管理线程的集合知道有几个集合在里面,也就是有几个在线用户在里面
                   String onlineUser = ManageServerConnectClientThread.getOnlineUser();
                    //返回message,需要构建一个message，返回给客户端
                   Message message2 = new Message();
                   message2.setMesTpye(MessegeTpye.MESSAGE_RET_ONLINE_FRIEND);
                   message2.setContent(onlineUser);
                   message2.setReceiver(message.getSender());
                   ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                   objectOutputStream.writeObject(message2);
               }else if(message.getMesTpye().equals(MessegeTpye.MESSAGE_CLIENT_EXIT)){  //客户端退出
                   System.out.println(message.getSender() + "退出系统");
                   //将这个客户端对应的线程从集合中删除
                   ManageServerConnectClientThread.removeServerConnectClientThread(message.getSender());
                   socket.close(); //关闭连接
                   //退出while循环(线程)
                   break;
               }else if(message.getMesTpye().equals(MessegeTpye.MESSAGE_COMM_MES)) {  //发普通消息
                   //根据message获取receiverId，得到Id，然后再通过id得到对应的线程
                   //得到socket对应的对象输出流,将message转发给指定的客户
                   //其实这里直接socket.getOutputstream 就行了，因为上面已经通过id把对应的线程取出来了，它调用了socket
                   //谁调用的socket就是谁的

                   //判断receiver是否在线
                   //在线返回false，不在线返回true
                   boolean b = ManageServerConnectClientThread.onlineOrNot(message.getReceiver());
                   if (b == false) {
                       //根据message获取receiverId，得到Id，然后再通过id得到对应的线程
                       ServerConnectClientThread serverConnectClientThread =
                               ManageServerConnectClientThread.getServerConnectClientThread(message.getReceiver());
                       ObjectOutputStream objectOutputStream =
                               new ObjectOutputStream(serverConnectClientThread.getSocket().getOutputStream());
                       objectOutputStream.writeObject(message);  //转发，如果客户不在线，那么可以保存到数据库，可以实现离线服务
                   } else {
                       //不在线,那就要把带receiverID的消息存到DB
                       DateBase.addMessageToOfflineDB(message.getReceiver(), message);
                       //接下来，当一个人上线之后，立马调用判断是否有消息的方法，已经调用了,接受了发来的消息
                   }
               } else if (message.getMesTpye().equals(MessegeTpye.MESSAGE_FIND_MY_MESSAGE)) {  //寻找消息
                   //现在要获得sender对应的线程
                   ObjectOutputStream objectOutputStream2 = new ObjectOutputStream
                           (ManageServerConnectClientThread.getServerConnectClientThread(message.getSender()).getSocket().getOutputStream());
                   if(DateBase.haveIdOrNot(message.getSender())){
                       Message message1 = DateBase.obtainMessageFromDB(message.getSender());
                       objectOutputStream2.writeObject(message1);
                       DateBase.removeMessage(message1.getSender());
                   }else {
                       objectOutputStream2.write("没有你的新消息".getBytes());
                   }
               } else if(message.getMesTpye().equals(MessegeTpye.MESSAGE_TO_ALL)){  //群发消息
                   //遍历ManageServerConnectClientThread，取出所有线程，排除自己
                    //得到所有线程的socket，然后把message进行转发即可
                   HashMap<String, ServerConnectClientThread> hashMap = ManageServerConnectClientThread.getHashMap();
                   //开始遍历
                   Iterator<String> iterator = hashMap.keySet().iterator();
                   while (iterator.hasNext()) {
                       //取出在线用户的id
                       String onlineUserId =  iterator.next();
                       //排除自己的id
                       if(onlineUserId != message.getSender()){   //这里也可以用values遍历。直接就是线程
                           //根据key（用户id）获得对应的线程(value) hashMap.get(onlineUserId),
                            // 再根据获得的线程 取出 socket   hashMap.get(onlineUserId).getSocket()
                            // 再根据socket getOutputstream
                           ObjectOutputStream objectOutputStream =
                                   new ObjectOutputStream(hashMap.get(onlineUserId).getSocket().getOutputStream());
                           //write
                           objectOutputStream.writeObject(message);
                       }
                   }
               } else if (message.getMesTpye().equals(MessegeTpye.MESSAGE_FILE)){
                   //根据receiverId获取到对应的线程，将message转发
                   ServerConnectClientThread serverConnectClientThread =
                           ManageServerConnectClientThread.getServerConnectClientThread(message.getReceiver());
                   //获得socket，getOutputStream
                   ObjectOutputStream objectOutputStream =
                           new ObjectOutputStream(serverConnectClientThread.getSocket().getOutputStream());
                   //write
                   objectOutputStream.writeObject(message);
               } else{
                   System.out.println("其他类型的message暂时不处理");
               }
           } catch (Exception e) {
               throw new RuntimeException(e);
           }
       }
    }
}
