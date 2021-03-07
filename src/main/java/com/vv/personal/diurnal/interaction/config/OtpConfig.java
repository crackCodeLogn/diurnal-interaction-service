package com.vv.personal.diurnal.interaction.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author Vivek
 * @since 07/03/21
 */
@Configuration
public class OtpConfig {

    @Value("${otp.mail.length:8}")
    private Integer otpMailLength;

    @Value("${otp.sms.length:8}")
    private Integer otpSmsLength;

    public Integer getOtpStartRangeForMail() {
        return (int) Math.pow(10, otpMailLength - 1);
    }

    public Integer getOtpEndRangeForMail() {
        return (int) Math.pow(10, otpMailLength) - 1;
    }

    public int getOtpMailLength() {
        return otpMailLength;
    }

    public int getOtpSmsLength() {
        return otpSmsLength;
    }
}
