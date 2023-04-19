package com.cui.rabbitmq.one;

import com.cui.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 优先级队列
 * 消费者
 */
public class PriorityConsumer {

    private final static String QUEUE_NAME = "PriorityHello";

    public static void main(String[] args) throws Exception {

        //创建信道
        Channel channel = RabbitMqUtils.getChannel();

        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-max-priority",10);
        channel.queueDeclare(QUEUE_NAME,true,false,false,arguments);

        //推送的消息如何进行消费的接口回调
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message= new String(delivery.getBody());
            System.out.println(message);
        };

        //取消消费的一个回调接口 如在消费的时候队列被删除掉了
        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println("消息消费被中断");
        };


        /**
         * 消费者消费消息
         * 1.消费哪个队列
         * 2.消费成功之后是否自动应答 true：表示自动应答 false：表示手动应答
         * 3.消费者未成功消费的回调
         * 4.消费者取消消费的回调
         */
        channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);
        System.out.println("等待接收消息 ......... ");

    }
}
