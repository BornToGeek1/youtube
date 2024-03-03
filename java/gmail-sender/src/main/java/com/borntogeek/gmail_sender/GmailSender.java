package com.borntogeek.gmail_sender;

import java.util.Properties;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class GmailSender {
	private static final String EMAIL_FROM = "email.from@gmail.com";
	private static final String EMAIL_TO = "email.to@email.com";
	private static final String APP_PASSWORD = "xxxx xxxx xxxx xxxx";
	
	public static void main(String[] args) throws Exception {
		Message message = new MimeMessage(getEmailSession());
		message.setFrom(new InternetAddress(EMAIL_FROM));
		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(EMAIL_TO));
		message.setSubject("Email subject");
		message.setText("This is my email sent from Gmail using Java");
		Transport.send(message);
	}
	
	private static Session getEmailSession() {
		return Session.getInstance(getGmailProperties(), new Authenticator() {
		    protected PasswordAuthentication getPasswordAuthentication() {
		        return new PasswordAuthentication(EMAIL_FROM, APP_PASSWORD);
		    }
		});
	}
	
	private static Properties getGmailProperties() {
		Properties prop = new Properties();
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.starttls.enable", "true");
		prop.put("mail.smtp.host", "smtp.gmail.com");
		prop.put("mail.smtp.port", "587");
		prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");
		return prop;
	}
}
