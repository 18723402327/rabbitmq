package com.sheng.producer.work_queue;

import com.rabbitmq.client.Channel;
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
    private static final String QUEUE_NAME="work_queue";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtil.getChannel();
        // 创建队列Queue
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        // 发送消息
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String message=scanner.next();
            channel.basicPublish("",QUEUE_NAME,null,message.getBytes("UTF-8"));
            System.out.println("消息发送成功：【"+message+"】");
        }
    }
}
