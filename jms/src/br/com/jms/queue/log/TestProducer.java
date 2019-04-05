package br.com.jms.queue.log;

import java.util.PriorityQueue;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
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
			Destination queue = (Destination) context.lookup("log");
			MessageProducer producer = session.createProducer(queue);

			
			//apache-activemq-5.12.0/conf/activemq.xml
			//<policyEntry queue=">" prioritizedMessages="true"/>
			
			//Uma alternativa seria separar por
			//MessageConsumer consumer = session.createConsumer(fila, "JMSPriority > 6" );
			
			String info = "2019-04-03 13:28:50,915 | INFO  | Creating Jetty connector | org.apache.activemq.transport.WebTransportServerSupport | main";
			String warn = "2019-04-03 13:28:51,179 | WARN  | ServletContext@o.e.j.s.ServletContextHandler@77102b91{/,null,STARTING} has uncovered http methods for path: / | org.eclipse.jetty.security.SecurityHandler | main";
			String err = "2019-04-03 11:19:25,597 | ERROR  | No Spring WebApplicationInitializer types detected on classpath | /api | main";
			
			TextMessage message = session.createTextMessage(info);
			producer.send(message, DeliveryMode.NON_PERSISTENT, 3, 60000);//LOW < 4
			
			message = session.createTextMessage(warn);
			producer.send(message, DeliveryMode.NON_PERSISTENT, 4, 60000);//DEFAULT = 4
			
			message = session.createTextMessage(err);
			producer.send(message, DeliveryMode.NON_PERSISTENT, 5, 60000);//HIGH > 4
			
			
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
