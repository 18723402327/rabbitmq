package com.sheng.producer.fanout交换机;

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
    private static final String EXCHANGE_NAME = "email";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtil.getChannel();
        String type = "fanout";
        channel.exchangeDeclare(EXCHANGE_NAME,type);
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String message = scanner.next();
            String routingKey = "consumer01";
            channel.basicPublish(EXCHANGE_NAME,routingKey,null,message.getBytes());
            System.out.println("生产者发出消息："+message);
        }
    }
}
