package com.sheng.producer.不公布分发;

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
        // 是否持久化 --->队列持久化
        boolean durable=false;
        // 是否排他性
        boolean exclusive=false;
        // 是否自动删除
        boolean autoDelete=false;
        channel.queueDeclare(QUEUE_NAME,durable,exclusive,autoDelete,null);
        // 发送消息
        int i=7;
        while (i>0){
            String message="test0"+i;
            channel.basicPublish("",QUEUE_NAME,null,message.getBytes("UTF-8"));
            System.out.println("消息发送成功：【"+message+"】");
            i--;
        }
    }
}
