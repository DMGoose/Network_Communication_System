package com.itheima.qqclient.service;

import com.itheima.qqcommon.Message;
import com.itheima.qqcommon.MessegeTpye;
import com.itheima.qqcommon.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * 该类完成用户登陆验证和用户注册等功能
 */
public class UserClientService {
    private User u = new User();  //为什么做成一个属性，因为我们可能在其他地方要使用User信息，就可以用get，set
    private Socket socket;
    boolean checkResult = false;

    /**
     * 根据id和psw到服务器验证是否合法
     */
    public boolean checkUser(String userId, String psw) {
        //创建User对象并赋值
        u.setUserId(userId);
        u.setPsw(psw);

        //连接到服务端，发送u对象
        //因为socket在其他地方也可能使用，因此也做成属性（要存放在线程里面的）
        try {
            socket = new Socket(InetAddress.getLocalHost(), 9999);
            //得到一个ObjcetOutputStream
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            //write
            objectOutputStream.writeObject(u); //发送User对象给服务器

            //读取从服务端回复的Message
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            //read
            Message message = (Message) objectInputStream.readObject(); //这里要转成Message,方便访问Message里面的属性

            if(message.getMesTpye().equals(MessegeTpye.MESSAGE_LOGIN_SUCCESS)){ //登陆成功
                checkResult = true;  //成功登录
                //那就要启动线程，持有Socket，和服务器端保持通信  --> 创建一个线程类 ClinetConnectServerThread
                ClinetConnectServerThread ccst = new ClinetConnectServerThread(socket);
                //启动客户端线程
                ccst.start();
                //这里为了后面客户端的扩展，将线程放入一个集合,调用一个静态方法
                ManageClientConnectServerThread.addClientConnectServerThread(userId,ccst);
            }else{
                //登陆失败，我们就不能启动和服务器通信的线程，关闭socket
                socket.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return checkResult;
    }

    //向服务器端请求获得在线用户列表
    public void onlineFriendList(){
        //发送一个Message，类型为 MESSAGE_GET_ONLINE_FRIEND ;
        Message message = new Message();
        message.setMesTpye(MessegeTpye.MESSAGE_GET_ONLINE_FRIEND);
        message.setSender(u.getUserId());
        //发送给服务器，需要得到当前线程的socket对应的ObjectOutputStream对象
        try {
            //先从集合中得到UserId对应的线程
            ClinetConnectServerThread clinetConnectServerThread
                    = ManageClientConnectServerThread.getClinetConnectServerThread(u.getUserId());
            //得到这个线程的socket
            Socket aSocketGetByThread = clinetConnectServerThread.getSocket();
            //获得这个socket的OutputStream
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(aSocketGetByThread.getOutputStream());
            //write Message
            objectOutputStream.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //编写方法，推出客户端，并给服务端发送一个退出系统的message对象
    public void logout(){
        Message message = new Message();
        message.setMesTpye(MessegeTpye.MESSAGE_CLIENT_EXIT);
        //一定要指明我是哪个客户端id，因为对面的线程集合里面有很多个线程
        message.setSender(u.getUserId());

        //发送message
        try {
            ClinetConnectServerThread clinetConnectServerThread
                    = ManageClientConnectServerThread.getClinetConnectServerThread(u.getUserId());
            //得到这个线程的socket
            Socket aSocketGetByThread = clinetConnectServerThread.getSocket();
            //获得这个socket的OutputStream
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(aSocketGetByThread.getOutputStream());
            //write Message
            objectOutputStream.writeObject(message);
            System.out.println(u.getUserId() + " Log out");
            System.exit(0);//结束进程
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void findMyMessage(){
        //去服务器的DB查找有没有自己的id
        Message message = new Message();
        message.setMesTpye(MessegeTpye.MESSAGE_FIND_MY_MESSAGE);
        message.setSender(u.getUserId());

        try {
            ClinetConnectServerThread clinetConnectServerThread
                    = ManageClientConnectServerThread.getClinetConnectServerThread(u.getUserId());
            //得到这个线程的socket
            Socket aSocketGetByThread = clinetConnectServerThread.getSocket();
            //获得这个socket的OutputStream
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(aSocketGetByThread.getOutputStream());
            //write Message
            objectOutputStream.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
