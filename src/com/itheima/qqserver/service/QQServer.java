package com.itheima.qqserver.service;

import com.itheima.qqcommon.Message;
import com.itheima.qqcommon.MessegeTpye;
import com.itheima.qqcommon.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 这是服务器，在监听9999，等待客户端连接，并保持通信
 */
public class QQServer {
    private ServerSocket serverSocket;

    //创建一个集合，存放多个用户，如果是这些用户登录，就认为是合法的
    //这里也可以使用 ConcurrentHashMap集合，可以处理并发的集合
    //HashMap没有处理线程安全，因此在多线程情况下，是不安全的
    //ConcurrentHashMap 处理的线程安全，即线程同步处理，在多线程情况下是安全的
    private static HashMap<String, User> validUsers = new HashMap<>();
    private static HashMap<String, ArrayList<Message>> offlineDB = new HashMap<>();

    static { //在静态代码块，初始化validUsers
        validUsers.put("100", new User("100","123456"));
        validUsers.put("200", new User("200","123456"));
        validUsers.put("300", new User("300","123456"));
        validUsers.put("紫霞仙子", new User("紫霞仙子","123456"));
        validUsers.put("小明", new User("小明","123456"));
        validUsers.put("小红", new User("小红","123456"));
    }

    //验证用户是否有效的方法
    private boolean checkUser(String userId, String psw){
        User user = validUsers.get(userId);
        //过关的验证方式
        if(user == null){  //不存在
            return false;
        }
        if(!user.getPsw().equals(psw)){  //ID
            return false;
        }
        return true;

    }
    public static void main(String[] args) {
        new QQServer();
    }

    public QQServer(){
        //注意：端口可以写在配置文件
        try {
            System.out.println("Server is listening on port 9999....");
            //启动推送新闻的服务(线程)
            new Thread(new SendNewsToAll()).start();
            serverSocket = new ServerSocket(9999);  //没有客户端连接，就会阻塞
            while(true){  //监听是循环的，当和某个客户端建立连接后，会继续监听，因此while
                Socket socket = serverSocket.accept();
                //getInputStream
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                //读取客户端发送的User对象
                User user = (User) objectInputStream.readObject();  //转型
                //验证,先创建一个Message对象，准备回复客户端
                Message message = new Message();
                if(checkUser(user.getUserId(), user.getPsw())){
                    //getOutputStream，一会用来回Message
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                    //登陆成功,要发消息告诉客户端你已经登陆成功
                        message.setMesTpye(MessegeTpye.MESSAGE_LOGIN_SUCCESS);
                    //将message对象回复
                        objectOutputStream.writeObject(message);
                    //创建一个线程ServerConnectClientThread，和客户端保持连接，该线程需要持有Socket对象
                        ServerConnectClientThread serverConnectClientThread
                            = new ServerConnectClientThread(socket, user.getUserId());
                    //启动线程
                        serverConnectClientThread.start();
                    //把该线程对象放入到集合中，进行管理，ManageServerConnectClientThread
                    ManageServerConnectClientThread.addServerConnectClientThread(user.getUserId(), serverConnectClientThread);
                }else{
                    //getOutputStream
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                    //登录失败
                    System.out.println(user.getUserId() + "Login in failed, Password is：" + user.getPsw() + "authentication failed");
                    message.setMesTpye(MessegeTpye.MESSAGE_LOGIN_FAIL);
                    objectOutputStream.writeObject(message);
                    socket.close();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally{
            //如果服务器退出了while，说明服务端不在监听，因此需要关闭ServerSocket
            try {
                serverSocket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
