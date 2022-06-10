package com.sheng.controller;

import com.sheng.config.ConfirmConfig;
import com.sheng.config.DelayedQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.UUID;

/**
 * @author 胜大富帅
 * @date 2022/6/10 13:13
 * <p>
 * 发送延迟消息
 */
@Slf4j
@RestController
@RequestMapping("/ttl")
public class SendMessageController {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/sendMessage/{message}")
    public void sendMessage(@PathVariable("message") String message) {
        log.error("当前时间：{},发送一条信息【{}】,给两个队列", new Date().toString(), message);
        rabbitTemplate.convertAndSend("ordinary_exchange", "ordinary_routingKey_1", "消息来自ttl为10s的队列：" + message);
        rabbitTemplate.convertAndSend("ordinary_exchange", "ordinary_routingKey_2", "消息来自ttl为10s的队列：" + message);
    }

    @GetMapping("/sendExpirationMessage/{message}/{ttlTime}")
    public void sendMessage(@PathVariable("message") String message, @PathVariable("ttlTime") String ttlTime) {
        log.error("当前时间：{},发送一条时长{}毫秒ttl信息【{}】,给队列", new Date().toString(), ttlTime, message);
        rabbitTemplate.convertAndSend("ordinary_exchange", "ordinary_routingKey_3", message, msg -> {
            log.debug(ttlTime);
            msg.getMessageProperties().setExpiration(ttlTime);
            return msg;
        });
    }

    @GetMapping("/sendDelayedMessage/{message}/{delayedTime}")
    public void sendMessage(@PathVariable("message") String message, @PathVariable("delayedTime") Integer delayedTime) {
        log.error("当前时间：{},发送一条时长{}毫秒ttl信息【{}】,给队列", new Date().toString(), delayedTime, message);
        rabbitTemplate.convertAndSend(DelayedQueueConfig.EXCHANGE_NAME, DelayedQueueConfig.ROUTING_KEY, message, msg -> {
            msg.getMessageProperties().setDelay(delayedTime);
            return msg;
        });
    }

    @GetMapping("/confirm/sendMessage/{message}")
    public void sendMessageAndConfirm(@PathVariable("message") String message) {
        log.error("当前时间：{},发送一条信息【{}】,给队列", new Date().toString(), message);
        String id = String.valueOf(UUID.randomUUID());
        CorrelationData correlationData = new CorrelationData(id);
        rabbitTemplate.convertAndSend(ConfirmConfig.EXCHANGE_NAME,ConfirmConfig.ROUTING_KEY+"123", message,correlationData);
    }
}
