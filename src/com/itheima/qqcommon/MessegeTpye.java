package com.itheima.qqcommon;

/**
 * 消息类型有哪些
 */
public interface MessegeTpye {
    //为了统一命名规范,接口里面的变量默认是static final 的常量
    //定义不同常量的值。表示不同的消息类型
    String MESSAGE_LOGIN_SUCCESS = "1"; //表示登陆成功
    String MESSAGE_LOGIN_FAIL = "2"; // 表示登陆失败
    String MESSAGE_COMM_MES = "3"; //一个普通的信息包
    String MESSAGE_GET_ONLINE_FRIEND = "4";//要求返回(要获得)在线用户列表
    String MESSAGE_RET_ONLINE_FRIEND = "5";//返回在线用户列表
    String MESSAGE_CLIENT_EXIT = "6"; //客户端请求退出
    String MESSAGE_TO_ALL = "7";//群发的消息
    String MESSAGE_FILE = "8"; //文件消息
    String MESSAGE_FIND_MY_MESSAGE = "9"; //离线用户上线后查找自己的信息
}
