package com.ruel;

import org.springframework.jms.core.MessageCreator;

import javax.jms.*;

public class MyMessageCreator implements MessageCreator {

    private String message;
    private Queue responseQueue;
    private TextMessage textMessage;

    private MyMessageCreator(){

    }

    public MyMessageCreator(String message, Queue responseQueue){
        this.message = message;
        this.responseQueue = responseQueue;
    }

    public TextMessage getTextMessage() {
        return textMessage;
    }

    public Message createMessage(Session session) throws JMSException {
        textMessage = session.createTextMessage();
        textMessage.setJMSReplyTo(responseQueue);
        textMessage.setText(message);
        return textMessage;
    }
}
