package br.com.jms;

import java.util.Properties;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.naming.InitialContext;

public class JMS {

	private InitialContext context;

	public JMS() {

		try {
			Properties properties = new Properties();
			properties.put("java.naming.factory.initial", "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
			// properties.put("java.naming.provider.url", "vm://localhost");
			properties.put("java.naming.provider.url", "tcp://localhost:61616");

			properties.put("queue.DLQ", "ActiveMQ.DLQ");
			properties.put("queue.financeiro", "fila.financeiro");
			properties.put("queue.log", "fila.log");

			properties.put("topic.loja", "topico.loja");

			context = new InitialContext(properties);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public InitialContext getContext() {
		return context;
	}

	public Connection getConnection() {
		try {
			ConnectionFactory connectionFactory = (ConnectionFactory) getContext().lookup("ConnectionFactory");
			return connectionFactory.createConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
