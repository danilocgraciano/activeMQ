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

public class TestConsumerEstoque {

	public static void main(String[] args) {

		try {
			JMS jms = new JMS();
			Connection connection = jms.getConnection();
			connection.setClientID("estoque");
			connection.start();

			InitialContext context = jms.getContext();

			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination queue = (Destination) context.lookup("loja");
			
			//o booleano neste método informa que caso a conexao utilizada para enviar alguma informação adiante eu quero ficar sabendo ou não,
			//ou seja, não quero consumir as mensagens que eu mesmo estou produzindo
//			MessageConsumer consumer = session.createDurableSubscriber((Topic) queue,"assinatura");
			MessageConsumer consumer = session.createDurableSubscriber((Topic) queue,"assinatura","ebook is null OR ebook=false", false);

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
