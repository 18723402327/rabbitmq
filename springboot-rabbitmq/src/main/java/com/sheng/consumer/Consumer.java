package com.sheng.consumer;

import com.sheng.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author 胜大富帅
 * @date 2022/6/10 20:19
 */
@Slf4j
@Component
public class Consumer {
    @RabbitListener(queues = ConfirmConfig.QUEUE_NAME)
    public void receiveConfirmMessage(Message message){
        String msg = new String(message.getBody());
        log.info("接收到消息：{}",msg);
    }
}
