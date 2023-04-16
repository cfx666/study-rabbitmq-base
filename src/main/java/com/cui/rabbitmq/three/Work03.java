package com.cui.rabbitmq.three;

import com.cui.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

import java.util.concurrent.TimeUnit;

/**
 * 消息在手动应答时时不丢失，放回队列中重新消费
 */
public class Work03 {

    //队列名称
    public static final String TASK_QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws Exception {

        Channel channel = RabbitMqUtils.getChannel();

        System.out.println("C1等待接收消息处理时间较短");

        //消息接收被取消时，执行下面的内容
        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println(consumerTag + "消费者取消消费的接口回调逻辑");
        };

        //消息的接收
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {

            //休眠1s
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            String message= new String(delivery.getBody(),"UTF-8");
            System.out.println("接收到的消息：" + message);

            /**
             * 手动应答
             * 1.消息的标记 tag
             * 2.是否批量应答
             */
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
        };

        //设置消息不公平分发
        //int prefetchCount = 2;
        //channel.basicQos(prefetchCount);

        //采用手动应答
        boolean autoAck = false;
        channel.basicConsume(TASK_QUEUE_NAME,autoAck,deliverCallback,cancelCallback);
    }
}
