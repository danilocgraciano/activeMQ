package br.com.jms.queue;

import java.util.Properties;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class TestProducer {

	public static void main(String[] args) {

		try {
			Properties properties = new Properties();
			properties.put("java.naming.factory.initial", "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
			// properties.put("java.naming.provider.url", "vm://localhost");
			properties.put("java.naming.provider.url", "tcp://localhost:61616");
			properties.put("queue.financeiro", "fila.financeiro");

			InitialContext context = new InitialContext(properties);
			ConnectionFactory connectionFactory = (ConnectionFactory) context.lookup("ConnectionFactory");
			Connection connection = connectionFactory.createConnection();
			connection.start();

			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination queue = (Destination) context.lookup("financeiro");
			MessageProducer producer = session.createProducer(queue);

			for (int i = 1; i <= 100; i++)
				producer.send(session.createTextMessage("<OrderID>" + i + "</OrderID>"));

			session.close();
			connection.close();
			context.close();
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}
