package com.cui.rabbitmq.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * 使用连接工厂创建连接信道
 */
public class RabbitMqUtils {

    //得到一个连接的channel
    public static Channel getChannel() throws Exception{

        //创建一个连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.137.88");
        factory.setUsername("admin");
        factory.setPassword("123");

        //创建连接
        Connection connection = factory.newConnection();

        //创建信道
        return connection.createChannel();
    }

}
