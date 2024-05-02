package com.borntogeek.gmail_reader;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import jakarta.mail.Address;
import jakarta.mail.FetchProfile;
import jakarta.mail.Folder;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.Part;
import jakarta.mail.Session;
import jakarta.mail.Store;
import jakarta.mail.UIDFolder.FetchProfileItem;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.search.ComparisonTerm;
import jakarta.mail.search.ReceivedDateTerm;
import jakarta.mail.search.SearchTerm;

public class GmailReader {

	private static final String EMAIL = "example@gmail.com";
	private static final String APP_PASSWORD = "xxxx xxxx xxxx xxxx";

	public static void main(String[] args) {
		readMessagesFromGmail();
	}

	private static void readMessagesFromGmail() {
		Store store = null;
		Folder folder = null;

		try {
			store = getImapStore();
			folder = getFolderFromStore(store, "INBOX");

			Message[] messages = folder.search(getMessagesSearchTerm());
			folder.fetch(messages, getFetchProfile());

			for (int i = 0; i < messages.length; i++) {
				printMessage(messages[i]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeFolder(folder);
			closeStore(store);
		}
	}

	private static Store getImapStore() throws Exception {
		Session session = Session.getInstance(getImapProperties());
		Store store = session.getStore("imaps");
		store.connect("imap.gmail.com", EMAIL, APP_PASSWORD);
		return store;
	}

	private static Properties getImapProperties() {
		Properties props = new Properties();
		props.put("mail.imaps.host", "imap.gmail.com");
		props.put("mail.imaps.ssl.trust", "imap.gmail.com");
		props.put("mail.imaps.port", "993");
		props.put("mail.imaps.starttls.enable", "true");
		props.put("mail.imaps.connectiontimeout", "10000");
		props.put("mail.imaps.timeout", "10000");
		return props;
	}

	private static Folder getFolderFromStore(Store store, String folderName) throws MessagingException {
		Folder folder = store.getFolder(folderName);
		folder.open(Folder.READ_ONLY);
		return folder;
	}

	private static SearchTerm getMessagesSearchTerm() {
		Date yesterdayDate = new Date(new Date().getTime() - (1000 * 60 * 60 * 24));
		return new ReceivedDateTerm(ComparisonTerm.GT, yesterdayDate);
	}

	private static FetchProfile getFetchProfile() {
		FetchProfile fetchProfile = new FetchProfile();
		fetchProfile.add(FetchProfileItem.ENVELOPE);
		fetchProfile.add(FetchProfileItem.CONTENT_INFO);
		fetchProfile.add("X-mailer");
		return fetchProfile;
	}

	private static void printMessage(Message message) throws MessagingException, IOException {
		StringBuilder messageBuilder = new StringBuilder();
		messageBuilder.append("RECEIVED ON: ").append(message.getReceivedDate()).append("\n");
		
		Address[] addressesFrom = message.getFrom();
		String from = addressesFrom != null ? ((InternetAddress) addressesFrom[0]).getAddress() : null;
		messageBuilder.append("FROM: ").append(from).append("\n");

		messageBuilder.append("SUBJECT: ").append(message.getSubject()).append("\n");

		StringBuilder textCollector = new StringBuilder();
		collectTextFromMessage(textCollector, message);
		messageBuilder.append("TEXT: ").append(textCollector.toString()).append("\n");

		System.out.println(messageBuilder.toString());
	}

	private static void collectTextFromMessage(StringBuilder textCollector, Part part)
			throws MessagingException, IOException {
		if (part.isMimeType("text/plain")) {
			textCollector.append((String) part.getContent());
		} else if (part.isMimeType("multipart/*") && part.getContent() instanceof Multipart) {
			Multipart multiPart = (Multipart) part.getContent();
			for (int i = 0; i < multiPart.getCount(); i++) {
				collectTextFromMessage(textCollector, multiPart.getBodyPart(i));
			}
		}
	}

	private static void closeFolder(Folder folder) {
		if (folder != null && folder.isOpen()) {
			try {
				folder.close(true);
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		}
	}

	private static void closeStore(Store store) {
		if (store != null && store.isConnected()) {
			try {
				store.close();
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		}
	}
}
