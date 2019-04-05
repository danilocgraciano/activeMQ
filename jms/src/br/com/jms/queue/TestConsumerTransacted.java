package br.com.jms.queue;

import java.util.Scanner;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import br.com.jms.JMS;

public class TestConsumerTransacted {

	public static void main(String[] args) {

		try {
			JMS jms = new JMS();
			Connection connection = jms.getConnection();
			connection.start();

			InitialContext context = jms.getContext();


			Session session = connection.createSession(true, Session.SESSION_TRANSACTED);
			Destination queue = (Destination) context.lookup("financeiro");
			MessageConsumer consumer = session.createConsumer(queue);

			consumer.setMessageListener(new MessageListener() {

				@Override
				public void onMessage(Message message) {
					TextMessage textTessage = (TextMessage) message;

					try {
						System.out.println(message);
						// CASO CLIENT_ACKNOWLEDGE
						// message.acknowledge();
						session.commit();
					} catch (Exception e) {
						try {
							session.rollback();
						} catch (JMSException e1) {
							e1.printStackTrace();
						}
					}
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
