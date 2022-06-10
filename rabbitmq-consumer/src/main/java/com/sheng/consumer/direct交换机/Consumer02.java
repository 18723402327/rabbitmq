package com.sheng.consumer.direct交换机;

import com.rabbitmq.client.BuiltinExchangeType;
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
    private static final String EXCHANGE_NAME = "email_direct";

    /**
     * 队列名称
     */
    private static final String QUEUE_NAME = "email_direct_consumer02";

    public static void main(String[] args) throws Exception {

        Channel channel = RabbitMqUtil.getChannel();
        // 1.声明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        // 声明一个队列
        // 是否持久化
        boolean durable=false;
        // 是否排他
        boolean exclusive=false;
        // 是否自动删除
        boolean autoDelete=false;
        channel.queueDeclare(QUEUE_NAME,durable,exclusive,autoDelete,null);
        // 队列绑定
        String routingKey = "consumer01_2";
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, routingKey);
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
        channel.basicConsume(QUEUE_NAME, autoAck, deliverCallback, cancelCallback);
    }
}
