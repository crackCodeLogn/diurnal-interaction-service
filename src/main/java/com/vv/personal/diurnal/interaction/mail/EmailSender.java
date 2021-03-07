package com.vv.personal.diurnal.interaction.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;

/**
 * @author Vivek
 * @since 07/03/21
 */
public class EmailSender {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailSender.class);

    public Boolean sendOtpMessage(Message message, String singleRecipient, String subject, String text) {
        try {
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(singleRecipient));
            message.setSubject(subject);
            message.setContent(text, "text/html");

            return sendMail(message, text);
        } catch (MessagingException e) {
            LOGGER.error("Failed to send otp message. ", e);
        }
        return false;
    }

    private Boolean sendMail(Message message, String text) throws MessagingException {
        LOGGER.info("Sending email to [{}], title: [{}], body: [{}]", message.getAllRecipients()[0], message.getSubject(), text);
        Transport.send(message);
        LOGGER.info("Mail dispatched to [{}]", message.getAllRecipients()[0]);
        return true;
    }

}
