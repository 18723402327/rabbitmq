package com.sheng.consumer.死信队列.ttl过期;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.sheng.consumer.utils.RabbitMqUtil;

import java.util.HashMap;

/**
 * @author 胜大富帅
 * @date 2022/6/9 20:33
 */
public class Consumer02 {
    /**
     * 死信队列名称
     */
    private static final String QUEUE_NAME2 = "dead_queue";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtil.getChannel();

        System.out.println("consumer02,等待接收消息......");

        boolean autoAck=true;

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("接收到消息：" + new String(message.getBody()));
        };

        // 取消接收消息回调
        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println("消息被消息者取消消费接口回调逻辑");
        };

        channel.basicConsume(QUEUE_NAME2,autoAck,deliverCallback,cancelCallback);

    }
}
