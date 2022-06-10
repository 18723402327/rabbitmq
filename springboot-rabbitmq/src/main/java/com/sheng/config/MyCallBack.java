package com.sheng.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author 胜大富帅
 * @date 2022/6/10 20:28
 */
@Slf4j
@Component
public class MyCallBack implements RabbitTemplate.ConfirmCallback ,RabbitTemplate.ReturnCallback{
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void init(){
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnCallback(this);
    }

    /**
     * 交换机确认回调方法
     * @param correlationData 保存回调消息的ID及相关信息
     * @param ack 交换机是否收到消息
     * @param cause 原因
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String id = correlationData != null ? correlationData.getId() : "";
        if (ack){
            log.error("交换机已接受成功 id:{} 的消息，并返回确认信息",id);
        }else {
            log.error("交换机还未收到 id:{} 的消息，原因：{}",id,cause);
        }
    }

    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        log.error("消息：{}，被交换机：{}退回，退回原因：{}，routingKey:{}",new String(message.getBody()),exchange,replyText,routingKey);
    }
}
