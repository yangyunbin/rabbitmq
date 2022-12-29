package com.example;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ProducerTest {


    @Autowired
    private RabbitTemplate rabbitTemplate;


    /**
     * 确认模式步骤:
     * 步骤：
     * 1.确认模式开启
     * 2.在rabbitTemplate定义ConfirmCallback回调函数
     */
    @Test
    public void testConfirm() {
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                System.out.println("confirm方法被执行");

                if (ack) {
                    //接收成功
                    System.out.println("接收消息成功" + cause);
                } else {
                    //接收失败
                    System.out.println("接收消息成功" + cause);
                    //做一些处理，让消息再次发送
                }
            }
        });

        //3.发送消息
        rabbitTemplate.convertAndSend("test_exchange_confirm111", "confirm", "message confirm...");
    }


    /**
     * 回退模式：当消息发送给Exchange后，Exchange路由到Queue失败时才会执行ReturnCallBack
     * 1.开启回退模式。
     * 2.设置ReturnCallBack
     * 3.设置Exchange
     */
    @Test
    public void testReturn() {

        //设置交换机处理消息失败的模式
        rabbitTemplate.setMandatory(true);

        rabbitTemplate.setReturnsCallback(new RabbitTemplate.ReturnsCallback() {
            @Override
            public void returnedMessage(ReturnedMessage returnedMessage) {
                System.out.println("return 执行了。。。。");
                System.out.println(returnedMessage);
            }
        });

        //3.发送消息
        rabbitTemplate.convertAndSend("test_exchange_confirm", "confirm", "message confirm...");
    }
}
