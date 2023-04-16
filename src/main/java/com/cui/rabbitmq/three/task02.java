package com.cui.rabbitmq.three;

import com.cui.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

import java.util.Scanner;

/**
 * 消息在手动应答时时不丢失，放回队列中重新消费
 */
public class task02 {

    //队列名称
    public static final String TASK_QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws Exception{

        //创建信道
        Channel channel = RabbitMqUtils.getChannel();

        //创建队列
        boolean durable = true; //需要让queue进行持久化
        channel.queueDeclare(TASK_QUEUE_NAME,durable,false,false,null);

        //从控制台输入数据
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();

            //设置生产者发送消息为持久化消息（要求保存到磁盘上），默认保存在内存中
            channel.basicPublish("",TASK_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN
                    , message.getBytes("UTF-8"));
            System.out.println("生产者发出消息：" + message);
        }

    }
}
