package com.cui.rabbitmq.eight;

import com.cui.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

/**
 * 死信队列
 * 生产者
 */
public class Producer {

    //普通交换机名字
    public static final String NORMAL_EXCHANGE = "normal_exchange";

    public static void main(String[] args) throws Exception {

        Channel channel = RabbitMqUtils.getChannel();

        //死信队列 设置消息的TTL时间  单位ms
        AMQP.BasicProperties properties =
                new AMQP.BasicProperties()
                        .builder().expiration("10000").build();

        //该信息是用作演示队列个数限制
        for (int i = 1; i <11 ; i++) {

            String message = "info" + i;
            channel.basicPublish(NORMAL_EXCHANGE,"zhangsan",properties,message.getBytes());

            System.out.println("生产者发送消息:"+message);
        }
    }
}
