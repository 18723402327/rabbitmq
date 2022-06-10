package com.sheng.producer.死信队列.消息被拒;

import com.rabbitmq.client.Channel;
import com.sheng.producer.utils.RabbitMqUtil;

/**
 * @author 胜大富帅
 * @date 2022/6/9 21:05
 */
public class Producer01 {
    /**
     * 正常交换机名称
     */
    private static final String EXCHANGE_NAME1 = "normal_exchange";

    /**
     * 正常队列名称
     */
    private static final String QUEUE_NAME1 = "normal_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtil.getChannel();

        String routingKey = "normal_routingKey";
        for (int i = 1; i < 11; i++) {
            String message = "info:" + i;
            channel.basicPublish(EXCHANGE_NAME1, routingKey, null, message.getBytes());
        }
    }
}
