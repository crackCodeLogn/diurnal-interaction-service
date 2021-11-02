package com.vv.personal.diurnal.interaction.mail;

import lombok.extern.slf4j.Slf4j;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;

/**
 * @author Vivek
 * @since 07/03/21
 */
@Slf4j
public class EmailSender {

    public boolean sendOtpMessage(Message message, String singleRecipient, String subject, String text) {
        try {
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(singleRecipient));
            message.setSubject(subject);
            message.setContent(text, "text/html");

            return sendMail(message, text);
        } catch (MessagingException e) {
            log.error("Failed to send otp message. ", e);
        }
        return false;
    }

    private boolean sendMail(Message message, String text) throws MessagingException {
        log.info("Sending email to [{}], title: [{}], body: [{}]", message.getAllRecipients()[0], message.getSubject(), text);
        Transport.send(message);
        log.info("Mail dispatched to [{}]", message.getAllRecipients()[0]);
        return true;
    }
}