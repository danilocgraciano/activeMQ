package br.com.jms.queue;

import java.util.Properties;
import java.util.Scanner;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class TestConsumerDLQ {

	public static void main(String[] args) {

		try {
			Properties properties = new Properties();
			properties.put("java.naming.factory.initial", "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
			// properties.put("java.naming.provider.url", "vm://localhost");
			properties.put("java.naming.provider.url", "tcp://localhost:61616");
			properties.put("queue.DLQ", "ActiveMQ.DLQ");

			InitialContext context = new InitialContext(properties);
			ConnectionFactory connectionFactory = (ConnectionFactory) context.lookup("ConnectionFactory");
			Connection connection = connectionFactory.createConnection();
			connection.start();

			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination queue = (Destination) context.lookup("DLQ");
			MessageConsumer consumer = session.createConsumer(queue);

			// Message message = consumer.receive();

			consumer.setMessageListener(new MessageListener() {

				@Override
				public void onMessage(Message message) {
					System.out.println(message);
				}
			});

			new Scanner(System.in).nextLine();

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