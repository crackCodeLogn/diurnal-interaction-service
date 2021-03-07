package com.vv.personal.diurnal.interaction.util;

import com.vv.personal.diurnal.artifactory.generated.OtpMailProto;
import com.vv.personal.diurnal.artifactory.generated.ResponsePrimitiveProto;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.SplittableRandom;

import static com.vv.personal.diurnal.interaction.constants.Constants.ZERO;

/**
 * @author Vivek
 * @since 07/03/21
 */
public class DiurnalUtil {
    public static final Logger LOGGER = LoggerFactory.getLogger(DiurnalUtil.class);

    private static final SplittableRandom randomizer = new SplittableRandom();

    public static Integer generateOtp(Integer startOtpRange, Integer endOtpRange) {
        return randomizer.nextInt(startOtpRange, endOtpRange);
    }

    public static ResponsePrimitiveProto.ResponsePrimitive generateResponsePrimitiveBool(Boolean value) {
        return ResponsePrimitiveProto.ResponsePrimitive.newBuilder()
                .setBoolResponse(value)
                .build();
    }

    public static ResponsePrimitiveProto.ResponsePrimitive generateResponsePrimitiveString(String value) {
        return ResponsePrimitiveProto.ResponsePrimitive.newBuilder()
                .setResponse(value)
                .build();
    }

    public static OtpMailProto.OtpMail generateOtpMail(String email) {
        return generateOtpMail(email, ZERO);
    }

    public static OtpMailProto.OtpMail generateOtpMail(String email, Integer otp) {
        return OtpMailProto.OtpMail.newBuilder()
                .setEmail(email)
                .setOtp(otp)
                .build();
    }

    public static StopWatch procureStopWatch() {
        return new StopWatch();
    }

}
