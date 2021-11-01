package com.vv.personal.diurnal.interaction.config;

import io.smallrye.config.ConfigMapping;

/**
 * @author Vivek
 * @since 30/10/21
 */
@ConfigMapping(prefix = "mail.otp")
public interface MailOtpConfig {

    String title();

    String body();

    int timeoutMinutes();
}