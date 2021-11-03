package com.vv.personal.diurnal.interaction.config;

import io.smallrye.config.ConfigMapping;

/**
 * @author Vivek
 * @since 30/10/21
 */
@ConfigMapping(prefix = "mail.smtp")
public interface MailSmtpConfig {

    String host();

    String port();

    boolean auth();

    boolean starttlsEnable();
}