package com.example.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_NAME = "test_exchange_confirm";
    public static final String QUEUE_NAME = "test_queue_confirm";

    //1.交换机
    @Bean("test_exchange_confirm")
    public Exchange bootExchange() {
        return ExchangeBuilder.topicExchange(EXCHANGE_NAME).durable(true).build();
    }

    //2.Queue队列
    @Bean("test_queue_confirm")
    public Queue bootQueue() {
        return QueueBuilder.durable(QUEUE_NAME).build();
    }

    //3.队列和交换机绑定
    /*
     * 队列，交换机，routing key
     * */
    @Bean("bindQueueExchange")
    public Binding bindQueueExchange(@Qualifier("test_queue_confirm") Queue queue, @Qualifier("test_exchange_confirm") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("boot.#").noargs();
    }

}
