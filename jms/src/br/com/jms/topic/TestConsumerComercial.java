package br.com.jms.topic;

import java.util.Scanner;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import br.com.jms.JMS;

public class TestConsumerComercial {

	public static void main(String[] args) {

		try {

			JMS jms = new JMS();
			Connection connection = jms.getConnection();
			connection.setClientID("comercial");
			connection.start();

			InitialContext context = jms.getContext();

			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination queue = (Destination) context.lookup("loja");
			MessageConsumer consumer = session.createDurableSubscriber((Topic) queue, "assinatura");

			consumer.setMessageListener(new MessageListener() {

				@Override
				public void onMessage(Message message) {
					TextMessage textMessage = (TextMessage) message;
					try {
						System.out.println(textMessage.getText());
					} catch (JMSException e) {
						e.printStackTrace();
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
