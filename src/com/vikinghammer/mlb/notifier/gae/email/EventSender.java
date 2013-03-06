package com.vikinghammer.mlb.notifier.gae.email;

import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.vikinghammer.mlb.notifier.gae.model.Event;

/**
 * Send events to our recipient(s).
 * 
 * Currently sends an email, using the system properties (defined in appengine-web.xml)
 * to determine the email address to send from and to.
 * 
 * @author sschulte
 */
public class EventSender {
	
	private static final Logger log = Logger.getLogger(EventSender.class.getName());
	
	public static void sendAll(List<Event> events) {
		for (Event event : events) {
			send(event);
		}
	}
	
	public static void send(Event event) {
		Session session = Session.getDefaultInstance(new Properties(), null);
		
		try {
			String messageText = String.format("[%s] %s", event.getTeams(), event.getText());
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(System.getProperty("from-email"), "MLB Notifier"));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(System.getProperty("to-email")));
			message.setSubject(messageText);
			message.setText(messageText);
			log.info(String.format("Sending email: %s", messageText));
			Transport.send(message);
		} catch (Exception e) {
			log.log(Level.SEVERE, "Failed to send email", e);
		}
	}

}
