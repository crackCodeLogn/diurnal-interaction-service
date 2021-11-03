package com.vv.personal.diurnal.interaction.util;

import com.vv.personal.diurnal.artifactory.generated.OtpMailProto;
import com.vv.personal.diurnal.artifactory.generated.ResponsePrimitiveProto;

import java.util.SplittableRandom;

/**
 * @author Vivek
 * @since 07/03/21
 */
public class DiurnalUtil {

    private DiurnalUtil() {
    }

    private static final SplittableRandom randomizer = new SplittableRandom();

    public static Integer generateOtp(Integer startOtpRange, Integer endOtpRange) {
        return randomizer.nextInt(startOtpRange, endOtpRange);
    }

    public static ResponsePrimitiveProto.ResponsePrimitive generateResponsePrimitiveString(String value) {
        return ResponsePrimitiveProto.ResponsePrimitive.newBuilder()
                .setResponse(value)
                .build();
    }

    public static OtpMailProto.OtpMail generateOtpMail(String email) {
        return generateOtpMail(email, 0);
    }

    public static OtpMailProto.OtpMail generateOtpMail(String email, Integer otp) {
        return OtpMailProto.OtpMail.newBuilder()
                .setEmail(email)
                .setOtp(otp)
                .build();
    }
}