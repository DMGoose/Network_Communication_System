package com.itheima.qqclient.view;

import com.itheima.qqclient.service.ClinetConnectServerThread;
import com.itheima.qqclient.service.FileClientServe;
import com.itheima.qqclient.service.MessageClientService;
import com.itheima.qqclient.service.UserClientService;
import com.itheima.qqclinet.utils.Utility;

import java.util.Scanner;

/**
 * 客户端的菜单界面
 */
public class QQView {
    private boolean loop = true;  //控制是否显示菜单
    private String key = ""; //用于接受用户的键盘输入
    private UserClientService  userClientService = new UserClientService();// 用于登陆服务器/注册用户
    private MessageClientService messageClientService = new MessageClientService(); //用于消息的发送，群聊，私聊
    private FileClientServe fileClientServe = new FileClientServe();//用于文件的发送

    public static void main(String[] args) throws InterruptedException {
        QQView qqView = new QQView();
        qqView.mainMenu();

    }

    //主菜单,写成一个方法
    private void mainMenu(){
        while(loop){
            System.out.println("===========Welcome to the network communication system===========");
            System.out.println("\t\t1.Log on");
            System.out.println("\t\t9.Log out");
            System.out.println("Please enter your choice");

            key = Utility.readString(1);

            //根据用户的输入，来处理不同的逻辑
            switch (key){
                case "1":
                    System.out.println("====Login====");
                    System.out.println("Please enter your userID");
                    String userId = Utility.readString(50);
                    System.out.println("Please enter your password");
                    String psw = Utility.readString(50);
                    //需要到服务端去验证该用户是否合法
                    //很多代码，先写个逻辑框架，这里编写一个类 UserClinetService [用户登录\注册]
                    if(userClientService.checkUser(userId,psw)){
                        System.out.println("===========Welcome, "+ userId + "===========");
                        //进入到二级菜单
                        while(loop){
                            System.out.println("\n===========Network Communication Second-level Menu" + "(" + userId + ")" + "===========");
                            System.out.println("\t\t1.Display online user list");
                            System.out.println("\t\t2.Send group messages");
                            System.out.println("\t\t3.Direct message");
                            System.out.println("\t\t4.Send Files");
                            System.out.println("\t\t9.Log out");
                            System.out.println("请输入您的选择");
                            key = Utility.readString(1);
                            switch (key){
                                case "1":
                                    //这里准备写一个方法来获取在线用户列表
                                    System.out.println("Show online users");
                                    userClientService.onlineFriendList();
                                    break;
                                case "2":
                                    System.out.println("The message will be posted to all online users");
                                    System.out.println("Please enter the message");
                                    String contentAll = Utility.readString(100);
                                    messageClientService.sendMessageToAll(contentAll,userId);
                                    break;
                                case "3":
                                    userClientService.findMyMessage();
                                    System.out.println("DM，Please enter the user you want to talk to (online user available only):");
                                    String receiverId = Utility.readString(50);
                                    System.out.println("Please enter the message");
                                    String contentOneToOne = Utility.readString(100);
                                    //编写一个方法，将消息发送给服务端
                                    //提示用户输入您要和哪个用户聊天
                                    messageClientService.sendMessageToOne(contentOneToOne,userId,receiverId);
                                    break;
                                case "4":
                                    System.out.println("Send files");
                                    System.out.println("Please enter the user you want send file to (online user available only)");
                                    String receiveId = Utility.readString(50);
                                    System.out.println("Please enter the complete path of the file to be sent(for example, F:\\123.png)");
                                    String src = Utility.readString(50);
                                    System.out.println("Please enter the complete path of the folder to be sent to the recipient");
                                    String dest = Utility.readString(50);
                                    fileClientServe.setFileToOne(src,dest,userId,receiveId);
                                    break;
                                case "9":
                                    System.out.println("Log out");
                                    //调用一个方法，给服务器发送给一个退出系统的message
                                    userClientService.logout();
                                    loop = false;
                                    break;
                            }
                        }
                    }else{
                        System.out.println("=======Login Failed=======");
                    }
                    break;  //break了switch
                case "9":
                    System.out.println("Log out");
                    loop = false;
                    break;
            }
        }
    }
}
