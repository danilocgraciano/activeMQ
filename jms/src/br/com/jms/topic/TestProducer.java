package br.com.jms.topic;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import br.com.jms.JMS;

public class TestProducer {

	public static void main(String[] args) {

		try {
			JMS jms = new JMS();
			Connection connection = jms.getConnection();
			connection.start();

			InitialContext context = jms.getContext();

			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination queue = (Destination) context.lookup("loja");
			MessageProducer producer = session.createProducer(queue);

			for (int i = 1; i <= 100; i++) {
				Message message = session.createTextMessage("<OrderID>" + i + "</OrderID>");
				message.setBooleanProperty("ebook", false);
				producer.send(message);
			}

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
