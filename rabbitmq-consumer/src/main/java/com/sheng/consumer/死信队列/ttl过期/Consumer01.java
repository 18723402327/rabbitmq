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
public class Consumer01 {
    /**
     * 正常交换机名称
     */
    private static final String EXCHANGE_NAME1 = "normal_exchange";

    /**
     * 死信交换机名称
     */
    private static final String EXCHANGE_NAME2 = "dead_exchange";

    /**
     * 正常队列名称
     */
    private static final String QUEUE_NAME1 = "normal_queue";

    /**
     * 死信队列名称
     */
    private static final String QUEUE_NAME2 = "dead_queue";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtil.getChannel();
        // 声明正常和死信交换机，类型为direct
        channel.exchangeDeclare(EXCHANGE_NAME1, BuiltinExchangeType.DIRECT);

        channel.exchangeDeclare(EXCHANGE_NAME2, BuiltinExchangeType.DIRECT);

        // 是否持久化
        boolean durable=false;
        // 是否排他
        boolean exclusive=false;
        // 是否自动删除
        boolean autoDelete=false;

        // 声明正常队列
        HashMap<String, Object> arguments = new HashMap<>();
        // 普通队列设置死信队列
        arguments.put("x-dead-letter-exchange",EXCHANGE_NAME2);
        // 设置死信routingKey
        String routingKey1="dead_routingKey";
        arguments.put("x-dead-letter-routing-key",routingKey1);

        channel.queueDeclare(QUEUE_NAME1,durable,exclusive,autoDelete,arguments);
        // 声明死信队列
        channel.queueDeclare(QUEUE_NAME2,durable,exclusive,autoDelete,null);

        // 普通交换机和普通队列绑定
        String routingKey2="normal_routingKey";
        channel.queueBind(QUEUE_NAME1,EXCHANGE_NAME1,routingKey2);

        // 死信交换机和普通队列绑定
        channel.queueBind(QUEUE_NAME2,EXCHANGE_NAME2,routingKey1);

        System.out.println("consumer01,等待接收消息......");

        boolean autoAck=true;

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("接收到消息：" + new String(message.getBody()));
        };

        // 取消接收消息回调
        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println("消息被消息者取消消费接口回调逻辑");
        };

        channel.basicConsume(QUEUE_NAME1,autoAck,deliverCallback,cancelCallback);

    }
}
