package model;

import jakarta.mail.MessagingException;

import java.io.UnsupportedEncodingException;

public class Main {
	public static void main(String[] args) throws MessagingException, UnsupportedEncodingException {
		EmailSender email = new EmailSender("nicola1.travaglini@gmail.com", 34123);
		email.send();

	}
}
