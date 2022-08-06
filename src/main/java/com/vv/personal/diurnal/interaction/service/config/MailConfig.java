package com.vv.personal.diurnal.interaction.service.config;

import io.smallrye.config.ConfigMapping;

/**
 * @author Vivek
 * @since 07/03/21
 */
@ConfigMapping(prefix = "mail")
public interface MailConfig {

    String from();

    String user();

    String cred();
}