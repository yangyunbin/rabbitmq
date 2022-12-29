package org.example.producer;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


public class ProducerTopic {
    //交换机名称
    static final String TOPIC_EXCHANGE = "topic_exchange";
    //队列名称
    static final String QUEUE_1 = "topic_queue_1";
    //队列名称
    static final String QUEUE_2 = "topic_queue_2";

    public static void main(String[] args) throws IOException, TimeoutException {
        //1.创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //2.设置参数
        factory.setHost("1.117.138.59");
        factory.setPort(5672);
        factory.setVirtualHost("/admin");
        factory.setUsername("admin");
        factory.setPassword("admin");
        //3.创建Connection
        Connection connection = factory.newConnection();
        //4.创建Channel
        Channel channel = connection.createChannel();
        //5.创建交换机
        /*
         * 声明交换机
         * 参数1：交换机名称
         * 参数2：交换机类型，fanout、topic、direct、headers
         */
        channel.exchangeDeclare(TOPIC_EXCHANGE, BuiltinExchangeType.TOPIC);
        //6.创建队列
        /*
         * 参数1：队列名称
         * 参数2：是否定义持久化队列
         * 参数3：是否独占本次连接
         * 参数4：是否在不使用的时候自动删除队列
         * 参数5：队列其它参数
         */
        channel.queueDeclare(QUEUE_1, true, false, false, null);
        channel.queueDeclare(QUEUE_2, true, false, false, null);
        //7.绑定队列和交换机
        channel.queueBind(QUEUE_1, TOPIC_EXCHANGE, "#.error");
        channel.queueBind(QUEUE_1, TOPIC_EXCHANGE, "order.*");
        channel.queueBind(QUEUE_2, TOPIC_EXCHANGE, "*.*");

        String body = "日志信息：张三调用了findAll方法。。。日志级别：info。。。";
        //8.发送消息
        channel.basicPublish(TOPIC_EXCHANGE,"goods.info",null,body.getBytes());
        //9.释放资源
        channel.close();
        connection.close();

    }
}
