package com.sheng.consumer;

import com.sheng.config.DelayedQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author 胜大富帅
 * @date 2022/6/10 17:25
 */
@Component
@Slf4j
public class DelayedQueueConsumer {
    /**
     * 监听消息
     */
    @RabbitListener(queues = DelayedQueueConfig.QUEUE_NAME)
    public void receiveDelayedQueue(Message message){
        String msg = new String(message.getBody());
        log.info("当前时间：{}，收到延迟队列的消息：{}",new Date(),msg);
    }
}
