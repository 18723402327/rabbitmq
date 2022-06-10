package com.sheng.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 胜大富帅
 * @date 2022/6/10 18:03
 */
@Configuration
public class ConfirmConfig {
    /**
     * 交换机名称
     */
    public static final String EXCHANGE_NAME="confirm_exchange";
    /**
     * 队列名称
     */
    public static final String QUEUE_NAME="confirm_queue";
    /**
     * routingKey
     */
    public static final String ROUTING_KEY="confirm_routing_key";

    /**
     * 交换机声明
     */
    @Bean
    public DirectExchange confirmExchange(){
        return new DirectExchange(EXCHANGE_NAME);
    }
    /**
     * 声明队列
     */
    @Bean
    public Queue confirmQueue(){
        return new Queue(QUEUE_NAME);
    }
    /**
     * 绑定
     */
    @Bean
    public Binding queueBindingExchange(@Autowired Queue confirmQueue,@Autowired DirectExchange confirmExchange){
        return BindingBuilder.bind(confirmQueue).to(confirmExchange).with(ROUTING_KEY);
    }
}
