package com.cui.rabbitmq.six;

import com.cui.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * 接收消息
 */
public class ReceiveLogsDirect02 {

    public static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws Exception {

        Channel channel = RabbitMqUtils.getChannel();

        //channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        channel.queueDeclare("disk",false,false,false,null);
        channel.queueBind("disk",EXCHANGE_NAME,"error");


        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            System.out.println("ReceiveLogsDirect02控制台打印接收到的消息：" + new String(delivery.getBody(),"UTF-8"));
        };

        channel.basicConsume("disk",true,deliverCallback,(consumerTag) -> {});


    }
}
