package edu.msg.jbook.backend.util;

import edu.msg.jbook.backend.model.UserState;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Created by cioncag on 01.08.2016.
 */
public class MailSender {
    // Sender: JBook
    private final static String fromEmail = "team.jbook@gmail.com";
    private final static String password = "Java2016";

    public MailSender() {
    }

    public static void sendEmail(UserState userState) {

        String cofirmationURL = "http://localhost:8080/jbook-web/confirm.xhtml?uuid=" + userState.getUuid();

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(fromEmail, password);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(userState.getEmail()));
            message.setSubject("Please Confirm Your Registration");
            message.setContent("Dear " + userState.getUser().getFirstName() + " " +
            userState.getUser().getLastName() + "," +
                    "<br><br>Thanks for joining JBook!<br>" +
                    "The Activation Code is: " + userState.getVerifyCode() + "<br>" +
                    "<center>Please click the link below to confirm your email:</center><br>" +
                    "<center><a href=\"" + cofirmationURL + "\">Activate your Account!</a></center>", "text/html; charset=utf-8");
            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

}
