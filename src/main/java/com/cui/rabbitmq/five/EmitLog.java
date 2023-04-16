package com.cui.rabbitmq.five;

import com.cui.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * 发送消息
 */
public class EmitLog {

    public static final String EXCHANGE_NAME = "log1";

    public static void main(String[] args) throws Exception {

        Channel channel = RabbitMqUtils.getChannel();

        //从控制台输入数据
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            channel.basicPublish(EXCHANGE_NAME,"", null
                    , message.getBytes(StandardCharsets.UTF_8));
            System.out.println("生产者发出消息：" + message);
        }

    }
}
