package com.rolandtalvar.asyncapplication.shop;

import java.math.BigDecimal;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import com.rolandtalvar.asyncapplication.model.Order;
import com.rolandtalvar.asyncapplication.model.Product;
import com.rolandtalvar.asyncapplication.Broker;
import com.rolandtalvar.asyncapplication.model.OrderRow;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;


public class ShopProducer {
	public static final String MESSAGES_FROM_SHOP = "shopMessageQueue";

	private static final Logger shopProducerLog = Logger.getLogger(ShopProducer.class);

	private long timeToLive = 1000000;

	private Session session;

	public ShopProducer(Session session) {
		this.session = session;
	}

	protected void sendOrder() throws Exception {

		Destination destinationToFactory = session.createQueue(MESSAGES_FROM_SHOP);
		MessageProducer producer = this.session.createProducer(destinationToFactory);
		producer.setTimeToLive(timeToLive);

		ObjectMessage objectMessage = session.createObjectMessage();

		Order order = createOrder();
		objectMessage.setObject(order);

		producer.send(objectMessage);

		shopProducerLog.debug("Order sent: " + order);
	}

	private Order createOrder() throws InterruptedException {

		shopProducerLog.debug("Creating order...");

		Product iPhone = new Product(111, "iPhone 7", new BigDecimal(700.00));
		Product samsung = new Product(222, "Samsung Galaxy S7", new BigDecimal(700.00));
		Product huawei = new Product(333, "Huawei P9", new BigDecimal(350.00));
		Product nexus = new Product(444, "Nexus 6P", new BigDecimal(500.00));
		Product lg = new Product(555, "LG G4", new BigDecimal(400.00));

		OrderRow orderRow1 = new OrderRow(iPhone, 15L);
		OrderRow orderRow2 = new OrderRow(samsung, 10L);
		OrderRow orderRow3 = new OrderRow(huawei, 8L);
		OrderRow orderRow4 = new OrderRow(nexus, 3L);
		OrderRow orderRow5 = new OrderRow(lg, 5L);

		Order order = new Order();
		order.addOrderRow(orderRow1);
		order.addOrderRow(orderRow2);
		order.addOrderRow(orderRow3);
		order.addOrderRow(orderRow4);
		order.addOrderRow(orderRow5);

		Thread.sleep(3000);

		return order;
	}


}


