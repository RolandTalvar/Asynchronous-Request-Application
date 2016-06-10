package com.rolandtalvar.asyncapplication.client;

import com.rolandtalvar.asyncapplication.Broker;
import com.rolandtalvar.asyncapplication.model.Order;
import com.rolandtalvar.asyncapplication.model.OrderRow;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;
import org.apache.log4j.or.ThreadGroupRenderer;

import javax.jms.*;
import java.math.BigDecimal;
import java.util.List;


public class FactoryConsumer {
	private static final Logger factoryConsumerLog = Logger.getLogger(FactoryConsumer.class);
	private String MESSAGES_FROM_SHOP = "shopMessageQueue";
	private Session session;
	private FactoryProducer factoryProducer;

	public static void main(String[] args) {
		FactoryConsumer factoryConsumer = new FactoryConsumer();
		factoryConsumer.run();
	}

	public void run() {
		Connection connection;

		try {
			factoryConsumerLog.info("Started factory");

			ActiveMQConnectionFactory connectionFactory =
					new ActiveMQConnectionFactory(
							ActiveMQConnection.DEFAULT_USER,
							ActiveMQConnection.DEFAULT_PASSWORD, Broker.URL);

			connection = connectionFactory.createConnection();
			connection.setExceptionListener(new ExceptionListenerImpl());
			connection.start();
			factoryConsumerLog.info("Connected Factory to " + Broker.URL);

			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			Destination destinationtoFactory = session.createQueue(MESSAGES_FROM_SHOP);
			MessageConsumer consumer = session.createConsumer(destinationtoFactory);
			consumer.setMessageListener(new MessageListenerImpl());

			factoryProducer = new FactoryProducer(session);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	class MessageListenerImpl implements javax.jms.MessageListener {

		public void onMessage(Message message) {
			try {
				if (message instanceof TextMessage) {
					TextMessage textMessage = (TextMessage) message;
					String textMessageText = textMessage.getText();
					factoryConsumerLog.info("Received text message: " + textMessageText);

				} else if (message instanceof ObjectMessage) {
					ObjectMessage objectMessage = (ObjectMessage) message;
					String objectMessageString = objectMessage.getObject().toString();

					factoryConsumerLog.info("Received object message: " + objectMessageString);

					Order order = (Order) objectMessage.getObject();
					BigDecimal totalSumOfOrder = getTotalSumOfOrder(order);
					long totalNumberOfProducts = getTotalNumberOfProducts(order);

					factoryConsumerLog.info("Processing order nr: " + order.getOrderId() + "...");

					printOrderInfo(order);

					Thread.sleep(3000);

					factoryProducer.sendAnswer(totalSumOfOrder, totalNumberOfProducts);

				} else {
					factoryConsumerLog.info("Received other message: " + message);
				}

			} catch (JMSException e) {
				factoryConsumerLog.warn("JMS Error in FactoryConsumer: " + e);
				e.printStackTrace();
			} catch (Exception e) {
				System.out.println("Error in FactoryConsumer" + e);
				e.printStackTrace();
			}
		}

		private BigDecimal getTotalSumOfOrder(Order order) {
			BigDecimal sum = new BigDecimal(0);
			List<OrderRow> orderRows = order.getOrderRows();
			for (OrderRow orderRow : orderRows){
				BigDecimal price = orderRow.getProduct().getPrice();
				BigDecimal quantity = new BigDecimal(orderRow.getQuantity());
				BigDecimal orderRowSum = price.multiply(quantity);
				sum = sum.add(orderRowSum);
			}
			return sum;
		}

		private void printOrderInfo(Order order) throws InterruptedException {
			List<OrderRow> orderRows = order.getOrderRows();
			for (OrderRow orderRow : orderRows){
				Thread.sleep(1000);
				factoryConsumerLog.info("-------------------------------------------");
				factoryConsumerLog.info("ID: " + orderRow.getProduct().getId());
				factoryConsumerLog.info("Name: " + orderRow.getProduct().getName());
				factoryConsumerLog.info("Price: " + orderRow.getProduct().getPrice());
				factoryConsumerLog.info("Quantity: " + orderRow.getQuantity());
				factoryConsumerLog.info("-------------------------------------------");
			}
		}

		private long getTotalNumberOfProducts(Order order) {
			long totalNumberOfProducts = 0;
			List<OrderRow> orderRows = order.getOrderRows();

			for (OrderRow orderRow : orderRows){
				totalNumberOfProducts += orderRow.getQuantity();
			}

			return totalNumberOfProducts;
		}


	}

	class ExceptionListenerImpl implements javax.jms.ExceptionListener {

		public synchronized void onException(JMSException ex) {
			factoryConsumerLog.error("Error occured in FactoryConsumer.");
			ex.printStackTrace();
		}
	}

}