package com.cui.rabbitmq.four;

import com.cui.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;

import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * 发布确认模式
 *使用的时间 比较哪种确认方式是最好的
 * 1.单个确认
 * 2.批量确认
 * 3.异步批量确认
 */
public class ConfirmMessage {

    //批量发消息的个数
    public static final int MESSAGE_COUNT = 1000;

    public static void main(String[] args) throws Exception {

        //1.单个确认（同步）    发布1000个单独确认消息，耗时：590ms
        //publishMessageIndiviDually();

        //2.批量确认（同步）    发布1000个批量确认消息，耗时：94ms
        //publishMessageBatch();

        //3.异步批量确认    发布1000个批量确认消息，耗时：23ms
        publishMessageAsync();

    }

    //单个确认
    public static void publishMessageIndiviDually() throws Exception {

        //创建信道
        Channel channel = RabbitMqUtils.getChannel();

        //声明队列
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName,true,false,false,null);

        //开启发布确认
        channel.confirmSelect();

        //开始时间
        long startTime = System.currentTimeMillis();

        //批量发送消息  单个发布确认
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("",queueName,null,message.getBytes());
            //单个消息就马上进行发布确认
            boolean flag = channel.waitForConfirms();
            if (flag) {
                System.out.println("消息发送成功");
            }
        }

        //结束时间
        long endTime = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "个单独确认消息，耗时：" + (endTime - startTime) + "ms");
    }

    //批量确认
    public static void publishMessageBatch() throws Exception {
        //创建信道
        Channel channel = RabbitMqUtils.getChannel();

        //声明队列
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName,true,false,false,null);

        //开启发布确认
        channel.confirmSelect();

        //开始时间
        long startTime = System.currentTimeMillis();

        //批量发送消息  批量发布确认
        int batchsize = 100;
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("",queueName,null,message.getBytes());
            //消息达到100的时候，就进行一次发布确认
            if (i % batchsize == 0) {
                //发布确认
                channel.waitForConfirms();
            }
        }

        //结束时间
        long endTime = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "个批量确认消息，耗时：" + (endTime - startTime) + "ms");
    }

    //异步发布确认
    public static void publishMessageAsync() throws Exception {

        //创建信道
        Channel channel = RabbitMqUtils.getChannel();

        //声明队列
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName,true,false,false,null);

        //开启发布确认
        channel.confirmSelect();

        /*
          线程安全有序的一个哈希表 适用于高并发的场景下
          1.轻松的将消息与序号关联起来
          2.轻松批量删除条目 给出序号即可
          3.支持高并发（多线程）
         */
        ConcurrentSkipListMap<Long,String> concurrentSkipListMap = new ConcurrentSkipListMap<>();

        //消息确认成功 回调函数
        ConfirmCallback ackCallback = (deliveryTag,multiple) -> {
            //@2删除已经确认的消息 剩下的就是未确认的消息
            if (multiple) {
                ConcurrentNavigableMap<Long, String> confrirmed =
                        concurrentSkipListMap.headMap(deliveryTag);
                confrirmed.clear();
            }else {
                concurrentSkipListMap.remove(deliveryTag);
            }

            System.out.println("确认的消息：" + deliveryTag);
        };

        /*
          消息确认失败 回调函数
          1.消息的标记
          2.是否批量确认
         */
        ConfirmCallback nackCallback1 = (deliveryTag,multiple) -> {
            //@3打印一下未确认的消息都有哪些
            String message = concurrentSkipListMap.get(deliveryTag);
            System.out.println("未确认的消息是：" + message + ":::未确认的消息tag：" + deliveryTag);
        };

        /*
          准备消息的监听器 哪些消息发送成功 哪些消息发送失败
         */
        channel.addConfirmListener(ackCallback,nackCallback1);   //异步通知

        //开始时间
        long startTime = System.currentTimeMillis();

        //批量发送消息  批量发布确认
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message =  "消息" + i;
            channel.basicPublish("",queueName,null,message.getBytes());
            //@1此处记录下所有要发布的消息 消息总和
            concurrentSkipListMap.put(channel.getNextPublishSeqNo(),message);
        }

        //结束时间
        long endTime = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "个批量确认消息，耗时：" + (endTime - startTime) + "ms");
    }
}
