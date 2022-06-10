package com.sheng.consumer;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author 胜大富帅
 * @date 2022/6/10 14:59
 */
@Slf4j
@Component
public class DeadLetterQueueConsumer {
   /**
    * 接收消息
    */
    @RabbitListener(queues = "dead_queue")
    public void receive(Message message, Channel channel)throws Exception{
        String msg = new String(message.getBody());
        log.error("当前时间：{}，收到死信队列的消息：{}",new Date().toString(),msg);
    }
}
