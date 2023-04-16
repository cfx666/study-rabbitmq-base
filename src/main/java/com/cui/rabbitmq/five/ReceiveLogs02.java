package com.cui.rabbitmq.five;

import com.cui.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * 接收消息
 */
public class ReceiveLogs02 {

    public static final String EXCHANGE_NAME = "log1";

    public static void main(String[] args) throws Exception {

        //创建信道
        Channel channel = RabbitMqUtils.getChannel();

        //声明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME,"fanout");
        //声明一个临时队列，名字是随机的
        String queue = channel.queueDeclare().getQueue();
        //交换机和队列绑定
        channel.queueBind(queue,EXCHANGE_NAME,"");

        System.out.println("等待接收消息，把接收到的消息写入到文件……");

        //推送的消息如何进行消费的接口回调
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            System.out.println("ReceiveLogs02控制台打印接收到的消息：" + new String(delivery.getBody(),"UTF-8"));
        };
        channel.basicConsume(queue,true,deliverCallback,(consumerTag) -> {});


    }
}
