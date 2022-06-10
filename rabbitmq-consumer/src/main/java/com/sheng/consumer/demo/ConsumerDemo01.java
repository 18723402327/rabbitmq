package com.sheng.consumer.demo;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.sheng.consumer.utils.RabbitMqUtil;

/**
 * @author 胜大富帅
 * @date 2022/6/6 15:48
 */
public class ConsumerDemo01 {
    /**
     * 队列名称
     */
    private static final String QUEUE_NAME = "work_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtil.getChannel();
        // 消息接收
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("接收到的消息：" + new String(message.getBody()));
        };
        // 消息接收被取消
        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println("消息被消息者取消消费接口回调逻辑");
        };
        // 接收消息
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);
    }
}
