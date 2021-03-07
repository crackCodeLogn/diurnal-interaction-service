package com.vv.personal.diurnal.interaction.controller;

import com.vv.personal.diurnal.artifactory.generated.OtpMailProto;
import com.vv.personal.diurnal.artifactory.generated.ResponsePrimitiveProto;
import com.vv.personal.diurnal.interaction.config.MailConfig;
import com.vv.personal.diurnal.interaction.config.OtpConfig;
import com.vv.personal.diurnal.interaction.util.DiurnalUtil;
import com.vv.personal.diurnal.interaction.util.TimerUtil;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ConcurrentHashMap;

import static com.vv.personal.diurnal.interaction.constants.Constants.RESPOND_FALSE_BOOL;
import static com.vv.personal.diurnal.interaction.constants.Constants.RESPOND_TRUE_BOOL;
import static com.vv.personal.diurnal.interaction.util.DiurnalUtil.generateOtpMail;

/**
 * @author Vivek
 * @since 07/03/21
 */
@RequestMapping("/diurnal/mail")
@RestController("mail-controller")
public class MailController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MailController.class);

    @Autowired
    private MailConfig mailConfig;
    @Autowired
    private OtpConfig otpConfig;

    private static final ConcurrentHashMap<String, Integer> otpMap = new ConcurrentHashMap<>();

    @ApiOperation(value = "generate mail-otp", hidden = true)
    @PostMapping("/generate/otp")
    public ResponsePrimitiveProto.ResponsePrimitive generateOtp(@RequestBody OtpMailProto.OtpMail otpMail) {
        final String email = refineEmail(otpMail.getEmail());
        if (otpMap.containsKey(email)) {
            LOGGER.warn("OTP already sent. Please try again after {} minutes", mailConfig.getOtpTimeoutMinutes());
            return RESPOND_FALSE_BOOL;
        }
        final Integer otp = DiurnalUtil.generateOtp(otpConfig.getOtpStartRangeForMail(), otpConfig.getOtpEndRangeForMail());
        LOGGER.info("Generated OTP for [{}]: {}", email, otp);
        insertOtpInMap(email, otp);

        LOGGER.info("Initiating mail generation for OTP");
        String mailBody = String.format(mailConfig.getOtpBody(), mailConfig.getOtpTimeoutMinutes(), otp);
        if (mailConfig.emailSender().sendOtpMessage(mailConfig.diurnalMailMessage(), email, mailConfig.getOtpTitle(), mailBody)) {
            TimerUtil.scheduleTimer(mailConfig.generateTimer(),
                    TimerUtil.generateTimedTask(this::removeOtpFromMap, email),
                    mailConfig.getOtpTimeoutMinutes() * 60);
            return RESPOND_TRUE_BOOL;
        } else {
            LOGGER.warn("Failed to generate mail-otp for [{}]", otpMail);
            removeOtpFromMap(email);
        }
        return RESPOND_FALSE_BOOL;
    }

    @GetMapping("/generate/manual/otp")
    public Boolean generateOtpManually(@RequestParam String rxEmail) {
        return generateOtp(generateOtpMail(rxEmail))
                .getBoolResponse();
    }

    @ApiOperation(value = "verify otp", hidden = true)
    @PostMapping("/verify/otp")
    public ResponsePrimitiveProto.ResponsePrimitive verifyOtp(@RequestBody OtpMailProto.OtpMail otpMail) {
        final String email = refineEmail(otpMail.getEmail());
        if (otpMap.containsKey(email)) {
            if (otpMap.get(email).equals(otpMail.getOtp())) {
                LOGGER.info("User with [{}] verified!", email);
                return RESPOND_TRUE_BOOL;
            } else {
                LOGGER.warn("Incorrect OTP entered by user");
            }
        } else {
            LOGGER.warn("Enquired mail's OTP is no longer in cache");
        }
        LOGGER.error("Denied verification!");
        return RESPOND_FALSE_BOOL;
    }

    @GetMapping("/verify/manual/otp")
    public Boolean verifyOtpManually(@RequestParam String email,
                                     @RequestParam Integer otp) {
        return verifyOtp(generateOtpMail(email, otp))
                .getBoolResponse();
    }

    @GetMapping("/map-otp")
    public String getOtpMap() {
        return otpMap.toString();
    }

    private String refineEmail(String email) {
        return email.toLowerCase().trim();
    }

    private void insertOtpInMap(String rxMail, Integer otp) {
        otpMap.putIfAbsent(rxMail, otp);
    }

    private Integer removeOtpFromMap(String mailToRemove) {
        LOGGER.info("Removing [{}] from map", mailToRemove);
        return otpMap.remove(mailToRemove);
    }

}
