package com.example;

import com.rabbitmq.client.AMQP;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.stereotype.Component;

@Component
public class AckListener implements MessageListener {


    @Override
    public void onMessage(Message message) {
        System.out.println(new String(message.getBody()));
    }
}
