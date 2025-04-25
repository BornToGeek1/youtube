package com.borntogeek.gmail_reader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import jakarta.mail.Address;
import jakarta.mail.BodyPart;
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

public class AdvancedGmailReader {

	private static final String EMAIL = "example@gmail.com";
	private static final String APP_PASSWORD = "xxxx xxxx xxxx xxxx";

	public static void main(String[] args) throws IOException {
		List<Email> emails = readEmailsFromGmail();

		// Sort emails by receivedOn date - latest first
		emails.sort(Comparator.comparing(Email::getReceivedOn, Comparator.nullsLast(Comparator.reverseOrder())));

		for (Email email : emails) {
			saveEmailAttachments(email.getAttachments());
			System.out.println(email);
		}
	}

	private static List<Email> readEmailsFromGmail() {
		Store store = null;
		Folder folder = null;

		try {
			store = getImapStore();
			folder = getFolderFromStore(store, "INBOX");

			Message[] messages = folder.search(getMessagesSearchTerm());
			folder.fetch(messages, getFetchProfile());

			List<Email> emails = new ArrayList<Email>(messages.length);

			for (int i = 0; i < messages.length; i++) {
				Email email = getEmailFromMessage(messages[i]);
				emails.add(email);
			}
			return emails;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeFolder(folder);
			closeStore(store);
		}

		return Collections.emptyList();
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

	private static Email getEmailFromMessage(Message message) throws MessagingException, IOException {
		Email email = new Email();

		Address[] addressesFrom = message.getFrom();
		email.setFrom(addressesFrom != null ? ((InternetAddress) addressesFrom[0]).getAddress() : null);
		email.setReceivedOn(message.getReceivedDate());
		email.setSubject(message.getSubject());

		Object messageContent = message.getContent();
		if (messageContent instanceof String) {
			email.getTextBuilder().append((String) messageContent);
		} else if (messageContent instanceof Multipart) {
			processMultipart((Multipart) messageContent, email);
		}

		return email;
	}

	private static void processMultipart(Multipart multipart, Email email) throws MessagingException, IOException {
		for (int i = 0; i < multipart.getCount(); i++) {
			BodyPart part = multipart.getBodyPart(i);

			if (part.isMimeType("text/plain")) {
				email.getTextBuilder().append(part.getContent().toString());
			} else if (part.isMimeType("multipart/*")) {
				processMultipart((Multipart) part.getContent(), email);
			} else if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())
					|| (part.getFileName() != null && !part.getFileName().isEmpty())) {
				readAttachment(part, email);
			}
		}
	}

	private static void readAttachment(BodyPart part, Email email) throws MessagingException, IOException {
		String filename = part.getFileName();
		try (InputStream is = part.getInputStream(); ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

			byte[] buf = new byte[4096];
			int bytesRead;
			while ((bytesRead = is.read(buf)) != -1) {
				baos.write(buf, 0, bytesRead);
			}

			FileByteData fileData = new FileByteData(filename, baos.toByteArray());
			email.getAttachments().add(fileData);
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

	private static void saveEmailAttachments(List<FileByteData> attachments) throws IOException {
		for (FileByteData attachment : attachments) {
			saveFileData(attachment);
		}
	}

	private static void saveFileData(FileByteData fileData) throws IOException {
		Path resourceDir = Paths.get("target", "output");
		if (!Files.exists(resourceDir)) {
			Files.createDirectories(resourceDir);
		}

		Path filePath = resourceDir.resolve(fileData.getFilename());
		Files.write(filePath, fileData.getData());
	}
}
