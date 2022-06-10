package com.sheng.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

/**
 * @author 胜大富帅
 * @date 2022/6/10 17:00
 */
@Configuration
public class DelayedQueueConfig {
    /**
     * 交换机名称
     */
    public static final String EXCHANGE_NAME="delayed_exchange";
    /**
     * 队列名称
     */
    public static final String QUEUE_NAME="delayed_queue";
    /**
     * routingKey
     */
    public static final String ROUTING_KEY="delayed_routing_key";

    /**
     * 声明交换机（自定义交换机）
     */
    @Bean
    public CustomExchange delayedExchange(){
        HashMap<String, Object> arguments = new HashMap<>();
        arguments.put("x-delayed-type","direct");
        return new CustomExchange(EXCHANGE_NAME,"x-delayed-message",false,false,arguments);
    }
    /**
     * 声明队列
     */
    @Bean
    public Queue delayedQueue(){
        return QueueBuilder.nonDurable(QUEUE_NAME).build();
    }
    /**
     * 绑定
     */
    @Bean
    public Binding delayedQueueBindingDelayedExchange(@Autowired Queue delayedQueue,@Autowired Exchange delayedExchange){
        return BindingBuilder.bind(delayedQueue).to(delayedExchange).with(ROUTING_KEY).noargs();
    }
}
