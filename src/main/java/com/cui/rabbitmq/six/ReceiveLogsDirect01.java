package com.cui.rabbitmq.six;

import com.cui.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * 接收消息
 */
public class ReceiveLogsDirect01 {

    public static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws Exception {

        Channel channel = RabbitMqUtils.getChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        channel.queueDeclare("console",false,false,false,null);
        channel.queueBind("console",EXCHANGE_NAME,"info");
        channel.queueBind("console",EXCHANGE_NAME,"warning");


        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            System.out.println("ReceiveLogsDirect01控制台打印接收到的消息：" + new String(delivery.getBody(),"UTF-8"));
        };

        channel.basicConsume("console",true,deliverCallback,(consumerTag) -> {});


    }
}
