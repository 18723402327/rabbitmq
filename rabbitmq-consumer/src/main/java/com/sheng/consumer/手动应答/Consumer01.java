package com.sheng.consumer.手动应答;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.sheng.consumer.utils.RabbitMqUtil;

import java.util.concurrent.TimeUnit;

/**
 * @author 胜大富帅
 * @date 2022/6/6 15:48
 */
public class Consumer01 {
    /**
     * 队列名称
     */
    private static final String QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtil.getChannel();
        System.out.println("C1等待接收消息，处理消息时间较短");
        // 消息接收
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            // 是否批量
            boolean multiple=false;
            channel.basicAck(message.getEnvelope().getDeliveryTag(),multiple);
            System.out.println("接收到的消息：" + new String(message.getBody()));
        };
        // 接收消息
        // 是否自动应答
        boolean autoAck=false;
        channel.basicConsume(QUEUE_NAME, autoAck, deliverCallback, (consumerTag) -> System.out.println("消息被消息者取消消费接口回调逻辑"));
    }
}
