package com.cui.rabbitmq.six;

import com.cui.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * warning
 * info
 * error
 */
public class DirectLogs {

    public static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws Exception {

        Channel channel = RabbitMqUtils.getChannel();

        //从控制台输入数据
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            channel.basicPublish(EXCHANGE_NAME,"warning", null
                    , message.getBytes(StandardCharsets.UTF_8));
            System.out.println("生产者发出消息：" + message);
        }

    }
}
