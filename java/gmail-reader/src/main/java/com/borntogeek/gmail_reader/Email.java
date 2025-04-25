package com.borntogeek.gmail_reader;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Email {

	private Date receivedOn;
	private String from;
	private String subject;
	private StringBuilder textBuilder;
	private List<FileByteData> attachments;

	public Email() {
		this.textBuilder = new StringBuilder();
		this.attachments = new LinkedList<FileByteData>();
	}

	public Date getReceivedOn() {
		return receivedOn;
	}

	public void setReceivedOn(Date receivedOn) {
		this.receivedOn = receivedOn;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public StringBuilder getTextBuilder() {
		return textBuilder;
	}

	public void setTextBuilder(StringBuilder textBuilder) {
		this.textBuilder = textBuilder;
	}

	public List<FileByteData> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<FileByteData> attachments) {
		this.attachments = attachments;
	}

	public String getText() {
		return textBuilder.toString();
	}

	public int getNumberOfAttachments() {
		return attachments.size();
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("RECEIVED ON: ").append(receivedOn).append("\n");
		stringBuilder.append("FROM: ").append(from).append("\n");
		stringBuilder.append("SUBJECT: ").append(subject).append("\n");
		stringBuilder.append("NUMBER OF ATTACHMENTS: ").append(getNumberOfAttachments()).append("\n");
		stringBuilder.append("TEXT: ").append(getText()).append("\n\n");
		return stringBuilder.toString();
	}
}
