package com.sheng.producer.direct交换机;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.sheng.producer.utils.RabbitMqUtil;

import java.util.Scanner;

/**
 * @author 胜大富帅
 * @date 2022/6/9 17:12
 */
public class Producer01 {
    /**
     * 交换机名称
     */
    private static final String EXCHANGE_NAME = "email_direct";

    private static final Scanner SYS_IN = new Scanner(System.in);

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtil.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        while (true) {
            System.out.println("请输入routingKey：");
            String routingKey = SYS_IN.next();
            if (routingKey.equalsIgnoreCase("exit")) {
                System.exit(0);
            }
            while (SYS_IN.hasNext()) {
                String message = SYS_IN.next();
                if (message.equalsIgnoreCase("exit")) {
                    break;
                }
                channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes());
                System.out.println("生产者向routingKey为：" + routingKey + "发出发出消息：" + message);

            }
        }
    }
}
