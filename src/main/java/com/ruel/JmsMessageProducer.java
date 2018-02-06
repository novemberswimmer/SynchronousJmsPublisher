package com.ruel;

import org.apache.activemq.command.ActiveMQQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.jms.JMSException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JmsMessageProducer {

    private static final Logger logger = LoggerFactory.getLogger(JmsMessageProducer.class);

    @Autowired
    private ActiveMQQueue destination;

    @Autowired
    private ActiveMQQueue responseQueue;

    @Autowired
    private JmsTemplate myJmsTemplate = null;
    private int messageCount = 11;

    @PostConstruct
    public void generateMessages() throws JMSException {
        String lastMessageId=null;
        List<String> listOfMessageId = new ArrayList();

        for (int i = 0; i < messageCount; i++) {
            final int index = i;
            final String text = "Remote Message number is " + i + ".";

            MyMessageCreator myMessageCreator = new MyMessageCreator(text, responseQueue);

            myJmsTemplate.send(myMessageCreator);

            lastMessageId = myMessageCreator.getTextMessage().getJMSMessageID();
            listOfMessageId.add(lastMessageId);
        }


        String message = null;

        while (message == null) {
            String messageSelector = "JMSCorrelationID='" + listOfMessageId.get(5) + "'";
            message = (String) myJmsTemplate.
                    receiveSelectedAndConvert(responseQueue, messageSelector);

            logger.info(message);
        }


    }

}
