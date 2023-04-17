package com.cui.rabbitmq.eight;

import com.cui.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.util.HashMap;
import java.util.Map;

/**
 * 死信队列
 * 消费者1
 */
public class Consumer01 {

    //普通交换机名字
    public static final String NORMAL_EXCHANGE = "normal_exchange";
    //死信交换机名字
    public static final String DEAL_EXCHANGE = "deal_exchange";
    //普通队列名字
    public static final String NORMAL_QUEUE = "normal_queue";
    //死信队列名字
    public static final String DEAL_QUEUE = "deal_queue";

    public static void main(String[] args) throws Exception {

        Channel channel = RabbitMqUtils.getChannel();

        //声明死信和普通交换机
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(DEAL_EXCHANGE, BuiltinExchangeType.DIRECT);

        //声明普通队列
        //正常队列绑定死信队列信息
        Map<String, Object> arguments = new HashMap<>();
        //过期时间10s
        //arguments.put("x-message-ttl", 100000);
        //正常队列设置死信交换机参数key是固定值
        arguments.put("x-dead-letter-exchange", DEAL_EXCHANGE);
        //正常队列设置死信routing-key参数key是固定值
        arguments.put("x-dead-letter-routing-key", "lisi");
        //正常队列设置长度
        arguments.put("x-max-length", 6);
        channel.queueDeclare(NORMAL_QUEUE,false,false,false,arguments);

        //声明死信队列
        channel.queueDeclare(DEAL_QUEUE,false,false,false,null);

        //绑定普通的交换机和普通队列
        channel.queueBind(NORMAL_QUEUE,NORMAL_EXCHANGE,"zhangsan");
        //绑定死信的交换机和死信队列
        channel.queueBind(DEAL_QUEUE,DEAL_EXCHANGE,"lisi");

        System.out.println("consumer01等待接收消息……");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            if (message.equalsIgnoreCase("info5")){
                System.out.println("consumer01接收到的消息是：" + message + "此消息被C1拒收");
                channel.basicReject(delivery.getEnvelope().getDeliveryTag(),false);
            }else {
                System.out.println("consumer01接收到的消息是：" + message);
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
            }

        };
        //开启手动应答
        boolean isautoAck = false;
        channel.basicConsume(NORMAL_QUEUE, isautoAck, deliverCallback, consumerTag -> { });
    }
}
