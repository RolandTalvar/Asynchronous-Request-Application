package com.rolandtalvar.asyncapplication.client;

import org.apache.log4j.Logger;

import javax.jms.*;
import java.math.BigDecimal;


public class FactoryProducer {
	private static final Logger factoryProducerLog = Logger.getLogger(FactoryProducer.class);
	private String MESSAGES_FROM_FACTORY = "FactoryMessageQueue";
	Session session;
	MessageProducer producer;

	private long timeToLive = 1000000;

	public FactoryProducer(Session session) {
		this.session = session;
	}

	protected void sendAnswer(BigDecimal totalSumOfOrder, long totalNumberOfProducts) throws Exception {

		TextMessage message = session
				.createTextMessage("We have received your order! Total sum of order is: "
						+ totalSumOfOrder + " for " + totalNumberOfProducts + " products.");


		Destination destinationToShop = session.createQueue(MESSAGES_FROM_FACTORY);
		producer = session.createProducer(destinationToShop);
		producer.setTimeToLive(timeToLive);
		producer.send(message);
		factoryProducerLog.debug("Message sent: " + message.getText());
	}


}