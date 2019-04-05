package br.com.jms.queue;

import java.util.Enumeration;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import br.com.jms.JMS;

public class TestBrowser {

	public static void main(String[] args) {

		try {
			JMS jms = new JMS();
			Connection connection = jms.getConnection();
			connection.start();

			InitialContext context = jms.getContext();

			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination queue = (Destination) context.lookup("financeiro");
			QueueBrowser browser = session.createBrowser((Queue) queue);

			Enumeration msgs = browser.getEnumeration();
			while (msgs.hasMoreElements()) {
				TextMessage msg = (TextMessage) msgs.nextElement();
				System.out.println("Message: " + msg.getText());
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
