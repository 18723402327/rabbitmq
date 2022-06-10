package com.sheng.producer.手动应答;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import com.sheng.producer.utils.RabbitMqUtil;

import java.util.Scanner;

/**
 * @author 胜大富帅
 * @date 2022/6/6 15:25
 */
public class Producer01 {
    /**
     * 队列名称
     */
    private static final String QUEUE_NAME="ack_queue";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtil.getChannel();
        // 创建队列Queue
        // 是否持久化 --->队列持久化
        boolean durable=false;
        // 是否排他性
        boolean exclusive=false;
        // 是否自动删除
        boolean autoDelete=false;
        channel.queueDeclare(QUEUE_NAME,durable,exclusive,autoDelete,null);
        // 发送消息
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String message=scanner.next();
            /**
             * MessageProperties.PERSISTENT_TEXT_PLAIN -> 消息属性：持久化文本 --->消息持久化
             */
            channel.basicPublish("",QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN,message.getBytes("UTF-8"));
            System.out.println("消息发送成功：【"+message+"】");
        }
    }
}
