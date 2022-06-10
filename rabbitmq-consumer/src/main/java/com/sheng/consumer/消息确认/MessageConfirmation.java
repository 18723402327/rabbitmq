package com.sheng.consumer.消息确认;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.sheng.consumer.utils.RabbitMqUtil;

import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @author 胜大富帅
 * @date 2022/6/7 14:04
 */
public class MessageConfirmation {
    /**
     * 批量发消息个数
     */
    private static final int MESSAGE_COUNT = 1000;
    /**
     * 队列名称
     */
    private static final String QUEUE_NAME = "confirmation_message_queue";

    /**
     * 发布确认模式
     * 1.单个确认
     * 2.批量确认
     * 3.异步批量确认
     *
     * @param args
     */
    public static void main(String[] args) throws Exception {
        // 1.单个确认 (163ms)
        MessageConfirmation.publishMessageIndividually();
        // 2.批量确认 (62ms)
        MessageConfirmation.publishMessageBatch();
        // 3.异步批量确认（39ms）
        MessageConfirmation.publishMessageAsync();
    }

    /**
     * 单个确认
     */
    public static void publishMessageIndividually() throws Exception {
        Channel channel = RabbitMqUtil.getChannel();
        // 声明队列
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        // 开启发布确认
        channel.confirmSelect();
        // 开始时间
        long beginTime = System.currentTimeMillis();

        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = "test_" + i;
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            // 消息确认
            boolean flag = channel.waitForConfirms();
            if (flag) {
                System.out.println("消息发送成功！");
            }
        }
        // 开始时间
        long endTime = System.currentTimeMillis();
        System.out.println("用时花费" + (endTime - beginTime));
    }

    // 2.批量确认
    public static void publishMessageBatch() throws Exception {
        Channel channel = RabbitMqUtil.getChannel();
        // 声明队列
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        // 开启发布确认
        channel.confirmSelect();
        // 批量确认消息大小
        int batchSize = 10;

        // 开始时间
        long beginTime = System.currentTimeMillis();
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = "test_" + i;
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            // 判断消息发送达到100条时，批量确认一次
            if ((i + 1) % batchSize == 0) {
                // 消息确认
                boolean flag = channel.waitForConfirms();
                if (flag) {
                    System.out.println("消息发送成功！");
                }
            }
        }
        // 开始时间
        long endTime = System.currentTimeMillis();
        System.out.println("用时花费" + (endTime - beginTime));
    }

    // 3.异步批量确认
    public static void publishMessageAsync() throws Exception {
        Channel channel = RabbitMqUtil.getChannel();
        // 声明队列
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        // 开启发布确认
        channel.confirmSelect();
        ConcurrentSkipListMap<Long,String> concurrentSkipListMap=new ConcurrentSkipListMap<>();
        // 开始时间
        long beginTime = System.currentTimeMillis();
        // 消息确认成功，回跳函数
        ConfirmCallback ackCallback = (deliveryTag, multiple) -> {
            if (multiple){
                ConcurrentNavigableMap<Long, String> headMap = concurrentSkipListMap.headMap(deliveryTag);
                headMap.clear();
            }else {
                concurrentSkipListMap.remove(deliveryTag);
            }
            System.out.println("确认消息：" + deliveryTag);
        };
        // 消息确认失败，回跳函数
        ConfirmCallback nackCallback = (deliveryTag, multiple) -> {
            System.out.println("未确认消息：" + deliveryTag);
        };
        channel.addConfirmListener(ackCallback, nackCallback);
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = "test_" + i;
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            concurrentSkipListMap.put(channel.getNextPublishSeqNo(),message);
        }
        // 结束时间
        long endTime = System.currentTimeMillis();
        System.out.println("用时花费" + (endTime - beginTime));
    }
}
