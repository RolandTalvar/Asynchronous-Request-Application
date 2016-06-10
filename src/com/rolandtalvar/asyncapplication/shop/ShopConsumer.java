package com.rolandtalvar.asyncapplication.shop;

import com.rolandtalvar.asyncapplication.Broker;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;

import javax.jms.*;


public class ShopConsumer {

	public static final String MESSAGES_FROM_FACTORY = "FactoryMessageQueue";
	private static final Logger shopConsumerLog = Logger.getLogger(ShopConsumer.class);

	public static void main(String[] args) {
		ShopConsumer shopConsumer = new ShopConsumer();
		shopConsumer.run();
	}

	public void run() {
		Connection connection;

		try {
			shopConsumerLog.info("Started shop.");

			ActiveMQConnectionFactory connectionFactory =
					new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER,
							ActiveMQConnection.DEFAULT_PASSWORD, Broker.URL);

			connection = connectionFactory.createConnection();
			connection.start();
			shopConsumerLog.info("Connected Shop to: " + Broker.URL);

			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			Destination destinationToShop = session.createQueue(MESSAGES_FROM_FACTORY);
			MessageConsumer consumer = session.createConsumer(destinationToShop);
			consumer.setMessageListener(new shopMessageListener());

			ShopProducer shopProducer = new ShopProducer(session);

			Thread.sleep(1000);

			shopProducer.sendOrder();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	class shopMessageListener implements javax.jms.MessageListener {

		public void onMessage(Message message) {
			try {
				if (message instanceof TextMessage) {
					TextMessage textMessage = (TextMessage) message;
					String textMessageText = textMessage.getText();
					Thread.sleep(1000);
					shopConsumerLog.info("Received text message: " + textMessageText);
				} else if (message instanceof ObjectMessage) {
					ObjectMessage objectMessage = (ObjectMessage) message;
					String objectMessageText = objectMessage.getObject().toString();
					shopConsumerLog.info("Received text message: " + objectMessageText);
				} else {
					shopConsumerLog.info("Received other message: " + message);
				}

			} catch (JMSException e) {
				shopConsumerLog.warn("Error: " + e);
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}


