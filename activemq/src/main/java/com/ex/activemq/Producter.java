package com.ex.activemq;


import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Producter {
    private static final String USERNAME= ActiveMQConnection.DEFAULT_USER;
    private static final String PASSWORD=ActiveMQConnection.DEFAULT_PASSWORD;
    private static final String BROKEN_URL=ActiveMQConnection.DEFAULT_BROKER_URL;

    AtomicInteger count= new AtomicInteger(0);

    ConnectionFactory connectionFactory;

    Connection connection;

    Session session;
    ThreadLocal<MessageProducer> threadLocal = new ThreadLocal<>();

    public void init(){
        try {
            connectionFactory=new ActiveMQConnectionFactory(USERNAME,PASSWORD,BROKEN_URL);
            connection=connectionFactory.createConnection();
            connection.start();
            session=connection.createSession(true,session.SESSION_TRANSACTED);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String disname){
        try {
            Queue queue =session.createQueue(disname);
            MessageProducer messageProducer=null;
            if (threadLocal.get()!=null){
                messageProducer=threadLocal.get();
            }else{
                messageProducer=session.createProducer(queue);
                threadLocal.set(messageProducer);
            }
            while (true){
                Thread.sleep(1000);
                int num = count.getAndIncrement();
                TextMessage msg = session.createTextMessage(Thread.currentThread().getName()+
                        "productor:我是大帅哥，我现在正在生产东西！,count:"+num);
                System.out.println(Thread.currentThread().getName()+
                        "productor:我是大帅哥，我现在正在生产东西！,count:"+num);
                messageProducer.send(msg);
                session.commit();
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}
