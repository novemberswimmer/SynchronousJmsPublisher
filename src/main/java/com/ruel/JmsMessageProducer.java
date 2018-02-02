package com.ruel;

import org.apache.activemq.command.ActiveMQQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

@Component
public class JmsMessageProducer {

    private static final Logger logger = LoggerFactory.getLogger(JmsMessageProducer.class);

    protected static final String MESSAGE_COUNT = "messageCount";

    @Autowired
    private ActiveMQQueue destination;

    @Autowired
    private JmsTemplate myJmsTemplate = null;
    private int messageCount = 11;

    /**
     * Generates JMS messages
     */
    @PostConstruct
    public void generateMessages() throws JMSException {
        for (int i = 0; i < messageCount; i++) {
            final int index = i;
            final String text = "Remote Message number is " + i + ".";

            myJmsTemplate.send(new MessageCreator() {
                public Message createMessage(Session session) throws JMSException {
                    TextMessage message = session.createTextMessage(text);
                   // message.setIntProperty(MESSAGE_COUNT, index);
                    message.setJMSCorrelationID("Ruel-"+ index);
                    System.out.println("Sending message: " + text + "Correlation="+ message.getJMSCorrelationID());

                    return message;
                }
            });

        }


        String message = null;

        while (message == null) {
            message = (String) myJmsTemplate.
                    receiveSelectedAndConvert(destination, "JMSCorrelationID='Ruel-10'");

            System.out.println(message);
        }


    }

}
