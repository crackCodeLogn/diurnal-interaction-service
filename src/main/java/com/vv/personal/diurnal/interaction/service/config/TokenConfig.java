package com.vv.personal.diurnal.interaction.service.config;

import io.smallrye.config.ConfigMapping;

/**
 * @author Vivek
 * @since 07/03/21
 */
@ConfigMapping(prefix = "token")
public interface TokenConfig {
    int length();

    int expiry();
}