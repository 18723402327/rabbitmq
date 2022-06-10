package com.sheng.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

/**
 * @author 胜大富帅
 * @date 2022/6/10 12:25
 */
@Configuration
public class TTLQueueConfig {
    /**
     * 普通交换机名称
     */
    public static final String EXCHANGE_NAME1 = "ordinary_exchange";
    /**
     * 死信交换机名称
     */
    public static final String EXCHANGE_NAME2 = "dead_letter_exchange";
    /**
     * 普通队列名称
     */
    public static final String QUEUE_NAME1 = "ordinary_queue_1";
    public static final String QUEUE_NAME2 = "ordinary_queue_2";
    /**
     * 死信队列名称
     */
    public static final String QUEUE_NAME3 = "dead_queue";

    /**
     * 声明交换机
     */
    @Bean
    public DirectExchange ordinary_exchange() {
        return new DirectExchange(EXCHANGE_NAME1);
    }

    /**
     * 声明死信交换机
     */
    @Bean
    public DirectExchange dead_letter_exchange() {
        return new DirectExchange(EXCHANGE_NAME2);
    }

    /**
     * 声明队列1,TTL为10s
     */
    @Bean
    public Queue ordinary_queue_1(){
        HashMap<String, Object> arguments = new HashMap<>(3);
        // 设置死信交换机
        arguments.put("x-dead-letter-exchange",EXCHANGE_NAME2);
        // 设置死信routingKey
        String routingKey="dead_routingKey";
        arguments.put("x-dead-letter-routing-key",routingKey);
        // 设置ttl
        arguments.put("x-message-ttl",10000);

        return QueueBuilder.nonDurable(QUEUE_NAME1).withArguments(arguments).build();
    }

    /**
     * 声明队列2,TTL为40s
     */
    @Bean
    public Queue ordinary_queue_2(){
        HashMap<String, Object> arguments = new HashMap<>();
        // 设置死信交换机
        arguments.put("x-dead-letter-exchange",EXCHANGE_NAME2);
        // 设置死信routingKey
        String routingKey="dead_routingKey";
        arguments.put("x-dead-letter-routing-key",routingKey);
        // 设置ttl
        arguments.put("x-message-ttl",40000);

        return QueueBuilder.nonDurable(QUEUE_NAME2).withArguments(arguments).build();
    }

    /**
     * 声明死信队列
     */
    @Bean
    public Queue dead_queue(){
        return QueueBuilder.nonDurable(QUEUE_NAME3).build();
    }

    /**
     * 绑定
     */
    @Bean
    public Binding queue1BindingExchange1(@Autowired Queue ordinary_queue_1,@Autowired DirectExchange ordinary_exchange){
        String routingKey="ordinary_routingKey_1";
        return BindingBuilder.bind(ordinary_queue_1).to(ordinary_exchange).with(routingKey);
    }

    @Bean
    public Binding queue2BindingExchange1(@Autowired Queue ordinary_queue_2,@Autowired DirectExchange ordinary_exchange){
        String routingKey="ordinary_routingKey_2";
        return BindingBuilder.bind(ordinary_queue_2).to(ordinary_exchange).with(routingKey);
    }

    @Bean
    public Binding queue3BindingExchange2(@Autowired Queue dead_queue,@Autowired DirectExchange dead_letter_exchange){
        String routingKey="dead_routingKey";
        return BindingBuilder.bind(dead_queue).to(dead_letter_exchange).with(routingKey);
    }

    /*
        延迟队列优化
            1.新增普通队列
     */

    public static final String QUEUE_NAME4 = "ordinary_queue_3";

    @Bean
    public Queue ordinary_queue_3(){
        HashMap<String, Object> arguments = new HashMap<>(3);
        // 设置死信交换机
        arguments.put("x-dead-letter-exchange",EXCHANGE_NAME2);
        // 设置死信routingKey
        String routingKey="dead_routingKey";
        arguments.put("x-dead-letter-routing-key",routingKey);
        return QueueBuilder.nonDurable(QUEUE_NAME4).withArguments(arguments).build();
    }

    @Bean
    public Binding queue4BindingExchange1(@Autowired Queue ordinary_queue_3,@Autowired DirectExchange ordinary_exchange){
        String routingKey="ordinary_routingKey_3";
        return BindingBuilder.bind(ordinary_queue_3).to(ordinary_exchange).with(routingKey);
    }
}

