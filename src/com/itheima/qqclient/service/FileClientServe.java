package com.itheima.qqclient.service;

import com.itheima.qqcommon.Message;
import com.itheima.qqcommon.MessegeTpye;

import java.io.*;

/**
 * 该类/对象完成文件传输服务
 */
public class FileClientServe {
    /**
     *
     * @param src 原文件目录
     * @param dest 把文件传输到的目录
     * @param senderId 发送者id
     * @param receiverId 接收者id
     */
    public void setFileToOne(String src, String dest, String senderId, String receiverId){
        //1.读取src文件,必须存在,封装到message对象里面
        Message message = new Message();
        message.setMesTpye(MessegeTpye.MESSAGE_FILE);
        message.setSender(senderId);
        message.setReceiver(receiverId);
        message.setSrc(src);
        message.setDest(dest);
        //读取文件
        FileInputStream fileInputStream = null;
        File file = new File(src);
        byte[] fileBytes = new byte[(int)file.length()]; //没有办法传大文件
        try {
            fileInputStream = new FileInputStream(src);
            fileInputStream.read(fileBytes);  //将src读取到程序的字节数组
            //将文件对应的字节数组设置到message对象
            message.setFileByte(fileBytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            if(fileInputStream != null){
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        //提示信息
        System.out.println(senderId + "sent" +receiverId + "a file:" + src + "to：" + dest);

        //发送,根据用户id拿到socket
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream
                    (ManageClientConnectServerThread.getClinetConnectServerThread(senderId).getSocket().getOutputStream());
            //write
            objectOutputStream.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
