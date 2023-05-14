package model;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

public class EmailSender {
	private final String emailFrom = "skillbuilders05@gmail.com";
	private final String passwordFrom = "f1rIXO3ySQn8PWFh";
	private final String providerFrom = "smtp-relay.sendinblue.com";
	private final int port = 587;
	private String emailTo;
	private int otp;

	public EmailSender(String emailTo, int otp) {
		this.emailTo = emailTo;
		this.otp = otp;
	}

	public void send() throws MessagingException, UnsupportedEncodingException {
		Properties props = new Properties();
		props.put("mail.smtp.host", this.providerFrom);
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.port", this.port);
		props.put("mail.smtp.auth", "true");

		Session session = Session.getDefaultInstance(props);

		MimeMessage msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress(emailFrom, "Skillbuilders"));
		msg.setRecipients(Message.RecipientType.TO, emailTo);
		msg.setSubject("OTP SkillBuilders");
		msg.setText(Integer.toString(otp));
		Transport.send(msg, emailFrom, passwordFrom);
	}
}
