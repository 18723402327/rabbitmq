package com.sheng.consumer.fanout交换机;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.sheng.consumer.utils.RabbitMqUtil;

/**
 * @author 胜大富帅
 * @date 2022/6/9 17:12
 */
public class Consumer02 {
    /**
     * 交换机名称
     */
    private static final String EXCHANGE_NAME = "email";

    public static void main(String[] args) throws Exception {

        Channel channel = RabbitMqUtil.getChannel();
        // 1.声明一个交换机
        // 1.1交换机类型
        String type = "fanout";
        channel.exchangeDeclare(EXCHANGE_NAME, type);
        /*
         * 声明一个临时队列，队列名称是随机的
         * 当消费者断开与队列的连接时，队列自动删除
         * */
        String queueName = channel.queueDeclare().getQueue();
        // 队列绑定
        String routingKey = "consumer02";
        channel.queueBind(queueName, EXCHANGE_NAME, routingKey);
        System.out.println("Consumer02,等待接收消息......");
        // 是否自动应答
        boolean autoAck = true;

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("接收到消息：" + new String(message.getBody()));
        };
        // 取消接收消息回调
        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println("消息被消息者取消消费接口回调逻辑");
        };
        channel.basicConsume(queueName, autoAck, deliverCallback, cancelCallback);
    }
}
