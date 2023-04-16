package com.cui.rabbitmq.two;

import com.cui.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

/**
 * 这是一个工作线程（相当于消费者）
 */
public class Worker01 {

    //队列名字
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {

        //消息接收被取消时，执行下面的内容
        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println(consumerTag + "消费者取消消费的接口回调逻辑");
        };

        //消息的接收
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message= new String(delivery.getBody());
            System.out.println("接收到的消息：" + message);
        };

        //创建信道
        Channel channel = RabbitMqUtils.getChannel();

        //System.out.println("C1等待接收消息……");
        System.out.println("C2等待接收消息……");
        channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);
    }
}
