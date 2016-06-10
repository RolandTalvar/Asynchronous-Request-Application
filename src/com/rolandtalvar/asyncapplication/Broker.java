package com.rolandtalvar.asyncapplication;

import org.apache.activemq.broker.BrokerService;
import org.apache.log4j.Logger;

public final class Broker {
    private static final Logger brokerServiceLog = Logger.getLogger(Broker.class);
    public static final String URL = "tcp://localhost:" + BrokerService.DEFAULT_PORT;

    private Broker() {
    }

    public static void main(String[] args) throws Exception {
        BrokerService broker = new BrokerService();
        broker.setBrokerName("Asynchronous-Request-Application_Broker");
        broker.addConnector(URL);
        broker.start();
        brokerServiceLog.info("Starting BrokerService on: " + URL);

        while (true) { Thread.sleep(1000); }
    }
}
