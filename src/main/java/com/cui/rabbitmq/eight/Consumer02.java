package com.cui.rabbitmq.eight;

import com.cui.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.util.HashMap;
import java.util.Map;

/**
 * 死信队列
 * 消费者2
 */
public class Consumer02 {

    public static final String DEAL_QUEUE = "deal_queue";

    public static void main(String[] args) throws Exception {

        Channel channel = RabbitMqUtils.getChannel();

        System.out.println("consumer02等待接收消息……");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("consumer02接收到的消息是：" + message);
        };

        channel.basicConsume(DEAL_QUEUE, true, deliverCallback, consumerTag -> { });
    }
}