package com.sheng.consumer.手动应答;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.sheng.consumer.utils.RabbitMqUtil;

import java.util.concurrent.TimeUnit;

/**
 * @author 胜大富帅
 * @date 2022/6/6 15:48
 */
public class Consumer02 {
    /**
     * 队列名称
     */
    private static final String QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtil.getChannel();
        System.out.println("C2等待接收消息，处理消息时间较长");
        // 消息接收
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
            System.out.println("接收到的消息：" + new String(message.getBody()));
        };
        // 消息接收被取消
        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println("消息被消息者取消消费接口回调逻辑");
        };
        // 接收消息
        channel.basicConsume(QUEUE_NAME, false, deliverCallback, cancelCallback);
    }
}
