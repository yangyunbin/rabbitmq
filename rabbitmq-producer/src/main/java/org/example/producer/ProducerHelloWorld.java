package org.example.producer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


public class ProducerHelloWorld {

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
        //5.创建队列Queue
        /*
         * 参数1：队列名称
         * 参数2：是否定义持久化队列
         * 参数3：是否独占本次连接
         * 参数4：是否在不使用的时候自动删除队列
         * 参数5：队列其它参数
         */
        channel.queueDeclare("hello_world", true, false, false, null);
        //6.发送消息
        /*
         * 参数1：交换机名称，如果没有指定则使用默认Default Exchange
         * 参数2：路由key,简单模式可以传递队列名称
         * 参数3：消息其它属性
         * 参数4：消息内容
         */
        String body = "hello rabbitmq~~~";
        channel.basicPublish("","hello_world",null,body.getBytes());

        //7.释放资源
        channel.close();
        connection.close();
    }


}
