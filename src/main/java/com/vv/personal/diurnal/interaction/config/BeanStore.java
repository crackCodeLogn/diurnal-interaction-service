package com.vv.personal.diurnal.interaction.config;

import com.vv.personal.diurnal.interaction.mail.EmailSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import javax.inject.Inject;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.Timer;

/**
 * @author Vivek
 * @since 30/10/21
 */
@Slf4j
@Configuration
public class BeanStore {

    @Inject
    MailSmtpConfig mailSmtpConfig;
    @Inject
    MailConfig mailConfig;

    @Bean
    @Qualifier("DiurnalSmtpProperties")
    public Properties diurnalSmtpProperties() {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", mailSmtpConfig.host());
        properties.put("mail.smtp.port", mailSmtpConfig.port());
        properties.put("mail.smtp.auth", mailSmtpConfig.auth());
        properties.put("mail.smtp.starttls.enable", mailSmtpConfig.starttlsEnable());
        return properties;
    }

    @Bean
    @Qualifier("DiurnalMailSession")
    public Session diurnalMailSession() {
        return Session.getInstance(diurnalSmtpProperties(),
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(mailConfig.user(), mailConfig.cred());
                    }
                });
    }

    @Bean
    public EmailSender emailSender() {
        return new EmailSender();
    }

    @Bean
    @Scope("prototype")
    @Qualifier("DiurnalMailMessage")
    public Message diurnalMailMessage() {
        Message message = new MimeMessage(diurnalMailSession());
        try {
            message.setFrom(new InternetAddress(mailConfig.from()));
        } catch (MessagingException e) {
            log.error("Failed to set 'from' for mailing session. ", e);
        }
        return message;
    }

    @Bean
    @Scope("prototype")
    public Timer generateTimer() {
        return new Timer();
    }
}