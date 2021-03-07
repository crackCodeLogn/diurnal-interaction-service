package com.vv.personal.diurnal.interaction.config;

import com.vv.personal.diurnal.interaction.mail.EmailSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.Timer;

/**
 * @author Vivek
 * @since 07/03/21
 */
@Configuration
public class MailConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(MailConfig.class);

    @Value("${mail.smtp.host:smtp.google.com}")
    private String host;

    @Value("${mail.smtp.port:587}")
    private Integer port;

    @Value("${mail.smtp.auth:true}")
    private Boolean auth;

    @Value("${mail.smtp.starttls.enable:true}")
    private Boolean enableTls;

    @Value("${mail.from:}")
    private String mailFrom;

    @Value("${mail.user:}")
    private String user;

    @Value("${mail.cred:}")
    private String cred;

    @Value("${mail.otp.title:}")
    private String otpTitle;

    @Value("${mail.otp.body:}")
    private String otpBody;

    @Value("${mail.otp.timeout.minutes:5}")
    private Integer otpTimeoutMinutes;

    @Bean
    @Qualifier("DiurnalSmtpProperties")
    public Properties diurnalSmtpProperties() {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", auth);
        properties.put("mail.smtp.starttls.enable", enableTls);
        return properties;
    }

    @Bean
    @Qualifier("DiurnalMailSession")
    public Session diurnalMailSession() {
        return Session.getInstance(diurnalSmtpProperties(),
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(user, cred);
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
            message.setFrom(new InternetAddress(mailFrom));
        } catch (MessagingException e) {
            LOGGER.error("Failed to set 'from' for mailing session. ", e);
        }
        return message;
    }

    @Bean
    @Scope("prototype")
    public Timer generateTimer() {
        return new Timer();
    }

    public String getOtpTitle() {
        return otpTitle;
    }

    public String getOtpBody() {
        return otpBody;
    }

    public Integer getOtpTimeoutMinutes() {
        return otpTimeoutMinutes;
    }
}
