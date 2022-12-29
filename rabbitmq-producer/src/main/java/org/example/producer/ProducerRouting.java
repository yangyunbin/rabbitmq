package org.example.producer;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


public class ProducerRouting {
    //交换机名称
    static final String DIRECT_EXCHANGE = "direct_exchange";
    //队列名称
    static final String DIRECT_QUEUE_INSERT = "direct_queue_insert";
    //队列名称
    static final String DIRECT_QUEUE_UPDATE = "direct_queue_update";

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
        channel.exchangeDeclare(DIRECT_EXCHANGE, BuiltinExchangeType.DIRECT);
        //6.创建队列
        /*
         * 参数1：队列名称
         * 参数2：是否定义持久化队列
         * 参数3：是否独占本次连接
         * 参数4：是否在不使用的时候自动删除队列
         * 参数5：队列其它参数
         */
        channel.queueDeclare(DIRECT_QUEUE_INSERT, true, false, false, null);
        channel.queueDeclare(DIRECT_QUEUE_UPDATE, true, false, false, null);
        //7.绑定队列和交换机
        channel.queueBind(DIRECT_QUEUE_INSERT, DIRECT_EXCHANGE, "insert");
        channel.queueBind(DIRECT_QUEUE_UPDATE, DIRECT_EXCHANGE, "update");

        String body = "新增了商品。路由模式；routing key 为 insert " ;
        //8.发送消息
        channel.basicPublish(DIRECT_EXCHANGE,"insert",null,body.getBytes());
        System.out.println("已发送消息：" + body);

        // 要发送的信息
        String message = "修改了商品。路由模式；routing key 为 update " ;
        /**
         * 参数1：交换机名称，如果没有指定则使用默认Default Exchange
         * 参数2：路由key,简单模式可以传递队列名称
         * 参数3：消息其它属性
         * 参数4：消息内容
         */
        channel.basicPublish(DIRECT_EXCHANGE, "update", null, message.getBytes());
        System.out.println("已发送消息：" + message);
        //9.释放资源
        channel.close();
        connection.close();

    }
}
