package com.borntogeek.sms_sender_twilio;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class SmsSender {
	public static final String ACCOUNT_SID = ""; // Twilio Account SID
	public static final String AUTH_TOKEN = ""; // Twilio authentication token 

	public static void main(String[] args) {
		Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

		Message message = Message.creator(
				new PhoneNumber(""), // To number
				new PhoneNumber(""), // From Twilio number
				"Hello from Java via Twilio!")
				.create();

		System.out.println("Message sent! SID: " + message.getSid());
	}
}
