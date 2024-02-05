package com.itheima.qqframe;

import com.itheima.qqserver.service.QQServer;

/**
 * 该类创建QQserver对象，启动后台的服务
 */
public class QQFrame {
    public static void main(String[] args) {
        new QQServer();
    }
}
