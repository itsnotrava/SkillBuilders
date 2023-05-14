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
	private final String usernameFrom = "AKIAQUOFW57HDGAXJLZZ";
	private final String passwordFrom = "BJbvk0wzBybbPdjEx8/wJTX68OmRlRtUt/2Joqms4CyX";
	private final String providerFrom = "email-smtp.eu-north-1.amazonaws.com";
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
		Transport transport = session.getTransport();
		transport.connect(providerFrom, usernameFrom, passwordFrom);
		transport.sendMessage(msg, msg.getAllRecipients());
	}
}
