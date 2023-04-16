package com.cui.rabbitmq.two;

import com.cui.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;

import java.util.Scanner;

/**
 * 生产者：发送大量消息
 */
public class Task01 {

    //队列名字
    public static final String QUEUE_NAME = "hello";

    //发送大量的消息
    public static void main(String[] args) throws Exception {

        //创建信道
        Channel channel = RabbitMqUtils.getChannel();

        //创建一个队列
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        //从控制台当中接收消息
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNext()){

            String message = scanner.next();

            channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
            System.out.println("发送消息完成" + message);
        }


    }
}
