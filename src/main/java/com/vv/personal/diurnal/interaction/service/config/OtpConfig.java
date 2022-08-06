package com.vv.personal.diurnal.interaction.service.config;

import io.smallrye.config.ConfigMapping;

/**
 * @author Vivek
 * @since 07/03/21
 */
@ConfigMapping(prefix = "otp")
public interface OtpConfig {

    int mailLength();

    int smsLength();

    default Integer getOtpStartRangeForMail() {
        return (int) Math.pow(10, mailLength() - 1.0);
    }

    default Integer getOtpEndRangeForMail() {
        return (int) Math.pow(10, smsLength()) - 1;
    }
}
