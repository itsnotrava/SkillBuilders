package model;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

public class EmailSender {
	private final String emailFrom = "nicola1.travaglini@gmail.com";
	private final String passwordFrom = "xtkicxlycjaprlpm";
	private final String providerFrom = "smtp.gmail.com";
	private String emailTo;
	private int otp;

	public EmailSender(String emailTo, int otp) {
		this.emailTo = emailTo;
		this.otp = otp;
	}

	public void send() throws MessagingException {
		Properties props = new Properties();
		props.put("mail.smtp.host", this.providerFrom);
		props.put("mail.smtp.starttls.enable", "true");
		Session session = Session.getInstance(props, null);

		MimeMessage msg = new MimeMessage(session);
		msg.setFrom(emailFrom);
		msg.setRecipients(Message.RecipientType.TO, emailTo);
		msg.setSubject("OTP SkillBuilders");
		msg.setText(Integer.toString(otp));
		Transport.send(msg, emailFrom, passwordFrom);
	}
}
