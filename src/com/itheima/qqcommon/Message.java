package com.itheima.qqcommon;

import java.io.Serializable;

/**
 * 表示客户端和服务器端通信的消息对象（可能有各种各样的类型）
 */
public class Message implements Serializable {
    private static final long serialVersionID = 1L;
    private String sender; //发送者
    private String receiver;
    private String content;
    private String sendTime; //发送时间，也需要被序列化，所以用String
    private String mesTpye; //消息类型，可以在接口中定义消息类型

    //进行拓展，和文件相关的成员
    private byte[] fileByte;
    private int length = 0;
    private String dest; //将文件传输到哪里
    private String src; //源文件目录

    public byte[] getFileByte() {
        return fileByte;
    }

    public void setFileByte(byte[] fileByte) {
        this.fileByte = fileByte;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getMesTpye() {
        return mesTpye;
    }

    public void setMesTpye(String mesTpye) {
        this.mesTpye = mesTpye;
    }


    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }
}
